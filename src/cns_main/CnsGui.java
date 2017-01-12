package cns_main;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;

import cns_controller.ModuleMonitor;
import cns_controller.NetworkMonitor;


public class CnsGui extends JFrame{
	/*
	 * Only GUI! should not include any functionality
	 * Config is the Model that should be visualized
	 * Calls the controllers ModuleMonitor and NetworkMonitor
	 * 
	 * 
	 */

	private static final long serialVersionUID = 1L;

	private CnsConfig config;
	private ModuleMonitor module_monitor;
	private NetworkMonitor network_monitor;
	
	public CnsGui(CnsConfig config, ModuleMonitor module_monitor, NetworkMonitor network_monitor){
		super("CNS_Monitor");
		this.config = config;
		this.module_monitor = module_monitor;
		this.network_monitor = network_monitor;

		JFrame total = new JFrame ("crazy CNS monitor");

		JButton load_config = new JButton("Load Config");
		JButton check_network = new JButton ("Check Network");
		JPanel buttons = new JPanel();
		buttons.add(load_config);
		buttons.add(check_network);

		total.add(buttons);
		total.setSize(300,100);
		total.setVisible(true);


		load_config.addActionListener ( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				config.load();
			}

		});
		
		check_network.addActionListener ( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				network_monitor.all_computers_reachable();
				//TODO update GUI
			}

		});


	}
	
	public static void main(String[] args) {
		CnsConfig config = CnsConfig.getInstance();
		ModuleMonitor module_monitor = new ModuleMonitor(config);
		NetworkMonitor network_monitor = new NetworkMonitor(config);
		
		
		CnsGui cns_gui = new CnsGui(config, module_monitor, network_monitor);

	}


}
