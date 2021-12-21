package Performance;

import UI.OshiJPanel;
import Ultils.MessageType;
import Ultils.NetUtils;
import Ultils.Packet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class CPU_Usage {
    // total usage
    // core
    // dynamic time series
    private static long[] oldTicks;
    private static long[][] oldProcTicks;

    public static double getCpuPercent(){
        oldTicks = new long[CentralProcessor.TickType.values().length];
        java.util.List<Double> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            SystemInfo si = new SystemInfo();
            CentralProcessor cpu = si.getHardware().getProcessor();
            oldProcTicks = new long[cpu.getLogicalProcessorCount()][CentralProcessor.TickType.values().length];

            double d = cpu.getSystemCpuLoadBetweenTicks(oldTicks);
            oldTicks = cpu.getSystemCpuLoadTicks();
            list.add(d);
        }
        AtomicReference<Double> sum = new AtomicReference<>((double) 0);
        list.stream().filter((e)->e<1&&e>0).forEach(sum::set);
        return sum.get()*100;
    }

    public static void main(String[] args) {
        System.out.println(getCpuPercent());
    }
    /*
    public static  void main(String[] args)
    {
        SystemInfo si = new SystemInfo();
        CPU_Usage cpu_usage = new CPU_Usage(si);

        GridBagConstraints sysConstraints = new GridBagConstraints();
        sysConstraints.weightx = 1d;
        sysConstraints.weighty = 1d;
        sysConstraints.fill = GridBagConstraints.BOTH;

        GridBagConstraints procConstraints = (GridBagConstraints) sysConstraints.clone();
        procConstraints.gridx = 1;

        DynamicTimeSeriesCollection[] cpuSeries = cpu_usage.CreateTimeSeries(si);

        JFreeChart systemCpu = ChartFactory.createTimeSeriesChart("System CPU Usage", "Time", "% CPU", cpuSeries[0], true,
                true, false);
        JFreeChart procCpu = ChartFactory.createTimeSeriesChart("Processor CPU Usage", "Time", "% CPU", cpuSeries[1], true,
                true, false);

        Timer timer = cpu_usage.UpdateUsage(si.getHardware().getProcessor(),cpuSeries[0],cpuSeries[1]);
        timer.start();

        JPanel cpuPanel = new JPanel();
        cpuPanel.setLayout(new GridBagLayout());
        cpuPanel.add(new ChartPanel(systemCpu), sysConstraints);
        cpuPanel.add(new ChartPanel(procCpu), procConstraints);

        OshiJPanel oshiJPanel = new OshiJPanel();
        oshiJPanel.add(cpuPanel, BorderLayout.CENTER);

        JFrame frame = new JFrame();
        frame.add(oshiJPanel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    */
}
