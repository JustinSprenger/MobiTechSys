package src.com.hoho.android.usbserial.examples;

import com.hoho.android.usbserial.util.HexDump;

public class zzzzztest {
    public static void main(String[] args) {
        final String FIRST = "zzzzz";
        String key = "zzzzzHallo wie gehts dir";
        byte[] message = key.getBytes();
        byte[] message2;
        byte[] pack = new byte[8];
        byte[] newMessage;
        String result = "";

        for (int i = 0;i<=8;i++) {
            if(message.length>8){
                System.arraycopy(message, 0, pack, 0, 8);
                newMessage = new byte[message.length - 8];
                System.arraycopy(message, 8, newMessage, 0, newMessage.length);
                message2 = new byte[FIRST.getBytes().length + newMessage.length];
                System.arraycopy(FIRST.getBytes(), 0, message2, 0, FIRST.getBytes().length);
                System.arraycopy(newMessage, 0, message2, FIRST.getBytes().length, newMessage.length);
                System.out.println(HexDump.dumpHexString(pack));
                message = message2;
            }else{
                System.out.println(HexDump.dumpHexString(message));
                break;
            }
        }
    }
}
