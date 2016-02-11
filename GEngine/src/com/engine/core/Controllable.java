package com.engine.core;

import com.engine.rendering.RenderingEngine;

import ggllib.core.Window;
import ggllib.utils.ContentManager;
import glib.interfaces.GControllable;
import glib.util.GOptions;
import glib.util.analytics.Performance;

public interface Controllable extends GControllable{
	public RenderingEngine getRenderingEngine();
	public ContentManager getContentManager();
	public Performance getPerformance();
	public GOptions getOptions();
	public Window getWindow();
	
}
