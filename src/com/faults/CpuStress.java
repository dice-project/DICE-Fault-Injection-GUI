package com.faults;

import com.utility.OSchecker;
import com.globals.Globals;
import com.jcraft.jsch.*;

import java.io.*;
import java.util.concurrent.TimeUnit;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

@ManagedBean
public class CpuStress {
	
	// Are these ever used? ----------
	public static String text = "";

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}// ------------------------------

	public void stresscpu(String cores, String time, String vmpassword, String host, String sshkeypath, String output) {
		Globals.cputext += "Stress CPU Started";
		RequestContext requestContextNew = RequestContext.getCurrentInstance();
		requestContextNew.update(output);
		
		// Calls OS checker to determine if Ubuntu or Centos is running
		OSchecker oscheck = new OSchecker();
		String localOS;
		oscheck.oscheck(host, vmpassword, sshkeypath);
		localOS = oscheck.OSVERSION;
		
		// LoggerWrapper.myLogger.info(localOS);
		Globals.cputext = Globals.cputext + localOS;
		RequestContext requestContext1 = RequestContext.getCurrentInstance();
		requestContext1.update(output);
		// Set up for First command sent
		String command;
		if (localOS == null) {
			Globals.cputext = Globals.cputext + " Unable to connect to VM";
			RequestContext requestContextexit = RequestContext.getCurrentInstance();
			requestContextexit.update(output);
			return;
		}
		if (localOS.equals("UBUNTU")) {
			command = "dpkg-query -W -f='${Status}' stress";
		} else {
			// CENTOS will not accept first command so "dud" command sent
			command = "";
		}
		
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		System.setOut(new PrintStream(baos));
//		System.out.println(" Test call at LINE 70");
		try {
//			System.out.println(" test call within try LINE 72");
			String info = null;

			JSch jsch = new JSch();

			String user = host.substring(0, host.indexOf('@'));
			host = host.substring(host.indexOf('@') + 1);
			Session session = jsch.getSession(user, host, 22);
			// Used to determine if ssh key or password is provided with command
			if (sshkeypath.equals("-no")) {
				session.setPassword(vmpassword);
			} else if (vmpassword.equals("-no")) {
				jsch.addIdentity(sshkeypath);
			}

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			// test.concat("Attempting to SSH to VM with ip " + host);
			// LoggerWrapper.myLogger.info("Attempting to SSH to VM with ip " +
			// host);
//			Globals.cputext += Globals.cputext + host + " LINE 94 ";
//			RequestContext requestContext2 = RequestContext.getCurrentInstance();
//			requestContext2.update(output);
			
			// Opens channel for sending first command. --- print stops working
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);

			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();
			channel.connect();
			System.out.print(" Test call after channel at LINE 106");
			// Prints out information of input stream, until no bytes remain.
			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					// prints the 'install ok installed' message, received form server
					System.out.println(new String(tmp, 0, i) + " LINE 115 ");
					info = new String(tmp, 0, i);
					// Outputs response for ssh connection
					System.out.println("CPU Test status: " + info + " LINE 118");
//					Globals.cputext = Globals.cputext + info;
//					RequestContext requestContext3 = RequestContext.getCurrentInstance();
//					requestContext3.update(output);
				}
				if (channel.isClosed()) {
					if (in.available() > 0)
						continue;
					System.out.println(" First command sent, exit status " + channel.getExitStatus() + " LINE 127 ");
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
					
				}

			}
			// Closes after first command is sent
			channel.disconnect();
			// Sets up for second command
			String command2 = null;
			// Different commands used if Centos or Ubuntu OS is used.
			if (localOS.equals("CENTOS")) {
				command2 = "wget http://dl.fedoraproject.org/pub/epel/6/x86_64/stress-1.0.4-4.el6.x86_64.rpm && rpm -ivh stress-1.0.4-4.el6.x86_64.rpm; stress -c "
						+ cores + " -t " + time;
				// LoggerWrapper.myLogger.info("Installing Stress tool if
				// required and running test..... ");
				Globals.cputext = Globals.cputext + " Installing Stress tool if required and running test..... ";
			}

			else if (localOS.equals("UBUNTU")) {
				if (info == null) {

					command2 = "sudo apt-get -q -y install stress; stress -c " + cores + " -t " + time;
					// LoggerWrapper.myLogger.info("Stress tool not
					// found..Installing...... The running test");
					Globals.cputext = Globals.cputext + " Stress tool not found..Installing...... The running test";
					RequestContext requestContext4 = RequestContext.getCurrentInstance();
					requestContext4.update(output);
				} else if (info.equals("install ok installed")) {
					command2 = "stress -c " + cores + " -t " + time;
					// LoggerWrapper.myLogger.info("Stress tool found..running
					// test......");
					Globals.cputext = Globals.cputext + " Stress tool not found..Installing...... The running test";
					RequestContext requestContext4 = RequestContext.getCurrentInstance();
					requestContext4.update(output);
				}
			}

			Channel channel2 = session.openChannel("exec");
			((ChannelExec) channel2).setCommand(command2);
			InputStream in1 = channel2.getInputStream();
			channel2.connect();
			while (true) {
				while (in1.available() > 0) {
					int i = in1.read(tmp, 0, 1024);
					if (i < 0)
						break;
					System.out.print(new String(tmp, 0, i));
					info = new String(tmp, 0, i);
				}
				if (channel2.isClosed()) {
					if (in.available() > 0)
						continue;
					System.out.println("exit-status: " + channel2.getExitStatus() + " LINE 185 ");
					Globals.cputext = Globals.cputext + channel2.getExitStatus();
					RequestContext requestContext5 = RequestContext.getCurrentInstance();
					requestContext5.update(output);
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}

			}
			in1.close();
			// Close after second command
			channel2.disconnect();
			// Close session after all commands are done
			session.disconnect();
			// LoggerWrapper.myLogger.info( baos.toString());
//			Globals.cputext = text + baos.toString() + " LINE 203 ";
//			RequestContext requestContext6 = RequestContext.getCurrentInstance();
//			requestContext6.update(output);

			RequestContext requestContext7 = RequestContext.getCurrentInstance();
			requestContext7.update(output);
			
			// Successfully appends text, without making a new 'requestcontext'
			RequestContext.getCurrentInstance().update(output);
		} catch (Exception e) {
			// LoggerWrapper.myLogger.severe("Unable to SSH to VM " +
			// e.toString());
			Globals.cputext = text + "Unable to SSH to VM " + e.toString();
			RequestContext requestContext7 = RequestContext.getCurrentInstance();
			requestContext7.update(output);

		}
	}
}