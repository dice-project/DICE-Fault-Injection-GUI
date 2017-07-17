package com.faults;

import com.utility.OSchecker;
import com.globals.Globals;
import com.jcraft.jsch.*;
import java.io.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;
@ManagedBean
@SessionScoped
public class DiskStress {
	public static String disktext = "disktest";
	
	public String getDisktext() {
		return disktext;
	}

	public void setDisktext(String disktext) {
		this.disktext = disktext;
	}
	public void stressdisk(String host, String vmpassword, String disktotal,String loops, String sshkeypath) {
	
		//Calls OS checker to determine if Ubuntu or Centos os
		OSchecker oscheck = new OSchecker();
		oscheck.oscheck(host, vmpassword, sshkeypath);
		String localOS = oscheck.OSVERSION;
		//LoggerWrapper.myLogger.info(localOS);
		Globals.disktext= Globals.disktext + "STARTED";
		RequestContext requestContext = RequestContext.getCurrentInstance();
		requestContext.update("DISKOUTPUT");
		String command;
		
		if (localOS.equals("UBUNTU"))
		{
		command ="dpkg-query -W -f='${Status}' bonnie++";

		}
		else
		{
			//CENTOS will not accept first command so "dud" command sent
		command ="";
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));
		/*
		@SuppressWarnings("unused")
		LoggerWrapper loggerWrapper = null;
		try {
			loggerWrapper = LoggerWrapper.getInstance();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 */
		
		try {
			
			String info = null;

			JSch jsch = new JSch();

			String user = host.substring(0, host.indexOf('@'));
			host = host.substring(host.indexOf('@') + 1);

			Session session = jsch.getSession(user, host, 22);
			 //Used to determine if ssh key or password is provided with command 
 
			if (sshkeypath.equals("-no")) {
				 session.setPassword(vmpassword);
			  }
			  else if (vmpassword.equals("-no"))
			  {
					 jsch.addIdentity(sshkeypath);
			  }
		
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
		
			session.connect();
			//LoggerWrapper.myLogger.info("Attempting to SSH to VM with ip " + host);
			Globals.disktext= Globals.disktext + host;
			RequestContext requestContext1 = RequestContext.getCurrentInstance();
			requestContext1.update("DISKOUTPUT");
			//Opens channel for sending first command.
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);

			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();

			channel.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					System.out.print(new String(tmp, 0, i));
					info = new String(tmp, 0, i);
					//Outputs responce for ssh connection
					System.out.print(" Disk Stress Status : " + info);
					Globals.disktext= Globals.disktext + info;
					RequestContext requestContext2 = RequestContext.getCurrentInstance();
					requestContext2.update("DISKOUTPUT");
				}
				if (channel.isClosed()) {
					if (in.available() > 0)
						continue;
					System.out.println("exit-status: "
							+ channel.getExitStatus());
					Globals.disktext= Globals.disktext + channel.getExitStatus();
					RequestContext requestContext3 = RequestContext.getCurrentInstance();
					requestContext3.update("DISKOUTPUT");
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}

			}
			//Closes after first command is sent
			channel.disconnect();
			//Sets up for second command
			String command2 = null;
			//Different commands used if Centos or Ubuntu OS is used.
			if  (localOS.equals("CENTOS"))
			{
				command2 = "sudo yum install bonnie++; /usr/sbin/bonnie++ -d /tmp -r " + disktotal + " -x" + loops;
				//LoggerWrapper.myLogger.info("Installing Disk Stress tool if required and running test..... ");
				Globals.disktext= Globals.disktext + "Installing Disk Stress tool if required and running test..... ";
				RequestContext requestContext4 = RequestContext.getCurrentInstance();
				requestContext4.update("DISKOUTPUT");
			}
			
			else if  (localOS.equals("UBUNTU"))
				
			{
		
			if (info == null) {
				
				command2 = "sudo apt-get -q -y install bonnie++";
				//LoggerWrapper.myLogger.info("bonnie++ not found..Installing......");
				Globals.disktext= Globals.disktext + "bonnie++ not found..Installing......";
				RequestContext requestContext5 = RequestContext.getCurrentInstance();
				requestContext5.update("DISKOUTPUT");
			}
			else if (info.equals("install ok installed")) {
			command2 = "bonnie++ -d /tmp -r " + disktotal + " -x " + loops;
			//LoggerWrapper.myLogger.info("bonnie++ found..running test......");
			Globals.disktext= Globals.disktext + "bonnie++ found..running test......";
			RequestContext requestContext6 = RequestContext.getCurrentInstance();
			requestContext6.update("DISKOUTPUT");
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
					System.out.print(info);
				}
				if (channel2.isClosed()) {
					if (in.available() > 0)
						continue;
					System.out.println("exit-status: "
							+ channel2.getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}

			}
			in1.close();
			//Close after second command
			channel2.disconnect();
			//Close session after all commands are done
			session.disconnect();
			//LoggerWrapper.myLogger.info( baos.toString());
			Globals.disktext= Globals.disktext + "COMPLETED";
			RequestContext requestContext7 = RequestContext.getCurrentInstance();
			requestContext7.update("DISKOUTPUT");

		} catch (Exception e) {
			//LoggerWrapper.myLogger.severe("Unable to SSH to VM " + e.toString());
			Globals.disktext= Globals.disktext + "Unable to SSH to VM " + e.toString();
			RequestContext requestContext8 = RequestContext.getCurrentInstance();
			requestContext8.update("DISKOUTPUT");
		}
	}
}