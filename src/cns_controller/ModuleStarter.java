package cns_controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.lang.*;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import config_utilities.Module;

public class ModuleStarter extends Thread{
	private Process p;
	private Module module;
	//private boolean running;
	
	public ModuleStarter (Module module){
		this.module = module;
		module.resetOutput();
	}
	
	public void run ()
	{
		String cmd = module.getStartCommand();
		System.out.println("starting module "+module.getName()+" with :\""+cmd+"\" ");
		if (module.getComputer().isThisPC()){
			try {
				p = Runtime.getRuntime().exec(cmd);
				// read output with BufferedReader
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String temp = null;
				while( ((temp = br.readLine())!= null)){
					module.addToOutput(temp+"\n");
				}
				//System.out.println(module.getReader().readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} /*catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		}
		else{
			try {

				//example from http://www.jcraft.com/jsch/examples/Exec.java.html
				JSch jsch=new JSch();  

				Session session=jsch.getSession(module.getComputer().getUser(), module.getComputer().getIpLan(), 22);

				// username and password will be given via UserInfo interface.
				//UserInfo ui=new MyUserInfo();
				//session.setUserInfo(ui);
				session.setPassword("test");
				session.connect();

				//String command=JOptionPane.showInputDialog("Enter command", 
				//		"set|grep SSH");

				Channel channel=session.openChannel("exec");
				((ChannelExec)channel).setCommand(cmd);

				// X Forwarding
				// channel.setXForwarding(true);

				//channel.setInputStream(System.in);
				channel.setInputStream(null);

				//channel.setOutputStream(System.out);

				//FileOutputStream fos=new FileOutputStream("/tmp/stderr");
				//((ChannelExec)channel).setErrStream(fos);
				((ChannelExec)channel).setErrStream(System.err);

				InputStream in=channel.getInputStream();

				channel.connect();

				byte[] tmp=new byte[1024];
				while(true){
					while(in.available()>0){
						int i=in.read(tmp, 0, 1024);
						if(i<0)break;
						System.out.print(new String(tmp, 0, i));
					}
					if(channel.isClosed()){
						if(in.available()>0) continue; 
						System.out.println("exit-status: "+channel.getExitStatus());
						break;
					}
					try{Thread.sleep(1000);}catch(Exception ee){}
				}
				channel.disconnect();
				session.disconnect();
			}
			catch(Exception e){
				System.out.println(e);
			}
		}

	}
	
	public void killModule ()
	{
		if (p!=null)
			p.destroy();
		
	}
	


}
