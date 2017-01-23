package config_utilities;

import java.io.BufferedReader;
import java.io.IOException;

import cns_main.CnsConfig;
import cns_main.ModuleOutputGui;

public class Module {
	
	private CnsConfig cnsConfig;
	private String name;
	private int listening_port;
	private String path;
	private String command;
	private Computer computer;
	private String output;
	private ModuleOutputGui outputGui;

	public Module (CnsConfig cnsConfig, String name, int listening_port, Computer computer){
		this.cnsConfig = cnsConfig;
		this.name = name;
		this.listening_port = listening_port;
		this.computer = computer;
		path="";
		command="";
		output="";
	}
	
	public String getName() {
		return name;
	}
	
	public int getListeningPort() {
		return listening_port;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public String getStartCommand(){
		return path+cnsConfig.parseCommand(command);
	}
	
	public Computer getComputer() {
		return computer;
	}
	
	public String getOutput() {
		return output;
	}
	
	public void setOutput(String output) {
		this.output = output;
	}
	
	public void addToOutput(String newOutput){
		//TODO secure access to output with mutex?
		output+=newOutput;
		if (outputGui != null)
			outputGui.update();
	}
	
	public void resetOutput(){
		output="";
	}

	public ModuleOutputGui getOutputGui() {
		return outputGui;
	}

	public void setOutputGui(ModuleOutputGui outputGui) {
		this.outputGui = outputGui;
	}

	@Override
	public String toString() {
		return "Module [name=" + name + ", listening_port=" + listening_port + ", path=" + path + ", command=" + command
				+ ", computer=" + computer + "]";
	}

	public Object getParamByName(String name){
		switch (name.toLowerCase()) {
		case "name":
			return getName();
		case "port":
			return getListeningPort();
		case "path":
			return getPath();
		case "computer":
			return getComputer();
		default:
			return null;
		}
	}
}
