
let stompClient;

const setConnected = connected => {
    $('#connect').prop('disabled', connected);
    $('#disconnect').prop('disabled', !connected);
    $('#conversation').attr('hidden', !connected);
    $('#userinfo').html('');
};

const connect = () => {
    const socket = new SockJS('/websocket-start');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, frame => {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/voice', voice => showReceivedText(voice.body));
        stompClient.subscribe('/topic/statistics', stats => showStats(JSON.parse(stats.body)));
    });
};

const disconnect = () => {
    if (stompClient) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log('Disconnected');
};

const sendWordParasite = () => {
    stompClient.send('/wordparasite/add', {}, $('#wordparasite').val());
};

const showReceivedText = message => $('#userinfo').append('<tr><td>' + message + '</td></tr>');

const showStats = stats => {
    const statsSelector = $('#stats');
    statsSelector.empty();
    Object.keys(stats).forEach(key => statsSelector.append(`<tr><td>${key} : ${stats[key]}</td></tr>`));
};

const cleanStats = () => stompClient.send('/wordparasite/clean', {});

$(function () {
    $('#sendword').click(() => sendWordParasite());
    $('#cleanstats').click(() => cleanStats());

    $('#connect').click(() => connect());
    $('#disconnect').click(() => disconnect());
    connect();
});
