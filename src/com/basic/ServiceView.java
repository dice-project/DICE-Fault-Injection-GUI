package com.basic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.model.UploadedFile;

import com.globals.Globals;
import com.utility.Uploader;

@ManagedBean
public class ServiceView {

	private String ip;
	private String username;
	private String password;
	private String service;
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

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public void setTest(String test) {
		this.test = test;
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
		setSshkeypath(uploader.upload(getFile(), Globals.getSSHKeyPath()));
	}

	public void message() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Fault Started : " + ip + " " + username + " " + password + " " + service));
		save();
	}

	public void save() {

		if (getPassword().equals("-no")) {
			System.out.println(getSshkeypath());
			// sshkeypath = "/Users/darrenw/Desktop/DICEREVIEW/test";
			password = "-no";
			String host = username + "@" + ip;
			StopService stopservice = new StopService();
			stopservice.stopservice(host, password, service, getSshkeypath());
			System.out.println(sshkeypath);
			File file = new File(getSshkeypath());
			file.delete();
		} else {
			sshkeypath = "-no";
			String host = username + "@" + ip;
			StopService stopservice = new StopService();
			stopservice.stopservice(host, password, service, sshkeypath);
			System.out.println(sshkeypath.toString());
		}

	}
}