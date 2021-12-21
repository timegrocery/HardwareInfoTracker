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

    public  CPU_Usage()
    {
        super();
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
        /*
        SystemInfo si = new SystemInfo();

        CentralProcessor cp = si.getHardware().getProcessor();
        Hardware.CPU_Usage p = new Hardware.CPU_Usage(si);
        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        DynamicTimeSeriesCollection sysData = new DynamicTimeSeriesCollection(1, 60, new Second());
        sysData.setTimeBase(new Second(date));
        sysData.addSeries(Hardware.CPU_Usage.floatArrayPercent(p.cpuData(cp)), 0, "All cpus");
        double[] procUsage = p.procData(cp);
        DynamicTimeSeriesCollection procData = new DynamicTimeSeriesCollection(procUsage.length, 60, new Second());
        procData.setTimeBase(new Second(date));
        for (int i = 0; i < procUsage.length; i++) {
            procData.addSeries(Hardware.CPU_Usage.floatArrayPercent(procUsage[i]), i, "cpu" + i);
        }
        */
        SystemInfo si = new SystemInfo();
        Hardware.CPU_Usage cpu_usage = new Hardware.CPU_Usage();

        DynamicTimeSeriesCollection[] timeSeries = cpu_usage.CreateTimeSeries(si);

        for (int i = 0; i < timeSeries[0].getSeriesCount(); ++i)
        {
            for (int j = 0; j < timeSeries[0].getItemCount(i); ++j)
            {
                System.out.print(timeSeries[0].getX(i,j) + " ");
            }
            System.out.println();
        }

    }

}

