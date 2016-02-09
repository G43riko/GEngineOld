package com.engine.core;

import com.engine.rendering.RenderingEngine;

import ggllib.core.Window;
import glib.interfaces.GControllable;
import glib.util.analytics.Performance;

public interface Controllable extends GControllable{
	public RenderingEngine getRenderingEngine();
	public Performance getPerformance();
	public Window getWindow();
}
