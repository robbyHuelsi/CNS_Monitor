package cns_controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.lang.*;

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
	
	public void killModule ()
	{
		p.destroy();
		
	}
	


}
