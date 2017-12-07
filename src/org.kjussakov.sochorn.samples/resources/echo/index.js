// Test the WebSocket polyfill

var ws = new WebSocket("ws://echo.websocket.org");

ws.onopen = function (data) {
    console.log("OPEN");
    console.log("Data: " + data);
    console.log("target: " + data.target);
    console.log("currentTarget: " + data.currentTarget);
    console.log("type: " + data.type);

    ws.send("Test SEND message!")
}

ws.onmessage = function (data) {
    console.log("MESSAGE");
    console.log("Data: " + data);
    console.log("target: " + data.target);
    console.log("currentTarget: " + data.currentTarget);
    console.log("type: " + data.type);
    console.log("data: " + data.data);
    console.log("origin: " + data.origin);

    ws.close(1000, "Test closing WebSocket");
}

ws.onclose = function (data) {
    console.log("CLOSE");
    console.log("Data: " + data);
    console.log("target: " + data.target);
    console.log("currentTarget: " + data.currentTarget);
    console.log("type: " + data.type);
    console.log("code: " + data.code);
    console.log("reason: " + data.reason);
    console.log("wasClean: " + data.wasClean);
}