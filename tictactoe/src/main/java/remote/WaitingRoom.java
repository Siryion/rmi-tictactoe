package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import client.Client;
import utils.Pair;

/**
 * Waiting room for players to wait for another player to join after sending a
 * request to start game
 * 
 * @author rkm
 *
 */
public class WaitingRoom extends UnicastRemoteObject implements IWaitingRoom {

	/**
	 * Number of clients who have sent a request to start game and who are waiting
	 * to
	 */
	private int nbClientsWaiting;

	/**
	 * Number of clients who are waiting in the room
	 */
	private int nbClientsInRoom;

	/**
	 * Number of games distributed
	 */
	private int nbGames;

	/**
	 * Contains a pair of clients
	 */
	private Pair<Client> clients;

	/**
	 * Indicates whether a client can enter the room (a client cannot enter when a
	 * room is being emptied or when a room is full)
	 */
	private boolean canEnter;

	/**
	 * Default constructor
	 * 
	 * @throws RemoteException
	 */
	public WaitingRoom() throws RemoteException {
		super();
		nbClientsWaiting = 0;
		nbClientsInRoom = 0;
		nbGames = 0;
		canEnter = true;
		clients = new Pair<>();
	}

	/**
	 * Returns the id of the given client
	 * 
	 * @param player client
	 * @return id 1 if the client is the first in the pair, 2 otherwise
	 */
	private int getPlayerId(Client client) {
		if (client.equals(clients.getFirst()))
			return 1;
		else
			return 2;
	}

	/**
	 * Returns the opponent of the given client
	 * 
	 * @param client requesting to get opponent client
	 * @return the opponent of the given client
	 */
	private Client getOpponentPlayer(Client client) {
		if (client.equals(clients.getFirst()))
			return clients.getSecond();
		else
			return clients.getFirst();
	}

	@Override
	public synchronized Lobby startGame(Client client) throws RemoteException, InterruptedException {

		nbClientsWaiting++;
		System.out.println("Received startGame from " + client.getUsername());
		System.out.println("Nb players connected : " + nbClientsWaiting);

		// Cannot enter if room is full or was previously full and waiting for all
		// players that were inside to get out
		while (!canEnter) {
			System.out.println("Cannot enter a lobby, yet. " + client.getUsername() + " put to wait in waiting room");
			wait();
		}

		// If limit number of players not yet reached, then add this player to the list
		if (!roomIsFull()) {
			nbClientsWaiting--;
			nbClientsInRoom++;
			clients.add(client);
		}

		// Ask the client to wait if number of players not reached
		while (!roomIsFull()) {
			System.out.println("Room is not full : " + client.getUsername() + " put to wait;");
			wait();
		}

		// If full number of players reached then wake up all clients
		canEnter = false;
		notifyAll();
		System.out.println("Room is full. Allowing : " + clients.getFirst().getUsername() + " and "
				+ clients.getSecond().getUsername() + " waiting in full room to start game.");

		// Get the id of the current player in the list
		int playerId = getPlayerId(client);
		Client opponentPlayer = getOpponentPlayer(client);
		int gameId = nbGames;

		// Empty the pair when both player that were part of the pair have awaken
		nbClientsInRoom--;
		if (nbClientsInRoom == 0) {
			clients.empty();
			nbGames++;

			// Notify all those that are waiting to enter that the room can now be entered
			canEnter = true;
			notifyAll();
		}

		// Send the tictactoe grill to the players
		return new Lobby(gameId, 1, playerId, opponentPlayer);

	}

	@Override
	public boolean roomIsFull() {
		return clients.isFull();
	}
}
