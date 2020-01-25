package client;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import remote.IPlayer;
import remote.IWaitingRoom;
import remote.Lobby;
import remote.Player;
import utils.IpAddress;
import utils.StringUtils;

/**
 * Client class that handles connection to the server and initialization of the
 * player
 * 
 * @author rkm
 *
 */
public class Client extends Thread implements Serializable {

	/**
	 * Waiting allowing the client to be put to wait while waiting for a another to
	 * come
	 */
	IWaitingRoom game = null;

	/**
	 * Player of this client (serves as server to other player)
	 */
	IPlayer player = null;

	/**
	 * Opponent player (serves as server to this player)
	 */
	IPlayer opponentPlayer = null;

	/**
	 * Lobby returned by the server containing information about the game and the
	 * other player
	 */
	Lobby gameInit;

	/**
	 * Indicates whether the game has started or not
	 */
	boolean gameStarted = false;

	/**
	 * Indicates whether the client is connected to the server
	 */
	boolean connectedToServer = false;

	/**
	 * Indicates whether the player has been connected to it's opponent
	 */
	boolean playersConnectionEstablished = false;

	/**
	 * Indiciates whether lan option has been specicied in class argument
	 */
	private boolean hasLanOption = false;

	/**
	 * Scans user input
	 */
	transient Scanner userInput = new Scanner(System.in);

	/**
	 * Port set as default to 5000
	 */
	private String port = "5000";

	/**
	 * IP address of the client set as default to 'localhost'
	 */
	private String ipAddress = "localhost";

	/**
	 * IP address of the server
	 */
	private String serverIpAdress = null;

	/**
	 * Username
	 */
	private String username = null;

	/**
	 * Client constructor
	 * 
	 * @param newPort         port specified in main arguments
	 * @param newHasLanOption indicates whether lan option is specified
	 * @throws RemoteException
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	public Client(String newPort, boolean newHasLanOption)
			throws RemoteException, UnknownHostException, SocketException {
		player = new Player();
		port = newPort;
		hasLanOption = newHasLanOption;
		System.out.println("Intialized to use port number : " + port);

		if (hasLanOption) {
			ipAddress = IpAddress.getPrivateIpAdress();
		}

		System.out.println("Privat ip adress : " + ipAddress);
	}

	@Override
	public void run() {

		try {

			while (!Thread.interrupted()) {

				// Ask user if they want to play on LAN or on localhost
				if (serverIpAdress == null)
					handleUserInputPlayOnLocalhostOrLan();

				// If not connected to server, connect to server
				if (!connectedToServer) {
					try {

						System.out.println("Connecting to server at -> rmi://" + serverIpAdress + ":" + port + "/game");
						game = (IWaitingRoom) Naming.lookup("rmi://" + serverIpAdress + ":" + port + "/game");
						System.out.println("Connected to server. Waiting to for another player to join the game.");
						connectedToServer = true;

					} catch (MalformedURLException | RemoteException | NotBoundException e) {
						System.out.println("Exception : " + e);
						
						// If ConnectException 
						if (e.getClass().getName().equals("java.rmi.ConnectException")) {
							
							System.out.println("The address you entered is either incorrect, or the the server is not started. Try again : ");
							handleUserInputPlayOnLocalhostOrLan();
						}
					}
				}

				// If game not full yet, then wait
				if (!gameStarted && connectedToServer) {

					// Ask user to enter a username
					// System.out.print("Enter a username : ");
					// username = userInput.nextLine();
					username = StringUtils.generateRandomString(7);

					// Request to start game
					gameInit = game.startGame(this);

					// First actions when game has started
					gameStarted = true;
					System.out.println("Game room full! Game starting ...");
					System.out.println("---------------------------------");
					gameInit.whoAmI();
					System.out.println("Game id : " + gameInit.getId());
					System.out.println("---------------------------------");

					// Set player turn
					player.setTurn(gameInit.hasTurn());

					String rmiAddress = "rmi://" + ipAddress + ":" + port + "/game/" + gameInit.getId() + "/player/"
							+ gameInit.getPlayerId();

					// Set this player as server for opponent player
					Naming.rebind(rmiAddress, player);
					System.out.println("Set self as server to opponent player at : " + rmiAddress);

				}

				// If game has started, apply game logic
				if (gameStarted && gameInit != null && !playersConnectionEstablished) {

					// Initialise opponent player
					Client opponent = gameInit.getOpponent();
					

					// Determine opponent id
					int opponentId = -1;
					if (gameInit.getPlayerId() == 1)
						opponentId = 2;
					else
						opponentId = 1;

					String rmiAddress = "rmi://" + opponent.getIpAdress() + ":" + port + "/game/" + gameInit.getId()
							+ "/player/" + opponentId;
					
					System.out.println("Connecting to other player at : " + rmiAddress);

					// Connect to the other player using the opponent id
					try {
						opponentPlayer = (IPlayer) Naming.lookup(rmiAddress);
						System.out.println("Connected to opponent player");
						playersConnectionEstablished = true;
						player.setSymbole(gameInit.getPlayerId());
						player.setConnectedToOther(true);
						System.out.println("---------------------------------\n");
						player.showTurn();

					} catch (MalformedURLException | RemoteException | NotBoundException e) {
						System.out.println("Exception : " + e);
					}

				}

				if (player.isConnectedWithOther()) {

					if (isMyTurn() && !gridIsFull() && !hasWinner()) {

						// Show current status of tictactoe game
						player.displayGame();

						// While the input is invalid, prompt user input for make coords or to quit game
						boolean validUserInput = false;

						while (!validUserInput) {
							System.out.println("Enter : ");
							System.out.println("q : to quit game (exits program)");
							System.out.println("l : leave game (reconnectes to server)");
							System.out.println(
									"l,c : where l is the line and c is the column that you want to mark. (e.g 1,2 to mark the grid in the first row and secnd column.");
							System.out.print("\ntictatoe> ");

							// String input = parseUserInput(userInput.nextLine());
							String input = userInput.nextLine();

							// Check if user wants to quit

							if (input.equals("q")) {
								validUserInput = true;
								System.out.println("You've chosen to quit the game.");
								opponentPlayer.quitFromOther();
								System.exit(0);
							} else if (input.equals("l")) {
								System.out.println("You've chosen to leave the current game and reconnect to server.");
								System.out.println("---------------------------------\n");
								validUserInput = true;
								opponentPlayer.quitFromOther();
								player = new Player();
								gameStarted = false;
								connectedToServer = false;
								playersConnectionEstablished = false;

							} else if (input.contains(",")) {

								String[] coords = input.split(",");

								if (isValidCoords(coords[0], coords[1])) {

									int line = Integer.parseInt(coords[0]);
									int col = Integer.parseInt(coords[1]);

									if (!positionIsAlreadyMarked(line, col)) {

										validUserInput = true;

										// Mark the spot
										player.mark(line - 1, col - 1);

										// Update other player's game, and notify that I've ended my turn
										opponentPlayer.updateMarkFromOther(line - 1, col - 1, player.getSymbole());

										System.out.println("It's now the other player's turn ...");
									}

									else {
										System.out.println(
												"\n---\n That spot is already marked! Please choose other coordinates.");
									}

								} else {
									System.out.println("\n---\nCoordinates are invalid.");
								}
							} else {
								System.out.println("\n---\nInvalid input. Please try again : \n");
							}
						}
					}

					// If game has winner, notify the player
					else if (hasWinner()) {

						System.out.println("---------------------------------\n");

						if (winnerIsMe()) {
							System.out.println("Congratulations, you've won!");
						} else {
							System.out.println("You lose :( Better luck next time!");
						}

						System.out.println();
						handleUserInputToExitOrStartNewGame();
					}

					else if (gridIsFull()) {
						System.out.println("---------------------------------\n");
						System.out.println("It's a draw!");
						System.out.println();
						handleUserInputToExitOrStartNewGame();
					}
				}

				// Handle the situation where the other player has quit the game
				if (playersConnectionEstablished && !player.isConnectedWithOther()) {

					System.out.println("---------------------------------\n");
					System.out.println("The other player has quit the game.");

					handleUserInputToExitOrStartNewGame();
				}
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			System.out.println("Exception -> " + e);

			// If connected to other player, notify the opponent player that I have quit
			try {
				if (player.isConnectedWithOther()) {
					player.setConnectedToOther(false);
					opponentPlayer.quitFromOther();
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}

		}
	}

	/**
	 * Handle user input depedending whether user is playing on localhost or on LAN
	 * 
	 * @throws RemoteException
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	public void handleUserInputPlayOnLocalhostOrLan() throws RemoteException, UnknownHostException, SocketException {

		boolean validUserInput = false;

		if (hasLanOption) {
			System.out.println("Playing on LAN");
			System.out.println("---------------------------------");
			ipAddress = IpAddress.getPrivateIpAdress();

			while (!validUserInput) {

				System.out.print("Enter the server's IP adress : ");
				String input = userInput.nextLine();

				if (IpAddress.isValid(input)) {
					validUserInput = true;
					serverIpAdress = input;
				}
			}
		} else {
			serverIpAdress = "localhost";
		}
	}

	/**
	 * Handles user input options to quit or start new game
	 * 
	 * @throws RemoteException
	 */
	public void handleUserInputToExitOrStartNewGame() throws RemoteException {
		boolean validUserInput = false;

		while (!validUserInput) {

			System.out.println("Enter :");
			System.out.println("c : to continue");
			System.out.println("q : to quit");
			System.out.print("\ntictactoe> ");
			String input = userInput.nextLine();

			if (input.equals("q")) {
				validUserInput = true;
				System.out.println("You've chosen to quit the game.");
				System.out.println("---------------------------------\n");
				System.exit(0);
			} else if (input.equals("c")) {
				validUserInput = true;
				System.out.println("You've chosen to continue playing. Returning to waiting room");
				System.out.println("---------------------------------\n");
				player = new Player();
				gameStarted = false;
				connectedToServer = false;
				playersConnectionEstablished = false;

			} else {
				System.out.println("\n---\nInvalid input. Please try again : \n");
			}
		}
	}

	/**
	 * Checks whether the position at the given coordinates is already marked in
	 * tictactoe
	 * 
	 * @param line
	 * @param col
	 * @return boolean indicating whether the position has already been marked
	 * @throws RemoteException
	 */
	public boolean positionIsAlreadyMarked(int line, int col) throws RemoteException {
		return player.tictactoeIsMarked(line - 1, col - 1);
	}

	/**
	 * Indicates whether it is the client turn or not to play
	 * 
	 * @return boolean indicating whether it is this client's turn to play
	 * @throws RemoteException
	 */
	public boolean isMyTurn() throws RemoteException {
		return player.getTurn();
	}

	/**
	 * Indicates whether the tictactoe grid is full or not
	 * 
	 * @return boolean indicating whether the grill is full
	 * @throws RemoteException
	 */
	public boolean gridIsFull() throws RemoteException {
		return player.tictactoeGridIsFull();
	}

	/**
	 * Indicates whether the there's a winner (either the player or the opponent
	 * must have a straight line)
	 * 
	 * @return boolean indicating whether the game has a winner
	 * @throws RemoteException
	 */
	public boolean hasWinner() throws RemoteException {
		return (player.tictactoeContainsStraightLine() || opponentPlayer.tictactoeContainsStraightLine());
	}

	/**
	 * Indicates whether the winner is this player or not
	 * 
	 * @return boolean indicating whether this player is the winner
	 * @throws RemoteException
	 */
	public boolean winnerIsMe() throws RemoteException {
		return player.tictactoeContainsStraightLine();
	}

	/**
	 * Checks whether the coordinates given by the user are valid
	 * 
	 * @param strLine string containing the index of the line in the grid
	 * @param strCol  string containing the index of the column in the grid
	 * @return boolean indicating whether the coordinates are valid
	 */
	public boolean isValidCoords(String strLine, String strCol) {

		boolean validity = false;
		validity = StringUtils.isNumeric(strLine) && StringUtils.isNumeric(strCol);

		if (validity) {
			int line = Integer.parseInt(strLine);
			int col = Integer.parseInt(strCol);
			validity = line >= 1 && line <= 3 && col >= 1 && col <= 3;
		}

		return validity;
	}

	/**
	 * Gets the ip address associated to this client
	 * 
	 * @return string containing the ip address
	 */
	public String getIpAdress() {
		return ipAddress;
	}

	/**
	 * Returns the username that the user entered in the cmd
	 * 
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {

			String port = "5000";
			boolean lanOption = false;

			if (args.length > 0 && StringUtils.isNumeric(args[0])) {
				int portnum = Integer.parseInt(args[0]);
				if (portnum > 0 && portnum < 65535) {
					port = args[0];
				}
			}

			if (args.length > 1 && args[1].equals("lan")) {
				lanOption = true;
			}

			Client client = new Client(port, lanOption);
			client.start();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
