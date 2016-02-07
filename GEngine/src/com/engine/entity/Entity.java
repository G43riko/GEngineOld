package com.engine.entity;

import org.json.JSONObject;

import glib.interfaces.InteractableGL;
import glib.interfaces.Saveable;

public class Entity implements InteractableGL, Saveable, Componentable{
	@Override
	public void update(float delta) {
		components.entrySet().stream()
							 .map(a -> a.getValue())
							 .forEach(a -> a.update(delta));
	}

	@Override
	public void input() {
		components.entrySet().stream()
							 .map(a -> a.getValue())
							 .forEach(a -> a.input());
	}
	
	@Override
	public String toString() {
		return components.toString();
	}
	
	public JSONObject toJSON(){
		JSONObject json = new JSONObject();
		
		return json;
	}
}
