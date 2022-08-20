//
// Created by sasonshi@wincs.cs.bgu.ac.il on 04/01/2022.
//

#ifndef Client_KEYBOARDREADER_H
#define Client_KEYBOARDREADER_H



#ifndef BOOST_ECHO_CLIENT_KEYBOARDREADER_H
#define BOOST_ECHO_CLIENT_KEYBOARDREADER_H

#include "../include/connectionHandler.h"
using namespace std;

class KeyboardReader {
private:
    ConnectionHandler& connectionHandler;
    bool shouldTerminate;

public:
    KeyboardReader(ConnectionHandler *ch);
    void run();
};
#endif
#endif