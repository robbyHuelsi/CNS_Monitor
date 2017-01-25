package cns_controller;



import java.util.Vector;

import cns_main.CnsConfig;
import cns_main.CnsSettings;
import config_utilities.Module;

public class ModuleMonitor {
	/*
	 * This class should monitor the modules
	 * gets the information from the config
	 * updates the config
	 */
	private Vector<ModuleStarter> moduleStarters = new Vector<ModuleStarter>();
	
	private CnsConfig config;
	private CnsSettings setting;
	
	public boolean start_all_modules(){
		moduleStarters.setSize(config.getAll_modules().size());
		for (int i=0; i<config.getAll_modules().size(); ++i){
			if (!config.getAll_modules().elementAt(i).getStartCommand().isEmpty()){
				moduleStarters.add(i, new ModuleStarter(config.getAll_modules().elementAt(i), setting));
				moduleStarters.elementAt(i).start();
			}
		}
		return true;
	}

	public boolean start_module(int module_num){
		moduleStarters.setSize(config.getAll_modules().size());
		Module module = config.getModule( module_num);
			if (!module.getStartCommand().isEmpty()){
				moduleStarters.add(module_num, new ModuleStarter(module, setting));
				moduleStarters.elementAt(module_num).start();
			}
		return true;
	}
	
	public boolean kill_module(int module_num){
		if (moduleStarters.size()>module_num && moduleStarters.elementAt(module_num) != null){
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

	public ModuleMonitor(CnsConfig config, CnsSettings setting){
		this.config = config;
		this.setting = setting;
		moduleStarters.setSize(config.getAll_modules().size());
	}

}
