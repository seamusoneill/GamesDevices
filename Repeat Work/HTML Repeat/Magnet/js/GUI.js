var GUI = function(){
	
}

GUI.prototype.draw =function(){

	ctx.fillStyle = rgb(255,215,0);
	ctx.fillRect(screensizeX - 150, 0, 150,25);
	ctx.strokeStyle = rgb(0,0,0);
	ctx.lineWidth = 2;
	ctx.strokeRect(screensizeX - 150, 0, 150,25);

	var	magnetSprite = document.createElement('img');
	magnetSprite.src = "./assets/Magnet.png";
	
	if (magnetsPlaced < numMagnets)
	{
		for (var i = 0; i < (numMagnets - magnetsPlaced); i++)
			ctx.drawImage(magnetSprite,screensizeX - 30 * (i + 1),2,20,20);		
	};

	if (paused == true)
	{
		ctx.fillStyle = rgb(0,69,0);
		ctx.fillRect(300, screensizeY/2 - 50, screensizeX/2, 80);
		ctx.strokeStyle = rgb(255,255,255);
		ctx.lineWidth = 2;
		ctx.strokeRect(300, screensizeY/2 - 50, screensizeX/2, 80);
		ctx.font="80px Georgia";
		ctx.fillStyle = rgb(255,255,255);
    	ctx.fillText("Game Paused", 350, screensizeY/2 + 20);
	};

	if (levelComplete == true)
	{
		ctx.fillStyle = rgb(255,69,0);
		ctx.fillRect(300, screensizeY/2 - 50, screensizeX/2, 80);
		ctx.strokeStyle = rgb(0,191,255);
		ctx.lineWidth = 2;
		ctx.strokeRect(300, screensizeY/2 - 50, screensizeX/2, 80);
		ctx.font="80px Georgia";
		ctx.fillStyle = rgb(255,215,0);
    	ctx.fillText("Level Complete!", 320, screensizeY/2 + 11);
	};
};