package com.basic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.Timestamp;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.faults.DiskStress;
import com.globals.Globals;
import com.utility.Uploader;

import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class DiskView {

	private String ip;
	private String username;
	private String password;
	private String disktotal;
	private String loops;
	private String sshkeypath;
	private UploadedFile file;

	public String getIp() {
		return ip;
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

	public String getDisktotal() {
		return disktotal;
	}

	public void setDisktotal(String disktotal) {
		this.disktotal = disktotal;
	}

	public String getLoops() {
		return loops;
	}

	public void setLoops(String loops) {
		this.loops = loops;
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
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Fault Started : " + ip + " " + username));
		save();
	}

	public void save() {

		System.out.println(getSshkeypath());

		if (getPassword().equals("-no")) {
			System.out.println(getSshkeypath());
			password = "-no";
			String host = username + "@" + ip;
			DiskStress diskstress = new DiskStress();
			diskstress.stressdisk(host, password, disktotal, loops, getSshkeypath());
			System.out.println(sshkeypath);
			File file = new File(getSshkeypath());
			file.delete();
		} else {
			sshkeypath = "-no";
			String host = username + "@" + ip;
			DiskStress diskstress = new DiskStress();
			diskstress.stressdisk(host, password, disktotal, loops, sshkeypath);
			System.out.println(sshkeypath.toString());
		}
	}
}