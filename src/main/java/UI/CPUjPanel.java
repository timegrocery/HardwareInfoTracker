/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package UI;

/**
 *
 * @author Admin
 */
public class CPUjPanel extends javax.swing.JPanel {

    /**
     * Creates new form CPUjPanel
     */
    public CPUjPanel() {
        initComponents();
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BGjPanel = new javax.swing.JPanel();
        jPnSysCpu = new javax.swing.JPanel();
        jPnProccessor = new javax.swing.JPanel();
        jLblSysCpu = new javax.swing.JLabel();
        jLblProccessor = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(810, 430));
        setRequestFocusEnabled(false);

        BGjPanel.setBackground(new java.awt.Color(161, 201, 241));
        BGjPanel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        BGjPanel.setPreferredSize(new java.awt.Dimension(800, 414));

        jPnSysCpu.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPnSysCpuLayout = new javax.swing.GroupLayout(jPnSysCpu);
        jPnSysCpu.setLayout(jPnSysCpuLayout);
        jPnSysCpuLayout.setHorizontalGroup(
            jPnSysCpuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 281, Short.MAX_VALUE)
        );
        jPnSysCpuLayout.setVerticalGroup(
            jPnSysCpuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jPnProccessor.setBackground(new java.awt.Color(255, 255, 255));
        jPnProccessor.setPreferredSize(new java.awt.Dimension(281, 300));

        javax.swing.GroupLayout jPnProccessorLayout = new javax.swing.GroupLayout(jPnProccessor);
        jPnProccessor.setLayout(jPnProccessorLayout);
        jPnProccessorLayout.setHorizontalGroup(
            jPnProccessorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 281, Short.MAX_VALUE)
        );
        jPnProccessorLayout.setVerticalGroup(
            jPnProccessorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jLblSysCpu.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLblSysCpu.setText("SYSTEM CPU USAGE");

        jLblProccessor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLblProccessor.setText("PROCESSOR USAGE");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Time");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Time");

        javax.swing.GroupLayout BGjPanelLayout = new javax.swing.GroupLayout(BGjPanel);
        BGjPanel.setLayout(BGjPanelLayout);
        BGjPanelLayout.setHorizontalGroup(
            BGjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BGjPanelLayout.createSequentialGroup()
                .addGap(186, 186, 186)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(230, 230, 230))
            .addGroup(BGjPanelLayout.createSequentialGroup()
                .addGroup(BGjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BGjPanelLayout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addComponent(jPnSysCpu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(BGjPanelLayout.createSequentialGroup()
                        .addGap(167, 167, 167)
                        .addComponent(jLblSysCpu)))
                .addGap(55, 55, 55)
                .addGroup(BGjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPnProccessor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BGjPanelLayout.createSequentialGroup()
                        .addComponent(jLblProccessor)
                        .addGap(65, 65, 65)))
                .addContainerGap(108, Short.MAX_VALUE))
        );
        BGjPanelLayout.setVerticalGroup(
            BGjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BGjPanelLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(BGjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLblProccessor, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(jLblSysCpu))
                .addGap(8, 8, 8)
                .addGroup(BGjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPnSysCpu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPnProccessor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(BGjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addGap(38, 38, 38))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BGjPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 810, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BGjPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BGjPanel;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLblProccessor;
    private javax.swing.JLabel jLblSysCpu;
    private javax.swing.JPanel jPnProccessor;
    private javax.swing.JPanel jPnSysCpu;
    // End of variables declaration//GEN-END:variables
}
