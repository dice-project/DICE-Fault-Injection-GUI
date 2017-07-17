package com.model;

import java.util.Arrays;

import javax.faces.bean.ManagedBean;

public class NodeObject {
	private String id;
    private String node_id;
    private String ip;
    private String[] components;
    String returntext;

   // private String components;
    
    public String toString() {
    	returntext = Arrays.toString(components);
    	return id + " - " + node_id + " - " + ip + " - "  + " - " + Arrays.toString(components);
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNode_id() {
		return node_id;
	}

	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String[] getComponents() {
		return components;
	}


	public void setComponents(String[] components) {
		this.components = components;
	}

	public String getReturntext() {
		return returntext;
	}

	public void setReturntext(String returntext) {
		this.returntext = returntext;
	}
}
