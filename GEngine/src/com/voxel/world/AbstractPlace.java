package com.voxel.world;

import java.util.function.Consumer;

import ggllib.entity.Entity;
import glib.util.vector.GVector3f;

public abstract class AbstractPlace<T> extends Entity{
	private GVector3f size;
	private T data[][][];

	@SuppressWarnings("unchecked")
	public AbstractPlace(int x, int y, int z){
		data = (T[][][])new Object[x][y][z];
		size = new GVector3f(x, y, z);
	}
	
	public void foreach(Consumer<T> action){
		for(int i=0 ; i<size.getX() ; i++)
			for(int j=0 ; j<size.getY() ; j++)
				for(int k=0 ; k<size.getZ() ; k++)
					action.accept(data[i][j][k]);
	}
	
	public boolean exist(int x, int y, int z){
		return x >= 0 && y >= 0 && z >= 0 && x < size.getX() && y < size.getY() && z < size.getZ();
	}
	
	public boolean exist(GVector3f v){
		return v.getX() >= 0 && v.getX() < size.getX() &&
			   v.getY() >= 0 && v.getY() < size.getY() && 
			   v.getZ() >= 0 && v.getZ() < size.getZ();
	}
	
	//GETTERS
	
	public T get(GVector3f v){
		return get(v.getXi(), v.getYi(), v.getZi());
	}
	
	public T get(int x, int y, int z){
		return exist(x, y, z) ? data[x][y][z] : null;
	}
	
	//SETTERS
	
	public void set(T b, GVector3f v){
		set(b, v.getXi(), v.getYi(), v.getZi());
	}
	
	public void set(T b, int x, int y, int z){
		if(exist(x, y, z))
			data[x][y][z] = b;
	}
}
