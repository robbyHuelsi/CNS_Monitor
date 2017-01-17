package cns_controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.lang.*;

import config_utilities.Module;

public class ModuleStarter extends Thread{
	private Process p;
	private Module module;
	
	public ModuleStarter (Module module){
		this.module = module;
	}
	
	public void run ()
	{
		/*if (!module.getStartCommand().isEmpty()){
		try {
			//ProcessBuilder pb = new ProcessBuilder("cmd.exe dir");
			//System.out.println(System.getenv("PATH"));
			//pb.environment().put("PATH", System.getenv("PATH"));
			Process p;
			p = pb.start();
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			System.out.println(reader.readLine());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
		String cmd = module.getStartCommand();
		System.out.println("starting module "+module.getName()+" with :\""+cmd+"\" ");
		try {
			p = Runtime.getRuntime().exec(cmd);
			// read output with BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String temp = null;
			while( (temp = br.readLine())!= null){
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

}
