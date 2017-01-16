package config_utilities;

public class Module {
	
	private String name;
	private int listening_port;
	private String path;
	private String command;
	private Computer computer;
	
	public String getStartCommand(){
		return path+command;
	}
	
	public Module (String name, int listening_port, Computer computer){
		this.name = name;
		this.listening_port = listening_port;
		this.computer = computer;
		path="";
		command="";
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
	

}
