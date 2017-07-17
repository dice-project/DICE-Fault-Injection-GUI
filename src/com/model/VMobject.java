package com.model;

import javax.faces.bean.ManagedBean;

import com.basic.Blueprint;
@ManagedBean
public class VMobject {
	private String id;
    private Blueprint blueprint;
    
    public String toString() {
    	return id + " - " + blueprint;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Blueprint getBlueprint() {
		return blueprint;
	}

	public void setBlueprint(Blueprint blueprint) {
		this.blueprint = blueprint;
	}
}
