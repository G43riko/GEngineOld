package com.engine.multiplayer;

import org.json.JSONObject;

import glib.network.tcp_server_client.GServer;

public class Server extends GServer{

	public Server(String name) {
		super(name);
	}

	@Override
	protected void processMessage(JSONObject txt) {
		
	}
}
