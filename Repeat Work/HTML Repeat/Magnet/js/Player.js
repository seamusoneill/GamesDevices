var Player = function (x,y,w,h)
{
	this.x = x;
	this.y = y;
	this.width = w;
	this.height = h;
	this.velocityX = 0;
	this.velocityY = 0;
	this.friction = 1;
	this.magForce = 8;

	this.tickCount = 0;
	this.ticksPerFrame = 200;
	this.animationFrame = 0;

	this.playBump = false;
};

Player.prototype.update = function()
{
	//Gives player velocity in the direction of the magnet
	if (magnet.placed == true)
	{
		distanceX = (magX - (this.x));
		distanceY = (magY - (this.y - (this.height/2)));
		distanceHyp = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

		dirX = distanceX/ distanceHyp;
		dirY = distanceY/ distanceHyp;

		this.velocityX = dirX * this.magForce;
		this.velocityY = dirY * this.magForce;
	};

	this.velocityY += gravity;
	this.velocityX = this.velocityX / this.friction;
	this.checkCollision();
	
	//Prevents sound effect from looping
	if(this.velocityY > 0)
	{
		this.playBump = true;
	}

	//Update position
	this.x += this.velocityX;
	this.y += this.velocityY;


	//Animation
	this.tickCount++;
	if (this.tickCount == this.ticksPerFrame)
	{
		this.ticksPerFrame =10;
		this.animationFrame++;
		this.tickCount =0 ;
	}

	if (this.animationFrame > 3)
		{
			this.ticksPerFrame = 200;
			this.animationFrame = 0;
		}
};

Player.prototype.draw = function(){
	var playerSprite = document.createElement('img');
	playerSprite.src = "./assets/Player.png";
	
	//ctx.drawImage(playerSprite,this.x - this.width/2 ,this.y - this.height/2,this.width,this.height)//,0,0,this.width,this.height);
	ctx.drawImage(playerSprite, 160 * this.animationFrame, 0, 160 , 120, this.x-this.width/2, this.y -this.height/2, this.width, this.height);		
};



Player.prototype.checkCollision = function()
{
	//Player against platform
	for (var i = 0; i < game.numPlats; i++)
	{
		
		if (game.platform[i].y < this.y + this.height/2
			&& game.platform[i].y > this.y
		&& game.platform[i].x < this.x + this.width + 27										//27 is the disance between the edge 
		&& game.platform[i].x+game.platform[i].blockWidth > this.x - this.width/2 + 27) 		//of the wheel and the edge of the sprite.
		{
			
			if (this.velocityY > 0)
			{
				this.velocityY = 0;
				this.friction = 1.025;

				if (this.playBump){
					bump.play();
					this.playBump = false;
				}	
			}
		}
		else if (game.platform[i].y + game.platform[i].blockHeight < this.y + this.height/2
		&& game.platform[i].y + game.platform[i].blockHeight > this.y - this.height/2
		&& game.platform[i].x < this.x  													
		&& game.platform[i].x+game.platform[i].blockWidth > this.x - this.width/2) 		
		{
			if (this.velocityY < 0)
			{
				this.velocityY = 0;
				this.friction = 1.25
			}
		}
		else
		{
			this.friction = 1;
		};
		
		if (game.platform[i].x <= this.x + this.width/2
			&& game.platform[i].x > this.x
			&& game.platform[i].y < this.y
			&& game.platform[i].y + game.platform[i].blockHeight > this.y)
		{
			if (this.velocityX > 0)
			{
				this.velocityX = 0;
			}
		}
		else if (game.platform[i].x + game.platform[i].blockWidth >= this.x - this.width/2
			&&  game.platform[i].x  + game.platform[i].blockWidth < this.x 
			&& game.platform[i].y < this.y
			&& game.platform[i].y + game.platform[i].blockHeight > this.y)
		{
			if (this.velocityX < 0)
			{
				this.velocityX = 0;
			}
		};
	
	};

//Player collides with magnet
	if(game.magnetPlaced > 0)
	{/*
	
		if (magY < this.y + this.height/2 
			&& magY > this.y 
			&& magX < this.x  + this.width								  
			&& magX + magnetSprite.width/2 > this.x - this.width/2) 		
		{
			if (this.velocityY > 0)
			{
				this.velocityY = 0;
				this.friction = 1.025;
			}
		}
		else if (magY + magnetSprite.height < this.y + this.height/2
			&& magY + magnetSprite.height > this.y - this.height/2
			&& magX < this.x  													
			&& magX + magnetSprite.width/2 > this.x - this.width/2) 		
		{
			if (this.velocityY < 0)
			{
				this.velocityY = 0;
				this.friction = 1.25
			}
		}

		if (magX <= this.x + this.width/2
			&& magX > this.x
			&& magY < this.y
			&& magY + magnetSprite.height > this.y)
		{
			if (this.velocityX > 0)
			{
				this.velocityX = 0;
			}
		}
		else if (magX + magnetSprite.width/2 >= this.x - this.width/2
			&&  magX  + magnetSprite.width/2 < this.x 
			&& magY < this.y
			&& magY + magnetSprite.height > this.y)
		{
			if (this.velocityX < 0)
			{
				this.velocityX = 0;
			}
		};*/
	}

	//Player collides with goal
	if (goal.y < this.y + this.height/2
			&& goal.y > this.y
		&& goal.x < this.x + this.width 
		&& goal.x+goal.width > this.x - this.width/2) 		
		{
			levelComplete = true;
		}
		else if (goal.y +goal.height < this.y + this.height/2
		&& goal.y + goal.height > this.y - this.height/2
		&& goal.x < this.x  													
		&& goal.x+goal.width > this.x - this.width/2) 		
		{
			levelComplete = true;
		};
		
		if (goal.x <= this.x + this.width/2
			&&goal.x > this.x
			&& goal.y < this.y
			&& goal.y + goal.height > this.y)
		{
			levelComplete = true;
		}
		else if (goal.x + goal.width >= this.x - this.width/2
			&& goal.x  + goal.width < this.x 
			&& goal.y < this.y
			&& goal.y + goal.height > this.y)
		{
			levelComplete = true;
		};
};