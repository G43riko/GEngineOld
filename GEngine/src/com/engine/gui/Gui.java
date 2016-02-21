package com.engine.gui;

import ggllib.render.material.Texture2D;
import ggllib.render.model.BorderedModel;
import ggllib.utils.Loader;
import ggllib.utils.Maths;
import glib.util.vector.GMatrix4f;
import glib.util.vector.GVector2f;

public class Gui {
	private static BorderedModel quad; 
	private Texture2D texture;
	private GVector2f position;
	private GVector2f scale;
	
	//CONSTRUCTORS
	
	public Gui(Loader loader, Texture2D texture, GVector2f position, GVector2f scale){
		this.position = position;
		this.texture = texture;
		this.scale = scale;
		
		if(quad == null)
			quad = loader.loadToVAO(new float[]{-1, 1, -1, -1, 1, 1, 1, -1});
	}

	//OTHERS
	
	
	
	//GETTERS
	
	public GMatrix4f getTransformationMatrix(){return Maths.MatrixToGMatrix(Maths.createTransformationMatrix(position, scale));}
	public static BorderedModel getQuad(){return quad;}
	public GVector2f getPosition(){return position;}
	public Texture2D getTexture(){return texture;}
	public GVector2f getScale(){return scale;}
}
