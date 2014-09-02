function Player(){
	this.x = 50;
	this.y = canvas.height/4;
	this.width = 64;
	this.height = 64;
	this.speed = 300;
	this.life = 3;

	this.playerSprite;

	this.tickCount = 0;
	this.ticksPerFrame = 8;
	this.animationFrame = 0;
}

Player.prototype.update = function(elapsed)
{
	if (this.life > 0) //player is alive
	{
		// Compute vector from player to mouse, then normalize for speed, then scale with elapsed time.
		var mx = InputManager.lastMouseX-this.x;
		var my = InputManager.lastMouseY-this.y;
		var dx = mx;
		var dy = my;
		var r2 = dx*dx+dy*dy;
		var se = this.speed*elapsed; // Maximum length of movement allowed 
		if (r2 > se*se)
		{
			var rInv = se / Math.sqrt(r2);
			dx *= rInv;
			dy *= rInv;
		}
		
		this.x += dx;
		this.y += dy;
		this.x = Clamp(this.x, 32, canvas.width - 100);
		this.y = Clamp(this.y, 75 + this.height/2, canvas.height - 75 - this.height/2);
	}

	//Animation
	this.tickCount++;
	if (this.tickCount == this.ticksPerFrame)
	{
		this.animationFrame--;
		this.tickCount =0 ;
	}

	if (this.animationFrame < 0)
	{
			this.animationFrame = 3;
	}
}

Player.prototype.draw = function()
{
	ctx.drawImage(this.playerSprite, 54 * this.animationFrame, 0, 53 ,42, this.x - this.width/2, this.y - this.height/2, this.width, this.height);		
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Player2
function Opponent()
{
	this.x = 50;
	this.y = canvas.height/4 * 3;
	this.width = 64;
	this.height = 64;
	this.speed = 300;
	this.life = 3;

	this.destinationX = 0;
	this.destinationY = 0;

	this.opponentSprite;

	this.tickCount = 0;
	this.ticksPerFrame = 8;
	this.animationFrame = 0;
}

Opponent.prototype.update = function(elapsed)
{
	// Compute vector from opponent to server message received, then normalize for speed, then scale with elapsed time.
	var mx = this.destinationX-this.x;
	var my = this.destinationY-this.y;
	var dx = mx;
	var dy = my;
	var r2 = dx*dx+dy*dy;
	var se = this.speed*elapsed; // Maximum length of movement allowed 
	if (r2 > se*se)
	{
		var rInv = se / Math.sqrt(r2);
		dx *= rInv;
		dy *= rInv;
	}
		
	this.x += dx;
	this.y += dy;
	this.x = Clamp(this.x, 32, canvas.width - 100);
	this.y = Clamp(this.y, 75 + this.height/2, canvas.height - 75 - this.height/2);
	

	//Animation
	this.tickCount++;
	if (this.tickCount == this.ticksPerFrame)
	{
		this.animationFrame--;
		this.tickCount =0 ;
	}

	if (this.animationFrame < 0)
	{
			this.animationFrame = 3;
	}
}

Opponent.prototype.draw = function()
{
	ctx.drawImage(this.opponentSprite, 54 * this.animationFrame, 0, 53 ,42, this.x - this.width/2, this.y - this.height/2, this.width, this.height);		
}

Opponent.prototype.setDestination = function(x,y)
{
	this.destinationX = x;
	this.destinationY = y;
}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Obstacles
Obstacle = function(obstacleSprite){
	this.pool = new Array();
	for (var i = 0; i < 40; ++i)
		this.pool.push({ alive:false });
	this.obstacleSprite = obstacleSprite;
}

Obstacle.prototype.createObstacle = function(y, alive){
	for (var s, i =0; (s = this.pool[i]) && s.alive; ++i)
		;
	if (s)
	{
		s.x = canvas.width;
		s.y = y;
		s.alive = true;
	}
}
Obstacle.prototype.update = function(elapsed){
	for (var s, i = 0; s = this.pool[i]; ++i)
	{
		if (s.alive)
		{
			s.x -= 200* (elapsed * 0.96875); //Number keeps it in sync with white lines
			if (s.x < 0 -32)
			{
				s.alive = false;
			}
		}	
	}
}

Obstacle.prototype.draw = function(){
	for (var s, i =0; s=this.pool[i]; i++)
	{
		if(s.alive)
			ctx.drawImage(this.obstacleSprite,s.x -16 ,s.y - 16,32,32);
	}
}

Obstacle.prototype.collide = function(x,y,callback)
{
	for (var s, i = 0; s = this.pool[i];i++)
	{
		if (s.alive)
		{
			var r2 = Pow2(48);
			var dx = x-s.x;
			var dy = y-s.y;
			if(dx*dx + dy*dy < r2)
			{
				if(!callback | !callback(s))
					return s;
			}
		}
	}
	return false;
}