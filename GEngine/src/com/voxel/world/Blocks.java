package com.voxel.world;

public enum Blocks {
	DIRT("dirt"), 
	WATER("water"), 
	GRASS("grass");
	
	private final String name;
	
	private Blocks(String name) {
		this.name = name;
	}

	public static Blocks getRandom(){
		return DIRT;
	}
	public String getName() {return name;}
}
