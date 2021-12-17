package Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import oshi.SystemInfo;

import oshi.hardware.*;

import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.hardware.GlobalMemory;
import oshi.hardware.VirtualMemory;
import oshi.util.EdidUtil;


import java.awt.*;
import java.text.DecimalFormat;


public class HW_Info {

    private static final long serialVersionUID = 1L;

    private static final String PHYSICAL_MEMORY = "Physical Memory";
    private static final String VIRTUAL_MEMORY = "Virtual Memory (Swap)";

    private static final String USED = "Used";
    private static final String AVAILABLE = "Available";

    public static String GetHw(SystemInfo si) {
        StringBuilder sb = new StringBuilder();
        ObjectMapper mapper = new ObjectMapper();
        ComputerSystem computerSystem = si.getHardware().getComputerSystem();
        try {
            sb.append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(computerSystem));
        } catch (JsonProcessingException e) {
            sb.append(e.getMessage());
        }
        return sb.toString();
    }

    public static String UpdatePhysTitle(GlobalMemory memory) {
        return memory.toString();
    }
    public static String UpdateVirtTitle(GlobalMemory memory) {
        return memory.getVirtualMemory().toString();
    }
    //Both of the method only update the TITLE (The black text on the UI)

    private static void UpdateDatasets(GlobalMemory memory, DefaultPieDataset physMemData,
                                       DefaultPieDataset virtMemData) {
        physMemData.setValue(USED, (double) memory.getTotal() - memory.getAvailable());
        physMemData.setValue(AVAILABLE, memory.getAvailable());

        VirtualMemory virtualMemory = memory.getVirtualMemory();
        virtMemData.setValue(USED, virtualMemory.getSwapUsed());
        virtMemData.setValue(AVAILABLE, (double) virtualMemory.getSwapTotal() - virtualMemory.getSwapUsed());
    }

    private static void ConfigurePlot(JFreeChart chart) {
        @SuppressWarnings("unchecked")
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint(USED, Color.red);
        plot.setSectionPaint(AVAILABLE, Color.green);
        plot.setExplodePercent(USED, 0.10);
        plot.setSimpleLabels(true);

        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0}: {1} ({2})",
                new DecimalFormat("0"), new DecimalFormat("0%"));
        plot.setLabelGenerator(labelGenerator);
    }

    public static  void main(String[] args) {
        SystemInfo si = new SystemInfo();
        GlobalMemory memory = si.getHardware().getMemory();

        System.out.println(UpdatePhysTitle(memory));
        System.out.println(UpdateVirtTitle(memory));
        System.out.println(GetHw(si));
    }
}