function Client(){

	var host = "localhost";
	var port = 8080;

	this.messageHandler = new MessageHandler();

	this.ws = new WebSocket("ws://" + host + ":" + port + '/wstest');

	this.ws.onmessage = function(evt) { net.messageHandler.handleMessage(evt); };

	this.ws.onclose = function(evt) { console.log('Connection close')};

	this.ws.onopen = function(evt) { console.log('open connection')};
}

Client.prototype.join = function(name) {
	data = {};
	data.type = "join";
	data.pid = name;
	this.ws.send(JSON.stringify(data));
}

Client.prototype.send = function (data)
{
	net.ws.send(JSON.stringify(data));
}