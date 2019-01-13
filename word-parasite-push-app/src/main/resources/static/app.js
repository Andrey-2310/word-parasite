
let stompClient;

const setConnected = connected => {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    $("#conversation").attr('hidden', !connected);
    $("#userinfo").html("");
};

const connect = () => {
    const socket = new SockJS('/websocket-start');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/voice', function (voice) {
            showGreeting(voice.body);
        });
    });
};

const disconnect = () => {
    if (stompClient) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
};

const sendName = () => {
    stompClient.send("/app/user", {}, JSON.stringify({'name': $("#name").val()}));
};

const showGreeting = message => {
    $("#userinfo").append("<tr><td>" + message + "</td></tr>");
};

const start = () => {
    $.post( "/start" );
};

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#start" ).click(function() { start(); });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
});
