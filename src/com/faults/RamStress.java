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
public class RamStress {
	
public static String ramtext = "ramtext";
	
	public String getRamtext() {
		return ramtext;
	}

	public void setRamtext(String ramtext) {
		this.ramtext = ramtext;
	}
	public void stressmemory(String host, String vmpassword,String memorytesterloops,String memeorytotal,String sshkeypath, String output) {
		
		//Calls OS checker to determine if Ubuntu or Centos os
		OSchecker oscheck = new OSchecker();
		oscheck.oscheck(host, vmpassword, sshkeypath);
		String localOS = oscheck.OSVERSION;
		//LoggerWrapper.myLogger.info(localOS);
		
		Globals.ramtext = localOS;
		RequestContext requestContext = RequestContext.getCurrentInstance();
		requestContext.update(output);
		
		String command;
		
		if (localOS.equals("UBUNTU"))
		{
		command ="dpkg-query -W -f='${Status}' memtester";

		}
		else
		{
			//CENTOS will not accept first command so "dud" command sent
		command ="";
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));

		try {
			
			String info = null;

			JSch jsch = new JSch();

			String user = host.substring(0, host.indexOf('@'));
			host = host.substring(host.indexOf('@') + 1);

			Session session = jsch.getSession(user, host, 22);
			 //Used to determine if ssh key or password is proivded with command 

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
			Globals.ramtext = ramtext + host;
			RequestContext requestContext1 = RequestContext.getCurrentInstance();
			requestContext1.update(output);	
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
					System.out.print(" Stress Status : " + info);
					Globals.ramtext = ramtext + " Stress Status : " + info;
					RequestContext requestContext2 = RequestContext.getCurrentInstance();
					requestContext2.update(output);
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
			//Closes after first command is sent
			channel.disconnect();
			//Sets up for second command
			String command2 = null;
			//Different commands used if Centos or Ubuntu OS is used.
			if  (localOS.equals("CENTOS"))
			{
				command2 = "sudo wget http://rpms.famillecollet.com/enterprise/remi-release-6.rpm && rpm -Uvh remi-release-6*.rpm && sudp yum -y update && sudo yum install memtester; memtester " + memeorytotal +" "+ memorytesterloops;

				//LoggerWrapper.myLogger.info("MemTester tool running");			
				Globals.ramtext = ramtext + " Stress Status : " + info;
				RequestContext requestContext2 = RequestContext.getCurrentInstance();
				requestContext2.update(output);

				}
			
			else if  (localOS.equals("UBUNTU"))
			
				{
			
				if (info == null) {
					
					command2 = "sudo apt-get -q -y install memtester";
					//LoggerWrapper.myLogger.info("MemTester tool not found..Installing......");
					Globals.ramtext = ramtext + " MemTester tool not found..Installing.....";
					RequestContext requestContext3 = RequestContext.getCurrentInstance();
					requestContext3.update("RAMOUTPUT");
				}

				else if (info.equals("install ok installed")) {
					command2 = " sudo memtester "+ memeorytotal +" "+ memorytesterloops;
					//LoggerWrapper.myLogger.info("MemTest tool found..running test......");
				//	LoggerWrapper.myLogger.info("memtester loop number: "
					//		+ memorytesterloops);
					Globals.ramtext = ramtext + "memtester loop number: "
							+ memorytesterloops;
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
					System.out.print(info);
					Globals.ramtext = ramtext + info;
					RequestContext requestContext5 = RequestContext.getCurrentInstance();
					requestContext5.update(output);
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

		} catch (Exception e) {
			//LoggerWrapper.myLogger.severe("Unable to SSH to VM " + e.toString());
		}
	}
}