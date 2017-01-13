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

	public String getName() {
		return name;
	}

	public int getListeningPort() {
		return listening_port;
	}

	public String getLocationPath() {
		return location_path;
	}

	@Override
	public String toString() {
		return "Module [name=" + name + ", listening_port=" + listening_port + ", location_path=" + location_path
				+ ", computer=" + computer + "]";
	}

	public Computer getComputer() {
		return computer;
	}

}
