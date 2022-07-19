var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
	$("#connectMsg").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect(contextPath) {
	//alert(contextPath);
	//var contextPath = '/sb';
    var socket = new SockJS(contextPath+'/chatting');
    //the above works as proxy for WebSocket, if you want ws protocol directly use below syntax ws://<URL>/websocket
	//var socket = new WebSocket('ws://localhost:9090/chatting/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content+JSON.parse(greeting.body).imagePath);
        });
/*		stompClient.subscribe('/topic/chatroom', function (msg) {
            showGreeting(JSON.parse(msg.body).msg+JSON.parse(msg.body).user);
        });*/
    });
}

function connectMsg(contextPath) {
	//alert(contextPath);
	//var contextPath = '/sb';
    var socket = new SockJS(contextPath+'/chatting');
    //the above works as proxy for WebSocket, if you want ws protocol directly use below syntax ws://<URL>/websocket
	//var socket = new WebSocket('ws://localhost:9090/chatting/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/chatroom', function (message) {
            //showGreeting(JSON.parse(message.body).msg+JSON.parse(message.body).user);
			showGreeting(JSON.parse(message.body).message+JSON.parse(message.body).author);
			//debug
			//showGreeting('Hi'+message); //MESSAGE content-length:59 message-id:qrowamgn-0 subscription:sub-0 content-type:text/plain;charset=UTF-8 destination:/topic/chatroom content-length:59 {"author":null,"message":"yes","timestamp":"1602828947159"}
        });
		//additional subscribe
		stompClient.subscribe('/topic/chatDefaultOffset', function (message) {
			showChatDefault(JSON.parse(message.body).message+' ('+JSON.parse(message.body).author+'-'+JSON.parse(message.body).timestamp+')');
        });
    });
}

function connectHistory(contextPath) {
    var socket = new SockJS(contextPath+'/chatting');
    //the above works as proxy for WebSocket, if you want ws protocol directly use below syntax ws://<URL>/websocket
	//var socket = new WebSocket('ws://localhost:9090/chatting/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
		//getHistory(); //place this before below listener if you want initial loading from the beginning a specific topic
        console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/history', function (message) {
			showHistory(JSON.parse(message.body).message+JSON.parse(message.body).author);
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

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function sendMsg() {
	stompClient.send("/app/pubMsg", {}, JSON.stringify({'msg': $("#name").val()}));
	//stompClient.send("/app/sendMessage", {}, JSON.stringify({'msg': $("#name").val()})); //(X)
}

function sendChatDefaultOffset() {
	stompClient.send("/app/pubChatDefaultOffset", {}, JSON.stringify({'msg': $("#name").val()}));
}

function getHistory() { //using redis
	stompClient.send("/app/fromTheBeginning", {}, JSON.stringify({'msg': $("#name").val()}));
}

function getByDateRange() { //using redis
	var params = { 'msg': $("#name").val(), 'minDT': $("#minDT").val(), 'maxDT': $("#maxDT").val() };
	stompClient.send("/app/dateRange", {}, JSON.stringify(params));
}

function startManualCommitListener() {
	stompClient.send("/app/startManualCommitListener", {}, JSON.stringify({'msg': $("#name").val()}));
}

function stopManualCommitListener() {
	stompClient.send("/app/stopManualCommitListener", {}, JSON.stringify({'msg': $("#name").val()}));
}

function startListenerByListenerId() {
	stompClient.send("/app/startListenerByListenerId", {}, JSON.stringify({'msg': $("#name").val()}));
}

function stopListenerByListenerId() {
	stompClient.send("/app/stopListenerByListenerId", {}, JSON.stringify({'msg': $("#name").val()}));
}

function showChatDefault(message) {
    $("#chat").append("<tr><td>" + message + "</td></tr>");
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function showHistory(message) {
    $("#history").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
	$( "#getHistory" ).click(function() { getHistory(); });
	$( "#getByDateRange" ).click(function() { getByDateRange(); });
	$( "#startManualCommitListener" ).click(function() { startManualCommitListener(); });
	$( "#stopManualCommitListener" ).click(function() { stopManualCommitListener(); });
	$( "#startListenerByListenerId" ).click(function() { startListenerByListenerId(); });
	$( "#stopListenerByListenerId" ).click(function() { stopListenerByListenerId(); });
    $( "#connect" ).click(function() { 
	connect($("#contextPath").val()); //for dynamic context path
	});
	$( "#connectChat" ).click(function() { 
	connectChat($("#contextPath").val()); //for dynamic context path
	});
	$( "#connectMsg" ).click(function() { 
	connectMsg($("#contextPath").val()); //for dynamic context path
	});
	$( "#connectHistory" ).click(function() { 
	connectHistory($("#contextPath").val()); //for dynamic context path
	});
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
	$( "#sendMsg" ).click(function() { sendMsg(); });
	$( "#sendChatDefaultOffset" ).click(function() { sendChatDefaultOffset(); });
	
});