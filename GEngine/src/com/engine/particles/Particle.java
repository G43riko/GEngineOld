package com.engine.particles;

import ggllib.core.Camera;
import glib.GConfig;
import glib.util.vector.GVector2f;
import glib.util.vector.GVector3f;

public class Particle {
	private GVector3f position;
	private GVector3f velocity;
	
	private float gravityEffect;
	private float lifeLenght;
	private float rotation;
	private float scale;
	private float distance;
	
	private ParticleTexture texture;
	
	private GVector2f textOffset1 = new GVector2f();
	private GVector2f textOffset2 = new GVector2f();
	private float blendFactor;
	
	private float elapsedTime;

	
	//CONSTRUCTORS
	
	public Particle(ParticleTexture texture, GVector3f position, GVector3f velocity, float gravityEffect, float lifeLenght, float rotation, float scale){
		this.gravityEffect = gravityEffect;
		this.lifeLenght = lifeLenght;
		this.position = position;
		this.velocity = velocity;
		this.rotation = rotation;
		this.texture = texture;
		this.scale = scale;
	}
	
	//OTHERS
	
	public ParticleTexture getTexture(){
		return texture;
	}

	public boolean update(float delta, Camera camera){
		velocity.addToY(GConfig.GRAVITY * gravityEffect * delta);
		position = position.add(velocity.mul(delta));
		updateTextureCoords();
		elapsedTime += delta;
		distance = camera.getPosition().distSQ(position);
		return elapsedTime <= lifeLenght;
	}
	
	private void updateTextureCoords(){
		float lifeFactor = elapsedTime / lifeLenght;
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgress = lifeFactor * stageCount;
		int index1 = (int)Math.floor(atlasProgress);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		this.blendFactor = atlasProgress % 1;
		setTextureOffset(textOffset1, index1);
		setTextureOffset(textOffset2, index2);
	}
	
	private void setTextureOffset(GVector2f offset, int index){
		float column = index % texture.getNumberOfRows();
		float row = index / texture.getNumberOfRows();
		offset.setX(column / texture.getNumberOfRows());
		offset.setY(row / texture.getNumberOfRows());
	}

	//GETTERS

	public GVector2f getTextOffset1(){return textOffset1;}
	public float getDistance(){
		return distance;
	}

	public GVector2f getTextOffset2(){return textOffset2;}
	public float getBlendFactor(){return blendFactor;}
	public GVector3f getPosition(){return position;}
	public float getRotation(){return rotation;}
	public float getScale(){return scale;}

}
