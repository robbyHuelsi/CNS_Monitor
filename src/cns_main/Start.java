package cns_main;

import cns_controller.ModuleMonitor;
import cns_controller.NetworkMonitor;

public class Start {
	public static void main(String[] args) {
		CnsConfig config = CnsConfig.getInstance();
		CnsSettings setting = new CnsSettings();
		
		ModuleMonitor module_monitor = new ModuleMonitor(config, setting);
		NetworkMonitor network_monitor = new NetworkMonitor(config);

		CnsGui cns_gui = new CnsGui(config, module_monitor, network_monitor, setting);
		config.setGui(cns_gui);
		network_monitor.setGui(cns_gui);
		module_monitor.setGui(cns_gui);
				

	}
}
