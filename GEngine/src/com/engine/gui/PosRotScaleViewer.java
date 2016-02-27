package com.engine.gui;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

import ggllib.entity.component.PosRotScaleComponent;
import glib.swing.components.GFloatEditor;
import glib.swing.windows.Viewer;

public class PosRotScaleViewer extends Viewer{
	private static final long serialVersionUID = 1L;
	private PosRotScaleComponent parent;
	private float step = 1;
	
	public PosRotScaleViewer(PosRotScaleComponent parent){
		super("PosRotScale viewer");
		this.parent = parent;
		
		add(createPanel());
	}
	
	private JPanel createPanel(){
		JPanel panel = new JPanel(new GridBagLayout());
		
		//position
		
		addComponent("posX", new GFloatEditor("posX", Integer.MAX_VALUE, Integer.MIN_VALUE, step, parent.getPosition().getX()));
		get("posX", GFloatEditor.class).addChangeListener(a -> parent.getPosition().setX(get("posX", GFloatEditor.class).getValue()));
		panel.add(get("posX"), getGridBack(0, 0));
		
		addComponent("posY", new GFloatEditor("posY", Integer.MAX_VALUE, Integer.MIN_VALUE, step, parent.getPosition().getY()));
		get("posY", GFloatEditor.class).addChangeListener(a -> parent.getPosition().setY(get("posY", GFloatEditor.class).getValue()));
		panel.add(get("posY"), getGridBack(0, 1));
		
		addComponent("posZ", new GFloatEditor("posZ", Integer.MAX_VALUE, Integer.MIN_VALUE, step, parent.getPosition().getZ()));
		get("posZ", GFloatEditor.class).addChangeListener(a -> parent.getPosition().setZ(get("posZ", GFloatEditor.class).getValue()));
		panel.add(get("posZ"), getGridBack(0, 2));
		
		//rotation
		
		addComponent("rotX", new GFloatEditor("rotX", Integer.MAX_VALUE, Integer.MIN_VALUE, step, parent.getRotation().getX()));
		get("rotX", GFloatEditor.class).addChangeListener(a -> parent.getRotation().setX(get("rotX", GFloatEditor.class).getValue()));
		panel.add(get("rotX"), getGridBack(1, 0));
		
		addComponent("rotY", new GFloatEditor("rotY", Integer.MAX_VALUE, Integer.MIN_VALUE, step, parent.getRotation().getY()));
		get("rotY", GFloatEditor.class).addChangeListener(a -> parent.getRotation().setY(get("rotY", GFloatEditor.class).getValue()));
		panel.add(get("rotY"), getGridBack(1, 1));
		
		addComponent("rotZ", new GFloatEditor("rotZ", Integer.MAX_VALUE, Integer.MIN_VALUE, step, parent.getRotation().getZ()));
		get("rotZ", GFloatEditor.class).addChangeListener(a -> parent.getRotation().setZ(get("rotZ", GFloatEditor.class).getValue()));
		panel.add(get("rotZ"), getGridBack(1, 2));
		
		//scale
		
		addComponent("scaleX", new GFloatEditor("scaleX", Integer.MAX_VALUE, Integer.MIN_VALUE, step, parent.getScale().getX()));
		get("scaleX", GFloatEditor.class).addChangeListener(a -> parent.getScale().setX(get("scaleX", GFloatEditor.class).getValue()));
		panel.add(get("scaleX"), getGridBack(2, 0));
		
		addComponent("scaleY", new GFloatEditor("scaleY", Integer.MAX_VALUE, Integer.MIN_VALUE, step, parent.getScale().getY()));
		get("scaleY", GFloatEditor.class).addChangeListener(a -> parent.getScale().setY(get("scaleY", GFloatEditor.class).getValue()));
		panel.add(get("scaleY"), getGridBack(2, 1));
		
		addComponent("scaleZ", new GFloatEditor("scaleZ", Integer.MAX_VALUE, Integer.MIN_VALUE, step, parent.getScale().getZ()));
		get("scaleZ", GFloatEditor.class).addChangeListener(a -> parent.getScale().setZ(get("scaleZ", GFloatEditor.class).getValue()));
		panel.add(get("scaleZ"), getGridBack(2, 2));
		return panel;
	}

	@Override
	public void updateData(){
		get("posX", GFloatEditor.class).setValue(parent.getPosition().getX());
		get("posY", GFloatEditor.class).setValue(parent.getPosition().getY());
		get("posZ", GFloatEditor.class).setValue(parent.getPosition().getZ());

		get("rotX", GFloatEditor.class).setValue(parent.getRotation().getX());
		get("rotY", GFloatEditor.class).setValue(parent.getRotation().getY());
		get("rotZ", GFloatEditor.class).setValue(parent.getRotation().getZ());

		get("scaleX", GFloatEditor.class).setValue(parent.getScale().getX());
		get("scaleY", GFloatEditor.class).setValue(parent.getScale().getY());
		get("scaleZ", GFloatEditor.class).setValue(parent.getScale().getZ());
	}

}
