package com.voxel.world;

import ggllib.entity.Entity;
import ggllib.entity.component.PosRotScaleComponent;
import glib.util.vector.GVector3f;

public class Block extends Entity{
	private Blocks type;
	
	public Block(GVector3f position, Blocks type){
		this.type = type;	
		
		addComponent(new PosRotScaleComponent());
		
		setPosition(position);
	}

	public Blocks getType() {
		return type;
	}

	public boolean isActive(){
		return false;
	}
	
	public boolean isVisible(int i){
		return false;
	}
}
