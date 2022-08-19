package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.io.IOException;
import java.util.function.Supplier;

public class TPCMain {
    public static <T> void main(String[] args) throws IOException {
        Supplier<BidiMessagingProtocol<T>> b = () -> (BidiMessagingProtocol<T>) new BidiMessagingProtocolImpl();
        Supplier<MessageEncoderDecoder<T>> m = () -> (MessageEncoderDecoder<T>) new MessageEncoderDecoderImpl();
        int p = Integer.parseInt(args[0]);
        try (BaseServer<T> server = BaseServer.threadPerClient(p, b, m)) {
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
