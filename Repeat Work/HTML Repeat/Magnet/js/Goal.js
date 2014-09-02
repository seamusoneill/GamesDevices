var Goal =function(x,y,w,h){
	this.x = x;
	this.y = y;
	this.width = w;
	this.height = h;
};

Goal.prototype.draw = function(){
	ctx.fillStyle = rgb(238,203,173);
	ctx.fillRect(this.x,this.y, this.width, this.height);
	ctx.strokeStyle = rgb(0,0,0);
	ctx.lineWidth = 0.5;
	ctx.strokeRect(innerWidth - 150, 0, 150,25);
};