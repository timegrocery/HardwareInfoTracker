/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.GUI;

import Server.ConnectedClient;
import Ultils.MessageType;
import Ultils.NetUtils;
import Ultils.Packet;

import java.util.ArrayList;

/**
 *
 * @author dell
 */
public class Gui_Keylogger extends javax.swing.JFrame {
    private ConnectedClient client;
    /**
     * Creates new form Gui_Keylogger
     */
    public Gui_Keylogger(ConnectedClient client) {
        this.client = client;
        initComponents();
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setTitle(client.getUsername() + " Keylogger + Clipboard");
        setResizable(false);
    }
    public void SeyKeylogger(String txt) {
        jTextAreaKeyLogger.setText(txt);
    }
    public void SetClipboard(String txt) {
        jTextAreaClipboard.setText(txt);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaKeyLogger = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextAreaClipboard = new javax.swing.JTextArea();
        jButtonKeyLogger = new javax.swing.JButton();
        jButtonClipboard = new javax.swing.JButton();

        setResizable(false);

        jLabel3.setText("Key Logger");

        jLabel4.setText("Clipboard");

        jTextAreaKeyLogger.setEditable(false);
        jTextAreaKeyLogger.setColumns(20);
        jTextAreaKeyLogger.setLineWrap(true);
        jTextAreaKeyLogger.setRows(10);
        jTextAreaKeyLogger.setWrapStyleWord(true);
        jScrollPane3.setViewportView(jTextAreaKeyLogger);

        jTextAreaClipboard.setEditable(false);
        jTextAreaClipboard.setColumns(20);
        jTextAreaClipboard.setLineWrap(true);
        jTextAreaClipboard.setRows(10);
        jTextAreaClipboard.setWrapStyleWord(true);
        jScrollPane4.setViewportView(jTextAreaClipboard);

        jButtonKeyLogger.setText("Reload");
        jButtonKeyLogger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonKeyLoggerActionPerformed(evt);
                Packet packet = new Packet();
                packet.action = MessageType.KEYLOGGER.getID();
                packet.data = new ArrayList<String>();
                try {
                    NetUtils.sendMessage(packet, client.getPrintWriter());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        jButtonClipboard.setText("Reload");
        jButtonClipboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClipboardActionPerformed(evt);
                Packet packet = new Packet();
                packet.action = MessageType.CLIPBOARD.getID();
                packet.data = new ArrayList<String>();
                try {
                    NetUtils.sendMessage(packet, client.getPrintWriter());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(198, 198, 198)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(164, 164, 164))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(194, 194, 194)
                .addComponent(jButtonKeyLogger)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonClipboard)
                .addGap(154, 154, 154))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .addComponent(jScrollPane4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonClipboard)
                    .addComponent(jButtonKeyLogger))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonKeyLoggerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonKeyLoggerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonKeyLoggerActionPerformed

    private void jButtonClipboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClipboardActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonClipboardActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClipboard;
    private javax.swing.JButton jButtonKeyLogger;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    public javax.swing.JTextArea jTextAreaClipboard;
    public javax.swing.JTextArea jTextAreaKeyLogger;
    // End of variables declaration//GEN-END:variables


}
