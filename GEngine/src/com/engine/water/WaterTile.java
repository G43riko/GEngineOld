package com.engine.water;

import com.engine.core.CoreEngine;

import ggllib.render.material.Texture2D;
import ggllib.render.model.BorderedModel;
public class WaterTile {
	private static BorderedModel quad;
	public static final float TILE_SIZE = 60;
	
	private float waterSpeed = 0.001f;
	private float height;
	private float x,z;
	
	private float moveFactor = 0;
	
	private Texture2D dudvTexture;
	private Texture2D normalTexture;
	
	public WaterFrameBuffers getFbos(){
		return fbos;
	}

	private WaterFrameBuffers fbos;
	
	public WaterTile(CoreEngine parent, float centerX, float centerZ, float height, WaterFrameBuffers fbos){
		this.fbos = fbos;
		
		if(quad == null)
	        quad = parent.getContentManager().loadWaterPlane();
		
		dudvTexture = parent.getContentManager().loadTexture("waterDUDV.png");
		normalTexture = parent.getContentManager().loadTexture("waterNormal.png");
		
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
	}

	public Texture2D getNormalTexture(){
		return normalTexture;
	}

	public void update(float delta){
		moveFactor += waterSpeed * delta;
		moveFactor %= 1;
		
	}
	
	public float getMoveFactor(){
		return moveFactor;
	}

	public float getWaterSpeed(){
		return waterSpeed;
	}

	public Texture2D getDudvTexture(){
		return dudvTexture;
	}

	public static BorderedModel getQuad(){
		return quad;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}
}
