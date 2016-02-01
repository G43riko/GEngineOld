package com.engine.entity;

import java.util.HashMap;
import java.util.Map;

import com.engine.entity.components.Component;
import com.engine.entity.components.LifeComponent;
import com.engine.entity.components.ModelAndTextureComponent;
import com.engine.entity.components.PosRotScaleComponent;

import ggllib.object.BorderedModel;
import ggllib.render.material.Material;
import glib.util.GLog;
import glib.util.vector.GVector3f;

public interface Componentable {
	public Map<Integer, Component> components = new HashMap<Integer, Component>();
	
	public default boolean hasComponent(Class<?> clazz){
		return components.containsKey(clazz.hashCode());
	}
	
	public default void addComponent(Component component){
		component.setParent(this);
		components.put(component.getID(), component);
	}
	
	public default<T> T getComponen(Class<T> clazz){
		if(components.containsKey(clazz.hashCode()))
			return clazz.cast(components.get(clazz.hashCode()));
		
		GLog.write("požaduje sa nenastavený komponent: " + clazz.getSimpleName());
		return null;
	}
	
	//POS_ROT_SCALE
	
	public default GVector3f	getPosition(){return getComponen(PosRotScaleComponent.class).getPosition();}
	public default GVector3f 	getRotation(){return getComponen(PosRotScaleComponent.class).getRotation();}
	public default GVector3f 	getScale(){return getComponen(PosRotScaleComponent.class).getScale();}

	public default void 	rotate(GVector3f vec){getComponen(PosRotScaleComponent.class).rotate(vec);}
	public default void 	scale(GVector3f vec){getComponen(PosRotScaleComponent.class).scale(vec);}
	public default void 	move(GVector3f vec){getComponen(PosRotScaleComponent.class).move(vec);}
	
	//MODEL_AND_TEXTURE
	
	public default BorderedModel	getBorderedModel(){return getComponen(ModelAndTextureComponent.class).getBorderedModel();}
	public default Material 		getMaterial(){return getComponen(ModelAndTextureComponent.class).getMaterial();}
	
	//LIFE

	public default float 	addHealt(float value){return getComponen(LifeComponent.class).add(value);}
	public default float 	getMaxHealt(){return getComponen(LifeComponent.class).getMaxHealt();}
	public default float 	getPercent(){return getComponen(LifeComponent.class).getPercent();}
	public default float 	getHealt(){return getComponen(LifeComponent.class).getHealt();}
	public default boolean	isAlive(){return getComponen(LifeComponent.class).isAlive();}
}
