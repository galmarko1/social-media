package bgu.spl.net.srv;

import bgu.spl.net.api.MessagingProtocol;
//import sun.util.calendar.BaseCalendar;

import javax.swing.*;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;
import bgu.spl.net.srv.ConnectionHandler;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<String> {

    private boolean shouldTerminate = false;
    private Connections connections = Connections.getInstance();
    private int connectionId;
    private LinkedList<String> words = new LinkedList<String>();
    private ConnectionHandler connectionHandler ;

    public BidiMessagingProtocolImpl(){
        int id =  connections.getID();
        start(id,Connections.getInstance());
        words.add("banana");
        words.add("this");
        words.add("project");
        words.add("is");
        words.add("the");
        words.add("best");
    }
    public int getConnectionId(){return this.connectionId;}
    public void setConnectionHandler(ConnectionHandler ch){this.connectionHandler = ch;}
    @Override
    public void start(int connectionId, Connections connections) {
        this.connections = Connections.getInstance();
        this.connectionId = connectionId;
    }

    @Override
    public void process(String message) throws IOException {
        String reply = "";
        int index = message.indexOf(' ');
        String action="";
        if(index==-1){
            action="LOGSTAT";
        }
        else{
            action = message.substring(0,index);
        }
        index ++;
        User newUser;
        switch (action){
            case "REGISTER":
                int ruIndex = message.indexOf('\0',index);
                String username = message.substring(index,ruIndex);
                ruIndex= ruIndex+2;
                int pIndex = message.indexOf('\0',ruIndex);
                String password = message.substring(ruIndex,pIndex);
                pIndex+=2;
                int bIndex = message.indexOf('\0',pIndex);
                String birthday = message.substring(pIndex,bIndex);
                String year=birthday.substring(6);
                int yearInt = Integer.parseInt(year);
                ZonedDateTime curr_time = ZonedDateTime.now();
                int curr_years=curr_time.getYear();
                int age = curr_years-yearInt;

                if(connections.getRegisteredUsers().containsKey(username)){
                    reply="11 1";
                }
                else{
                    newUser = new User(username,password,birthday,age);
                    connections.getRegisteredUsers().put(username,newUser);
                    newUser.setCh(this.connectionHandler);
                    reply = "10 1";
                }
                connections.getConnectionHandler(connectionId).send(reply+";");
                break;


            case "LOGIN":
                int loIndex = message.indexOf('\0',index);
                String LogInUsername = message.substring(index,loIndex);
                loIndex += 2;
                int passIndex = message.indexOf('\0',loIndex);
                String password2 = message.substring(loIndex,passIndex);
                passIndex += 2;
                int cpIndex = message.indexOf('\0',passIndex);
                String capcha=message.substring(passIndex,cpIndex);
                boolean bCapcha=capcha.equals("1");
                boolean isRegistered=connections.getRegisteredUsers().containsKey(LogInUsername);
                boolean isLoggedIn=!connections.getLoggedInUsers().containsKey(LogInUsername);
                if(bCapcha&&isRegistered&&isLoggedIn ) { //user is registered and not log in
                    if (connections.getRegisteredUser(LogInUsername).getPassword().equals(password2)) {
                        User loginUser = connections.getRegisteredUser(LogInUsername);
                        ConnectionHandler newCh = connections.getConnectionHandler(connectionId);
                        loginUser.logInUser(newCh);
                        connections.logInNewUser(loginUser);
                        connections.getConnectionHandler(connectionId).setCurrentUser(loginUser);
                        BlockingQueue<String> waitingMSG=loginUser.getWaitingQueue();
                        while(!waitingMSG.isEmpty()){
                            String newMSG=waitingMSG.remove();
                            newCh.send(newMSG+";");
                        }
                        reply = "10 2";
                    } else {
                        reply = "11 2";
                    }
                }
                else
                    reply = "11 2";
                connections.getConnectionHandler(connectionId).send(reply+";");
                break;


            case "LOGOUT":
                if(connections.getLoggedInUsers().values().isEmpty())
                    reply="11 3";
                else{
                    reply="10 3";
//                    shouldTerminate=true;
//                    try {
//                        connections.disconnect(connectionId);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    User logoutUser=connections.getConnectionHandler(connectionId).getCurrentUser();
                    logoutUser.logoutUser();
                    if(connections.getLoggedInUsers().containsKey(logoutUser.getName())){
                        connections.getLoggedInUsers().remove(logoutUser.getName());
                        reply="10 3";
                    }
                    else
                        reply="11 3";
                }

                connections.getConnectionHandler(connectionId).send(reply+";");
                if(reply=="10 3"){
                    try {
                        shouldTerminate=true;
                        connections.disconnect(connectionId);
                        System.out.println("disconnect in process");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;


            case "FOLLOW":

//                System.out.println("message is: "+message);
                int followIndex = message.indexOf('\0',index);
//                int followIndex=index+1;
                String follow = message.substring(index,followIndex);
                followIndex+=2;
                int nameIndex = message.indexOf('\0',followIndex+1);
                String name = message.substring(followIndex,nameIndex);
                if(connections.getConnectionHandler(connectionId).getCurrentUser() == null){
                    reply="11 4";
                }
                else {
                    User currUser = connections.getConnectionHandler(connectionId).getCurrentUser();
                    User followUser = connections.getRegisteredUser(name);
                    if (connections.getLoggedInUsers().containsKey(currUser.getName()) && followUser != null && connections.getRegisteredUsers().containsKey(currUser.getName())
                            && !currUser.getUsersIBlocked().contains(followUser.getName()) && !currUser.getUsersBlockedMe().contains(followUser.getName())) {
                        if (follow.equals("1") && currUser.getFollowing().contains(name) && followUser.getFollowers().contains(currUser.getName())) {
                            if (!followUser.getUsersBlockedMe().contains(currUser.getName())) {
                                currUser.getFollowing().remove(name);
                                followUser.getFollowers().remove(currUser.getName());
                                reply = "10 4 " + name;
                            } else {
                                reply = "11 4";
                            }
                        } else if (follow.equals("0") && !currUser.getFollowing().contains(name) && !followUser.getFollowers().contains(currUser.getName())) {
                            currUser.getFollowing().add(name);
                            followUser.getFollowers().add(currUser.getName());
                            reply = "10 4 " + name;
                        } else {
                            reply = "11 4";
                        }
                    } else
                        reply = "11 4";
                }
                connections.getConnectionHandler(connectionId).send(reply + ";");
                break;


            case "POST":
                boolean error=false;
                if(connections.getConnectionHandler(connectionId).getCurrentUser() == null){
                    error=true;
                }
                else {
                    LinkedList<String> nameList = new LinkedList<String>();
                    int nIndex1 = message.indexOf('@', index);
                    while (nIndex1 != -1) {
                        int indexSpace = message.indexOf('\0', nIndex1);
                        String names = message.substring(nIndex1, indexSpace);
                        names = names.substring(1);
                        nameList.add(names);
                        indexSpace = indexSpace + 2;
                        nIndex1 = message.indexOf('@', indexSpace);
                    }
                    User user;
                    String msg_post = message.substring(index);
                    User userCurent = connections.getConnectionHandler(connectionId).getCurrentUser();
                    if (!connections.getLoggedInUsers().containsKey(userCurent.getName())) {
                        error = true;
                    }

                    userCurent.getYourAllPosts().add(message);
                    userCurent.getYourPosts_noPM().add(message);

                    for (int i = 0; i < nameList.size() && !error; i++) {
                        String nameInList = nameList.get(i);
                        if (connections.getRegisteredUsers().containsKey(nameInList)) {
                            user = connections.getRegisteredUser(nameInList);
                            user.getRecievePosts().add(message);
                            String notification = "9 1 " + userCurent.getName() + "\0 " + msg_post + "\0";
                            if (connections.getLoggedInUsers().containsKey(user.getName())) {
                                user.getCh().send(notification + ";");
                            } else {
                                user.getWaitingQueue().add(notification);
                            }
                        } else {
                            error = true;
                        }
                    }

                    for (int i = 0; i < userCurent.getFollowers().size() && !error; i++) {
                        String followName = (String) userCurent.getFollowers().get(i);
                        if (connections.getRegisteredUsers().containsKey(followName)) {
                            user = connections.getRegisteredUser(followName);
                            user.getRecievePosts().add(message);
                            String notification = "9 1 " + userCurent.getName() + "\0 " + msg_post + "\0";
                            if (connections.getLoggedInUsers().containsKey(user.getName())) {
                                user.getCh().send(notification + ";");

                            } else {
                                user.getWaitingQueue().add(notification);
                            }
                        } else {
                            error = true;
                        }
                    }
                }
                    if (error == true) {
                        reply = "11 5";
                    } else {
                        reply = "10 5";
                    }

                connections.getConnectionHandler(connectionId).send(reply+";");
                break;


            case "PM":
                if(connections.getConnectionHandler(connectionId).getCurrentUser() == null){
                    reply = "11 6";
                }
                else {
                    int naIndex = message.indexOf('\0', index);
                    String user_name = message.substring(index, naIndex);
                    naIndex += 2;
//                int contentIndex = message.indexOf('\0',naIndex);
                    String contentInfo = message.substring(naIndex, message.length() - 1);
//                contentIndex +=2;
//                int timetIndex = message.indexOf('\0',contentIndex);
//                String timeInfo = message.substring(contentIndex,timetIndex);

                    User curr_user = connections.getConnectionHandler(connectionId).getCurrentUser();

                    if (connections.getLoggedInUsers().containsKey(curr_user.getName()) && connections.getRegisteredUsers().containsKey(user_name) && curr_user.getFollowing().contains(user_name)) {
                        User sendUser = connections.getRegisteredUser(user_name);
                        if (!sendUser.getUsersBlockedMe().contains(curr_user)) {
                            String filteredString = contentInfo;
                            for (int i = 0; i < words.size(); i++) {
                                String curr_word = words.get(i);
                                if (filteredString.contains(curr_word)) {
                                    int startWordIndex = filteredString.indexOf(curr_word);
                                    String newString = filteredString.substring(0, startWordIndex) + "<filtered>" + filteredString.substring(startWordIndex + curr_word.length());
                                    filteredString = newString;
                                }
                            }
                            sendUser.getRecievePosts().add(filteredString);
                            curr_user.getYourAllPosts().add(filteredString);

                            String notificationMsg = "9 0 " + curr_user.getName() + "\0" + filteredString + "\0";
                            if (connections.getLoggedInUsers().containsKey(sendUser.getName())) {
                                sendUser.getCh().send(notificationMsg + ";");
                            } else {
                                sendUser.getWaitingQueue().add(notificationMsg);
                            }
                            reply = "10 6";
                        } else {
                            reply = "11 6";
                        }
                    } else
                        reply = "11 6";
                }
                connections.getConnectionHandler(connectionId).send(reply+";");
                break;


            case "LOGSTAT":
                User curUser = null;
                boolean sendError=false;
                if(connections.getConnectionHandler(connectionId).getCurrentUser() == null){
                    sendError = true;
                }
                else {
                    curUser = connections.getConnectionHandler(connectionId).getCurrentUser();

                    if (connections.getRegisteredUsers().containsKey(curUser.getName())) {
                        Collection<User> loggedInCol = connections.getLoggedInUsers().values();
                        Iterator<User> iter = loggedInCol.iterator();
                        while (iter.hasNext() && !sendError) {
                            User ACKtoUser = iter.next();
                            if (connections.getRegisteredUsers().containsKey(ACKtoUser.getName())) { //todo livdok- why user is logged in and not registered?
                                if (!ACKtoUser.getUsersBlockedMe().contains(curUser.getName()) && !ACKtoUser.getUsersIBlocked().contains(curUser.getName())) {
                                    int userAge = ACKtoUser.getAge();
                                    int numPosts = ACKtoUser.getYourAllPosts().size();
                                    int numFollowers = ACKtoUser.getFollowers().size();
                                    int numFollowing = ACKtoUser.getFollowing().size();
                                    String sendString = "10 7 " + userAge + numPosts + numFollowers + numFollowing;
                                    if (connections.getLoggedInUsers().containsKey(ACKtoUser.getName())) {
                                        curUser.getCh().send(sendString + ";");
                                    } else {
                                        curUser.getWaitingQueue().add(sendString);
                                    }
                                } else
                                    sendError = true;
                            } else {
                                sendError = true;
                            }
                        }
                    } else {
                        sendError = true;
                    }
                }
                if(sendError)
                    reply="11 7";
                else
                    reply="10 7";
                connections.getConnectionHandler(connectionId).send(reply+";");

                break;


            case "STAT":
                boolean send_error=false;
                if(connections.getConnectionHandler(connectionId).getCurrentUser() == null){
                    send_error = true;
                }
                else {
                    LinkedList<String> users_names = new LinkedList<>();
                    int index_name = message.indexOf('|', index);
                    String User_Name = "";
                    while (index_name != -1) {
                        User_Name = message.substring(index, index_name);
                        users_names.add(User_Name);
                        index_name++;
                        index = index_name;
                        index_name = message.indexOf('|', index_name);
                    }

                    User CurrUser = connections.getConnectionHandler(connectionId).getCurrentUser();
                    if (connections.getLoggedInUsers().containsKey(CurrUser.getName())) {
                        for (int i = 0; i < users_names.size(); i++) {
                            if (connections.getRegisteredUsers().containsKey(users_names.get(i))) {
                                User send_user = connections.getRegisteredUser(users_names.get(i));
                                int userAge = send_user.getAge();
                                int numPosts = send_user.getYourAllPosts().size();
                                int numFollowers = send_user.getFollowers().size();
                                int numFollowing = send_user.getFollowing().size();

                                System.out.println("userAge=" + userAge);
                                System.out.println("numFollowers=" + numFollowers);
                                System.out.println("numFollowing=" + numFollowing);
                                String sendString = "";
                                sendString += "10 8 " + userAge + numPosts + numFollowers + numFollowing;

                                if (send_user.getUsersBlockedMe().contains(CurrUser.getName()) || send_user.getUsersIBlocked().contains(CurrUser.getName())) {
                                    send_error = true;
                                } else {
                                    if (connections.getLoggedInUsers().containsKey(send_user.getName())) {
                                        CurrUser.getCh().send(sendString + ";");
                                    } else {
                                        CurrUser.getWaitingQueue().add(sendString);
                                    }
                                }

                            } else {
                                send_error = true;
                            }
                        }
                    } else {
                        send_error = true;
                    }
                }
                if(send_error){
                    reply="11 8";
                    connections.getConnectionHandler(connectionId).send(reply+";");
                }
                else{
                    reply="10 8";
                    connections.getConnectionHandler(connectionId).send(reply+";");
                }
                break;

            case "BLOCK":
                int getNameIndex = message.indexOf('\0',index);
                String BlokedName = message.substring(index,getNameIndex);
                User curr_USer=connections.getConnectionHandler(connectionId).getCurrentUser();

                if(connections.getLoggedInUsers().containsKey(curr_USer.getName())){
                    if(connections.getRegisteredUsers().containsKey(BlokedName)){
                        curr_USer.getUsersIBlocked().add(BlokedName);
                        User block=connections.getRegisteredUser(BlokedName);
                        block.getUsersBlockedMe().add(curr_USer.getName());
                        if(curr_USer.getFollowing().contains(BlokedName)){
                            curr_USer.getFollowing().remove(BlokedName);
                            block.getFollowers().remove(curr_USer.getName());
                        }
                        if(curr_USer.getFollowers().contains(BlokedName)){
                            curr_USer.getFollowers().remove(BlokedName);
                            block.getFollowing().remove(curr_USer.getName());
                        }
//                        if(block.getUsersIBlocked().contains(curr_USer.getName())){
//                            block.getUsersIBlocked().remove(curr_USer.getName());
//                        }
//                        if(block.getUsersBlockedMe().contains(curr_USer.getName())){
//                            block.getUsersBlockedMe().remove(curr_USer.getName());
//                        }
                        reply="10 12";
                    }
                    else{
                        reply="11 12";
                    }
                }
                else{
                    reply="11 12";
                }
                connections.getConnectionHandler(connectionId).send(reply+";");
                break;
        }


//        try {
//            this.connections.getConnectionHandler(connectionId).send(reply);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public boolean shouldTerminate() { /////// todo livdok
        return shouldTerminate;
    }

    public int getID(){return this.connectionId;}
    public void setWords(LinkedList<String> newWords){this.words=newWords;}

    public Connections getConnections(){return connections;}
}