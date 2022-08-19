README File:
How to run out code:
reactor
mvn clean 
mvn compile


TPC
mvn clean 
mvn compile

Client 
make clean all
cd ./bin
./echo.elf <server IP><Port>
 

1.2
An example:
REGISTER shir 123 05/03/1999
LOGIN shir 123
LOGOUT
FOLLOW 0 shir
POST hi @shir have a good day
PM shir hi have a good day
LOGSTAT
STAT shir|gal|
BLOCK shir

1.3
The filtered words: spl/net/srv/BidiMessagingProtocolImpl

