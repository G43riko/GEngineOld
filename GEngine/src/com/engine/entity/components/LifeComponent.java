package com.engine.entity.components;

import glib.math.GMath;

public class LifeComponent extends Component{
	private float healt;
	private float maxHealt;
	private boolean relative;
	private final long created = System.currentTimeMillis();
	
	//CONSTRUCTORS
	
	public LifeComponent(float maxHealt) {
		this(maxHealt, false);
	}
	
	public LifeComponent(float maxHealt, boolean relative) {
		this.maxHealt = healt = maxHealt;
		this.relative = relative;
	}
	

	//ADDERS
	
	public float add(float value){
		return this.healt += value;
	}
	
	public float addPercent(float value){
		return add(maxHealt / 100f * value);
	}
	
	//OTHERS
	
	public float fix(){
		return healt = GMath.between(healt, 0, maxHealt);
	};
	
	//GETTERS
	
	public float getHealt() {return relative ? healt : (System.currentTimeMillis() - created);}
	public float getPercent(){return maxHealt / 100f * healt;}
	public float getMaxHealt() {return maxHealt;}

	public boolean isAlive(){return healt <= 0;}
}
