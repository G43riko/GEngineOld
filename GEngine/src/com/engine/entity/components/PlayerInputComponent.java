package com.engine.entity.components;

import ggllib.core.Input;
import glib.util.vector.GVector3f;

public class PlayerInputComponent extends Component{
	private float speed = 5;
	
	
	@Override
	public void update(float delta) {
		PosRotScaleComponent posRotScale = getParent().getComponent(PosRotScaleComponent.class);
		
		if(posRotScale == null)
			return;
		
		if(Input.isKeyDown(Input.KEY_S))
			posRotScale.move(new GVector3f(0, -speed, 0));
		
		if(Input.isKeyDown(Input.KEY_W))
			posRotScale.move(new GVector3f(0, speed, 0));
	}
	
}
