package remote;

import java.io.Serializable;

import client.Client;

/**
 * Lobby class containing the game information such as turn, player id and
 * opponent
 * 
 * @author rkm
 *
 */
public class Lobby implements Serializable {

	/**
	 * Id of the game given by WaitingRoom
	 */
	private int id;

	/**
	 * Turn of the player to start the game (randomly set by WaitingRoom)
	 */
	private int turn;

	/**
	 * Id of the player who requested to start the game
	 */
	private int playerId;

	/**
	 * Opponent client
	 */
	private Client opponent;

	/**
	 * Default constructor
	 */
	public Lobby() {
	}

	/**
	 * 
	 * @param newId
	 * @param newStartingPlayer
	 * @param newPlayerId
	 * @param newOpponentPlayer
	 */
	public Lobby(int newId, int newStartingPlayer, int newPlayerId, Client newOpponentPlayer) {
		id = newId;
		turn = newStartingPlayer;
		playerId = newPlayerId;
		opponent = newOpponentPlayer;
	}

	/**
	 * Prints the id the of player to the console
	 */
	public void whoAmI() {
		System.out.println("You are player" + playerId + " playing against " + opponent.getUsername());
	}

	/**
	 * Returns the id of the player
	 * 
	 * @return id of the player
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * Returns the turn
	 * 
	 * @return current turn (can be either 1 or 2)
	 */
	public int getTurn() {
		return turn;
	}

	/**
	 * Set the value of the turn
	 * 
	 * @param newTurn can be 1 or 2
	 */
	public void setTurn(int newTurn) {
		turn = newTurn;
	}

	/**
	 * Prints the current turn to the console
	 */
	public void showTurn() {
		if (turn == playerId)
			System.out.println("It's your turn to play!");
		else
			System.out.println("It's the other player's turn.");
	}

	/**
	 * Returns a boolean indicating whether it's the turn of the player
	 * 
	 * @return true if turn corresponds to the current playerId, false otherwise
	 */
	public boolean hasTurn() {
		return (turn == playerId);
	}

	/**
	 * Returns the id of the game
	 * 
	 * @return id of the game
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the opponent client
	 * 
	 * @return opponent client
	 */
	public Client getOpponent() {
		return opponent;
	}
}
