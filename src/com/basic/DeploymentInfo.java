package com.basic;

import java.util.Arrays;

import javax.faces.bean.ManagedBean;

public class DeploymentInfo {
	private String name;
	private String faultname;
	private String faulttype;
	private String username;
	private String password;
	
	
	private String token;
	private String nodeid;

    public String toString() {
    	return name + " - " + faultname + " - " + faulttype + " - " + password + " - " + token + " - " + nodeid + " - " +username;
    }
    public String getToken() {
		return token;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}
    public String getNodeid() {
		return nodeid;
	}
    public void setUsername(String username) {
		this.username = username;
	}
    public String getUsername() {
		return username;
	}

	public void setToken(String token) {
		this.token = token;
	}
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
			return password;
		}

	public void setPassword(String password) {
			this.password = password;
		}
	public String getFaultname() {
		return faultname;
	}

	public void setfaultname(String faultname) {
		this.faultname = faultname;
	}
	
	public String getFaulttype() {
		return faulttype;
	}

	public void setfaulttype(String faulttype) {
		this.faulttype = faulttype;
	}
}
