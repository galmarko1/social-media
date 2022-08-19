package bgu.spl.net.srv;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import bgu.spl.net.srv.ConnectionHandler;

public class Connections<T> {
    private static Connections instance = null;
    private ConcurrentHashMap<Integer,ConnectionHandler> id_ch=new ConcurrentHashMap<Integer, ConnectionHandler>();
    private ConcurrentHashMap<String, User> RegisteredUsers=new ConcurrentHashMap<String,User>();
    private ConcurrentHashMap<String,User> loggedInUsers=new ConcurrentHashMap<String,User>();
    private AtomicInteger currID = new AtomicInteger(0);

    public static synchronized Connections getInstance(){
        if(instance == null){
            synchronized (Connections.class){
                if(instance == null)
                    instance = new Connections();
            }
        }
        return instance;
    }
    public ConnectionHandler getConnectionHandler(int conntId){
        return id_ch.get(conntId);
    }

    public User getRegisteredUser(String name){
        return RegisteredUsers.get(name);
    }
    public int getID(){
        this.currID.incrementAndGet();
        return this.currID.get()-1;

    }
    public ConcurrentHashMap<String,User> getRegisteredUsers(){return this.RegisteredUsers;}
    public User getLoggedInUser(String userName){return this.loggedInUsers.get(userName);}
    public ConcurrentHashMap<String,User> getLoggedInUsers(){return this.loggedInUsers;}
    public ConcurrentHashMap<Integer,ConnectionHandler> getIdCh(){return this.id_ch;}

    public void logInNewUser(User newUser){
        this.loggedInUsers.put(newUser.getName(), newUser);
    }


    public boolean send(int connId, String msg) throws IOException {
        ConnectionHandler ch= id_ch.get(connId);
        return ch.send(msg);
    }

//    public void broadcast(T msg) throws IOException {
//        Collection usersValue= RegisteredUsers.values();
//        Iterator<User> iter=usersValue.iterator();
//        while(iter.hasNext()){
//            User user=iter.next();
//            if(user.getCh()==null){
//                user.getWaitingQueue().add(msg);
//            }
//            else{
//                user.getCh().send(msg);
//            }
//        }
//    }

    public void disconnect(int connId) throws IOException {
        ConnectionHandler ch=id_ch.get(connId);
//        ch.close();
        id_ch.remove(connId);
    }




}
