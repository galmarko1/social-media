//
// Created by sasonshi@wincs.cs.bgu.ac.il on 04/01/2022.
//

#ifndef Client_SOCKETREADER_H
#define Client_SOCKETREADER_H

#endif //Client_SOCKETREADER_H

#include <mutex>
#include <condition_variable>
#include "../include/connectionHandler.h"

class SocketReader {
private:
    ConnectionHandler *connectionHandler;
//    std::mutex& mtx;
//    std::condition_variable& conditionVariable;
    bool shouldTerminate;
public:
    SocketReader(ConnectionHandler *ch, bool shouldTerminate);
    ~SocketReader()= default;
    SocketReader(const SocketReader&)= default;
    SocketReader &operator=(const SocketReader&)= default;
    void run();
//    short bytesToShort(char *bytesArr);
};
