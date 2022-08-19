package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import bgu.spl.net.srv.ConnectionHandler;


public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final BidiMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private User currentUser=null; //todo livdok

    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, BidiMessagingProtocol<T> protocol) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        if(protocol.getClass()==BidiMessagingProtocolImpl.class){
            ((BidiMessagingProtocolImpl) protocol).setConnectionHandler(this);
            Connections.getInstance().getIdCh().put(((BidiMessagingProtocolImpl) protocol).getConnectionId(),this);
        }


    }

    @Override
    public void run() {
        try (Socket sock = this.sock) { //just for automatic closing
            int read;

            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());

            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                T nextMessage = encdec.decodeNextByte((byte) read);
//                System.out.println("message received");
                if (nextMessage != null) {
                    protocol.process(nextMessage);
//                    in.reset();
//                    T response = protocol.process(nextMessage);
//                    if (response != null) {
//                        out.write(encdec.encode(response));
//                        out.flush();
//                    }
                }
            }
            this.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

    public boolean send(T message) throws IOException {
        out.write(encdec.encode(message));
        out.flush();
        return true;
    }



    public User getCurrentUser(){return this.currentUser;}
    public void setCurrentUser(User newUser){this.currentUser=newUser;}
}
