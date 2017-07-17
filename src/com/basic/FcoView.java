package com.basic;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;
@ManagedBean
@SessionScoped
public class FcoView {
     
    private String username;
    private String password;
    private String apiendpoint;
    private String clouduuid;
    private UploadedFile file;

	private String test = "test";
	
    public String getApiendpoint() {
        return apiendpoint;
    }
    public String getTest() {
        return test;
    }
    public void setApiendpoint(String apiendpoint) {
        this.apiendpoint = apiendpoint;
    }
 
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
 
    public String getClouduuid() {
        return clouduuid;
    }
 
    public void setClouduuid(String clouduuid) {
        this.clouduuid = clouduuid;
    }
    
	public UploadedFile getFile() {
        return file;
}
 
	public void setFile(UploadedFile file) {
        this.file = file;
	}
	
    
    public void setTest(String test) {
        this.test = test;
    }	

	    public void message()
	    {
	    	FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage("Fault Started : " + clouduuid + " " + username  + " " + apiendpoint));
	    	save();
	    }
    public void save() {

        FCOListVM listvms = new FCOListVM();
        listvms.listvms(username, password, apiendpoint, clouduuid);
		//LoggerWrapper.myLogger.info( "Executing random VM stop");
    }
}