function Menu(){
	this.initialise();
};

Menu.prototype.initialise = function(){
	this.title = "Main Menu";
	this.first = "Play";

	this.highlight = false;
};

Menu.prototype.update = function(){
	this.draw();
};

Menu.prototype.draw = function()
{	//Background
	ctx.fillStyle = rgb(51,0,102);
	ctx.fillRect(0,0, canvas.width,canvas.height);
	
	ctx.font="80px Georgia";
	ctx.fillStyle = rgb(255,215,0);
    ctx.fillText(this.title, 320, screensizeY/4);


	ctx.strokeStyle = rgb(0,0,0);
	ctx.strokeRect(screensizeX/2 - 120, screensizeY/4 + 40,100,50);
	ctx.font = "50px Georgia";

	if (!this.highlight)
	{
	ctx.fillStyle = rgb(255,255,255);
	ctx.fillRect(screensizeX/2 - 120, screensizeY/4 + 40,100,50);
    ctx.fillStyle = rgb(0,0,0);
    ctx.fillText(this.first, screensizeX/2 - 120, screensizeY/4 + 80);
	}
	else if (this.highlight)
	{
		ctx.fillStyle = rgb(0,0,0);
		ctx.fillRect(screensizeX/2 - 120, screensizeY/4 + 40,100,50);
    	ctx.fillStyle = rgb(255,0,0);
    	ctx.fillText(this.first, screensizeX/2 - 120, screensizeY/4 + 80);
	}

	//Border
	ctx.strokeStyle = rgb(0,0,0);
	ctx.strokeRect(0,0,canvas.width,canvas.height);
};