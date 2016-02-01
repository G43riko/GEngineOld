package com.engine.core;

import glib.interfaces.GControllable;
import glib.util.analytics.Performance;

public interface Controllable extends GControllable{
	public RenderingEngine getRenderingEngine();
	public Performance getPerformance();
}
