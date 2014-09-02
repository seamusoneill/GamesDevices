var player;
var opponent;

function Game(){
};

Game.prototype.initWorld = function(){
    this.paused = false;
    player = new Player();
    player.playerSprite = document.createElement('img');
    player.playerSprite.src = "./assets/gfx/player1.png";

    opponent = new Opponent();
    opponent.opponentSprite = document.createElement('img');
    opponent.opponentSprite.src = "./assets/gfx/player2.png";

    this.whiteLine = 0;
    this.obstacleSprite = document.createElement('img');
    this.obstacleSprite.src = "./assets/gfx/obstacle.png"

    this.deathTimer = 0;
    this.obstacles = new Obstacle(this.obstacleSprite);
}

Game.prototype.initAudio = function(){
        // Async load audio and start gameplay when loaded
        AudioManager.load(
        {
           'bump'   : 'assets/sound/bump',
           'death' : 'assets/sound/explosion'
        },
            function() 
        {
            GameLoopManager.run(game.update); //Start game after loading audio,
        } );
}

Game.prototype.update = function(elapsed){
    
    fps.update(elapsed);
    
    InputManager.padUpdate();

    if (InputManager.padPressed & InputManager.PAD.OK)
    {
        if (!game.paused)
        {
            game.paused = true;
        }
        else if (game.paused)
        {
            game.paused = false;
        };
    }

    if (!game.paused)
    {
        // Return to menu delay
        if (player.life <= 0)
        {
            game.deathTimer += elapsed;
            if (game.deathTimer >= 3)
            {
                game.disposeScene();
                sceneManager.setScene("mainMenuScene");
                return;
            }
        }

        // Return to menu delay
        if (opponent.life <= 0)
        {
            game.deathTimer += elapsed;
            if (game.deathTimer >= 3)
            {
                game.disposeScene();
                sceneManager.setScene("mainMenuScene");
                return;
            }
        }

        if (RandomInt(50) == 0)
            game.obstacles.createObstacle(RandomFloat(canvas.height - 150) + 75 - 16,true);

        game.obstacles.update(elapsed);

        player.update(elapsed);
        opponent.update(elapsed);

         // Collision detection and response
        if (player.life > 0 && opponent.life > 0)
        {
       
        // Player vs obstacles
            if (player.life > 0)
            {
                game.obstacles.collide(player.x, player.y, function(s) 
                {
                    player.life -= 1;
                    if (player.life > 0)
                    {
                        AudioManager.play("bump");
                    }
                    else if (player.life <= 0)
                    {
                        AudioManager.play("death");
                    }
                    s.alive = false; // Kill obstacle
                    return player.life > 0;
                });
            }
        //Opponent vs obstacles
            if (opponent.life > 0)
            {
                game.obstacles.collide(opponent.x, opponent.y, function(s) 
                {
                    opponent.life -= 1;
                    if (opponent.life > 0)
                    {
                        AudioManager.play("bump");
                    }
                    else if (opponent.life <= 0)
                    {
                        AudioManager.play("death");
                    }
                    s.alive = false; // Kill obstacle
                    return opponent.life > 0;
                });
            }
        }
        game.draw(elapsed);


    }
    
}

Game.prototype.draw = function(elapsed)
{
    // Draw background
    ctx.fillStyle = MakeColor(100,200,100);
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    
    //Draw Road
    ctx.fillStyle = MakeColor(100,100,100);
    ctx.fillRect(0, 75, canvas.width, canvas.height - 150);

    ctx.fillStyle = MakeColor(255,255,255);
    game.whiteLine -= 200*elapsed;
    if (game.whiteLine < -100)
    {
        game.whiteLine = 0;
    }
    for (i =0; i < 9; i++){
        
        var posX = (100 * i) + game.whiteLine;
        if (posX < 0)
        {
            ctx.fillRect(0, canvas.height/2, 50 + posX, 10 );
        }
        else
        {
            ctx.fillRect(posX, canvas.height/2, 50, 10);
        }
    }

    //Draw GUI
    ctx.fillStyle = MakeColor(255,255,255);
    ctx.font = "30px Times New Roman";
    ctx.fillText(player1Name, 100 ,25);
    ctx.fillText("LIVES: " + player.life, 200 ,55);
    ctx.fillText(player2Name,canvas.width -  200 ,25);
    ctx.fillText("LIVES: " + opponent.life, canvas.width - 100 ,55);
    game.obstacles.draw();
    if (player.life > 0)
    {
        player.draw();
    }
    if (opponent.life > 0)
    {
        opponent.draw();
    }
}

Game.prototype.disposeScene = function(){
    
    GameLoopManager.stop();
    player = null;
    opponent = null;
    game = null;
    //TODO unload assets
}