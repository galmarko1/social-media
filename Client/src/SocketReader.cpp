//
// Created by sasonshi@wincs.cs.bgu.ac.il on 04/01/2022.
//

#include "../include/SocketReader.h"
using namespace std;
SocketReader::SocketReader(ConnectionHandler *connectionHandler, bool shouldTerminate) : connectionHandler(connectionHandler), shouldTerminate(shouldTerminate) {}

void SocketReader::run()  {
    while(!shouldTerminate){
        std::string answer;
        if (!connectionHandler->getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        this->connectionHandler->getProtocol().process(answer);

        int len=answer.length();
        std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
        if (answer == "10 3 ") {
            std::cout << "Exiting...\n" << std::endl;
            this->connectionHandler->close();
            shouldTerminate=true;
            break;
        }
        if(answer=="11 3"){
            this->connectionHandler->setErrorLogout(true);
        }
    }

}