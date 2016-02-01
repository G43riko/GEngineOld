package com.engine.multiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import glib.GConfig;
import glib.util.GLog;
import glib.util.IDGenerator;
import glib.util.Utils;

public abstract class Server {
	public static final String LEVEL_INFO      = "LEVEL INFO";
	public static final String PLAYER_MOVE     = "PLAYER MOVE";
	public static final String PLAYER_NAME     = "PLAYER NAME";
	public static final String PLAYER_IS_READY = "PLAYER IS READY";
	public static final String PUT_HELPER      = "PUT HELPER";
	public static final String HIT_BLOCK       = "BLOCK CHANGE";
	public static final String PUT_BOMB 	   = "PUT BOMB";
	
	private HashMap<String, ClientPlayer> clients = new HashMap<String, ClientPlayer>();
	
	private ServerSocket serverSocket;
	private boolean readerIsRunning = true;
	private boolean accepterIsRunning = true;
	private Thread acceptThread;
	private Thread listenThread;
	
	public Server(){
		try {
			serverSocket = new ServerSocket(GConfig.PORT);
		} catch (IOException e) {
			GLog.write("S: " + GLog.ERROR_SERVER_INIT, GLog.NETWORKING);
		}
		
		listen();
		accept();
	}
	
	private void listen() {
		listenThread = new Thread(new Runnable(){
			public void run(){
				while(readerIsRunning){
					new HashMap<String, ClientPlayer>(clients).entrySet()
															  .stream()
															  .map(a -> a.getValue())
															  .forEach(a -> read(a));

					Utils.sleep(1);						 
				}
			}
		});
		listenThread.start();
	}
	
	private void read(ClientPlayer a){
		Object o = a.read();
		if(o == null)
			return;
		if(o instanceof String)
			processMessage(String.valueOf(o), a);
		GLog.write("S:  " + GLog.OKEY_MSG_RECIEVED + o, GLog.MESSAGES);
	}
	
	private void accept(){
		acceptThread = new Thread(new Runnable(){
			public void run(){
				while(accepterIsRunning){
					try {
						Socket client = serverSocket.accept();
						ClientPlayer c = new ClientPlayer(client);
						clients.put(c.getId() + "", c);
						
						GLog.write("S: " + GLog.OKEY_CLIENT_CONNECT, GLog.NETWORKING);
						
					} catch (IOException e) {
						GLog.write("S: " + GLog.ERROR_SERVER_CONN, GLog.NETWORKING);	
					}
				}
			}
		});
		acceptThread.start();
	}
	
	protected void renameClient(String oldName, String newName){
		ClientPlayer temp = clients.get(oldName); 
		temp.setName(newName);
		clients.remove(oldName);
		clients.put(newName, temp);
	}
	
	public void write(String o, String type){
		new HashMap<String, ClientPlayer>(clients).entrySet()
												  .stream()
												  .forEach(a -> a.getValue().write(o, type));
	}
	
	protected void writeExcept(String o, String type, ClientPlayer client) {
		new HashMap<String, ClientPlayer>(clients).entrySet()
		  										  .stream()
		  										  .filter(a -> a.getValue().getId() != client.getId())
		  										  .forEach(a -> a.getValue().write(o, type));
	}
	
	public void cleanUp() {
		readerIsRunning = false;
		accepterIsRunning = false;
		
		clients.entrySet()
		       .stream()
			   .forEach(a -> a.getValue().cleanUp());
		
		clients.clear();
		
		try {
			if(serverSocket != null)
				serverSocket.close();
		} catch (IOException e) {
			System.out.println("nepodarilo sa zavrie server");
		}
		serverSocket = null;
	}

	protected abstract void processMessage(String txt, ClientPlayer client);
	
	//GETTERS
	
	public int getNumberOfClients(){
		return clients.size();
	}
}