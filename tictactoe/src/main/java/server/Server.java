package server;

import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;

import remote.IWaitingRoom;
import remote.WaitingRoom;
import utils.IpAddress;
import utils.StringUtils;

/**
 * Server class used to handle incomming requests from clients to start a tictactoe game
 * @author rkm
 *
 */
public class Server {

	/**
	 * Default constructor
	 */
	public Server() {

	}

	/**
	 * Initializes the server and binds it to an address and a port
	 * @param args
	 * @throws RemoteException
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	public static void main(String[] args) throws RemoteException, UnknownHostException, SocketException {

		/**
		 * Port set by default to '5000'
		 */
		String port = "5000";
		
		/**
		 * IP Address is set by default to 'localhost'
		 */
		String ipAdress = "localhost";
		
		System.out.println("TicTacToe server");

		// Check validity of arguments
		if (args.length > 0) {
			
			// Check that first argument is a valid port number
			if (StringUtils.isNumeric(args[0])) {
				int portnum = Integer.parseInt(args[0]);
				if (portnum > 0 && portnum < 65535) {
					port = args[0];
				}
			}
			
			// If there's a second argument, check that it equals to lan
			if (args.length > 1 && args[1].equals("lan")) {
				ipAdress = IpAddress.getPrivateIpAdress();
			}
		}

		System.out.println("Private IP address : " + ipAdress);
		System.out.println("Using port : " + port);
		System.out.println("Bound to -> rmi://" + ipAdress + ":" + port + "/game");

		// Initialise waiting room and bind it to an address and port
		IWaitingRoom game = new WaitingRoom();
		
		try {
			Naming.rebind("rmi://" + ipAdress + ":" + port + "/game", game);
			System.out.println("Server is ready.");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO : handle connection timeout exception
			System.out.println(e);
		}
	}
}
