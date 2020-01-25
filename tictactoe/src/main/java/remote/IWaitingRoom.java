package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.Client;

/**
 * Waiting room for players to wait till there's another play to play with
 * 
 * @author rkm
 *
 */
public interface IWaitingRoom extends Remote {

	/**
	 * Handles client requests to start the game
	 * 
	 * @param client client request to start a game
	 * @return lobby containing the game information (starting player, and opponent
	 *         player)
	 * @throws RemoteException
	 * @throws InterruptedException
	 */
	Lobby startGame(Client client) throws RemoteException, InterruptedException;

	/**
	 * Returns a boolean indicating whether the room is full or not
	 * 
	 * @return true if the room is full, false otherwise
	 * @throws RemoteException
	 */
	boolean roomIsFull() throws RemoteException;

}
