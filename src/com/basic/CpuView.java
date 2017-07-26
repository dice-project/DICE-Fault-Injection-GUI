package com.basic;
import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

import com.faults.CpuStress;
import com.globals.Globals;
import com.utility.Uploader;
 
@ManagedBean
@SessionScoped
public class CpuView{
     
    private String ip;
    private String username;
    private String password;
    private String cpu;
    private String time;
	private String sshkeypath;
	private UploadedFile file;
	private String test = "test";
	
    public String getIp() {
        return ip;
    }
    
    public String getTest() {
        return test;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
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
 
    public String getCpu() {
        return cpu;
    }
 
    public void setCpu(String cpu) {
        this.cpu = cpu;
    }
    
    public String getTime() {
        return time;
    }
 
    public void setTime(String time) {
        this.time = time;
    }	
    
	public UploadedFile getFile() {
	        return file;
	}
	 
	public void setFile(UploadedFile file) {
	        this.file = file;
	}
	
	public String getSshkeypath() {
		return sshkeypath;
	}
	public void setSshkeypath(String sshkeypath) {
		this.sshkeypath = sshkeypath;
	}
	     

	 public void upload() throws IOException {
	        
	    	Uploader uploader = new Uploader();
	    	setSshkeypath(uploader.upload(getFile(),Globals.getSSHKeyPath()));
	    	
	}
	 
	 public void message()
	 {	
		 	// Creates a message on the page
	    	FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage("Fault started on: " + ip + " " + username + " " + cpu  + " " + time + " seconds" ));
//	    	Thread t = new Thread() {
//	    		public void run(){
//	    			try{
//	    				System.out.println("New thread started, calling CPU fault");
//	    				save();
//	    			}catch(Exception e){}
//	    		}
//	    	};
//	    	t.start();
	    	save();
			RequestContext requestContextNew = RequestContext.getCurrentInstance();
			requestContextNew.update("CPUTEST");
	 }
	 
	 public void poll(){
		RequestContext requestContextNew = RequestContext.getCurrentInstance();
		requestContextNew.update("CPUTEST");
	 }
 
    public void save(){
    	
    	System.out.println(getSshkeypath());
  
    	if (getPassword().equals("-no"))
    	{
			System.out.println(getSshkeypath());
			//password= "-no";
			String host = username +"@"+ip;
			CpuStress cpustress = new CpuStress();
			cpustress.stresscpu(cpu,time,password,host,getSshkeypath(),"CPUTEST");
			System.out.println(sshkeypath);
			//File file = new File(getSshkeypath());
			//file.delete();
			//System.out.println("Running without creating & deleting a file");
			//LoggerWrapper.myLogger.info( "Executing random VM stop");
			//Logger.getGlobal().log(new LogRecord(Level.ALL, "Testing logger"));
    	}
    	else
    	{
	 		sshkeypath = "-no";
	  	    String host= username +"@"+ip;
	  	    CpuStress cpustress = new CpuStress();
	  	    cpustress.stresscpu(cpu,time,password,host,sshkeypath,"CPUTEST");
			System.out.println(sshkeypath.toString());
    	}
    }
}