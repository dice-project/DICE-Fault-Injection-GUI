package com.model;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Details {

	private String name;
	private String fault_type;
	
	
	public String name() {
		return name;
	}

	public void setname(String name) {
		this.name = name;
	}
	@Override
    public String toString() {
        return name + " - " + fault_type;
    }

	public String getFault_type() {
		return fault_type;
	}

	public void setFault_type(String fault_type) {
		this.fault_type = fault_type;
	}
}
