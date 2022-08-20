
#ifndef Client_ENCODERDECODER_H
#define Client_ENCODERDECODER_H
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
#endif
