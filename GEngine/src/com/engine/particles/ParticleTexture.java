package com.engine.particles;

import ggllib.render.material.Texture2D;
import glib.util.vector.GVector2f;

public class ParticleTexture {
	private Texture2D texture;
	private int numberOfRows;
	
	//CONSTRUCTORS
	
	public ParticleTexture(Texture2D texture, int numberOfRows){
		this.texture = texture;
		this.numberOfRows = numberOfRows;
	}

	//GETTERS
	
	public Texture2D getTexture(){return texture;}
	public int getNumberOfRows(){return numberOfRows;}
}
