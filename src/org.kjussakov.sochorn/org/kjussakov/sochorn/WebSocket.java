package org.kjussakov.sochorn;

import jdk.incubator.http.HttpClient;
import org.kjussakov.sochorn.model.CloseEvent;
import org.kjussakov.sochorn.model.Event;
import org.kjussakov.sochorn.model.MessageEvent;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

/**
 * Implements the Web Sockets API specified in https://html.spec.whatwg.org/multipage/web-sockets.html
 * The class is a wrapper around the Java 9 HttpClient API
 */
public class WebSocket implements jdk.incubator.http.WebSocket.Listener {

    private static HttpClient httpClient = HttpClient.newHttpClient();

    private CompletableFuture<jdk.incubator.http.WebSocket> wsFuture;
    private jdk.incubator.http.WebSocket webSocket = null;
    private String url;

    // ready state
    public static final int CONNECTING = 0;
    public static final int OPEN = 1;
    public static final int CLOSING = 2;
    public static final int CLOSED = 3;
    private int readyState = CLOSED;
    private long bufferedAmount = 0; // TODO: not implemented yet

    // networking
    private Consumer<Event> onopen;
    private Consumer<Event> onerror;
    private Consumer<Event> onclose;
    private String extensions = "";
    private String protocol = "";

    // messaging
    private Consumer<Event> onmessage;
    private String binaryType = BinaryType.BLOB;

    public WebSocket(String url, String... protocols) {
        this.url = url;
        jdk.incubator.http.WebSocket.Builder wsBuilder = httpClient.newWebSocketBuilder(
                URI.create(url),
                this);

        if(protocols.length > 0) {
            wsBuilder.subprotocols(protocols[0], protocols);
        }
        readyState = CONNECTING;

        wsFuture = wsBuilder.buildAsync().whenComplete((sock, throwable) -> {
            if(throwable != null) {
                readyState = CLOSED;
                throwable.printStackTrace();
            }
        });
    }

    public String getUrl() {
        return url;
    }

    public int getReadyState() {
        return readyState;
    }

    public String getExtensions() {
        return extensions;
    }

    public String getProtocol() {
        return protocol;
    }

    public long getBufferedAmount() {
        return bufferedAmount;
    }

    public void close() {
        if(webSocket == null) {
            wsFuture.cancel(true);
        } else if(readyState != CLOSED && readyState != CLOSING) {
            webSocket.sendClose(1000, "");
        }
        readyState = CLOSING;
    }

    public void close(String reason) {
        checkCloseReason(reason);
        if(webSocket == null) {
            wsFuture.cancel(true);
        } else if(readyState != CLOSED && readyState != CLOSING) {
            webSocket.sendClose(1000, reason);
        }
        readyState = CLOSING;
    }

    /**
     * @param closeCode see https://tools.ietf.org/html/rfc6455#section-11.7
     */
    public void close(int closeCode) {
        checkCloseCode(closeCode);
        if(webSocket == null) {
            wsFuture.cancel(true);
        } else if(readyState != CLOSED && readyState != CLOSING) {
            webSocket.sendClose(closeCode, "");
        }
        readyState = CLOSING;
    }

    /**
     * @param closeCode see https://tools.ietf.org/html/rfc6455#section-11.7
     */
    public void close(int closeCode, String reason) {
        checkCloseCode(closeCode);
        checkCloseReason(reason);
        if(webSocket == null) {
            wsFuture.cancel(true);
        } else if(readyState != CLOSED && readyState != CLOSING) {
            webSocket.sendClose(closeCode, reason);
        }
        readyState = CLOSING;
    }

    // Only text send is supported now.
    // TODO: consider adding support for the following overloads (i.e., binary data):
    // void send(Blob data);
    // void send(ArrayBuffer data);
    // void send(ArrayBufferView data);
    public void send(String data) {
        if(readyState == CONNECTING || webSocket == null) {
            throw new RuntimeException("InvalidStateError");
        }

        webSocket.sendText(data, true);
    }

    public Consumer<Event> getOnopen() {
        return onopen;
    }

    public void setOnopen(Consumer<Event> onopen) {
        this.onopen = onopen;

    }

    public Consumer<Event> getOnerror() {
        return onerror;
    }

    public void setOnerror(Consumer<Event> onerror) {
        this.onerror = onerror;
    }

    public Consumer<Event> getOnclose() {
        return onclose;
    }

    public void setOnclose(Consumer<Event> onclose) {
        this.onclose = onclose;
    }

    public Consumer<Event> getOnmessage() {
        return onmessage;
    }

    public void setOnmessage(Consumer<Event> onmessage) {
        this.onmessage = onmessage;
    }

    public String getBinaryType() {
        return binaryType;
    }

    public void setBinaryType(String binaryType) {
        if(!BinaryType.BLOB.equals(binaryType) && !BinaryType.ARRAY_BUFFER.equals(binaryType)) {
            throw new RuntimeException("BinaryType must be either " + BinaryType.BLOB + " or " + BinaryType.ARRAY_BUFFER);
        }
        this.binaryType = binaryType;
    }

    @Override
    public void onOpen(jdk.incubator.http.WebSocket webSocket) {
        readyState = OPEN;
        this.webSocket= webSocket;
        protocol = webSocket.getSubprotocol();
        webSocket.request(1);

        if(onopen != null) {
            Event event = new Event(this, this, "open");
            onopen.accept(event);
        }
    }

    @Override
    public CompletionStage<?> onClose(jdk.incubator.http.WebSocket webSocket, int statusCode, String reason) {
        readyState = CLOSED;
        if(onclose != null) {
            Event event = new CloseEvent(this, this, "close", statusCode, reason, true);
            onclose.accept(event);
        }

        return null;
    }

    @Override
    public CompletionStage<?> onText(jdk.incubator.http.WebSocket webSocket, CharSequence message, jdk.incubator.http.WebSocket.MessagePart part) {
        webSocket.request(1);
        if(readyState == OPEN && onmessage != null) {
            Event event = new MessageEvent(this, this, "message", message.toString(), url);
            onmessage.accept(event);
        }
        return null;
    }

    @Override
    public CompletionStage<?> onBinary(jdk.incubator.http.WebSocket webSocket, ByteBuffer message, jdk.incubator.http.WebSocket.MessagePart part) {
        webSocket.request(1);
        // TODO: implement receiving binary messages
        return null;
    }

    @Override
    public void onError(jdk.incubator.http.WebSocket webSocket, Throwable error) {
        if(onerror != null) {
            Event event = new Event(this, this, "error");
            onerror.accept(event);
        }
    }

    private void checkCloseCode(int closeCode) {
        // See https://html.spec.whatwg.org/multipage/web-sockets.html#dom-websocket-close
        if(closeCode != 1000 && (closeCode < 3000 || closeCode > 4999)) {
            throw new RuntimeException("InvalidAccessError");
        }
    }

    private void checkCloseReason(String reason) {
        try {
            if (reason.getBytes("UTF-8").length > 123) {
                throw new RuntimeException("SyntaxError");
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("WebSocket polyfill internal error");
        }
    }

    public final class BinaryType {
        public static final String BLOB = "blob";
        public static final String ARRAY_BUFFER = "arraybuffer";

        private BinaryType() {}
    }
}
