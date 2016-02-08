package com.engine.multiplayer;

import org.json.JSONObject;

import glib.network.tcp_server_client.GClient;

public class Client extends GClient{

	public Client(String name) {
		super(name);
	}

	@Override
	protected void processMessage(JSONObject txt) {

	}

}
