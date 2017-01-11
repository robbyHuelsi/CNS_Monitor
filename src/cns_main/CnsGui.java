package cns_main;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class CnsGui extends JFrame{

	private static final long serialVersionUID = 1L;

	private CnsConfig config;
	
	public CnsGui(CnsConfig config){
		super("CNS_Monitor");
		this.config = config;

		JFrame total = new JFrame ("crazy CNS monitor");

		JButton load_config = new JButton("Load Config");

		total.add(load_config);
		total.setSize(100,100);
		total.setVisible(true);


		load_config.addActionListener ( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				config.load();
			}

		});

	}
	
	public static void main(String[] args) {
		CnsConfig config = CnsConfig.getInstance();
		CnsGui cns_gui = new CnsGui(config);

	}


}
