package UI;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.*;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

public class CPU_Usage extends OshiJPanel {

    private static final long serialVersionUID = 1L;
    public static final int REFRESH_RATE = 1000;
    private long[] oldTicks;
    private long[][] oldProcTicks;

    public CPU_Usage(SystemInfo si) {
        super();
        CentralProcessor cpu = si.getHardware().getProcessor();
        oldTicks = new long[CentralProcessor.TickType.values().length];
        oldProcTicks = new long[cpu.getLogicalProcessorCount()][CentralProcessor.TickType.values().length];
        init(cpu);
    }

    public DynamicTimeSeriesCollection[] CreateTimeSeries(CentralProcessor processor) {
        DynamicTimeSeriesCollection[] usageCollection = new DynamicTimeSeriesCollection[2];
        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        DynamicTimeSeriesCollection sysData = new DynamicTimeSeriesCollection(1, 60, new Second());
        sysData.setTimeBase(new Second(date));
        sysData.addSeries(floatArrayPercent(cpuData(processor)), 0, "All cpus");

        double[] procUsage = procData(processor);
        DynamicTimeSeriesCollection procData = new DynamicTimeSeriesCollection(procUsage.length, 60, new Second());
        procData.setTimeBase(new Second(date));

        for (int i = 0; i < procUsage.length; i++) {
            procData.addSeries(floatArrayPercent(procUsage[i]), i, "cpu" + i);
        }

        usageCollection[0] = sysData;
        usageCollection[1] = procData;

        return usageCollection;
    }

    public void init(CentralProcessor processor) {

        GridBagConstraints sysConstraints = new GridBagConstraints();
        sysConstraints.weightx = 1d;
        sysConstraints.weighty = 1d;
        sysConstraints.fill = GridBagConstraints.BOTH;

        GridBagConstraints procConstraints = (GridBagConstraints) sysConstraints.clone();
        procConstraints.gridx = 1;

        DynamicTimeSeriesCollection[] usageCollection = CreateTimeSeries(processor);

        JFreeChart systemCpu = ChartFactory.createTimeSeriesChart("System CPU Usage", "Time", "% CPU", usageCollection[0], true,
                true, false);
        JFreeChart procCpu = ChartFactory.createTimeSeriesChart("Processor CPU Usage", "Time", "% CPU", usageCollection[1], true,
                true, false);

        JPanel cpuPanel = new JPanel();
        cpuPanel.setLayout(new GridBagLayout());
        cpuPanel.add(new ChartPanel(systemCpu), sysConstraints);
        cpuPanel.add(new ChartPanel(procCpu), procConstraints);

        add(cpuPanel, BorderLayout.CENTER);

        //Timer timer = UpdateUsage(processor, usageCollection[0], usageCollection[1]);
        //timer.start();
    }

    public Timer UpdateUsage(CentralProcessor processor, DynamicTimeSeriesCollection sysData, DynamicTimeSeriesCollection procData) {
        Timer timer = new Timer(REFRESH_RATE, e -> {
            sysData.advanceTime();
            sysData.appendData(floatArrayPercent(cpuData(processor)));
            procData.advanceTime();
            int newest = procData.getNewestIndex();
            double[] procUsageData = procData(processor);
            for (int i = 0; i < procUsageData.length; i++) {
                procData.addValue(i, newest, (float) (100 * procUsageData[i]));
            }
        });
        return timer;
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

    public static void main(String[] args) {
        SystemInfo si = new SystemInfo();
        CPU_Usage cpu_usage = new CPU_Usage(si);

        DynamicTimeSeriesCollection[] usageCollection = cpu_usage.CreateTimeSeries(si.getHardware().getProcessor());

        System.out.println(usageCollection[0].getX(16,0));

        usageCollection[0].appendData(floatArrayPercent(cpu_usage.cpuData(si.getHardware().getProcessor())));


            for (int j = 0; j < usageCollection[1].getItemCount(0); ++j)
            {
                System.out.print(usageCollection[1].getX(0,j) + " ");
            }
            System.out.println();

    }
}





