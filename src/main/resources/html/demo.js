var ws
function initWs() {
    ws = new WebSocket("ws://localhost:5999");
    ws.onmessage = function (event) {
        console.log('*****');
        document.getElementById("contentId").value += (event.data + "\r\n");
    };
    ws.onclose = function (event) {

    };
    ws.onopen = function (event) {
        ws.send("hello tio server");
    };
    ws.onerror = function (event) {
        console.log(event)
    };
}
function send() {
    var msg = document.getElementById("textId");
    //alert(msg.value);
    ws.send(msg.value);
}
