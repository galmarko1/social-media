package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static java.lang.Byte.decode;
import static java.lang.Short.valueOf;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<String> {
    private byte[] bytes = new byte[1 << 10];
    private int len = 0;

    public void resetBytesArray(){bytes = new byte[1 << 10];}
    @Override
    public String decodeNextByte(byte nextByte) {
        if (nextByte == ';') {
            String opString = "";
            byte[] opByte = new byte[2];
            opByte[0] = bytes[0];
            opByte[1] = bytes[1];
            Short op = bytesToShort(opByte);
            switch (op.intValue()) {
                case 1:
                    opString = opString + "REGISTER ";
                    break;
                case 2:
                    opString = opString + "LOGIN ";
                    break;
                case 3:
                    opString = opString + "LOGOUT ";
                    break;
                case 4:
                    opString = opString + "FOLLOW ";
                    break;
                case 5:
                    opString = opString + "POST ";
                    break;
                case 6:
                    opString = opString + "PM ";
                    break;
                case 7:
                    opString = opString + "LOGSTAT ";
                    break;
                case 8:
                    opString = opString + "STAT ";
                    break;
                case 9:
                    opString = opString + "NOTIFICATION ";
                    break;
                case 12:
                    opString = opString + "BLOCK ";
                    break;

            }
            String answer = popString();
            resetBytesArray();
            return opString + answer;
        } else {
            pushByte(nextByte);
            return null;
        }
    }




//        String opString = "";
//        if (nextByte == ';') {
//            return opString + popString();
//        }
//        if(len == 2){
//            Short op = bytesToShort(bytes);
//            switch (op.intValue()){
//                case 1:
//                    opString = opString + "REGISTER ";
//                    break;
//                case 2:
//                    opString = opString + "LOGIN ";
//                    break;
//                case 3:
//                    opString = opString + "LOGOUT ";
//                    break;
//                case 4:
//                    opString = opString + "FOLLOW ";
//                    break;
//                case 5:
//                    opString = opString + "POST ";
//                    break;
//                case 6:
//                    opString = opString + "PM ";
//                    break;
//                case 7:
//                    opString = opString + "LOGSTAT ";
//                    break;
//                case 8:
//                    opString = opString + "STAT ";
//                    break;
//                case 9:
//                    opString = opString + "NOTIFICATION ";
//                    break;
//                case 12:
//                    opString = opString + "BLOCK ";
//                    break;
//
//            }
//        }
//        pushByte(nextByte);
//        return null;
//    }

    @Override
    public byte[] encode(String message) {
//        message = message+ ";";
        int index = message.indexOf(' ');
        String op1 = message.substring(0,index);
        short shortop1 = valueOf(op1);
        switch(op1) {
            case "9":
                byte[] o = shortToBytes(shortop1); //op code
                int nextS1 = message.substring(index + 1).indexOf(' ');
                nextS1=nextS1+index+1;
                String type = message.substring(index+1,nextS1);
                byte[] t = type.getBytes(); // type
                String content = message.substring(nextS1 + 1);
                byte[] contentBytes  = content.getBytes();
                byte[] ans = new byte[contentBytes.length+3];

                ans[0] = o[0];
                ans[1] = o[1];
                ans[2] = t[0];
                for(int i = 0; i < contentBytes.length; i ++){
                    ans[i+3] = contentBytes[i];
                }

                return ans;

            case "10":
                byte[] o2 = shortToBytes(shortop1);
                int nextS = message.substring(index + 1).indexOf(' ');
//                System.out.println("msggggggggg"+message.substring(index + 1));
                byte[] op2;
                byte[] ans1;
                if(nextS == -1){
                    op2 = shortToBytes(valueOf(message.substring(index+1,message.length()-1)));
                    byte[] b=";".getBytes();
                    ans1 = new byte[5];
                    ans1[0] = o2[0];
                    ans1[1] = o2[1];
                    ans1[2] = op2[0];
                    ans1[3] = op2[1];
                    ans1[4]=b[0];
                }
                else {
                    nextS=nextS+index+1;
                    op2 = shortToBytes(valueOf(message.substring(index + 1, nextS)));
                    byte[] data2 = message.substring(nextS + 1, message.length()).getBytes();
                    ans1 = new byte[data2.length+4];
                    ans1[0] = o2[0];
                    ans1[1] = o2[1];
                    ans1[2] = op2[0];
                    ans1[3] = op2[1];
                    for(int i = 0; i < data2.length; i ++){
                        ans1[i+4] = data2[i];
                    }
                }
                return ans1;

            case "11":
                byte[] o3 = shortToBytes(shortop1);
                byte[] op3 = shortToBytes(valueOf(message.substring(index+1,message.length()-1)));
                byte[] b=";".getBytes();
                byte[] ans2 = new byte[5];
                ans2[0] = o3[0];
                ans2[1] = o3[1];
                ans2[2] = op3[0];
                ans2[3] = op3[1];
                ans2[4]=b[0];
                return ans2;
        }
        return null;
    }

    public short bytesToShort(byte[] byteArr) {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
    public byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private String popString() {
        String result = new String(bytes, 2, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }


}
