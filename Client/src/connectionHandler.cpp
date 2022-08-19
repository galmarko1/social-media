
#include <boost/asio/ip/tcp.hpp>
#include "../include/connectionHandler.h"

//using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

class BidiMessagingProtocol;
ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(), socket_(io_service_){}
    
ConnectionHandler::~ConnectionHandler() {
    close();
}

bool ConnectionHandler::connect() {
    std::cout << "Starting connect to " 
        << host_ << ":" << port_ << std::endl;
    try {
		tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
		boost::system::error_code error;
		socket_.connect(endpoint, error);
		if (error)
			throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
			tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);
//            std::cout<<"getBytes MSG"<<std::endl;
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(std::vector<char> vec, int bytesToWrite) {
    char frameArray[vec.size()];
    int newFrameSize = vec.size();

    for( int i =0; i < newFrameSize; i ++){
        frameArray[i] = vec.front();
        vec.erase(vec.begin());
    }
    int tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
			tmp += socket_.write_some(boost::asio::buffer(frameArray + tmp, bytesToWrite - tmp), error);
        }
//        std::cout<<"sendBytes MSG"<<std::endl;
        if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed2 (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getLine(std::string& line) {
    return getFrameAscii(line, ';');
}

bool ConnectionHandler::sendLine(std::string& line) {
    return sendFrameAscii(line, ';');
}
 
bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;
    std::vector<char> vector;
    // Stop when we encounter the null character. 
    // Notice that the null character is not appended to the frame string.
    try {
		do{

			if(!getBytes(&ch, 1))
                return false;
            vector.push_back(ch);
//            std::cout<<"getFrameAscii MSG"<<std::endl;
        }while (delimiter != ch);
        std::string s = this->getEncDec().decode(vector);
        frame.append(s);
    } catch (std::exception& e) {
        std::cerr << "recv failed3 (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
    std::vector<char> newFrame=this->getEncDec().encode(frame);

//    std::cout<<"sendFrameAscii MSG"<<std::endl;
	bool result=sendBytes(newFrame,newFrame.size());
	if(!result) return false;
    std::vector<char> v;
    v.push_back(delimiter);
//    std::cout<<"sendFrameAscii delimiter"<<std::endl;
    return sendBytes(v,1);
}
 
// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
    isTerminate_= true;
}
encoderDecoder ConnectionHandler::getEncDec(){return this->encDec;}
BidiMessagingProtocol ConnectionHandler::getProtocol() {return this->protocol;}
bool ConnectionHandler::getTerminate() {return this->isTerminate_;}
void ConnectionHandler::setErrorLogout(bool isError) {this->isErrorLogout=isError;}
bool ConnectionHandler::getErrorLogout() {return this->isErrorLogout;}