
cd bin

:: Example usages:
:: For LAN use : ./startup.bat 5000 2 lan
:: For localhsot use : ./startup.bat 5000 2

:: Params:
set portnum=%1
set nbClients=%2
set serveopts=%3

echo "Executing rmiregistry"
start cmd /k rmiregistry %portnum%

:: Timeout necessary to garantee that server doesn't start before rmiregistry is fully launched
timeout /t 2

echo "Starting server"
start cmd /k java server.Server %portnum% %serveopts% 

echo Launching %nbClients% clients

:: Start nbClients in a new terminal
for /l %%c in (1, 1, %nbClients%) do (
   start cmd /k java client.Client %portnum% %serveopts% 
)
