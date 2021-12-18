package DTO;

import javax.swing.*;

public class OsHwDTO {
    private JTextField jtfOS;
    private  JTextField jtfProc;
    private  JTextField jtfDisplay;
    private JTextField jtfHI;

    public OsHwDTO(){

    }

    public OsHwDTO(JTextField jtfOS, JTextField jtfProc, JTextField jtfDisplay, JTextField jtfHI) {
        this.jtfOS = jtfOS;
        this.jtfProc = jtfProc;
        this.jtfDisplay = jtfDisplay;
        this.jtfHI = jtfHI;
    }

    public JTextField getJtfOS() {
        return jtfOS;
    }

    public void setJtfOS(JTextField jtfOS) {
        this.jtfOS = jtfOS;
    }

    public JTextField getJtfProc() {
        return jtfProc;
    }

    public void setJtfProc(JTextField jtfProc) {
        this.jtfProc = jtfProc;
    }

    public JTextField getJtfDisplay() {
        return jtfDisplay;
    }

    public void setJtfDisplay(JTextField jtfDisplay) {
        this.jtfDisplay = jtfDisplay;
    }

    public JTextField getJtfHI() {
        return jtfHI;
    }

    public void setJtfHI(JTextField jtfHI) {
        this.jtfHI = jtfHI;
    }
    
}
