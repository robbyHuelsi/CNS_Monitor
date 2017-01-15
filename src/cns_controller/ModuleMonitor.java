package cns_controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cns_main.CnsConfig;
import config_utilities.Module;

public class ModuleMonitor {
	/*
	 * This class should monitor the modules
	 * gets the information from the config
	 * updates the config
	 */
	
	private CnsConfig config;
	
	public boolean start_all_modules(){
		for (Module module : config.getAll_modules()){
			
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
			
			
			
			Process p;
			String cmd = module.getStartCommand();
			if (!cmd.isEmpty()){
				cmd ="cmd.exe \\c "+cmd;
				System.out.println("starting module "+module.getName()+" with :\""+cmd+"\" ");

				try {
					p = Runtime.getRuntime().exec(cmd);
					p.waitFor();
					// read output with BufferedReader
					BufferedReader reader = new BufferedReader(new InputStreamReader(
							p.getInputStream()));
					String temp = null;
					while( (temp = reader.readLine())!= null){
						System.out.println(temp);
					}
					System.out.println(reader.readLine());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				//String line = reader.readLine();
			}

		}
		return true;
	}
	
	public ModuleMonitor(CnsConfig config){
		this.config = config;
	}

}
