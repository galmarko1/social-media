
#include <iostream>
#include "../include/BidiMessagingProtocol.h"

using boost::asio::ip::tcp;

BidiMessagingProtocol::BidiMessagingProtocol() {}

void BidiMessagingProtocol::process(std::string &message) {
    int firstSpace = message.find_first_of(" ");
    int opCode_1 = std::stoi(message.substr(0, firstSpace));
    std::string msgRest = message.substr(firstSpace);

    switch (opCode_1) {
        case 9: {
            int secondSpace = msgRest.find_first_of(" ");
            std::string opCode_2 = msgRest.substr(0, secondSpace);
            msgRest = msgRest.substr(secondSpace);
            std::string msg_type = "";
            if (opCode_2 == "0") {
                msg_type = msg_type + "PM";
            } else {
                msg_type = msg_type + "Public";
            }

            int zero = msgRest.find_first_of(" ");
            std::string postUser = msgRest.substr(0, zero);
            msgRest = msgRest.substr(zero);
            std::string content = msgRest.substr(zero);
            std::cout << "NOTIFICATION " << msg_type << " " << postUser << " " << content << " " << std::endl;
        }

        case 10: {
            int secondSpace = msgRest.find_first_of(" ");
            std::string opCode_2 = msgRest.substr(0, secondSpace);
            std::string optional = msgRest.substr(secondSpace);
            std::cout << "ACK" << opCode_2 << optional << std::endl;
            if (!(std::equal(opCode_2.begin(), opCode_2.end(), "3"))) {
                std::string opCode_2 = msgRest.substr(0);
                std::cout << "ERROR" << opCode_2 << std::endl;
            }
        }
    }

}





