//
// Created by sasonshi@wincs.cs.bgu.ac.il on 03/01/2022.
//
#ifndef SPLCLIENT2_BIDIMESSAGINGPROTOCOL_H
#define SPLCLIENT2_BIDIMESSAGINGPROTOCOL_H

#include <boost/asio/ip/tcp.hpp>
//#include "connectionHandler.h"

using boost::asio::ip::tcp;

class BidiMessagingProtocol{
private:
//    connectionHandler ch;
//    boost::asio::io_service io_service_;   // Provides core I/O functionality
public:
    BidiMessagingProtocol();
    void process(std::string& message);
};
#endif //SPLCLIENT2_BIDIMESSAGINGPROTOCOL_H