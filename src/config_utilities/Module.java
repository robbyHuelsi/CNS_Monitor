package config_utilities;

import java.io.BufferedReader;
import java.io.IOException;

import cns_main.ModuleOutputGui;

public class Module {
	
	private String name;
	private int listening_port;
	private String path;
	private String command;
	private Computer computer;
	private String output;
	private ModuleOutputGui outputGui;

	public String getOutput() {
		return output;
	}
	
	public void addToOutput(String newOutput){
		//TODO secure access to output with mutex?
		output+=newOutput;
		if (outputGui != null)
			outputGui.update();
	}

	public void setOutput(String output) {
		this.output = output;
	}
	
	public void resetOutput(){
		output="";
	}

	public String getStartCommand(){
		return path+command;
	}
	
	public Module (String name, int listening_port, Computer computer){
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

	@Override
	public String toString() {
		return "Module [name=" + name + ", listening_port=" + listening_port + ", path=" + path + ", command=" + command
				+ ", computer=" + computer + "]";
	}

	public Computer getComputer() {
		return computer;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	
	public ModuleOutputGui getOutputGui() {
		return outputGui;
	}

	public void setOutputGui(ModuleOutputGui outputGui) {
		this.outputGui = outputGui;
	}


}
