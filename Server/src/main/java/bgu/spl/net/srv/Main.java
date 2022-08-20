package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.io.IOException;
import java.util.function.Supplier;

public class Main {
    public static <T> void main(String[] args) throws IOException {
        Supplier<BidiMessagingProtocol<T>> b = () -> (BidiMessagingProtocol<T>) new BidiMessagingProtocolImpl();
        Supplier<MessageEncoderDecoder<T>> m = () -> (MessageEncoderDecoder<T>) new MessageEncoderDecoderImpl();
        if (args.length < 2) {
            System.out.println("input invalid! (port, number Of Threads)");
            System.exit(1);
        }

        Server.reactor(Integer.parseInt(args[1]), Integer.parseInt("7777"), b, m).serve();

    }
}



