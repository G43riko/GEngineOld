package com.engine.core;

import org.lwjgl.opengl.Display;

import com.engine.rendering.ToFrameBufferRendering;

import ggllib.render.material.Texture2D;
import ggllib.render.model.BorderedModel;
import ggllib.utils.ContentManager;
import ggllib.utils.Maths;
import glib.util.vector.GMatrix4f;
import glib.util.vector.GVector2f;

public class Screen {
	private static BorderedModel MODEL;
	private Texture2D texture;
	
	private GVector2f position;
	private GVector2f scale;
	
	private ToFrameBufferRendering frameRenderer;
	
	//CONSTRUCTORS
	
	public Screen(ContentManager manager) {
		this(manager, new GVector2f(Display.getWidth(), Display.getHeight()));
	}
	
	public Screen(ContentManager manager, GVector2f resolution) {
		frameRenderer = new ToFrameBufferRendering(resolution);
		texture = frameRenderer.getTexture();
		position = new GVector2f();
		scale = new GVector2f(1);
		
		if(MODEL == null)
			MODEL = manager.loadGuiModel();
	}
	
	//OTHERS

	public void cleanUp(){
		frameRenderer.cleanUp();
	}

	public void startRenderToScreen() {
		frameRenderer.startRenderToFrameBuffer();
	}

	public void stopRenderToScreen() {
		frameRenderer.stopRenderToFrameBuffer();
	}
	
	//GETTERS

	public GMatrix4f getTransformationMatrix() {return Maths.MatrixToGMatrix(Maths.createTransformationMatrix(position, scale));}

	public BorderedModel getModel(){return MODEL;}
	public Texture2D getTexture() {return texture;}

	//SETTERS
	
	public void setTexture(Texture2D texture) {
		this.texture = texture;
	}
}
