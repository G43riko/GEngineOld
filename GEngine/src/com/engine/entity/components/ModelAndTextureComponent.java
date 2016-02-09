package com.engine.entity.components;

import ggllib.render.material.Material;
import ggllib.render.model.BorderedModel;
import ggllib.render.model.MaterialedModel;

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
