package cns_controller;



import java.util.Vector;

import cns_main.CnsConfig;
import config_utilities.Module;

public class ModuleMonitor {
	/*
	 * This class should monitor the modules
	 * gets the information from the config
	 * updates the config
	 */
	private Vector<ModuleStarter> moduleStarters = new Vector<ModuleStarter>();
	
	private CnsConfig config;
	
	public boolean start_all_modules(){
		moduleStarters.setSize(config.getAll_modules().size());
		for (int i=0; i<config.getAll_modules().size(); ++i){
			if (!config.getAll_modules().elementAt(i).getStartCommand().isEmpty()){
				moduleStarters.add(i, new ModuleStarter(config.getAll_modules().elementAt(i)));
				moduleStarters.elementAt(i).start();
			}
		}
		return true;
	}

	public boolean start_module(int module_num){
		Module module = config.getModule( module_num);
			if (!module.getStartCommand().isEmpty()){
				moduleStarters.add(module_num, new ModuleStarter(module));
				moduleStarters.elementAt(module_num).start();
			}
		return true;
	}
	
	public boolean kill_module(int module_num){
		if (moduleStarters.elementAt(module_num) != null){
			moduleStarters.elementAt(module_num).killModule();
		}
		return true;
	}
	
	public boolean kill_all_modules(){
		for (int i=0; i<moduleStarters.size(); ++i){
			if (moduleStarters.elementAt(i) != null)
				moduleStarters.elementAt(i).killModule();
		}
		return true;
	}

	public ModuleMonitor(CnsConfig config){
		this.config = config;
		moduleStarters.setSize(config.getAll_modules().size());
	}

}
