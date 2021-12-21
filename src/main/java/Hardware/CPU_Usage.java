package Hardware;

import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class CPU_Usage {
    // total usage
    // core
    // dynamic time series


    public CPU_Usage(SystemInfo si)
    {

    }

    public DynamicTimeSeriesCollection[] CreateTimeSeries(CentralProcessor processor) {

        UI.CPU_Usage cpu_usage = new UI.CPU_Usage();
        DynamicTimeSeriesCollection[] usageCollection = new DynamicTimeSeriesCollection[2];
        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        DynamicTimeSeriesCollection sysData = new DynamicTimeSeriesCollection(1, 60, new Second());
        sysData.setTimeBase(new Second(date));
        sysData.addSeries(floatArrayPercent(cpu_usage.cpuData()), 0, "All cpus");

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
            procData.advanceTime();
        });

        return  timer;
    }

    public static float[] floatArrayPercent(double d) {
        float[] f = new float[1];
        f[0] = (float) (100d * d);
        return f;
    }

}
