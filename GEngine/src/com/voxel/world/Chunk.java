package com.voxel.world;

import com.engine.entity.components.PosRotScaleComponent;

import glib.util.vector.GVector3f;

public class Chunk extends AbstractPlace<Block>{
	public final static int NUM_X = 15;
	public final static int NUM_Y = 15;
	public final static int NUM_Z = 15;
	public final static GVector3f NUM = new GVector3f(NUM_X, NUM_Y, NUM_Z);
	
	private Chunk[] neighboards = new Chunk[6];
	
	public Chunk(GVector3f position){
		super(NUM_X, NUM_Y, NUM_Z);
		
		addComponent(new PosRotScaleComponent(position));
	}
	
	public void create(){
		for(int i=0 ; i<NUM_X ; i++)
			for(int k=0 ; k<NUM_Z ; k++)
				for(int j=0 ; j<NUM_Y ; j++)
					set(new Block(new GVector3f(i, j, k), Blocks.getRandom()), i, j, k);
	}
	
	public Chunk getNeighboard(int num){
		return neighboards[num];
	}
	
	public void setNeighboard(int num, Chunk chunk){
		neighboards[num] = chunk;
	}
}
