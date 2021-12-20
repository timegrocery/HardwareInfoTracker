package Ultils;

import java.io.PrintWriter;
import java.util.ArrayList;

public class NetUtils {
    public static void sendMessage(Packet packet, PrintWriter pw) throws Exception {
        if (packet == null || pw == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(packet.action + "");
        if (packet.data != null) {
            for (String str : packet.data) {
                sb.append("@@@");
                sb.append(str);
            }
        }

        pw.println(sb.toString());
    }

    public static Packet readPacket(String text) throws Exception {
        String[] args = text.split("@@@");
        Packet packet = new Packet();
        packet.action = Integer.parseInt(args[0]);

        text = StringUltils.cleanTextContent(text);
        if (args.length > 1) {
            packet.data = new ArrayList<String>();
            for (int i = 1; i < args.length; i++) {
                packet.data.add(args[i]);
            }
        }
        return packet;
    }
}
