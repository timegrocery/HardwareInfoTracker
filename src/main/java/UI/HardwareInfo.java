package UI;

import javax.swing.*;

public class HardwareInfo extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JTable processList;

    public HardwareInfo() {
        JFrame frame = new JFrame("Task Manager");
        frame.setContentPane(new HardwareInfo().mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        HardwareInfo hi = new HardwareInfo();
    }
}
