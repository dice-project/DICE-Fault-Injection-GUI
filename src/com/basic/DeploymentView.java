package com.basic;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

import com.globals.Globals;
import com.google.gson.Gson;
import com.utility.Uploader;
@ManagedBean
@SessionScoped
public class DeploymentView {
     
    private String token;
    private String id;
    private UploadedFile file;
    
    private UploadedFile sshfile;

	public UploadedFile getSshfile() {
		return sshfile;
	}
	public void setSshfile(UploadedFile sshfile) {
		this.sshfile = sshfile;
	}

	private String configpath;
	private String sshkeypath;
	

    public UploadedFile getFile() {
        return file;
    }
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getConfigkeypath() {
		return configpath;
	}
	public void setConfigkeypath(String configpath) {
		this.configpath = configpath;
	}
	public String getSshkeypath() {
		return sshkeypath;
	}
	public void setSshkeypath(String sshkeypath) {
		this.sshkeypath = sshkeypath;
	}
		public void upload() throws IOException {
			
    	try (InputStream input = file.getInputstream()) {
            Date date = new Date();
            long datetime = date.getTime();
            //Files.copy(input, new File("/Users/darrenw/dicegui/DICEFITGUI/Uploads/"+ datetime + file.getFileName()).toPath());
            //File fileper = new File("/Users/darrenw/dicegui/DICEFITGUI/Uploads/"+ datetime + file.getFileName());
            Files.copy(input, new File("/home/ubuntu/Uploads/"+ datetime + file.getFileName()).toPath());
    	    File fileper = new File("/home/ubuntu/Uploads/"+ datetime + file.getFileName());
    	    fileper.setReadable(true, false);
    	    fileper.setWritable(true, false);
    	    //setConfigkeypath("/Users/darrenw/dicegui/DICEFITGUI/Uploads/"+ datetime + file.getFileName());
    	    setConfigkeypath("/home/ubuntu/Uploads/"+ datetime + file.getFileName());
    	    System.out.println(configpath.toString());
    	}    
		//setConfigkeypath("/Users/darrenw/Downloads/PrimeTest/Uploads/1488896529554jsontest1.json");
    	//readconfig();
	    System.out.println("UPLOADS");

    }
   		public void deploymentlist()
    {
    	DiceDeploymentCall dicedeployment = new DiceDeploymentCall();
    	dicedeployment.dicedeploymentlist(token, id);
		//LoggerWrapper.myLogger.info("DICE deployment node info");
    }
	    public void readconfig()
	    {
	    //setConfigkeypath("/Users/darrenw/Downloads/PrimeTest/Uploads/1488896529554jsontest1.json");
	    Gson gson = new Gson();  
	    try { 
	    	
	    	   System.out.println("Reading JSON from a file");  
	    	   System.out.println("----------");  
	    	     
	    	   BufferedReader br = new BufferedReader(  
	    	     new FileReader(configpath.toString()));  
	    	    
	    	    //convert the json string back to object  
	    	   DeploymentInfo deploymentinfo = gson.fromJson(br, DeploymentInfo.class);  
	    	     
	    	   System.out.println("Name: "+deploymentinfo.getName() + "Fault Name: "+deploymentinfo.getFaultname() + " token: " + deploymentinfo.getToken() + " nodeid " + deploymentinfo.getNodeid());  
	           System.out.println(deploymentinfo.getToken().toString());
	           br.close();
	           DiceDeploymentCall dicedeployment = new DiceDeploymentCall();
	       		dicedeployment.dicedeploymentfaultcaller1(deploymentinfo.getToken(),
	       				deploymentinfo.getNodeid(), deploymentinfo.getName(),deploymentinfo.getFaulttype(),deploymentinfo.getUsername(),getSshkeypath());
	    	    
	    } catch (IOException e) {  
	    	   e.printStackTrace();  
	    	  }  
	    	 } 
	    public void faultcaller()
	    {
	    	DiceDeploymentCall dicedeployment = new DiceDeploymentCall();
	    	dicedeployment.dicedeploymentlist(token, id);
	    }
    	public void message()
	    {
	    	FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage("Fault Started : " + token  ));
	    	save();
	    }
	    public void save() {
    	DiceDeploymentCall dicedeployment = new DiceDeploymentCall();
    	dicedeployment.dicedeploymentservice(token);
		//LoggerWrapper.myLogger.info("DICE deployment info");
    }
	    
	    public void uploadsshkey() throws IOException {
	        
	    	Uploader uploader = new Uploader();
			setSshkeypath(uploader.upload(getFile(), Globals.getSSHKeyPath()));     
	    }
}