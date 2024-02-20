var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function getFromInput(inputName) {
    return document.getElementById(inputName).value;
}

function connect() {
    let connectionAddr = 'ws://localhost:8080/squid-game-socket?username=';
    connectionAddr = connectionAddr.concat(getFromInput('username'));

    let subscribeAddr = getFromInput('sub_address');
    let token = getFromInput('token');
    console.log(token);

    let socket = new WebSocket(connectionAddr);
    stompClient = Stomp.over(socket);
    stompClient.connect(
        {
            'Authorization': 'Bearer ' + token
        },
        function (frame) {
            setConnected(true);
            console.log('Connected: ' + frame);
            stompClient.subscribe(subscribeAddr, function (greeting) {
                showMessage(greeting);
            });
        });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    let sendDist = getFromInput('send_dist');
    let message = getFromInput('send_obj');
    if (message !== '') {
        message = JSON.parse(getFromInput('send_obj'));
    }

    let token = getFromInput('token');

    stompClient.send(sendDist,
        {
            'Authorization': 'Bearer ' + token
        }, JSON.stringify(message));
}

function showMessage(message) {
    $("#message").append("<tr><td>" + JSON.parse(message.body) + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendMessage();
    });
});