package com.engine.multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.json.JSONObject;

import glib.GConfig;
import glib.util.GLog;

public abstract class Client {
	private ObjectInputStream objectReader;
	private ObjectOutputStream objectWritter;

	private Socket socket;
	private String name;
	private boolean readerIsRunning = true;
	
	public Client(String name) {
		this.name = name;
		try {
			socket = new Socket("localhost", GConfig.PORT);
			objectWritter = new ObjectOutputStream(socket.getOutputStream());
			objectWritter.flush();
			objectReader = new ObjectInputStream(socket.getInputStream());
			GLog.write("C: " + GLog.OKEY_CLIENT_CONNECT, GLog.NETWORKING);
		} catch (IOException e) {
			GLog.write("C: " + GLog.ERROR_CLIENT_CONNECT + name, GLog.NETWORKING);
		}
		
		listen();
	}
	
	public abstract void sendPlayerInfo();

	protected abstract void processMessage(String txt);
	
	private void listen(){
		Thread listenThread = new Thread(new Runnable(){
			public void run() {
				while(readerIsRunning){
					Object o = read();
					if(o != null){
						GLog.write("C:  " + GLog.OKEY_MSG_RECIEVED + o, GLog.MESSAGES);
						processMessage(String.valueOf(o));
					}
						
				}
			}
		});
		listenThread.start();
	}
	
	private Object read(){
		try {
			return objectReader.readObject();	
		} catch (ClassNotFoundException | IOException e) {
			return null;
		}
	}
	
	
	
	public void write(String o, String type){
		JSONObject object = new JSONObject();
		object.put("type", type);
		object.put("msg", o.toString());
		try {
			objectWritter.writeObject(object.toString());
		} catch (IOException e) {
			GLog.write("C: " + GLog.ERROR_MSG_SENDING + o, GLog.MESSAGES);
		}
	}
	

	public void cleanUp() {
		readerIsRunning = false;
		try {
			if(socket != null)
				socket.close();
			
			objectReader.close();
			objectWritter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}