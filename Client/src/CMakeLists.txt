cmake_minimum_required(VERSION 3.20)
project(splClient2)

set(CMAKE_CXX_STANDARD 14)

add_executable(splClient2 include/connectionHandler.h include/BidiMessagingProtocol.h include/KeyboardReader.h include/SocketReader.h include/encoderDecoder.h src/connectionHandler.cpp src/echoClient.cpp src/BidiMessagingProtocol.cpp src/KeyboardReader.cpp src/SocketReader.cpp src/encoderDecoder.cpp)

set(BOOST_ROOT "C:Boost")
set(Boost_USE_STATIC_LIBS OFF)
set(Boost_USE_MULTITHREADED ON)
set(Boost_USE_STATIC_RUNTIME off)
find_package(Boost REQUIRED COMPONENTS system)

target_include_directories(splClient2 PUBLIC ${Boost_INCLUDE_DIRD})
target_link_libraries(splClient2 ${Boost_LIBRARIES})

SET(LDFLAGS "-lboost_system -lboost_thread -pthread")
SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${LDFLAGS}")