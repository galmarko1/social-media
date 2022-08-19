package bgu.spl.net.srv;

import bgu.spl.net.srv.User;

import java.io.Closeable;
import java.io.IOException;

public interface ConnectionHandler<T> extends Closeable{

    boolean send(T msg) throws IOException;

    User getCurrentUser();

    void setCurrentUser(User loginUser);
}
