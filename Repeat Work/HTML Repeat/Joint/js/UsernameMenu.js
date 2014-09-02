var player1Name = "PLAYER 1";
var player2Name = "PLAYER 2"

function UsernameMenu(){
	player1Name = "";
	this.currentKey;
	this.previousKey;
}

UsernameMenu.prototype.update = function(elapsed)
{	
	InputManager.padUpdate();
	//Currently doesn't allow for two of the same letters in a row without a non letter button pressed
	//usernameMenu.inputTimer += elapsed
	//if usernameMenu.inpurTimer > 1;{
	//usernameMenu.allowInput = true;} 

	usernameMenu.currentKey = InputManager.lastKeyPressed;

	if (usernameMenu.currentKey != usernameMenu.previousKey && player1Name.length < 10
	 && usernameMenu.currentKey <= 90 && usernameMenu.currentKey >= 65)
	{
		player1Name += "" + String.fromCharCode(usernameMenu.currentKey);
	}

	if (usernameMenu.currentKey == InputManager.KEY.BACKSPACE && player1Name.length > 0 && usernameMenu.currentKey != usernameMenu.previousKey)
	{
		var temp = player1Name.substr(0,player1Name.length - 1)
		player1Name = temp;
	}

	usernameMenu.draw();
	usernameMenu.previousKey = usernameMenu.currentKey;

	if (InputManager.padPressed & InputManager.PAD.CANCEL)
	{
		usernameMenu.disposeScene();
		sceneManager.setScene("mainMenuScene");
	}
}

UsernameMenu.prototype.draw = function(){

	ctx.fillStyle = MakeColor(200,100,100);
	ctx.fillRect(0,0,canvas.width,canvas.height);
	ctx.textAlign = "center";
	ctx.fillStyle = "White";
	ctx.font = "50px Times New Roman";
	ctx.fillText(player1Name, canvas.width/2, canvas.height/2);
}

UsernameMenu.prototype.disposeScene = function()
{
	GameLoopManager.stop();
	usernameMenu = null;

	//TODO unload audio. destroy images.
}