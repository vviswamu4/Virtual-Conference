var loginPage = document.querySelector('#loginPage');
var usernameInput = document.querySelector('#usernameInput');
var loginBtn = document.querySelector('#loginBtn');
var videoFrame = document.querySelector('#videoFrame');
var callPage = document.querySelector('#callPage');
var callToUsernameInput = document.querySelector('#callToUsernameInput');
var callBtn = document.querySelector('#callBtn');
var hangUpBtn = document.querySelector('#hangUpBtn');
callPage.style.display = "none";
var stream;

var configuration = {
	"iceServers" : [ {
		"url" : "stun:stun2.1.google.com:19302"
	} ]
};

var connection;


loginBtn.addEventListener("click", function(event) {
	var name = usernameInput.value;

	if (name.length > 0) {
		send({
			type : "login",
			name : name
		});
	}

});

var websocketConnection = new WebSocket('ws://localhost:9090/virtual-conference/');

websocketConnection.onopen = function() {
	console.log("Connected to the signaling server");
};

websocketConnection.onmessage = function(msg) {
	console.log("Got message", msg.data);

	var data = JSON.parse(msg.data);

	switch (data.type) {
	case "login":
		handleLogin(data.success);
		break;
	// when somebody wants to call us
	case "offer":
		handleOffer(data.offer, data.name);
		break;
	case "answer":
		handleAnswer(data.answer);
		break;
	// when a remote peer sends an ice candidate to us
	case "candidate":
		handleCandidate(data.candidate);
		break;
	case "leave":
		handleLeave();
		break;
	default:
		break;
	}
};

websocketConnection.onerror = function(err) {
	console.log("Got error", err);
};

function send(message) {
	websocketConnection.send(JSON.stringify(message));
};

callBtn.addEventListener("click", function() {
	var callToUsername = callToUsernameInput.value;
	initializeConnection(callToUsername);
	createOffer(callToUsername);

});

function initializeConnection(name) {

	connection = new webkitRTCPeerConnection(configuration);
	connection.addStream(stream);
	connection.onaddstream = function(e) {
		var remoteVideo = document.createElement("VIDEO"); 
		remoteVideo.src = window.URL.createObjectURL(e.stream);
		videoFrame.appendChild(remoteVideo);
	};
	
	connection.onicecandidate = function(event) {
		if (event.candidate) {
			send({
				type : "candidate",
				candidate : event.candidate,
				name : name
			});
		}

	};

}

function createOffer(name) {
	connection.createOffer(function(offer) {
		send({
			type : "offer",
			offer : offer,
			name : name
		});

		connection.setLocalDescription(offer);

	}, function(error) {
		alert("Error when creating an offer");
	});

}

function handleOffer(offer, name) {
	
	initializeConnection();
	connection.setRemoteDescription(new RTCSessionDescription(offer));

	connection.createAnswer(function(answer) {
		connection.setLocalDescription(answer);

		send({
			type : "answer",
			answer : answer,
			name : name
		});

	}, function(error) {
		alert("Error when creating an answer");
	});
};

function handleLogin(success) {

	if (success === false) {
		alert("Ooops...try a different username");
	} else {
		loginPage.style.display = "none";
		callPage.style.display = "block";

		navigator.webkitGetUserMedia({
			video : true,
			audio : false
		}, function(myStream) {
			stream = myStream;
			localVideo.src = window.URL.createObjectURL(stream);
		}, function(error) {
			console.log(error);
		});
	}
};

function handleAnswer(answer) {
	connection.setRemoteDescription(new RTCSessionDescription(answer));
};

function handleCandidate(candidate) {
	connection.addIceCandidate(new RTCIceCandidate(candidate));
};