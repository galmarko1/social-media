#include <stdlib.h>

#include <iostream>
#include <thread>
#include "../include/connectionHandler.h"
#include "../include/SocketReader.h"
#include "../include/KeyboardReader.h"


using boost::asio::ip::tcp;
/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
class KeyboardReader;
class SocketReader;
int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }

    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler *ch=new ConnectionHandler(host, port);
    if (!ch->connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    std::mutex mutex;
    std::condition_variable conditionVariable;
    bool shouldTerminate = false;

    KeyboardReader keyboardReader(ch);
    SocketReader socketReader(ch,shouldTerminate);

    std::thread keyboardThread(&KeyboardReader::run , &keyboardReader);
    std::thread socketThread(&SocketReader::run,&socketReader);

    socketThread.join();
    keyboardThread.join();
}
