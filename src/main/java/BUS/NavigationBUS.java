package BUS;

import DTO.ListDTO;
import UI.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.*;
public class NavigationBUS {
    private JPanel root;
    private String kindSelected = "";
    private ArrayList<ListDTO> listItem = null;
    public NavigationBUS (JPanel jpnRoot) {
        this.root = jpnRoot;
    }
    public void setView (JPanel jpnItem, JLabel jlbItem) {
        kindSelected = "OS and HW Info";
        jpnItem.setBackground (new Color (96, 100, 191));
        jlbItem.setBackground (new Color (96, 100, 191));
        root.removeAll();
        root.setLayout(new BorderLayout());
        root.add (new OsHwJPanel());
        root.validate();
        root.repaint ();
    }
    public void setEvent( ArrayList<ListDTO> listItem){
        this.listItem = listItem;
        for( var item : listItem) {
            item.getJlb().addMouseListener(new LabelEvent(item.getKind(), item.getJpn(), item.getJlb()));

        }
    }    
    class LabelEvent implements MouseListener {
        private JPanel node;
        private String kind;
        private JPanel jpnItem;
        private JLabel jlblItem;

        public LabelEvent(String kind, JPanel jpnItem, JLabel jLblItem) {
            this.kind = kind;
            this.jpnItem = jpnItem;
            this.jlblItem = jLblItem;
        }
        
        
        @Override
        public void mouseClicked(MouseEvent e) {
            switch (kind){
                case "OS & HW Info":
                    node = new OsHwJPanel();
                    break;
                case "Memory":
                    node = new MemoryJPanel();
                    break;
            default:
                break;
            }        
            root.removeAll();
            root.setLayout (new BorderLayout ());
            root.add (node);
            root.validate();
            root.repaint();
            setChangeBackgroud(kind);
        }
        @Override
        public void mousePressed(MouseEvent e) {
            kindSelected = kind;
            jpnItem.setBackground (new Color (96, 100, 191));
            jlblItem.setBackground (new Color (96, 100, 191));
        }
        @Override
        public void mouseReleased(MouseEvent e){
       
        }
        @Override
        public void mouseEntered (MouseEvent e){
            jpnItem.setBackground (new Color (96, 100, 191));
            jlblItem.setBackground (new Color (96, 100, 191));
        }
        @Override
        public void mouseExited(MouseEvent e){
            if (!kindSelected.equalsIgnoreCase (kind)){
                jpnItem.setBackground (new Color (76, 175, 80));
                jlblItem.setBackground (new Color (76, 175, 80));
            }    
        }
    }
    private void setChangeBackgroud (String kind){
        for (var item : listItem) {
            if(item.getKind ().equalsIgnoreCase (kind)) {
                item.getJpn ().setBackground (new Color (96, 100, 191));
                item.getJlb ().setBackground (new Color (96, 100, 191));
            }else{
                item.getJpn ().setBackground (new Color (76, 175, 80));
                item.getJlb().setBackground(new Color (76, 175, 80));
            }     
        }  
    }    
}
