package Server.GUI;

import Server.ConnectedClient;
import UI.CPU_Usage;
import UI.OshiJPanel;
import Ultils.MessageType;
import Ultils.NetUtils;
import Ultils.Packet;
import org.jetbrains.annotations.NotNull;
import org.jfree.data.time.DynamicTimeSeriesCollection;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class GuiCPU extends javax.swing.JFrame {
    private JFrame frame;
    private ConnectedClient client;
    private CPU_Usage cpuPanel;
    private JPanel contentPane;
    public GuiCPU(@NotNull ConnectedClient client) {
        this.client = client;
        frame = new JFrame();
        cpuPanel = new CPU_Usage(client.connectedTimeSeries);
        cpuPanel.setSize(new Dimension(1800,800));
        contentPane = new JPanel();
        contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        cpuPanel.setBounds(1, 24, 399, 353);
        cpuPanel.setBorder(new LineBorder(new Color(0, 0 ,0), 1, true));
        cpuPanel.setLayout(new BorderLayout(0, 0));
        contentPane.add(cpuPanel);
        setTitle(client.getUsername() + " CPU Usage");
        this.setResizable(false);
        setBounds(100, 100, 1200, 800);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(1400, 800);
    }


    public void SetCpuUsage(DynamicTimeSeriesCollection[] dynamicTimeSeriesCollections) {
        if (dynamicTimeSeriesCollections == null) {
            return;
        }
        if (this.isVisible()) {
            this.cpuPanel.init(dynamicTimeSeriesCollections);
        } else {
            Packet packet = new Packet();
            packet.action = MessageType.PERFORMANCE_TRACK.getID();
            packet.data = new ArrayList<>();
            packet.data.add("stop");
            try {
                NetUtils.sendMessage(packet, client.getPrintWriter());
            } catch (Exception e) {
                System.out.println("Failed to send stop tracking packet");
            }
        }

    }

    public static void main(String[] args) {
        try {
            ConnectedClient fake = new ConnectedClient(new Socket());
            GuiCPU test = new GuiCPU(fake);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
