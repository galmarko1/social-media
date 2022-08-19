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
//    unordered_map<string, short> opCodesMap;
//    mutex& mtx;
//    condition_variable& conditionVariable;
    bool shouldTerminate;
    //void shortToBytes(short num, char* bytesArr);
    //int copyBytesArray(char *toArray , const char *fromArray, int to_begin, size_t from_length);

public:
    KeyboardReader(ConnectionHandler *ch);
    //~KeyboardReader()= default;
    //KeyboardReader(const KeyboardReader&)= default;
    //KeyboardReader &operator=(const KeyboardReader&)= default;
    void run();
};
#endif //Client_KEYBOARDREADER_H
#endif //BOOST_ECHO_CLIENT_KEYBOARDREADER_H