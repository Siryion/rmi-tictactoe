package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote player interface
 * 
 * @author rkm
 *
 */
public interface IPlayer extends Remote {

	/**
	 * Returns the copy (because the opponent player also has one) of tictactoe game
	 * owned by the player
	 * 
	 * @return instance of the tictactoe game
	 * @throws RemoteException
	 */
	public Tictactoe getTictactoe() throws RemoteException;

	/***
	 * Returns a boolean indicating whether it is this player's turn to play or not
	 * 
	 * @return true if it's this player's turn to play, false otherwise
	 * @throws RemoteException
	 */
	public boolean getTurn() throws RemoteException;

	/**
	 * Returns the symbole used by this player to mark a position
	 * 
	 * @return string containing the symbole
	 * @throws RemoteException
	 */
	public String getSymbole() throws RemoteException;

	/**
	 * Returns the username chosen by the player
	 * 
	 * @return username
	 * @throws RemoteException
	 */
	public String getUsername() throws RemoteException;

	/**
	 * Set the current turn to true or false
	 * 
	 * @param turn boolean should be true if it's this player's turn to player,
	 *             false otherwise
	 * @throws RemoteException
	 */
	public void setTurn(boolean turn) throws RemoteException;

	/**
	 * Set a symbole for the player to use to mark the position in the tictactoe
	 * grid
	 * 
	 * @param playerId id of the player used to determine which symbole to give (if
	 *                 playerId is 1 then symbole is 'x', 'O' otherwise)
	 * @throws RemoteException
	 */
	public void setSymbole(int playerId) throws RemoteException;

	/**
	 * Sets a username for the player
	 * 
	 * @param username username chosen by the player from user input
	 * @throws RemoteException
	 */
	public void setUsername(String username) throws RemoteException;

	/**
	 * Sets the boolean indicatig whether this player is remotely connected to the
	 * other player
	 * 
	 * @param connectedWithOther true if this player is connected to the other,
	 *                           false otherwise
	 * @throws RemoteException
	 */
	public void setConnectedToOther(boolean connectedWithOther) throws RemoteException;

	/**
	 * Prints the tictactoe grid in the console
	 * 
	 * @throws RemoteException
	 */
	public void displayGame() throws RemoteException;

	/**
	 * Prints the current turn to the console
	 * 
	 * @throws RemoteException
	 */
	public void showTurn() throws RemoteException;

	/**
	 * Returns a boolean indicating whether the player is remotely connected to the
	 * other player
	 * 
	 * @return true if player is connected to the other, false otherwise
	 * @throws RemoteException
	 */
	public boolean isConnectedWithOther() throws RemoteException;

	/**
	 * Marks to the position in the grid
	 * 
	 * @param line index of the line in the grid
	 * @param col  index of the column in the grid
	 * @throws RemoteException
	 */
	public void mark(int line, int col) throws RemoteException;

	/**
	 * Update the marking on grid sent by the other player
	 * 
	 * @param line         index of the line in the grid
	 * @param col          index of the column in the grid
	 * @param otherSymbole marking symbole of the other player
	 * @throws RemoteException
	 */
	public void updateMarkFromOther(int line, int col, String otherSymbole) throws RemoteException;

	/**
	 * Receive quit from the other player and set connectedWithOther to false
	 * 
	 * @throws RemoteException
	 */
	public void quitFromOther() throws RemoteException;

	/**
	 * Returns a boolean indicating whether the tictactoe contains a straight line
	 * 
	 * @return boolean true if tictactoe contains a straight line, false otherwise
	 * @throws RemoteException
	 */
	public boolean tictactoeContainsStraightLine() throws RemoteException;

	/**
	 * Returns a boolean indicating whether the tictactoe grid is full
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public boolean tictactoeGridIsFull() throws RemoteException;

	/**
	 * Retusns a boolean indiciating whether the grid at the position [line,col] is
	 * marked
	 * 
	 * @param line index of the line in the grid
	 * @param col  index of the column in the grid
	 * @return true if the position is marked, false otherwise
	 * @throws RemoteException
	 */
	boolean tictactoeIsMarked(int line, int col) throws RemoteException;

}
