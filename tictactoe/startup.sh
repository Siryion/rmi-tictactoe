
cd bin
# echo "Executing : rmic remote.Tictactoe"
# rmic remote.Tictactoe

# $1 : portnumber
# $2 : number of clients
# $3 : server options (lan)

portnum=$1
secondportnum=$[$portnum+1]

echo "Executing rmiregistry"
xterm -e rmiregistry $portnum &

# Timeout necessary to garantee that server doesn't start before rmiregistry is fully launched
sleep 2

echo "Starting server"
xterm -e java server.Server $portnum $3 &

if [ -n "$2" ] 
then 

    # Start clients in terminal
    echo "Launching $2 clients"
    c=0

    while [ $c -lt $2 ] 
    do 
        echo "Launching client $c"
        xterm -e java client.Client $portnum $3 &
        c=$[$c+1]
    done
fi