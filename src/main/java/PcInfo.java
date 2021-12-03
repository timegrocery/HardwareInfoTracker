import javax.swing.*;
import java.awt.*;


public class PcInfo extends JFrame {
    public static final String GUI_TITLE = "Operating System & Hardware Information";
    public static final int GUI_WIDTH = 800;
    public static final int GUI_HEIGHT = 500;

    public PcInfo() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle(GUI_TITLE);
        this.setSize(GUI_WIDTH, GUI_HEIGHT);
        this.setResizable(true);
        this.setLocationByPlatform(true);
        this.setLayout(new BorderLayout());
        init();
    }
    public void init(){
        JPanel itemView = new JPanel(null);
        itemView.setBounds(new Rectangle(0, 0, GUI_WIDTH  , GUI_HEIGHT ));
        itemView.setBackground(Color.lightGray);

        JLabel lbHI = new JLabel("Hardware Information");
        lbHI.setBounds(new Rectangle(540,40,200,30));
        JTextArea jtHI = new JTextArea("");
        jtHI.setBounds((new Rectangle(450,70,300,350)));
        jtHI.setBorder(BorderFactory.createLineBorder(Color.black));

        JLabel lbOS = new JLabel("Operating System");
        lbOS.setBounds(new Rectangle(230,40,200,30));
        JTextArea jtOS = new JTextArea("");
        jtOS.setBounds((new Rectangle(130,70,300,50)));
        jtOS.setBorder(BorderFactory.createLineBorder(Color.black));

        JLabel lbProcess = new JLabel("Processor");
        lbProcess.setBounds(new Rectangle(250,130,200,30));
        JTextArea jtProcess = new JTextArea("");
        jtProcess.setBounds((new Rectangle(130,160,300,180)));
        jtProcess.setBorder(BorderFactory.createLineBorder(Color.black));

        JLabel lbDisplay = new JLabel("Displays");
        lbDisplay.setBounds(new Rectangle(250,350,200,30));
        JTextArea jtDisplay = new JTextArea("");
        jtDisplay.setBounds((new Rectangle(130,380,300,20)));
        jtDisplay.setBorder(BorderFactory.createLineBorder(Color.black));


        itemView.add(lbOS);
        itemView.add(jtOS);
        itemView.add(lbProcess);
        itemView.add(jtProcess);
        itemView.add(lbDisplay);
        itemView.add(jtDisplay);
        itemView.add(lbHI);
        itemView.add(jtHI);
        add(itemView);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        PcInfo pcInfo = new PcInfo();
    }
}
