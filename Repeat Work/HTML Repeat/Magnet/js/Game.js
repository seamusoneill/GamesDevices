var canvas,ctx;
var player;
var goal;
var gravity = 9.81;
var magnet;
var numMagnets, magnetsPlaced;
var gui;
var goal;
var screensizeX =1200,screensizeY = 600;
var paused = false;
var levelComplete = false;

var bump, music, win, p;

var menu;
var gameOn = false;

function Game(){
};

Game.prototype.initWorld = function()
{	
	menu = new Menu();
	this.blockHeight = 64;
	this.numPlats = 3;
	this.platform = new Array(this.numPlats);
	this.platform[0] = new Platform(0, screensizeY - this.blockHeight, screensizeX, this.blockHeight); //floor
	this.platform[1] = new Platform(200, 120 + 60, screensizeX/3, this.blockHeight);
	this.platform[2] = new Platform(200, 0, this.blockHeight, 180);

	player = new Player(360,120,160,120);
    
    magnet = new Magnet();
    magnetsPlaced = 0;
    numMagnets = 2;

    goal = new Goal(200,screensizeY - this.blockHeight - 140,180,140);

    gui = new GUI();

};

Game.prototype.initCanvas = function(){
	canvas = document.createElement('canvas');
	ctx = canvas.getContext('2d');
	document.body.appendChild(canvas);

	document.addEventListener("mousedown",doMousedown,false);
	document.addEventListener("mousemove",doMousemove,false);
	document.addEventListener("touchStart",doMousedown,false);

	//setting canvas size for screen size
	canvas.width = screensizeX; //Note window.innerWidth seems to make the canvas too big, these variable are chosen from experience
	canvas.height = screensizeY;
};
Game.prototype.initAudio = function()
{
	
	music = new Audio("assets/audio/Tundra.m4a"); // buffers automatically when created
		
	menuSelect = new Audio("assets/audio/MenuSelect.wav");
	bump = new Audio('assets/audio/Bump2.wav');
	win = new Audio('assets/audio/LevelComplete.wav');
	p = new Audio('assets/audio/Pause.wav');

	this.winPlayed = false;
	this.pausePlayed = false;
	this.menuPlayed = false;
};

function doMousedown(event)
{
	if (gameOn)
	{
		if(paused == false)
		{
			if (magnetsPlaced < numMagnets)
			{
				magnet.setPosition(event.x,event.y);
				magnetsPlaced++;
			}
		}
	}
	else if (!gameOn && event.x > screensizeX/2-120 
		&& event.x < screensizeX/2 -20
		&& event.y > screensizeY/4 + 40
		&& event.y < screensizeY/4 + 90)
	{
		gameOn = true;
	};	
};

function doMousemove(event)
{
	if (!gameOn && event.x > screensizeX/2-120 
		&& event.x < screensizeX/2 -20
		&& event.y > screensizeY/4 + 40
		&& event.y < screensizeY/4 + 90)
	{
		menu.highlight = true;
		if (!menuPlayed){
			menuSelect.play();
			menuPlayed = true
		}	
	}
	else
	{
		menu.highlight = false;
		menuPlayed =  false;
	}
};

Game.prototype.update = function()
{
	magnet.update();
	player.update();


	music.play();

	if (levelComplete)
	{
		music.pause();
		if(this.winPlayed == false)
		{
			win.play();
			this.winPlayed = true;
		}
	}
};

Game.prototype.gameLoop = function(){

	ctx.clearRect ( 0 , 0 , screensizeX , screensizeY );
	if (gameOn)
	{
		game.draw();
	
		if (paused == false)
		{
			game.update();
			this.pausePlayed = false;
		}
		else if(paused == true)
		{
			music.pause();
			if (!this.pausePlayed)
			{ 
				p.play();
				this.pausePlayed = true;
			};
		};

		
	}
	else
	{
		menu.update();
	};

	window.requestAnimFrame(game.gameLoop);
};

Game.prototype.draw = function(){
	//Draw a backgound, should be always drawn first
	ctx.fillStyle = rgb(44,22,8);
	ctx.fillRect(0,0, canvas.width,canvas.height);
	goal.draw();

	
	for (var i = 0; i < this.numPlats; i++)
	{
		this.platform[i].draw();
	};	
	


	magnet.draw();
	player.draw();
	gui.draw();
	


	//Draw a border, should be always drawn last
	ctx.strokeStyle = rgb(0,0,0);
	ctx.strokeRect(0,0,canvas.width,canvas.height);
};