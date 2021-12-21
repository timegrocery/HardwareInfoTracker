package UI;

import Hardware.GPU;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.software.os.OperatingSystem;
import oshi.util.EdidUtil;
import oshi.util.FormatUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.util.List;

public class OsHwJPanel extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;
    private static final String OPERATING_SYSTEM = "Operating System";
    private static final String HARDWARE_INFORMATION = "Hardware Information";
    private static final String PROCESSOR = "Processor";
    private static final String DISPLAYS = "Displays";
    public static final int REFRESH_FAST = 1000;
    private String osPrefix;
    public OsHwJPanel(SystemInfo si) {
        initComponents(si);
        setEditable(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents(SystemInfo si) {
        osPrefix = getOsPrefix(si);
        jLblOS = new javax.swing.JLabel();
        jLblProc = new javax.swing.JLabel();
        jLblDisplay = new javax.swing.JLabel();
        jLblHI = new javax.swing.JLabel();
        jScrollOS = new javax.swing.JScrollPane();
        jtaOS = new javax.swing.JTextArea();
        jScrollHI = new javax.swing.JScrollPane();
        jtaHI = new javax.swing.JTextArea();
        jScrollDisplay = new javax.swing.JScrollPane();
        jtaDisplay = new javax.swing.JTextArea();
        jScrollProc = new javax.swing.JScrollPane();
        jtaProc = new javax.swing.JTextArea();

        setBackground(new java.awt.Color(161, 201, 241));
        setPreferredSize(new java.awt.Dimension(810, 480));

        jLblOS.setText("Operating System");

        jLblProc.setText("Processor");

        jLblDisplay.setText("Display");

        jLblHI.setText("Hardware Infomation");

        jtaOS.setColumns(20);
        jtaOS.setRows(5);
        jScrollOS.setViewportView(jtaOS);

        jtaHI.setColumns(20);
        jtaHI.setRows(5);
        jScrollHI.setViewportView(jtaHI);

        jtaDisplay.setColumns(20);
        jtaDisplay.setRows(5);
        jScrollDisplay.setViewportView(jtaDisplay);

        jtaProc.setColumns(20);
        jtaProc.setRows(5);
        jScrollProc.setViewportView(jtaProc);

        jtaOS.setText(updateOsData(si));
        jtaProc.setText(getProc(si));
        jtaDisplay.setText(getDisplay(si));
        jtaHI.setText(getHw(si));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(140, 140, 140)
                                .addComponent(jLblOS))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(158, 158, 158)
                                .addComponent(jLblDisplay))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollProc, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                                    .addComponent(jScrollOS)
                                    .addComponent(jScrollDisplay))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLblProc)
                        .addGap(149, 149, 149)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollHI, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLblHI)
                        .addGap(179, 179, 179))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLblOS)
                    .addComponent(jLblHI))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLblProc, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollProc, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLblDisplay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollHI))
                .addContainerGap(86, Short.MAX_VALUE))
        );
        // Update up time every second
        Timer timer = new Timer(REFRESH_FAST, e -> jtaOS.setText(updateOsData(si)));
        timer.start();
    }// </editor-fold>//GEN-END:initComponents

    private void jtaOSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtaOSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtaOSActionPerformed

    private void jtaProcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtaProcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtaProcActionPerformed

    private void jtaDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtaDisplayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtaDisplayActionPerformed

    private void jtaHIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtaHIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtaHIActionPerformed

    private static String getOsPrefix(SystemInfo si) {
        StringBuilder sb = new StringBuilder();

        OperatingSystem os = si.getOperatingSystem();
        sb.append(String.valueOf(os));
        sb.append("\n\n").append("Booted: ").append(Instant.ofEpochSecond(os.getSystemBootTime())).append('\n')
                .append("Uptime: ");
        return sb.toString();
    }

    private static String getHw(SystemInfo si) {
        StringBuilder sb = new StringBuilder();
        ObjectMapper mapper = new ObjectMapper();
        ComputerSystem computerSystem = si.getHardware().getComputerSystem();
        try {
            sb.append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(computerSystem));
        } catch (JsonProcessingException e) {
            sb.append(e.getMessage());
        }
        return sb.toString();
    }

    private static String getProc(SystemInfo si) {
        StringBuilder sb = new StringBuilder();
        GPU gpu = new GPU();
        CentralProcessor proc = si.getHardware().getProcessor();
        sb.append(proc.toString());
        sb.append(gpu.getGPUInformation());
        return sb.toString();
    }

    private static String getDisplay(SystemInfo si) {
        StringBuilder sb = new StringBuilder();
        List<Display> displays = si.getHardware().getDisplays();
        if (displays.isEmpty()) {
            sb.append("None detected.");
        } else {
            int i = 0;
            for (Display display : displays) {
                byte[] edid = display.getEdid();
                byte[][] desc = EdidUtil.getDescriptors(edid);
                String name = "Display " + i;
                for (byte[] b : desc) {
                    if (EdidUtil.getDescriptorType(b) == 0xfc) {
                        name = EdidUtil.getDescriptorText(b);
                    }
                }
                if (i++ > 0) {
                    sb.append('\n');
                }
                sb.append(name).append(": ");
                int hSize = EdidUtil.getHcm(edid);
                int vSize = EdidUtil.getVcm(edid);
                sb.append(String.format("%d x %d cm (%.1f x %.1f in)", hSize, vSize, hSize / 2.54, vSize / 2.54));
            }
        }
        return sb.toString();
    }

    private String updateOsData(SystemInfo si) {
        return osPrefix + FormatUtil.formatElapsedSecs(si.getOperatingSystem().getSystemUptime());
    }

   private void setEditable(boolean flag) {
        jtaDisplay.setEditable(false);
        jtaHI.setEditable(false);
        jtaOS.setEditable(false);
        jtaProc.setEditable(false);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLblDisplay;
    private javax.swing.JLabel jLblHI;
    private javax.swing.JLabel jLblOS;
    private javax.swing.JLabel jLblProc;
    private javax.swing.JScrollPane jScrollDisplay;
    private javax.swing.JScrollPane jScrollHI;
    private javax.swing.JScrollPane jScrollOS;
    private javax.swing.JScrollPane jScrollProc;
    private javax.swing.JTextArea jtaDisplay;
    private javax.swing.JTextArea jtaHI;
    private javax.swing.JTextArea jtaOS;
    private javax.swing.JTextArea jtaProc;
    // End of variables declaration//GEN-END:variables
}
