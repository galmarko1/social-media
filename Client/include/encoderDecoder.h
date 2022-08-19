//
// Created by galmarko@wincs.cs.bgu.ac.il on 03/01/2022.
//

#ifndef SPLCLIENT2_ENCODERDECODER_H
#define SPLCLIENT2_ENCODERDECODER_H
#include <string>
#include <iostream>
#include <boost/asio.hpp>
#include <vector>
class encoderDecoder {
public:
    std:: vector<char> encode(std::string str);
    std::string decode(std::vector<char>);
    short bytesToShort(char* bytesArr);
    void shortToBytes(short num, char* bytesArr);


};


#endif //SPLCLIENT2_ENCODERDECODER_H
