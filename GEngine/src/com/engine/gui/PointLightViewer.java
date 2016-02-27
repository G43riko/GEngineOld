package com.engine.gui;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

import ggllib.entity.component.light.PointLightComponent;
import glib.swing.components.GFloatEditor;
import glib.swing.windows.Viewer;

public class PointLightViewer extends Viewer {
	private static final long serialVersionUID = 1L;
	private PointLightComponent parent;
	private float step = 0.1f;
	public PointLightViewer(PointLightComponent parent){
		super("PointLigtViewer");
		this.parent = parent;
		
		add(createPanel());
	}

	private JPanel createPanel(){
		JPanel panel = new JPanel(new GridBagLayout());
		
		//color
		
		addComponent("r", new GFloatEditor("r", Integer.MAX_VALUE, Integer.MIN_VALUE, step, parent.getColor().getX()));
		get("r", GFloatEditor.class).addChangeListener(a -> parent.getColor().setX(get("r", GFloatEditor.class).getValue()));
		panel.add(get("r"), getGridBack(0, 0));
		
		addComponent("g", new GFloatEditor("g", Integer.MAX_VALUE, Integer.MIN_VALUE, step, parent.getColor().getY()));
		get("g", GFloatEditor.class).addChangeListener(a -> parent.getColor().setY(get("g", GFloatEditor.class).getValue()));
		panel.add(get("g"), getGridBack(1, 0));
		
		addComponent("b", new GFloatEditor("b", Integer.MAX_VALUE, Integer.MIN_VALUE, step, parent.getColor().getZ()));
		get("b", GFloatEditor.class).addChangeListener(a -> parent.getColor().setZ(get("b", GFloatEditor.class).getValue()));
		panel.add(get("b"), getGridBack(2, 0));
		
		//attenuation
		
		addComponent("atX", new GFloatEditor("atX", Integer.MAX_VALUE, Integer.MIN_VALUE, step, parent.getAttenuation().getX()));
		get("atX", GFloatEditor.class).addChangeListener(a -> parent.getAttenuation().setX(get("atX", GFloatEditor.class).getValue()));
		panel.add(get("atX"), getGridBack(0, 1));
		
		addComponent("atY", new GFloatEditor("atY", Integer.MAX_VALUE, Integer.MIN_VALUE, step, parent.getAttenuation().getY()));
		get("atY", GFloatEditor.class).addChangeListener(a -> parent.getAttenuation().setY(get("atY", GFloatEditor.class).getValue()));
		panel.add(get("atY"), getGridBack(1, 1));
		
		addComponent("atZ", new GFloatEditor("atZ", Integer.MAX_VALUE, Integer.MIN_VALUE, step, parent.getAttenuation().getZ()));
		get("atZ", GFloatEditor.class).addChangeListener(a -> parent.getAttenuation().setZ(get("atZ", GFloatEditor.class).getValue()));
		panel.add(get("atZ"), getGridBack(2, 1));
		
		
		//TODO pridaù slider pre intenzitu
		
		return panel;
	}
	
	@Override
	public void updateData(){
		get("r", GFloatEditor.class).setValue(parent.getColor().getX());
		get("g", GFloatEditor.class).setValue(parent.getColor().getY());
		get("b", GFloatEditor.class).setValue(parent.getColor().getZ());
		

		get("atX", GFloatEditor.class).setValue(parent.getColor().getX());
		get("atY", GFloatEditor.class).setValue(parent.getColor().getY());
		get("atZ", GFloatEditor.class).setValue(parent.getColor().getZ());
	}

}
