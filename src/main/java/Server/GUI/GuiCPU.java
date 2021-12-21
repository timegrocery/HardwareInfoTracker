package Server.GUI;

import Server.ConnectedClient;
import UI.CPU_Usage;
import UI.OshiJPanel;

import javax.swing.*;

public class GuiCPU extends javax.swing.JFrame {
    private JFrame frame;
    private ConnectedClient client;
    public GuiCPU(ConnectedClient client) {
        this.client = client;
        setTitle(client.getUsername() + " CPU Usage");
        frame.setDefaultCloseOperation(HIDE_ON_CLOSE);
        JFrame frame = new JFrame();
        this.setResizable(false);
    }

    public void init() {
        CPU_Usage cpuPanel = new CPU_Usage();
        frame.add(cpuPanel);
        frame.setVisible(true);
    }


}
