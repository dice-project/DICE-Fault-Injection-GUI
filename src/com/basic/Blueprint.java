package com.basic;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Blueprint {

	private String state_name;
	private String in_error;
	
	
	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
	}
	@Override
    public String toString() {
        return state_name + " - " + in_error;
    }

	public String getIn_error() {
		return in_error;
	}

	public void setIn_error(String in_error) {
		this.in_error = in_error;
	}
}
