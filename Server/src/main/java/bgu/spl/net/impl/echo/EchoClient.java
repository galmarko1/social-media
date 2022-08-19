//package bgu.spl.net.impl.echo;
//
//import bgu.spl.net.api.MessageEncoderDecoder;
//import bgu.spl.net.api.MessagingProtocol;
//import bgu.spl.net.srv.BaseServer;
//import bgu.spl.net.srv.BidiMessagingProtocol;
//import bgu.spl.net.srv.BidiMessagingProtocolImpl;
//import bgu.spl.net.srv.MessageEncoderDecoderImpl;
//import jdk.internal.org.jline.utils.InputStreamReader;
//
////import java.io.BufferedReader;
////import java.io.BufferedWriter;
////import java.io.IOException;
////import java.io.InputStreamReader;
////import java.io.OutputStreamWriter;
////import java.net.Socket;
//import java.io.*;
//import java.net.Socket;
//import java.util.function.Supplier;
//
//import static bgu.spl.net.srv.Server.threadPerClient;
//
//public class EchoClient {
//    public static byte[] shortToBytes(short num)
//
//    {
//
//        byte[] bytesArr = new byte[2];
//
//        bytesArr[0] = (byte)((num >> 8) & 0xFF);
//
//        bytesArr[1] = (byte)(num & 0xFF);
//
//        return bytesArr;
//
//    }
//    public static void main(String[] args) throws IOException {
////        try(BaseServer<String> server = BaseServer.threadPerClient(7777,()->  new BidiMessagingProtocolImpl(), ()->new BidiMessagingProtocolImpl())){
////            server.serve();
////        }
////        catch(Exception e){
////            e.printStackTrace();
////        }
//        if (args.length == 0) {
//            args = new String[]{"localhost", "hello"};
//        }
//
////        if (args.length < 2) {
////            System.out.println("you must supply two arguments: host, message");
////            System.exit(1);
////        }
//
//        //BufferedReader and BufferedWriter automatically using UTF-8 encoding
//        try (Socket sock = new Socket(args[0], 7777);
//             BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {
//
//
//            System.out.println("sending message to server");
//            int o = 1;
//            short op =(short) o;
//            byte[] op1 = shortToBytes(op);
//            byte[] s = "Shir\0 123\0 05/03/1999\0;".getBytes();
//            byte[] ans = new byte[s.length + 2];
//            ans[0] = op1[0];
//            ans[1] = op1[1];
//            for(int i = 0; i < s.length; i ++){
//                ans[i+2] = s[i];
//            }
//
//
//
//
//
//
//            out.write(ans);
//            out.newLine();
//            out.flush();
//
//            System.out.println("awaiting response");
//            String line = in.readLine();
//            System.out.println("message from server: " + line);
//        }
//    }
//}
