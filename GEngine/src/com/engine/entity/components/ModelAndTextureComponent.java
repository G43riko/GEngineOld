package com.engine.entity.components;

import ggllib.object.BorderedModel;
import ggllib.object.MaterialedModel;
import ggllib.render.material.Material;

public class ModelAndTextureComponent extends Component{
	private MaterialedModel model;

	public ModelAndTextureComponent(MaterialedModel model){
		this.model = model;
	}

	public BorderedModel getBorderedModel() {
		return model.getModel();
	}
	
	public Material getMaterial(){
		return model.getMaterial();
	}
}
