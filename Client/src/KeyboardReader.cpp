//
// Created by sasonshi@wincs.cs.bgu.ac.il on 04/01/2022.
//

#include "../include/KeyboardReader.h"
using namespace std;
KeyboardReader::KeyboardReader(ConnectionHandler *connectionHandler): connectionHandler(*connectionHandler), shouldTerminate(false){}

void KeyboardReader::run() {
    string line;
    while (!connectionHandler.getTerminate()) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        int len=line.length();
        if (!connectionHandler.sendLine(line)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        std::cout << "Sent " << len+1 << " bytes to server" << std::endl;

        if(line.substr(0,7)=="LOGOUT"){
            while(!connectionHandler.getErrorLogout() && !connectionHandler.getTerminate()){
            }
            connectionHandler.setErrorLogout(false);
        }
    }
}
