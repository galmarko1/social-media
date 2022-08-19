package bgu.spl.net.srv;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import bgu.spl.net.srv.ConnectionHandler;

public class User<T> {
    private String userName;
    private String password;
    private String birthday;
    private int age;
    private BlockingQueue<T> waitingQueue=new LinkedBlockingQueue<>();
    private ConnectionHandler ch=null;

    private LinkedList<String> following=new LinkedList<String>();
    private LinkedList<String> followers=new LinkedList<String>();
    private LinkedList<String> yourAllPosts=new LinkedList<String>();
    private LinkedList<String> yourPosts_noPM=new LinkedList<String>();
    private LinkedList<String> recievePosts=new LinkedList<String>();
    private LinkedList<String> usersBlockedMe=new LinkedList<>();
    private LinkedList<String> usersIBlocked= new LinkedList<>();


    private boolean loggedIn=false;

    public User(String userName, String password,String birthday, int age){
        this.userName=userName;
        this.password=password;
        this.birthday=birthday;
        this.age=age;
    }

    public String getName(){
        return this.userName;
    }

    public String getPassword(){
        return this.password;
    }
    public String getBirthday(){
        return this.birthday;
    }
    public BlockingQueue<T> getWaitingQueue(){
        return this.waitingQueue;
    }
    public ConnectionHandler getCh(){
        return this.ch;
    }

    public void setCh(ConnectionHandler ch){
        this.ch=ch;
    }

    public LinkedList<String> getFollowing(){return this.following;}
    public LinkedList<String> getFollowers(){return this.followers;}
    public boolean getLoggedIn(){return loggedIn;}
    public void logInUser(ConnectionHandler ch){
        loggedIn=true;
        this.ch=ch;
    }
    public void logoutUser(){loggedIn=false;}

    public LinkedList<String> getYourAllPosts(){return this.yourAllPosts;}
    public LinkedList<String> getRecievePosts(){return this.recievePosts;}
    public LinkedList<String> getYourPosts_noPM(){return this.yourPosts_noPM;}
    public int getAge(){return this.age;}
    public LinkedList<String> getUsersIBlocked(){return this.usersIBlocked;}
    public LinkedList<String> getUsersBlockedMe(){return this.usersBlockedMe;}


}
