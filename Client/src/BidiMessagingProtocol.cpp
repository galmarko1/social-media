
#include <iostream>
#include "../include/BidiMessagingProtocol.h"

using boost::asio::ip::tcp;

BidiMessagingProtocol::BidiMessagingProtocol() {}

void BidiMessagingProtocol::process(std::string &message) {
    int indexOfSpace = message.find_first_of(" ");
    std::string opCode_1 = message.substr(0, indexOfSpace);
    std::string restMSG = message.substr(indexOfSpace);

    if (opCode_1=="9") { //Notifications
        int indexOfSpace2 = restMSG.find_first_of(" ");
        std::string opCode_2 = restMSG.substr(0, indexOfSpace2);
        restMSG = restMSG.substr(indexOfSpace2);
        std::string kind_MSG = "";
        if (opCode_2 == "0") {
            kind_MSG = kind_MSG + "PM";
        } else {
            kind_MSG = kind_MSG + "Public";
        }

        int index_Zero1 = restMSG.find_first_of(" ");
        std::string postUser = restMSG.substr(0, index_Zero1);
        restMSG = restMSG.substr(index_Zero1);
        std::string content = restMSG.substr(index_Zero1);
        std::cout << "NOTIFICATION " << kind_MSG <<" "<< postUser <<" "<< content <<" "<< std::endl;

    } else if (opCode_1 == "10") { //ACK
        int indexOfSpace2 = restMSG.find_first_of(" ");
        std::string opCode_2 = restMSG.substr(0, indexOfSpace2);
        std::string optional = restMSG.substr(indexOfSpace2);
        std::cout << "ACK" << opCode_2 << optional << std::endl;
        if (std::equal(opCode_2.begin(), opCode_2.end(), "3")) { //todo terminate

        }
    } else if (opCode_1=="11") { //Error
        std::string opCode_2 = restMSG.substr(0);
        std::cout << "ERROR" << opCode_2 << std::endl;

    }
}





