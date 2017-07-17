package com.basic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.util.ArrayUtils;

import com.globals.Globals;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.NodeObject;
import com.model.VMobject;
import com.faults.CpuStress;
import com.faults.RamStress;
import com.faults.DiskStress;

@ManagedBean
@SessionScoped
public class DiceDeploymentCall {

	public void dicedeploymentservice(String token) {

		try {
			//String Token = "Token 540e5f240a1e609d7c021f3e40a591d6db6c522b";
			String resturl = "http://109.231.122.194/containers";

			URL url = new URL(resturl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization", token);

			if (conn.getResponseCode() != 200) {
				Globals.deploymentnodelist= Globals.deploymentnodelist.concat("Failed : HTTP error code : " + conn.getResponseCode());
				RequestContext requestContext = RequestContext.getCurrentInstance();
				requestContext.update("DEPLOYMENTOUTPUT");
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			
			String output;
			String jsonString = "";
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				jsonString = output;

			}

			Gson gson = new GsonBuilder().create();
			VMobject[] vm = gson.fromJson(jsonString, VMobject[].class);

			String errorstate = "false";
			for(int i = 0; i< vm.length ; i++)
			{
				if (vm[i].getBlueprint() !=null)
				{

				if (vm[i].getBlueprint().getIn_error() !=null | (vm[i].getBlueprint().getIn_error().toString().equals(errorstate)))
				{
	
			System.out.println(vm[i]);
			String deploymenttext =  vm[i].toString()+ "\n";
			Globals.deploymenttext= Globals.deploymenttext.concat(deploymenttext);
				}
				}
				else
				{
					Globals.deploymenttext= Globals.deploymenttext.concat("Unable to get deployment information for this deployement, perhaps the blueprint is empty? \n");
				}
			}
			RequestContext requestContext = RequestContext.getCurrentInstance();
			requestContext.update("DEPLOYMENTOUTPUT");
			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}
}

	public void dicedeploymentfaultcaller(String token, String id,String name, String fault,String username, String password) {
		try {
			
			String resturl = "http://109.231.122.194/containers/"+id+"/nodes";
			URL url = new URL(resturl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization", token);

			if (conn.getResponseCode() != 200) {
				Globals.deploymentnodelist= Globals.deploymentnodelist.concat("Failed : HTTP error code : " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			
			String output;
			String jsonString = "";
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				jsonString = output;
			}
			File file = new File("Deployment.txt");

			Gson gson = new GsonBuilder().create();
			NodeObject[] node = gson.fromJson(jsonString, NodeObject[].class);

			for(int i = 0; i< node.length ; i++)
			{
			System.out.println(node[i]);
			System.out.println(node[i].getIp());

			if (ArrayUtils.contains(node[i].getComponents(), name)){
			String host;
			//check if name exists from deployment
			//get username for VM
			//Get SSH key
			
			String setConfigkeypath = ("/Users/darrenw/Downloads/PrimeTest/Uploads/test");
			switch(fault){
			
			case "cpu" :
			host= username +"@"+node[i].getIp();
            System.out.println(host);
	  	    CpuStress cpustress = new CpuStress();
	  	    //cpustress.stresscpu("1","50",password,host,"-no","DEPLOYMENTOUTPUTDETAILS");
	  	    cpustress.stresscpu("1","5","-no",host,setConfigkeypath,"DEPLOYMENTOUTPUTDETAILS");

	  	    System.out.println("Completed");
	  	    break;
	  	    
			case "ram":
			host= name +"@"+node[i].getIp();
			RamStress ramstress = new RamStress();
			ramstress.stressmemory(host, "-no", "1", "200", setConfigkeypath,"DEPLOYMENTOUTPUTDETAILS");
	  	    break;
	  	    
			default :
	            System.out.println("Invalid");
			}
			}
			//Globals.deploymentnodelist= Globals.deploymentnodelist.concat(node[i].toString());
			//Globals.deploymentnodelist= Globals.deploymentnodelist.concat(fault);
			}
			
			RequestContext requestContext = RequestContext.getCurrentInstance();
			requestContext.update("DEPLOYMENTOUTPUTDETAILS");
			conn.disconnect();

		} catch (MalformedURLException e) {

			//Globals.deploymentnodelist= Globals.deploymentnodelist.concat(e.toString());

		} catch (IOException e) {

			//Globals.deploymentnodelist= Globals.deploymentnodelist.concat(e.toString());
		}
	}
	
	public void dicedeploymentfaultcaller1(String token, String id,String name, String fault,String username, String sshkey) {
		try {
    	    System.out.println(sshkey);

			String resturl = "http://109.231.122.194/containers/"+id+"/nodes";
			URL url = new URL(resturl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization", token);

			if (conn.getResponseCode() != 200) {
				Globals.deploymentnodelist= Globals.deploymentnodelist.concat("Failed : HTTP error code : " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			
			String output;
			String jsonString = "";
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				jsonString = output;
			}
			File file = new File("Deployment.txt");

			Gson gson = new GsonBuilder().create();
			NodeObject[] node = gson.fromJson(jsonString, NodeObject[].class);

			for(int i = 0; i< node.length ; i++)
			{
		//	System.out.println(node[i]);
		
			if (ArrayUtils.contains(node[i].getComponents(), name)){
			//String host;
			System.out.println(node[i].toString());
			//String Configkeypath = ("/Users/darrenw/Downloads/PrimeTest/Uploads/test");
			String host;
			switch(fault){
			
			case "cpu" :
				 host= username +"@"+node[i].getIp();
	            System.out.println(host);
		  	    CpuStress cpustress = new CpuStress();
		  	    //cpustress.stresscpu("1","50",password,host,"-no","DEPLOYMENTOUTPUTDETAILS");
		  	    cpustress.stresscpu("1","60","-no",host,sshkey,"DEPLOYMENTOUTPUTDETAILS");
		  	    System.out.println("Completed");
		  	    break;
	  	    
			case "ram":
				host= username +"@"+node[i].getIp();
				RamStress ramstress = new RamStress();
				ramstress.stressmemory(host, "-no", "1", "256", sshkey,"DEPLOYMENTOUTPUTDETAILS");
				System.out.println("Completed");
				break;
	  	    
			case "disk":
				 host= username +"@"+node[i].getIp();
				DiskStress diskstress = new DiskStress();
				diskstress.stressdisk(host, "-no", "", "", sshkey);
				System.out.println("Completed");
		  	    break;
			case "blockvm":
				host= username +"@"+node[i].getIp();
				BlockVM blockvm = new BlockVM();
				blockvm.blockfirewall(host, "-no", sshkey);
				System.out.println("Completed");
		  	    break; 
			case "stopservice":
				host= username +"@"+node[i].getIp();
				StopService stopservice = new StopService();
				stopservice.stopservice(host, "-no", "", sshkey);
				System.out.println("Completed");
		  	    break;
			case "bandwidth":
				host= username +"@"+node[i].getIp();
				System.out.println("Completed");
		  	    break; 
		  	    
			default :
	            System.out.println("Invalid");
			}
			
			}
			}
			RequestContext requestContext = RequestContext.getCurrentInstance();
			requestContext.update("DEPLOYMENTOUTPUTDETAILS");
			conn.disconnect();

		} catch (MalformedURLException e) {

			Globals.deploymentnodelist= Globals.deploymentnodelist.concat(e.toString());

		} catch (IOException e) {

			Globals.deploymentnodelist= Globals.deploymentnodelist.concat(e.toString());
		}	
}
		
	public void dicedeploymentlist(String token, String id) {
		try {
			
			String resturl = "http://109.231.122.194/containers/"+id+"/nodes";

			URL url = new URL(resturl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization", token);

			if (conn.getResponseCode() != 200) {
				Globals.deploymentnodelist= Globals.deploymentnodelist.concat("Failed : HTTP error code : " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			

			String output;
			String jsonString = "";
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				jsonString = output;
			}
			File file = new File("Deployment.txt");

			Gson gson = new GsonBuilder().create();
			NodeObject[] node = gson.fromJson(jsonString, NodeObject[].class);

			for(int i = 0; i< node.length ; i++)
			{
				
			//	try {
				//    Scanner scanner = new Scanner(file);
				  //  while (scanner.hasNextLine()) {
				    //    String line = scanner.nextLine();

			System.out.println(node[i]);
			
			//if ( ArrayUtils.contains(node[i].getComponents(), line))
			//{
			//TODO Read properties to compare and use this to start faults on VM
			//System.out.println("Found medium");
			//}
		//	else 
			//{
			//	Globals.deploymentnodelist= Globals.deploymentnodelist.concat("Not found details from file");
		//	}
			//	    }
			//	} finally
			//	{
					
			//	}
			Globals.deploymentnodelist= Globals.deploymentnodelist.concat(node[i].toString());
			
			}
			RequestContext requestContext = RequestContext.getCurrentInstance();
			requestContext.update("DEPLOYMENTOUTPUTDETAILS");
			conn.disconnect();

		} catch (MalformedURLException e) {

			Globals.deploymentnodelist= Globals.deploymentnodelist.concat(e.toString());

		} catch (IOException e) {

			Globals.deploymentnodelist= Globals.deploymentnodelist.concat(e.toString());
		}
	}	
}
		