package com.voxel.world;

import glib.util.vector.GVector3f;

public class World extends AbstractPlace<Chunk>{
	private final static int NUM_X = 3;
	private final static int NUM_Y = 1;
	private final static int NUM_Z = 3;
	
	public World(){
		super(NUM_X, NUM_Y, NUM_Z);
	}

	public Block getBlock(int x, int y, int z){
		return getBlock(new GVector3f(x, y, z));
	}
	
	public Block getBlock(GVector3f v){
		Chunk c = get(v.div(Chunk.NUM));
		return c == null ? null : c.get(v.mod(Chunk.NUM));
	}
}
