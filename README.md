# DICE Fault Injection Tool GUI.
This is the GUI for the  DICE Fault Injection Tool, used to generate faults within Virtual Machines and a FCO Cloud Provider. It can generate service faults such as stopping random VM's or blocking external access and create VM resource faults such as overloading memory or CPU. 

This project can be deployed on a server such as Apache Tomcat and then immediately used to generate faults.
To access the VM level and issue commands the DICE tools use JSCH (http://www.jcraft.com/jsch/) to SSH to the Virtual Machines. 

Supported OS:
Ubuntu - Tested Ubuntu 14.0, 15.10
Centos (With set Repo configured & wget installed) - Tested Centos 7

Current faults that can be run:  
* Deployment faults  
* Shutdown a random VM  
* Block VM external access  
* Stop service running on VM  
* CPU Overload  
* Memory Overload  
* Disk I/O Overload  
