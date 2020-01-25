package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Player class which is also a remote object
 * 
 * @author rkm
 *
 */
public class Player extends UnicastRemoteObject implements IPlayer {

	/**
	 * Player username
	 */
	private String username;

	/**
	 * Indicates whether it is this player's turn to play
	 */
	private boolean turn;

	/**
	 * Contains the tictactoe grid
	 */
	private Tictactoe tictactoe;

	/**
	 * Symbole the player uses to mark a position can be 'X' or 'O' (automatically
	 * assigned based on their the player id)
	 */
	private String symbole;

	/***
	 * Indicates whether this player is connected with the other
	 */
	private boolean connectedWithOther;

	/**
	 * Default constructor
	 * 
	 * @throws RemoteException
	 */
	public Player() throws RemoteException {
		super();
		connectedWithOther = false;
		tictactoe = new Tictactoe();
	}

	public Player(boolean newTurn) throws RemoteException {
		super();
		turn = newTurn;
		connectedWithOther = false;
		tictactoe = new Tictactoe();
	}

	@Override
	public Tictactoe getTictactoe() throws RemoteException {
		return tictactoe;
	}

	@Override
	public boolean getTurn() throws RemoteException {
		return turn;
	}

	@Override
	public String getSymbole() throws RemoteException {
		return symbole;
	}

	@Override
	public String getUsername() throws RemoteException {
		return username;
	}

	@Override
	public void setTurn(boolean newTurn) throws RemoteException {
		turn = newTurn;
	}

	@Override
	public void setSymbole(int playerId) throws RemoteException {
		if (playerId == 1)
			symbole = "X";
		else
			symbole = "O";
	}

	@Override
	public void setUsername(String newUsername) throws RemoteException {
		username = newUsername;
	}

	@Override
	public void setConnectedToOther(boolean newConnectedWithOther) throws RemoteException {
		connectedWithOther = newConnectedWithOther;
	}

	@Override
	public void displayGame() throws RemoteException {
		tictactoe.display();
	}

	@Override
	public void showTurn() throws RemoteException {
		if (turn)
			System.out.println("It's your turn to play!");
		else
			System.out.println("It's the other player's turn.");
	}

	@Override
	public boolean isConnectedWithOther() throws RemoteException {
		return connectedWithOther;
	}

	@Override
	public void mark(int line, int col) throws RemoteException {
		tictactoe.mark(line, col, symbole);
		tictactoe.display();
		turn = false;
	}

	@Override
	public void updateMarkFromOther(int line, int col, String otherSymbole) throws RemoteException {
		tictactoe.mark(line, col, otherSymbole);
		turn = true;
	}

	@Override
	public void quitFromOther() throws RemoteException {
		connectedWithOther = false;
	}

	@Override
	public boolean tictactoeContainsStraightLine() throws RemoteException {
		return tictactoe.getContainsStraightLine();
	}

	@Override
	public boolean tictactoeGridIsFull() throws RemoteException {
		return tictactoe.gridIsFull();
	}

	@Override
	public boolean tictactoeIsMarked(int line, int col) throws RemoteException {
		return tictactoe.isMarked(line, col);
	}

}
