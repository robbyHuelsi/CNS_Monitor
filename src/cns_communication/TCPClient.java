package cns_communication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient
{
	private Socket socket = null;

	/**
	 * Opens a socket to incoming connections on specified device and port
	 * 
	 * @param targetIP the IP of the device we want to listen to, null if all available devices can be used
	 * @param targetPort the port we listen to
	 * @param keepAlive true if after 2h of no com the connection should be kept alive 
	 */
	public TCPClient(InetAddress targetIP, int targetPort, boolean keepAlive)
	{
		System.out.println("Connection established to: " + socket.getInetAddress() + ":" + socket.getPort());
		System.out.println("My stuff is: " + socket.getLocalAddress() + ":" + socket.getLocalPort());
		try
		{
			socket = new Socket(targetIP, targetPort);
			socket.setKeepAlive(keepAlive);
		} catch (IOException e)
		{
			System.err.println("Error on connecting to: " + targetIP + ":" + targetPort);
			e.printStackTrace();
		}
	}

	public boolean endConnection()
	{
		if (socket == null)
			return false;
		try
		{
			socket.close();
		} catch (IOException e)
		{
			System.err.println("Error closing socket!");
		}
		return true;
	}

	public boolean send(String message)
	{
		try
		{

			DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
			outToClient.writeBytes(message+"\n");
		} catch (IOException e)
		{
			System.err.println("Sending impossible, connection errors");
			// e.printStackTrace();
			return false;
		}

		return true;
	}

	public String receive()
	{
		try
		{
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			return inFromClient.readLine();
			
		} catch (IOException e)
		{
			System.err.println("Receiving impossible, connection errors");
			// e.printStackTrace();
			return null;
		}
	}
}
