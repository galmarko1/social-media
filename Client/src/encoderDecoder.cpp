//
// Created by galmarko@wincs.cs.bgu.ac.il on 03/01/2022.
//

#include "../include/encoderDecoder.h"

std::vector<char> encoderDecoder::encode(std::string str){
    int space = str.find(' ');
    short op;
    std::vector<char> ans;
    std::string action = str.substr(0,space);
    char* stb = new char[2];
    if(action == "REGISTER")
        op = 1;
    else if(action == "LOGIN")
        op = 2;
    else if(action == "LOGOUT")
        op = 3;
    else if(action == "FOLLOW")
        op = 4;
    else if(action == "POST")
        op = 5;
    else if(action == "PM")
        op = 6;
    else if(action == "LOGSTAT")
        op = 7;
    else if(action == "STAT")
        op = 8;
    else if(action == "BLOCK")
        op = 12;

    shortToBytes(op,stb);
    ans.push_back(stb[0]);
    ans.push_back(stb[1]); // first 2 bytes are op

    int i = space + 1;
    int len = str.length();
    while(i < len){
        if(str.at(i) == ' '){
            ans.push_back('\0');
            ans.push_back(' ');
        }
        else
            ans.push_back(str.at(i));
        i ++;
    }
//    if(op==2){
//        ans.push_back('\0');
//        ans.push_back(' ');
//        ans.push_back('1');
//    }
    ans.push_back('\0');
//    ans.push_back(';');///////////////////////////////////////////////////////////////////////
    return ans;
}
std::string encoderDecoder::decode(std::vector<char> vec) {
    char bytes [2];
    bytes[0] = vec.front();
    vec.erase(vec.begin());
    bytes[1] = vec.front();
    vec.erase(vec.begin());
    short op = bytesToShort(bytes);
    std::string message = std::to_string(op); // first op
    if(op == 9) {
        char bytes2[1];
        bytes2[0] = vec.front();
        vec.erase(vec.begin());
        message.append(" ");
        std::string t;
        if((char)bytes2[0] =='1')
            t = "1";
        else
            t = "0";
        message.append("" + t);
        message.append(" ");
        std::vector<char> temp1;
        while (vec.size() != 2) {  // not to take the last \0
            if (vec.front() != '\0') {
                temp1.push_back(vec.front());
                vec.erase((vec.begin()));
            } else {
                temp1.push_back(' '); //replace \0 with " "
                vec.erase((vec.begin()));
            }
        }
        std::string data1(temp1.begin(), temp1.end());
        message.append(data1); // data

    }
    else if( op == 10) {

        char bytes2[2];
        bytes2[0] = vec.front();
        vec.erase(vec.begin());
        bytes2[1] = vec.front();
        vec.erase(vec.begin());
        short op2 = bytesToShort(bytes2);
        message.append(" ");
        message.append(std::to_string(op2)); // second op
        std::vector<char> temp;
        while (vec.size() != 1) { // not to take the last \0
            if (vec.front() != '\0') {
                temp.push_back(vec.front());
                vec.erase((vec.begin()));
            } else {
                temp.push_back(' '); //replace \0 with " "
                vec.erase((vec.begin()));
            }
        }
        message.append(" ");
        std::string data(temp.begin(), temp.end());
        message.append(data); // data
    }

      else if(op == 11){
            char bytes2 [2];
            bytes2[0] = vec.front();
            vec.erase(vec.begin());
            bytes2[1] = vec.front();
            vec.erase(vec.begin());
            short op2 = bytesToShort(bytes2);
            message.append(" ");
            message.append(std::to_string(op2)); // second op

    }
    return message;
}
short encoderDecoder::bytesToShort(char* bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}
void encoderDecoder::shortToBytes(short num, char* bytesArr)
{
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}