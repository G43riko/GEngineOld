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
	
	public default<T> T getComponent(Class<T> clazz){
		if(components.containsKey(clazz.hashCode()))
			return clazz.cast(components.get(clazz.hashCode()));
		
		GLog.write("požaduje sa nenastavený komponent: " + clazz.getSimpleName());
		return null;
	}
	
	//POS_ROT_SCALE
	
	public default GVector3f	getPosition(){return getComponent(PosRotScaleComponent.class).getPosition();}
	public default GVector3f 	getRotation(){return getComponent(PosRotScaleComponent.class).getRotation();}
	public default GVector3f 	getScale(){return getComponent(PosRotScaleComponent.class).getScale();}

	public default void 	rotate(GVector3f vec){getComponent(PosRotScaleComponent.class).rotate(vec);}
	public default void 	scale(GVector3f vec){getComponent(PosRotScaleComponent.class).scale(vec);}
	public default void 	move(GVector3f vec){getComponent(PosRotScaleComponent.class).move(vec);}
	
	public default void		setPosition(GVector3f position){getComponent(PosRotScaleComponent.class).setPosition(position);}
	public default void		setRotation(GVector3f rotation){getComponent(PosRotScaleComponent.class).setRotation(rotation);}
	public default void		setScale(GVector3f scale){getComponent(PosRotScaleComponent.class).setScale(scale);}
	
	//MODEL_AND_TEXTURE
	
	public default BorderedModel	getBorderedModel(){return getComponent(ModelAndTextureComponent.class).getBorderedModel();}
	public default Material 		getMaterial(){return getComponent(ModelAndTextureComponent.class).getMaterial();}
	
	//LIFE

	public default float 	addHealt(float value){return getComponent(LifeComponent.class).add(value);}
	public default float 	getMaxHealt(){return getComponent(LifeComponent.class).getMaxHealt();}
	public default float 	getPercent(){return getComponent(LifeComponent.class).getPercent();}
	public default float 	getHealt(){return getComponent(LifeComponent.class).getHealt();}
	public default boolean	isAlive(){return getComponent(LifeComponent.class).isAlive();}
}
