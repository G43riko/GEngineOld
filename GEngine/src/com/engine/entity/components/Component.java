package com.engine.entity.components;

import com.engine.entity.Componentable;
import com.engine.entity.Entity;

import glib.interfaces.InteractableGL;

public abstract class Component implements InteractableGL{
//	public final static int TEXT_AND_MODEL 	= ModelAndTextureComponent.class.hashCode();
//	public final static int POS_ROT_SCALE 	= PosRotScaleComponent.class.hashCode();
//	public final static int PLAYER_INPUT 	= PlayerInputComponent.class.hashCode();
//	public final static int RENDERABLE 		= RenderComponent.class.hashCode();
	private Componentable parent;
	private int name = this.getClass().hashCode();
	

	public int getID() {return name;}
	
	public void setParent(Componentable parent) {this.parent = parent;}

	public Componentable getParent() {return parent;}

	@Override
	public void update(float delta) {
	}

	@Override
	public void input() {
	}
}
