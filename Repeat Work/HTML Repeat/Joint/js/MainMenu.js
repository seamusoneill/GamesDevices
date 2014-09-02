function MainMenu(){
};

function StartGame(){
	mainMenu.disposeScene();
	sceneManager.setScene("gameScene");
}

MainMenu.prototype.initAudio = function(){
	AudioManager.load({
		'select' : 'assets/sound/select',
		'change' : 'assets/sound/change'
	},
		function()
	{
		GameLoopManager.run(mainMenu.update);
	});
}
MainMenu.prototype.initMenu = function(){
	this.title = "Main Menu";
	this.items = ["Play", "Set Username","Quit"];
	this.selectedItem = 0;
	this.y = 70;
	this.size = 50;
	this.width = 400;
}

MainMenu.prototype.selected = function(numItem){
	if (numItem == 0)
	{
		mainMenu.disposeScene();
		sceneManager.setScene("gameScene");
	}
	else if (numItem == 1)
	{
		mainMenu.disposeScene();
		sceneManager.setScene("usernameScene");
	}
	else if (numItem == 2)
	{
		//TODO No quitting
	}
}

MainMenu.prototype.update = function(elapsed){

	InputManager.padUpdate();

	fps.update(elapsed);

	if (InputManager.padPressed & InputManager.PAD.OK)
	{
		AudioManager.play("select");
		mainMenu.selected(mainMenu.selectedItem);
		return;
	}
	if (InputManager.padPressed & InputManager.PAD.CANCEL)
	{
		return;
	}

	var prevSelected = mainMenu.selectedItem;
	if (InputManager.padPressed & InputManager.PAD.UP)
	{
		mainMenu.selectedItem = (mainMenu.selectedItem + mainMenu.items.length - 1) % mainMenu.items.length;
	}

	if (InputManager.padPressed & InputManager.PAD.DOWN)
	{
		if (mainMenu.selectedItem < mainMenu.items.length -1)
		{
			mainMenu.selectedItem = (mainMenu.selectedItem +  1);
		}
		else
			mainMenu.selectedItem = 0;
	}
	
	if (prevSelected != mainMenu.selectedItem)
	{
		AudioManager.play("change");
	}

	mainMenu.draw();
}

MainMenu.prototype.draw = function(){

	ctx.fillStyle = MakeColor(200,100,100);
	ctx.fillRect(0,0,canvas.width,canvas.height);
	ctx.textAlign = "center";
	ctx.fillStyle = "White";

	var y = this.y;
	if (this.title)
	{
		ctx.font = Math.floor(this.size*1.3).toString() + "px Times New Roman";
		ctx.fillText(this.title, canvas.width/2, y);
		y += this.size;
	}

	for (var i = 0; i < this.items.length; ++i)
	{
		var size = Math.floor(this.size*0.8);
		if (i == this.selectedItem)
		{
			var v = Math.floor(127*Math.sin(GameLoopManager.lastTime*0.04) + 127);
			ctx.fillStyle = "rgba(255,255,"+v.toString()+",255)";
			size = this.size;
		}

		ctx.font = size.toString() + "px Times New Roman";
		y += this.size;
		ctx.fillText(this.items[i], canvas.width/2, y);
		ctx.fillStyle = "White";
	}
}

MainMenu.prototype.disposeScene = function()
{
	GameLoopManager.stop();
	mainMenu = null;

	//TODO unload audio. destroy images.
}

