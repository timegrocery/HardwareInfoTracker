package Test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

import javax.swing.*;

import com.sun.jna.platform.mac.SystemB;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.UIUtils;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Dynamic_chart extends  OshiJPanel {

    private static final long serialVersionUID = 1L;

    private long[] oldTicks;
    private long[][] oldProcTicks;

    public Dynamic_chart(SystemInfo si) {
        super();
        CentralProcessor cpu = si.getHardware().getProcessor();
        oldTicks = new long[CentralProcessor.TickType.values().length];
        oldProcTicks = new long[cpu.getLogicalProcessorCount()][CentralProcessor.TickType.values().length];
        init(cpu);
    }

    private void init(CentralProcessor processor) {

        GridBagConstraints sysConstraints = new GridBagConstraints();
        sysConstraints.weightx = 1d;
        sysConstraints.weighty = 1d;
        sysConstraints.fill = GridBagConstraints.BOTH;

        GridBagConstraints procConstraints = (GridBagConstraints) sysConstraints.clone();
        procConstraints.gridx = 1;

        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        DynamicTimeSeriesCollection sysData = new DynamicTimeSeriesCollection(1, 60, new Second());
        sysData.setTimeBase(new Second(date));
        sysData.addSeries(floatArrayPercent(cpuData(processor)), 0, "All cpus");
        JFreeChart systemCpu = ChartFactory.createTimeSeriesChart("System CPU Usage", "Time", "% CPU", sysData, true,
                true, false);

        double[] procUsage = procData(processor);
        DynamicTimeSeriesCollection procData = new DynamicTimeSeriesCollection(procUsage.length, 60, new Second());
        procData.setTimeBase(new Second(date));
        for (int i = 0; i < procUsage.length; i++) {
            procData.addSeries(floatArrayPercent(procUsage[i]), i, "cpu" + i);
        }
        JFreeChart procCpu = ChartFactory.createTimeSeriesChart("Processor CPU Usage", "Time", "% CPU", procData, true,
                true, false);

        JPanel cpuPanel = new JPanel();
        cpuPanel.setLayout(new GridBagLayout());
        cpuPanel.add(new ChartPanel(systemCpu), sysConstraints);
        cpuPanel.add(new ChartPanel(procCpu), procConstraints);

        add(cpuPanel, BorderLayout.CENTER);

        Timer timer = new Timer(Config.REFRESH_FAST, e -> {
            sysData.advanceTime();
            sysData.appendData(floatArrayPercent(cpuData(processor)));
            procData.advanceTime();
            int newest = procData.getNewestIndex();
            double[] procUsageData = procData(processor);
            for (int i = 0; i < procUsageData.length; i++) {
                procData.addValue(i, newest, (float) (100 * procUsageData[i]));
            }
        });
        timer.start();
    }

    private static float[] floatArrayPercent(double d) {
        float[] f = new float[1];
        f[0] = (float) (100d * d);
        return f;
    }

    private double cpuData(CentralProcessor proc) {
        double d = proc.getSystemCpuLoadBetweenTicks(oldTicks);
        oldTicks = proc.getSystemCpuLoadTicks();
        return d;
    }

    private double[] procData(CentralProcessor proc) {
        double[] p = proc.getProcessorCpuLoadBetweenTicks(oldProcTicks);
        oldProcTicks = proc.getProcessorCpuLoadTicks();
        return p;
    }

    public static  void main(String[] args)
    {
        JFrame frame = new JFrame();
        SystemInfo si = new SystemInfo();
        frame.add(new Dynamic_chart(si));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
