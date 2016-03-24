package com.engine;

import ggllib.entity.Entity;
import ggllib.entity.component.PosRotScaleComponent;
import ggllib.render.model.BorderedModel;
import ggllib.utils.Loader;
import glib.util.vector.GVector3f;

public class GLine extends Entity{
	private final static int[] INDICES = new int[]{0, 1};
	protected GVector3f a;
	protected GVector3f b;
	protected BorderedModel model;

	protected GVector3f color;
	
	//CONSTRUCTORS
	
	public GLine(GVector3f a, GVector3f b, Loader loader) {
		addComponent(new PosRotScaleComponent(b));
		this.a = a;
		this.b = b;
		this.model = createModel(loader);
		this.color = new GVector3f(1,0,1);
	}

	//CREATORS
	
	private BorderedModel createModel(Loader loader){
		float[] position = new float[]{a.getX(), a.getY(), a.getZ(), b.getXi(), b.getYi(), b.getZi()};
		return loader.loadToVAO(position, INDICES);
	}
	
	//GETTERS
	
	public GVector3f getColor() {
		return color;
	}
	
	public BorderedModel getModel() {
		return model;
	}

}
