#Class that stores the session state
class Session:	
	WAITING_FOR_PLAYER="waitingScene";
	STARTING_GAME="gameScene";

	def __init__(self):		
		self.SceneType = Session.WAITING_FOR_PLAYER
		self.player=list()

	def addPlayer(self, pid):
		
		result = True;
		if(len(self.player) < 2):
			self.player.append(pid)	
			self.sceneManager.setScene(Session.WAITING_FOR_PLAYER)
			#if there are now 2 players
			if(len(self.player) == 2):
				self.setState(Session.STARTING_GAME)		
		elif(len(self.player) == 2):			
			result = False;

		return result;


	def getNumPlayers(self):
		return len(self.player)

	def getState(self):
		return self.SceneType

	def setState(self,SceneType):
		self.SceneType = SceneType

	def getStateAsString(self, state):
		pass

