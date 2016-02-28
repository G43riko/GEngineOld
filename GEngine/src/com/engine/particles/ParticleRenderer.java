package com.engine.particles;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

import ggllib.core.Camera;
import ggllib.render.model.BorderedModel;
import ggllib.utils.Loader;

public class ParticleRenderer {
	
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	
	private BorderedModel quad;
	
	protected ParticleRenderer(Loader loader, Matrix4f projectionMatrix){

	}
	
	protected void render(List<Particle> particles, Camera camera){

	}

	//The code below is for the updateModelViewMatrix() method
	//modelMatrix.m00 = viewMatrix.m00;
	//modelMatrix.m01 = viewMatrix.m10;
	//modelMatrix.m02 = viewMatrix.m20;
	//modelMatrix.m10 = viewMatrix.m01;
	//modelMatrix.m11 = viewMatrix.m11;
	//modelMatrix.m12 = viewMatrix.m21;
	//modelMatrix.m20 = viewMatrix.m02;
	//modelMatrix.m21 = viewMatrix.m12;
	//modelMatrix.m22 = viewMatrix.m22;

	
	private void prepare(){

	}
	
	private void finishRendering(){

	}

}
