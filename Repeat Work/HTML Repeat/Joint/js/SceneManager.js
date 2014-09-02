var game;
var mainMenu;
var usernameMenu;

var canvas,ctx;

function SceneManager() {
	this.initCanvas();

	this.currentScene = SceneType.SCENE_MENU;
	this.setScene(this.currentScene);
}

var SceneType = {
  SCENE_GAME : "gameScene",
  SCENE_MENU: "mainMenuScene",
  SCENE_USERSNAME: "usernameScene",
  SCENE_WAITING_FOR_PLAYER: "waitingScene"
};

SceneManager.prototype.initCanvas = function(){

	canvas = document.createElement("canvas");
	ctx = canvas.getContext("2d");

	document.body.appendChild(canvas);
    InputManager.connect(document, canvas);

	canvas.width = 800;
	canvas.height = 480;
	ctx.fillRect(0,0,canvas.width,canvas.height);
}

SceneManager.prototype.setScene = function(newScene){
	this.currentScene = newScene;

	if (this.currentScene == SceneType.SCENE_GAME)
	{ 
		InputManager.reset();;
		ctx.clearRect ( 0 , 0 , canvas.width , canvas.height );
		game = new Game();
		game.initWorld();
		game.initAudio();
        
	}

	else if (this.currentScene == SceneType.SCENE_MENU)
	{
		InputManager.reset();
		ctx.clearRect ( 0 , 0 , canvas.width , canvas.height );
		mainMenu = new MainMenu();
		mainMenu.initMenu();
		mainMenu.initAudio();
	}

	else if (this.currentScene == SceneType.SCENE_USERSNAME){
		InputManager.reset();
		ctx.clearRect ( 0 , 0 , canvas.width , canvas.height );
		usernameMenu = new UsernameMenu();
		GameLoopManager.run(usernameMenu.update);
	}
	else if (this.currentScene == SceneType.SCENE_WAITING_FOR_PLAYER)
	{
		//Show player waiting
	}
}

SceneManager.prototype.getScene = function()
{
	return this.currentScene;
}

