from tornado import websocket, web, ioloop, httpserver
import tornado
import ast #to convert unicode type to dict type
from Session import Session
import json

#list of WebSocket connections
connections={}

session = Session()

class WSHandler(tornado.websocket.WebSocketHandler):
	
	def open(self):
		print ("WebSocket opened")
		print ("from %s" %self.request.remote_ip)
		  
	def on_message(self, message):
		#if socket id not defined yet, set it to 0
		if( hasattr(self,"id") == False ):
			self.id = 0
		
		#When a message comes in on a socket,the player id can be obtained from the socket	
		#and passed on to the handler
		messageHandler.handleIncomingMsg(message,self,self.id)
		
 
	def on_close(self):
	    print ("WebSocket closed")

class MessageHandler:
	def __init__(self):
		pass

	def handleIncomingMsg(self, data, socket,pid):
		try:
			print ('message received %s' %data)
			
			#converts the unicode data that arrives into a dict
			data = ast.literal_eval(data)
			
			type = data['type']

		except :
			print ("Unexpected error:" +  sys.exc_info()[0])
			type = 'error'
			print('except')
					

		if type == "join":
			#add to connection list
			self.addToConnectionList(socket, data)
			success = session.addPlayer(data['pid'])
						
			if(success):
				#self.sendMessage(data['pid'], "state", str(session.getState())  )
				self.sendToAll("scene", str(session.getState())  )
			else:
				self.sendMessage(data['pid'], "error", "No available space: Two players already in the game!")  
		
		elif type == "mouseclick":
			self.sendToOtherPlayer(pid,type,data);
			

		elif type == "updateState":
			self.updateState(data, pid)
		else:
			msg = 'Error reading game request. Please make sure message type is either join, updateState, or...'
			message={'type':'error', "data":msg}
			print ('Error reading game request.')
			
	#Adds new player to the list of connected players
	def addToConnectionList(self, socket, message):
		socket.id= message['pid']
		connections[socket.id]=socket
		print(str(socket.id) + " joined")

	#add in types 
	def sendMessage(self,pid,type,data):
		try:
			msg=dict()
			msg["type"]=type;
			msg["data"]=data;
			msg=json.dumps(msg)
			connections[pid].write_message(msg)
		except KeyError:
			print("Player " + str(pid) + " isn't connected")
	
	#Assuming two players, sends to the player that isn't my_pid
	def sendToOtherPlayer(self,my_pid,type,data):
		for pid in connections:
			#print(my_pid)
			#print(pid)
			if my_pid != pid:		
				#print("equals")		
				self.sendMessage(pid,type,data)

	#Sends to all connected players
	def sendToAll(self,type,data):
		for pid in connections:
			self.sendMessage(pid,type,data)





#needs to be after the class def
messageHandler = MessageHandler();
 

app= tornado.web.Application([
	#map the handler to the URI named "wstest"
	(r'/wstest', WSHandler),
])
 
if __name__ == '__main__':
	app.listen(8080)
	tornado.ioloop.IOLoop.instance().start()