var magX,magY;
var magnetSprite;

var Magnet = function()
{
	this.placed = false;
};

Magnet.prototype.update = function()
{
};

Magnet.prototype.draw = function()
{

	magnetSprite = document.createElement('img');
	magnetSprite.src = "./assets/Magnet.png";

	ctx.drawImage(magnetSprite,magX, magY);		
};

Magnet.prototype.setPosition = function(x,y){

	magX = x - magnetSprite.width/2;
	magY = y - magnetSprite.height;
	this.placed = true;
}