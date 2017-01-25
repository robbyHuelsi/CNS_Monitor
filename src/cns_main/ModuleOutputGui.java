package cns_main;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import config_utilities.Module;

public class ModuleOutputGui extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	private Module module;
	private JTextArea output;
	private JScrollPane scrollPane;
	private JFrame total;
	
	public ModuleOutputGui (Module module){
		super("Output "+module.getName());
		this.module = module;
		
		
		total = new JFrame ("Output "+module.getName());

		//if (module.getReader()!= null){

			output = new JTextArea(
					"Welcome to our wonderfully charming program ;) \n", 8, 70);
			output.setEditable(false);
			output.setBackground(new Color(0));
			output.setForeground(new Color(16777000));
			output.setMargin(new Insets(5, 5, 5, 5));
			//output.append("you need to select a prototype to be added to Commands list \n");
			
			scrollPane =new JScrollPane(output);
			
			total.add(scrollPane);
			total.setSize(500,300);
			total.setLocation(1000, 0);
			total.setVisible(true);
			
			output.setText(module.getStartCommand()+"\n");
			module.setOutputGui(this);
			
	}
	
	public void update(){
		output.setText(module.getStartCommand()+"\n\n"+module.getOutput());
		scrollPane.getVerticalScrollBar().setValue( scrollPane.getVerticalScrollBar().getMaximum());
	}
	
	public void show(){
		total.setVisible(true);
	}
	
	public void hide(){
		total.setVisible(false);
	}
	

}
