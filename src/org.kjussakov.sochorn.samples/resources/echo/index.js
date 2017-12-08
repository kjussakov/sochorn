// Test the WebSocket polyfill
// This code should run and yield the same result in both Nashorn and in browser

var ws = new WebSocket("wss://echo.websocket.org");

ws.onopen = function (data) {
    console.log("OPEN");
    console.log("Type: " + data.type);
    console.log("------");

    ws.send("Test SEND message!")
}

ws.onmessage = function (data) {
    console.log("MESSAGE");
    console.log("Type: " + data.type);
    console.log("Data: " + data.data);
    console.log("Origin: " + data.origin);
    console.log("------");

    ws.close(1000, "Test closing WebSocket");
}

ws.onclose = function (data) {
    console.log("CLOSE");
    console.log("Type: " + data.type);
    console.log("Code: " + data.code);
    console.log("Reason: " + data.reason);
    console.log("Was clean: " + data.wasClean);
    console.log("------");

    if (typeof Echo !== 'undefined') {
        Echo.done();
    }
}