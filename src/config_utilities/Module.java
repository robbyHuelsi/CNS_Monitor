package config_utilities;

public class Module {
	
	private String name;
	private int listening_port;
	private String location_path;
	private Computer computer;
	
	public String getCommandString(){
		String command="";
		return command;
	}
	
	public Module (String name, int listening_port, Computer computer){
		this.name = name;
		this.listening_port = listening_port;
		this.computer = computer;
	}

}
