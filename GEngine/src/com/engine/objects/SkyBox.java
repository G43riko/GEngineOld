package com.engine.objects;

import ggllib.core.Camera;
import ggllib.entity.Entity;
import ggllib.entity.component.PosRotScaleComponent;
import ggllib.render.material.Material;
import ggllib.render.material.Texture2D;
import ggllib.render.model.BorderedModel;
import ggllib.render.model.MaterialedModel;
import ggllib.utils.Loader;
import glib.util.vector.GVector3f;

public class SkyBox extends Entity{
	private static int size = (int)(Math.sqrt(100 * 100 / 4));
	private Camera camera;
	private MaterialedModel materialedModel;
	private GVector3f rotationSpeed = new GVector3f(0, 0.001f, 0);
	
	//CONSTRUCTORS
	
	public SkyBox(Camera camera, Loader loader) {
		this(camera, "textures/skyHD2.jpg", loader);
		
	}
	
	public SkyBox(Camera camera, String fileName, Loader loader) {
		addComponent(new PosRotScaleComponent());
		setScale(new GVector3f(size));
		this.camera = camera;
//		System.out.println(this);

		if(materialedModel == null)
			materialedModel = new MaterialedModel(getBox(1, 1, 1, loader), new Material(new Texture2D(fileName)));
	}
	
	//OVERRIDES
	
	public void update(){
		rotate(rotationSpeed);
		setPosition(camera.getPosition());
	}
	
	
	//GETTERS
	
	public static BorderedModel getBox(int w, int h, int d, Loader loader){
		float[] vertices = new float[]{			
				-w,h,-d,	
				-w,-h,-d,	
				w,-h,-d,	
				w,h,-d,		
				
				-w,h,d,	
				-w,-h,d,	
				w,-h,d,	
				w,h,d,
				
				w,h,-d,	
				w,-h,-d,	
				w,-h,d,	
				w,h,d,
				
				-w,h,-d,	
				-w,-h,-d,	
				-w,-h,d,	
				-w,h,d,
				
				-w,h,d,
				-w,h,-d,
				w,h,-d,
				w,h,d,
				
				-w,-h,d,
				-w,-h,-d,
				w,-h,-d,
				w,-h,d
				};
		float t = 0.125f;  
		float[] texture ={0.25f,0.25f+t,
				 		  0.25f,0.50f+t,
				 		  0.50f,0.50f+t,
				 		  0.50f,0.25f+t,
				 		  
				 		  1.00f,0.25f+t,
				 		  1.00f,0.50f+t,
				 		  0.75f,0.50f+t,
				 		  0.75f,0.25f+t,
				 		  
				 		  0.50f,0.25f+t,
				 		  0.50f,0.50f+t,
				 		  0.75f,0.50f+t,
				 		  0.75f,0.25f+t,
				 		  
				 		  0.25f,0.25f+t,
				 		  0.25f,0.50f+t,
				 		  0.00f,0.50f+t,
				 		  0.00f,0.25f+t,
				 		  
				 		  0.25f,0.00f+t,
				 		  0.25f,0.25f+t,
				 		  0.50f,0.25f+t,
				 		  0.50f,0.00f+t,
				 		  
				 		  0.25f,0.75f+t,
				 		  0.25f,0.50f+t,
				 		  0.50f,0.50f+t,
				 		  0.50f,0.75f+t,};
		
		int[] indices ={0,1,3,	
						3,1,2,	
						
						7,5,4,
						6,5,7,
						
						8,9,11,
						11,9,10,
						
						15,13,12,
						14,13,15,
						
						16,17,19,
						19,17,18,
						
						23,21,20,
						22,21,23};
		
		return loader.loadToVAO(vertices, texture, indices);
	}

	public MaterialedModel getModel() {
		return materialedModel;
	}

	//SETTERS
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public void setRotationSpeed(GVector3f rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}
}
