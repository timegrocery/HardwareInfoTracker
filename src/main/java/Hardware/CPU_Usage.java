package Hardware;

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

public class CPU_Usage {
    // total usage
    // core
    // dynamic time series
    private long[] oldTicks;
    private long[][] oldProcTicks;

    public CPU_Usage(SystemInfo si)
    {
        CentralProcessor cpu = si.getHardware().getProcessor();
        oldTicks = new long[CentralProcessor.TickType.values().length];
        oldProcTicks = new long[cpu.getLogicalProcessorCount()][CentralProcessor.TickType.values().length];
    }

    public DynamicTimeSeriesCollection[] CreateTimeSeries(SystemInfo si) {
        CentralProcessor processor = si.getHardware().getProcessor();
        Hardware.CPU_Usage cpu_usage = new Hardware.CPU_Usage(si);
        DynamicTimeSeriesCollection[] usageCollection = new DynamicTimeSeriesCollection[2];
        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        DynamicTimeSeriesCollection sysData = new DynamicTimeSeriesCollection(1, 60, new Second());
        sysData.setTimeBase(new Second(date));
        sysData.addSeries(floatArrayPercent(cpu_usage.cpuData(processor)), 0, "All cpus");

        double[] procUsage = cpu_usage.procData(processor);
        DynamicTimeSeriesCollection procData = new DynamicTimeSeriesCollection(procUsage.length, 60, new Second());
        procData.setTimeBase(new Second(date));

        for (int i = 0; i < procUsage.length; i++) {
            procData.addSeries(floatArrayPercent(procUsage[i]), i, "cpu" + i);
        }

        usageCollection[0] = sysData;
        usageCollection[1] = procData;

        return usageCollection;
    }

    public Timer UpdateUsage(CentralProcessor processor, DynamicTimeSeriesCollection sysData, DynamicTimeSeriesCollection procData)
    {
        int REFRESH_FAST = 1000;

        Timer timer = new Timer(REFRESH_FAST, e -> {
            sysData.advanceTime();
            sysData.appendData(floatArrayPercent(cpuData(processor)));
            procData.advanceTime();
            int newest = procData.getNewestIndex();
            double[] procUsageData = procData(processor);
            for (int i = 0; i < procUsageData.length; i++) {
                procData.addValue(i, newest, (float) (100 * procUsageData[i]));
            }
        });

        return  timer;
    }

    public static float[] floatArrayPercent(double d) {
        float[] f = new float[1];
        f[0] = (float) (100d * d);
        return f;
    }

    public double cpuData(CentralProcessor proc) {
        double d = proc.getSystemCpuLoadBetweenTicks(oldTicks);
        oldTicks = proc.getSystemCpuLoadTicks();
        return d;
    }

    public double[] procData(CentralProcessor proc) {
        double[] p = proc.getProcessorCpuLoadBetweenTicks(oldProcTicks);
        oldProcTicks = proc.getProcessorCpuLoadTicks();
        return p;
    }

    public static void sendCpuUsage(PrintWriter pw)
    {
        SystemInfo si = new SystemInfo();
        Hardware.CPU_Usage cpu_usage = new CPU_Usage(si);
        try {
            Packet packet = new Packet();
            packet.action = MessageType.PERFORMANCE_TRACK.getID();

            DynamicTimeSeriesCollection[] cpuTimeSeries = cpu_usage.CreateTimeSeries(si);

            Number cpuResult = cpuTimeSeries[0].getX(0,0);
            Number[] procResult = new Number[cpuTimeSeries[1].getItemCount(0)];

            for (int j = 0; j < cpuTimeSeries[1].getItemCount(0); ++j)
            {
                procResult[j] = cpuTimeSeries[1].getX(0,j);
            }

            packet.data = new ArrayList<>();

            packet.data.add(String.valueOf(cpuResult));

            for (int i = 0; i < procResult.length; ++i) {
                packet.data.add(String.valueOf(procResult[i]));
            }

            NetUtils.sendMessage(packet,pw);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

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

        JPanel cpuPanel = new JPanel();
        cpuPanel.setLayout(new GridBagLayout());
        cpuPanel.add(new ChartPanel(systemCpu), sysConstraints);
        cpuPanel.add(new ChartPanel(procCpu), procConstraints);

        OshiJPanel oshiJPanel = new OshiJPanel();
        oshiJPanel.add(cpuPanel, BorderLayout.CENTER);

        Timer timer = cpu_usage.UpdateUsage(si.getHardware().getProcessor(),cpuSeries[0],cpuSeries[1]);
        timer.start();

        JFrame frame = new JFrame();
        frame.add(oshiJPanel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

}
