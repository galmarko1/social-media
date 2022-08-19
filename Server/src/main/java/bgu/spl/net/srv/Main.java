package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.io.IOException;
import java.util.function.Supplier;

public class Main {
    public static <T> void main(String[] args) throws IOException {
        Supplier<BidiMessagingProtocol<T>> b = () -> (BidiMessagingProtocol<T>) new BidiMessagingProtocolImpl();
        Supplier<MessageEncoderDecoder<T>> m = () -> (MessageEncoderDecoder<T>) new MessageEncoderDecoderImpl();

//        try (BaseServer<T> server = BaseServer.threadPerClient(8888, b, m)) {
//            server.serve();
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            Server.reactor(Integer.parseInt(args[1]), Integer.parseInt(args[0]), b, m).serve();
        if (args.length < 2) {
            System.out.println("input invalid! (port, number Of Threads)");
            System.exit(1);
        }

        Server.reactor(Integer.parseInt(args[1]), Integer.parseInt(args[0]), b, m).serve();
//            Server.reactor(Integer.parseInt("2"), Integer.parseInt("8888"), b, m).serve();
        }
    }









//        BidiMessagingProtocolImpl bidiProtocol=new BidiMessagingProtocolImpl();
//        MessageEncoderDecoderImpl encDec=new MessageEncoderDecoderImpl();
//        Connections connection=new Connections();
//        Socket sock=new Socket();
//        BlockingConnectionHandler ch=new BlockingConnectionHandler(sock,encDec,bidiProtocol);
//        connection.getIdCh().put(1,ch);
//        bidiProtocol.start(1,connection);

//                BidiMessagingProtocolImpl bidiProtocol=new BidiMessagingProtocolImpl();
//                MessageEncoderDecoderImpl encDec=new MessageEncoderDecoderImpl();
//                Connections connection=new Connections();
//                Socket sock=new Socket();
//                BlockingConnectionHandler ch=new BlockingConnectionHandler(sock,encDec,bidiProtocol);
//                connection.getIdCh().put(1,ch);
//                bidiProtocol.start(1,connection);
//
//                bidiProtocol.process("REGISTER Shir\0 123\0 05/03/1999\0;");
//                bidiProtocol.process("LOGIN Shir\0 123\0 1\0;");
//                bidiProtocol.process("REGISTER Gal\0 456\0 11/01/2000\0;");
//                bidiProtocol.process("REGISTER Dan\0 789\0 24/11/1997\0;");
//                bidiProtocol.process("LOGIN Gal\0 456\0 1\0;");
//                bidiProtocol.process("FOLLOW 0Shir;");
//                bidiProtocol.process("FOLLOW 0Dan;");
////        bidiProtocol.process("FOLLOW 1GalStuma;");
//                LinkedList<String> words=new LinkedList<>();
//                words.add("Zibi");
//                words.add("Emo");
//                bidiProtocol.setWords(words);
//                bidiProtocol.process("PM Gal\0 Zibi wants to eat banana with Emo\0 01/01/2022\0");
//                bidiProtocol.process("POST @Shir and @Gal and @Dan eat choclate");
//                bidiProtocol.process("FOLLOW 1Dan;");
//                bidiProtocol.process("LOGSTAT");
//                bidiProtocol.process("STAT Dan|Shir|");
//            }
//        }
//






