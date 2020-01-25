# RMI Tic Tac Toe

A simple command line tictactoe game using Java RMI.

## Compile

To compile the project, in the `tictactoe` folder, run : `.\build.bat`.

## Run

## On localhost

To launch the server and clients on localhost, run the script in the `tictactoe` folder :

```
startup port nbClients
```

For example :

```
startup.sh 5000 2
```

## On LAN

Add the 'lan' argument to the startup script.

### Example

On machine A, use the `startup.sh` to launch a the rmi registry, the server, with 0 clients :

```
startup 5000 0 lan
```

On machine B, go to folder `bin` and start an RMI regsitry at the desired port (must be the same port as machine A) with the command :

```
rmiregistry 5000
```

Then start a client :

```
java client.Client 5000 lan
```

On the prompt, enter the ip address of the server started on machine A.

On machine C, do the same as for machine B. The clients on machine B and machine C should then be able to communicate with the server and with each other.

