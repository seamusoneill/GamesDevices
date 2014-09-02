var Platform = function(x,y,w,h)
{
	this.x =x;
	this.y = y;
	this.blockWidth = w;
	this.blockHeight= h;
};

Platform.prototype.draw = function()
{
	ctx.fillStyle = rgb(0,0,0);
	ctx.fillRect(this.x,this.y,this.blockWidth,this.blockHeight);
};
