package com.engine.particles;

import ggllib.render.model.BorderedModel;
import glib.GConfig;
import glib.util.vector.GVector3f;

public class Particle {
	private GVector3f position;
	private GVector3f velocity;
	
	private float gravityEffect;
	private float lifeLenght;
	private float rotation;
	private float scale;
	
	private float elapsedTime;

	
	//CONSTRUCTORS
	
	public Particle(GVector3f position, GVector3f velocity, float gravityEffect, float lifeLenght, float rotation, float scale){
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLenght = lifeLenght;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	//OTHERS
	
	public boolean update(float delta){
		velocity.addToY(GConfig.GRAVITY * gravityEffect * delta);
		position = position.add(velocity.mul(delta));
		
		elapsedTime += delta;
		
		return elapsedTime <= lifeLenght;
	}

	//GETTERS
	
	public GVector3f getPosition(){return position;}
	public float getRotation(){return rotation;}
	public float getScale(){return scale;}

}
