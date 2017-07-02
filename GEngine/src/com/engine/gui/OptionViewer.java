package com.engine.gui;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.util.Map.Entry;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.IO;

import glib.swing.components.GFloatEditor;
import glib.swing.components.GVector3Editor;
import glib.swing.windows.Viewer;
import glib.util.GOptions;
import glib.util.GType;
import glib.util.Utils;
import glib.util.vector.GVector3f;

public class OptionViewer extends Viewer{
	private GOptions options;
	public OptionViewer(GOptions options) {
		super("Options viewer");
		this.options = options;
		
		add(createPanel());
	}
	
	

	private JPanel createPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		
		int counter = 0;
//		for(Entry<String, Object> opt : options.getOptions().entrySet()){
//			String value = opt.getValue().toString();
//			System.out.println(value);
//			GType type = Utils.getType(value); 
//			if(type == GType.BOOLEAN){
//				addComponent(opt.getKey(), new JCheckBox(opt.getKey(), Boolean.parseBoolean(value)));
//				get(opt.getKey(), JCheckBox.class).addChangeListener(a -> options.put(opt.getKey(), get(opt.getKey(), JCheckBox.class).isSelected()));
//				panel.add(get(opt.getKey()), getGridBack(0, counter++));
//			}
//			if(type == GType.GVECTOR3F){
//				GVector3f vector = new GVector3f(value);
//				GVector3Editor obj = new GVector3Editor(opt.getKey(), Integer.MAX_VALUE, Integer.MIN_VALUE, vector);
//				addComponent(opt.getKey(), obj);
//				obj.addChangeListener(a -> {
//					options.put(opt.getKey(), vector.set(obj.getValX(), obj.getValY(), obj.getValZ()));
//					System.out.println(options.get(opt.getKey()));
//					System.exit(1);
//				});
//				panel.add(obj, getGridBack(0, counter++));
//			}
//		}
		
		
		
		return panel;
	}



	@Override
	public void updateData() {
		// TODO Auto-generated method stub
		
	}

}
