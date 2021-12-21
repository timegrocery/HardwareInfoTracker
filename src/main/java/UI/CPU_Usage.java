package UI;

import java.awt.*;
import java.io.Serial;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.*;

import Hardware.CPU;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.*;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

public class CPU_Usage extends OshiJPanel {

    private static final long serialVersionUID = 1L;

    public CPU_Usage(DynamicTimeSeriesCollection[] cpuTimeSeries) {
        super();
        init(cpuTimeSeries);
    }

    public void init(DynamicTimeSeriesCollection[] cpuTimeSeries) {

        GridBagConstraints sysConstraints = new GridBagConstraints();
        sysConstraints.weightx = 1d;
        sysConstraints.weighty = 1d;
        sysConstraints.fill = GridBagConstraints.BOTH;

        GridBagConstraints procConstraints = (GridBagConstraints) sysConstraints.clone();
        procConstraints.gridx = 1;

        JFreeChart systemCpu = ChartFactory.createTimeSeriesChart("System CPU Usage", "Time", "% CPU",cpuTimeSeries[0], true,
                true, false);
        JFreeChart procCpu = ChartFactory.createTimeSeriesChart("Processor CPU Usage", "Time", "% CPU", cpuTimeSeries[1], true,
               true, false);

        JPanel cpuPanel = new JPanel();
        cpuPanel.setLayout(new GridBagLayout());
        cpuPanel.add(new ChartPanel(systemCpu), sysConstraints);
        cpuPanel.add(new ChartPanel(procCpu), procConstraints);

        add(cpuPanel, BorderLayout.CENTER);

    }

    public static  void main(String[] args) {

    }

}

