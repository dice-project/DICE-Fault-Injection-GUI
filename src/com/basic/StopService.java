package com.basic;

import com.globals.Globals;
import com.jcraft.jsch.*;
import com.utility.OSchecker;

import java.io.*;

import org.primefaces.context.RequestContext;

public class StopService {
	
public static String text = "servicetest";
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	public void stopservice(String host, String vmpassword,String service,String sshkeypath) {
		Globals.servicettext= Globals.servicettext + "STARTED";
		RequestContext requestContext = RequestContext.getCurrentInstance();
		requestContext.update("SERVICEOUTPUT");
		//Calls OS checker to determine if Ubuntu or Centos os
		OSchecker oscheck = new OSchecker();
		oscheck.oscheck(host, vmpassword, sshkeypath);
		String localOS = oscheck.OSVERSION;
		//LoggerWrapper.myLogger.info(localOS);


		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));

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

			session.setPassword(vmpassword);
		
		
		
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
		
			session.connect();
			//LoggerWrapper.myLogger.info("Attempting to SSH to VM with ip " + host);
			String command2 = null;
			//Different commands used if Centos or Ubuntu OS is used.
			if  (localOS.equals("CENTOS"))
			{
				command2 ="sudo /sbin/service " + service + " stop";
			}
			
			else if  (localOS.equals("UBUNTU"))
			{
				 command2 = "sudo service " + service + " stop";

			
				
			}
			byte[] tmp = new byte[1024];
			//Opens channel for sending  command.
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command2);
			InputStream in = channel.getInputStream();
			channel.connect();
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					System.out.print(new String(tmp, 0, i));
					info = new String(tmp, 0, i);
					//Outputs responce for ssh connection
					System.out.print(info);
				}
				if (channel.isClosed()) {
					if (in.available() > 0)
						continue;
					System.out.println("exit-status: "
							+ channel.getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}

			}
			in.close();
			//Closes after first command is sent
			channel.disconnect();
			//Close session after all commands are done
			session.disconnect();
			//LoggerWrapper.myLogger.info( baos.toString());
			Globals.servicettext= Globals.servicettext + "Stopped service " + service;
			RequestContext requestContext1 = RequestContext.getCurrentInstance();
			requestContext1.update("SERVICEOUTPUT");

		} catch (Exception e) {
			Globals.servicettext= Globals.servicettext + "Unable to stop service";
			RequestContext requestContext2 = RequestContext.getCurrentInstance();
			requestContext2.update("SERVICEOUTPUT");		}
	}
}