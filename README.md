# WebSocket polyfill for Nashorn

This is [WHATWG Web sockets](https://html.spec.whatwg.org/multipage/web-sockets.html) API implementation for Nashorn JavaScript engine.

## Use cases

There are use cases where one needs to run a piece of client code in a JVM client instead of in a real browser.
One example of such scenario is to (load-) test your back-end services by simulating client connections using Nashorn-based clients.
If your client and back-end code communicate through Web sockets then you will have to use WebSocket polyfill because Nashorn engine does not provide one out of the box.

## Dependencies

Sochorn polyfill is a wrapper around the new Java 9's HttpClient module - `jdk.incubator.httpclient`
The wrapper implements the [WHATWG Web sockets](https://html.spec.whatwg.org/multipage/web-sockets.html) API and is packaged as a Java 9 module - `org.kjussakov.sochorn`

Therefore the only dependency is JDK 9.

## Limitations

Sending and receiving binary messages is not yet supported. (See the `TODOs` in the source code).

## Usage

Sochorn defines two Java 9 modules - the actual polyfill (`org.kjussakov.sochorn`) and a sample application using the WebSocket polyfill (`org.kjussakov.sochorn.samples`).

The sample application demonstrates how to run client code using WHATWG WebSocket API in Nashorn using the polyfill.

### Build and run the sample application 

Since there are no external dependencies and at the time of writing Gradle (current version 4.2) does not have first-class support for Java 9 modules the project does not use any external build tool. The only prerequisite is to have JDK 9 installed with `java` and `javac` available on the path.

#### Compile the two modules

> javac -d mods --module-source-path src $(find src -name "*.java")

#### Copy the JavaScript resources

> cp -r src/org.kjussakov.sochorn.samples/resources/ mods/org.kjussakov.sochorn.samples/resources

#### Run the Echo sample application

> java --module-path mods -m org.kjussakov.sochorn.samples/org.kjussakov.sochorn.samples.Echo