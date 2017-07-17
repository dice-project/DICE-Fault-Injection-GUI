package com.basic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.UploadedFile;

import com.faults.RamStress;
import com.globals.Globals;
import com.utility.Uploader;

@ManagedBean
@SessionScoped
public class RamView {

	private String ip;
	private String username;
	private String password;
	private String memtotal;
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

	public String getMemtotal() {
		return memtotal;
	}

	public void setMemtotal(String memtotal) {
		this.memtotal = memtotal;
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

	private static final File LOCATION = new File("/Users/darrenw/Downloads/PrimeTest/Uploads");

	public void upload() throws IOException {

		Uploader uploader = new Uploader();
		setSshkeypath(uploader.upload(getFile(), Globals.getSSHKeyPath()));
	}

	public void message() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Fault Started on: " + ip + " " + username + " " + memtotal + " " + sshkeypath));
		save();
	}

	public void save() {

		System.out.println(getSshkeypath());

		if (getPassword().equals("-no")) {
			System.out.println(getSshkeypath());
			// sshkeypath = "/Users/darrenw/Desktop/DICEREVIEW/test";
			password = "-no";
			String host = username + "@" + ip;
			RamStress ramstress = new RamStress();
			ramstress.stressmemory(host, password, loops, memtotal, getSshkeypath(), "RAMOUTPUT");
			File file = new File(getSshkeypath());
			file.delete();
			System.out.println(sshkeypath);

		} else {
			sshkeypath = "-no";
			String host = username + "@" + ip;
			RamStress ramstress = new RamStress();
			ramstress.stressmemory(host, password, loops, memtotal, sshkeypath, "RAMOUTPUT");

			System.out.println(sshkeypath.toString());
		}
	}
}