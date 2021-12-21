/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.GUI;

import Server.ConnectedClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author dell
 */
public class Gui_Performance extends javax.swing.JFrame {
    private ConnectedClient client;

    public Gui_Performance(ConnectedClient client) {
        this.client = client;
        initComponents();
        setTitle(client.getUsername() + " Performance");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setResizable(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelCpuUsage = new javax.swing.JLabel();
        jLabelRAM = new javax.swing.JLabel();
        jLabelDisk = new javax.swing.JLabel();
        jTextFieldCPU = new javax.swing.JTextField();
        jLabelProccessList = new javax.swing.JLabel();
        jScrollPaneProcessList = new javax.swing.JScrollPane();
        jTextAreaProcessList = new javax.swing.JTextArea();
        jScrollPaneRAM = new javax.swing.JScrollPane();
        jTextAreaRAM = new javax.swing.JTextArea();
        jScrollPaneDisk = new javax.swing.JScrollPane();
        jTextAreaDisk = new javax.swing.JTextArea();

        setTitle("Performance");
        setResizable(false);

        jLabelCpuUsage.setText("CPU Usage: ");

        jLabelRAM.setText("RAM:");

        jLabelDisk.setText("Disk: ");

        jTextFieldCPU.setEditable(false);
        jTextFieldCPU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCPUActionPerformed(evt);
            }
        });

        jLabelProccessList.setText("Process List");

        jTextAreaProcessList.setEditable(false);
        jTextAreaProcessList.setColumns(20);
        jTextAreaProcessList.setRows(5);
        jScrollPaneProcessList.setViewportView(jTextAreaProcessList);

        jTextAreaRAM.setEditable(false);
        jTextAreaRAM.setColumns(20);
        jTextAreaRAM.setRows(5);
        jScrollPaneRAM.setViewportView(jTextAreaRAM);

        jTextAreaDisk.setColumns(20);
        jTextAreaDisk.setRows(5);
        jScrollPaneDisk.setViewportView(jTextAreaDisk);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPaneRAM, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                                        .addComponent(jScrollPaneDisk)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabelRAM)
                                                        .addComponent(jLabelDisk)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabelCpuUsage)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jTextFieldCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPaneProcessList, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelProccessList)
                                .addGap(178, 178, 178))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabelProccessList)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jTextFieldCPU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabelCpuUsage)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jLabelRAM)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jScrollPaneRAM, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabelDisk)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPaneDisk))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPaneProcessList, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 23, Short.MAX_VALUE)))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldCPUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCPUActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldCPUActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelCpuUsage;
    private javax.swing.JLabel jLabelDisk;
    private javax.swing.JLabel jLabelProccessList;
    private javax.swing.JLabel jLabelRAM;
    private javax.swing.JScrollPane jScrollPaneDisk;
    private javax.swing.JScrollPane jScrollPaneProcessList;
    private javax.swing.JScrollPane jScrollPaneRAM;
    private javax.swing.JTextArea jTextAreaDisk;
    private javax.swing.JTextArea jTextAreaProcessList;
    private javax.swing.JTextArea jTextAreaRAM;
    private javax.swing.JTextField jTextFieldCPU;
    // End of variables declaration//GEN-END:variables

    public void setCpu(String txt) {
        jTextFieldCPU.setText(txt + "%");
    }
    public void setRAM(String txt) {
        jTextAreaRAM.setText(txt);
    }
    public void setDisk(String txt) {
        jTextAreaDisk.setText(txt);
    }
    public void setProcess(String txt) {
        jTextAreaProcessList.setText(txt);
    }
}
