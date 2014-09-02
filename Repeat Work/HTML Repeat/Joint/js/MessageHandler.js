function MessageHandler(){

}

MessageHandler.prototype.handleMessage = function(evt)
{
	message = JSON.parse(evt.data)
	type = message.type;
	data = message.data;
	console.log(type);
	console.log(data);

	if (type == "scene")
	{
		//IF THIS DOESN'T WORK MAKE SURE DATA IS A STRING
		if (data == SceneType.SCENE_WAITING_FOR_PLAYER)
		{
			sceneManager.setScene(data);
			console.log("WAITING FOR PLAYERS");
		}
		else if(data == SceneType.SCENE_GAME)
		{
			sceneManager.setScene(data);
			console.log("STARTING GAME");
		}
	}

	//Message for when android player touches screen
	if (type == "touch")
	{
		gameScene.opponent.setDestination(evt.x,evt.y);
	}
	if (type == "error")
	{
		console.log("ERROR" + data);
	}
}