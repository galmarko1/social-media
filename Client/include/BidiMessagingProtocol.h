
#ifndef Client_BIDIMESSAGINGPROTOCOL_H
#define Client_BIDIMESSAGINGPROTOCOL_H

#include <boost/asio/ip/tcp.hpp>


using boost::asio::ip::tcp;

class BidiMessagingProtocol{
private:
//    connectionHandler ch;
//    boost::asio::io_service io_service_;   // Provides core I/O functionality
public:
    BidiMessagingProtocol();
    void process(std::string& message);
};
#endif