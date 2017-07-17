package com.globals;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class Globals {
	
	public static String text = "Globalfirst";
	public static String cputext = "CPU Output";
	public static String ramtext = "RAM Output";
	public static String disktext = "DISK Output";
	public static String fcotext = "FCO Output";
	public static String deploymenttext = "";
	public static String deploymentnodelist = "";
	public static String servicettext = "SERVICE";
	public static String blockvmttext = "";	
	public static String SSHKeyPath = "/home/ubuntu/Uploads/";

	public String getBlockvmtext() {
		RequestContext.getCurrentInstance().update("BLOCKVMTEXT");
		return blockvmttext;
	}

	public void setBlockvmtext(String blockvmttext) {
		this.blockvmttext = blockvmttext;
	}

	
	public String getDeploymentnodelist() {
		RequestContext.getCurrentInstance().update("DEPLOYMENTOUTPUTDETAILS");
		return deploymentnodelist;
	}

	public void setDeploymentnodelist(String deploymentnodelist) {
		this.deploymentnodelist = deploymentnodelist;
	}
	public String getServicettext() {
		RequestContext.getCurrentInstance().update("SERVICEOUTPUT");
		return servicettext;
	}

	public void setServicettext(String servicettext) {
		this.servicettext = servicettext;
	}

	public String getDeploymenttext() {
		RequestContext.getCurrentInstance().update("DEPLOYMENTOUTPUT");
		return deploymenttext;
	}

	public void setDeploymenttext(String deploymenttext) {
		this.deploymenttext = deploymenttext;
	}
	
//	public String getFCOtext() {
//		RequestContext.getCurrentInstance().update("FCOOUTPUT");
//		return fcotext;
//	}

	public void setFCOtest(String fcotext) {
		this.fcotext = fcotext;
	}
	public String getDisktext() {
		RequestContext.getCurrentInstance().update("DISKOUTPUT");
		return disktext;
	}

	public void setDisktest(String disktext) {
		this.disktext = disktext;
	}
	
	public String getFcotext() {
		RequestContext.getCurrentInstance().update("FCOOUTPUT");
		return fcotext;
	}

	public void setFcotest(String fcotext) {
		this.fcotext = fcotext;
	}

	public String getText() {
		RequestContext.getCurrentInstance().update("CPUTEST");
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getRamtext() {
		RequestContext.getCurrentInstance().update("RAMOUTPUT");
		return ramtext;
	}

	public void setRamtext(String ramtext) {
		this.ramtext = ramtext;
	}
	
	public String getCputext(){
			RequestContext.getCurrentInstance().update("CPUTEST");
			return cputext;
	}
	
	public void setCputext(String cputext) {
		this.cputext = cputext;
	}
	
	

	public static String getSSHKeyPath() {
		return SSHKeyPath;
	}

	public static void setSSHKeyPath(String sSHKeyPath) {
		SSHKeyPath = sSHKeyPath;
	}
}