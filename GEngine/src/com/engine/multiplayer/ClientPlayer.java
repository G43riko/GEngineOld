package com.engine.multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import org.json.JSONObject;

import glib.util.GLog;
import glib.util.IDGenerator;

public class ClientPlayer {
	private Socket socket;
	private ObjectInputStream objectReader;
	private ObjectOutputStream objectWritter;
	private String name;
	private int id = IDGenerator.getId();
	
	public ClientPlayer(Socket socket) {
		this.socket = socket;
		try {
			objectWritter = new ObjectOutputStream(socket.getOutputStream());
			objectWritter.flush();
			objectReader = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			GLog.write( "S: " + GLog.ERROR_CLIENT_CONNECT + name, GLog.NETWORKING); 
		}
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Object read(){
		try {
			Object o = objectReader.readObject();
			return o;
		} catch (ClassNotFoundException | IOException e) {
//			GLog.write(GLog.SITE, "S: Nepodarila sa preèíta správa pri hráèovy menom: " + name);
		}
		return null;
	}
	
	public void cleanUp() {
		try {
			socket.close();

			objectReader.close();
			objectWritter.close();
		} catch (IOException e) {
			GLog.write("S: " + GLog.ERROR_CLIENT_CLOSING + name, GLog.NETWORKING);
		}
	}

	public void write(Serializable o, String type){
		JSONObject object = new JSONObject();
		object.put("type", type);
		object.put("msg", o.toString());
		try {
			objectWritter.writeObject(object.toString());
			objectWritter.flush();
		} catch (IOException e) {
			GLog.write("S: " + GLog.ERROR_MSG_SENDING + o, GLog.NETWORKING);
		}
	}

}