package com.engine.entity.components;

import org.json.JSONObject;

import glib.interfaces.Saveable;
import glib.util.vector.GVector3f;

public class PosRotScaleComponent extends Component implements Saveable{
	private GVector3f position;
	private GVector3f rotation;
	private GVector3f scale;
	
	public PosRotScaleComponent(){
		this(new GVector3f(), new GVector3f(), new GVector3f(1));
	}
	
	public PosRotScaleComponent(GVector3f position){
		this(position, new GVector3f(), new GVector3f(1));
	}

	public PosRotScaleComponent(GVector3f position, GVector3f rotation, GVector3f scale){
		this.position = position == null ? new GVector3f() : position;
		this.rotation = rotation == null ? new GVector3f() : rotation;
		this.scale = scale == null ? new GVector3f(1) : scale;
	}
	
	public PosRotScaleComponent(JSONObject object){
		this(new GVector3f(object.getString("position")), 
			 new GVector3f(object.getString("rotation")), 
			 new GVector3f(object.getString("scale")));
	}
	
	public void move(GVector3f move) {
		position = position.add(move);
	}
	
	public void rotate(GVector3f rotate) {
		rotation = rotation.add(rotate);
	}
	
	public void scale(GVector3f size) {
		scale = scale.add(size);
	}
	
	@Override
	public String toString() {
		return "[position: " + position + ", rotation: " + rotation + ", scale: " + scale + "]";
	}
	
	public JSONObject toJSON(){
		JSONObject json = new JSONObject();
		
		json.put("position", position);
		json.put("rotation", rotation);
		json.put("scale", scale);
		
		return json;
	}
	
	public void setPosition(GVector3f position){this.position = position;}
	public void setRotation(GVector3f rotation){this.rotation = rotation;}
	public void setScale(GVector3f scane){this.scale = scane;}
	
	public GVector3f getPosition(){return position;}
	public GVector3f getRotation(){return rotation;}
	public GVector3f getScale(){return scale;}
}
