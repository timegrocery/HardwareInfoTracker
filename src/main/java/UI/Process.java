package UI;

import oshi.PlatformEnum;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Process extends OshiJPanel {
    private static final long serialVersionUID = 1L;
    public static final int REFRESH_RATE = 5000;
    private static final String PROCESSES = "Processes";
    private static final String[] COLUMNS = { "PID", "PPID", "Threads", "% CPU", "Cumulative", "VSZ", "RSS", "% Memory",
            "Process Name" };
    private static final double[] COLUMN_WIDTH_PERCENT = { 0.07, 0.07, 0.07, 0.07, 0.09, 0.1, 0.1, 0.08, 0.35 };

    private transient Map<Integer, OSProcess> priorSnapshotMap = new HashMap<>();

    private transient ButtonGroup cpuOption = new ButtonGroup();
    private transient JRadioButton perProc = new JRadioButton("of one Processor");
    private transient JRadioButton perSystem = new JRadioButton("of System");

    private transient ButtonGroup sortOption = new ButtonGroup();
    private transient JRadioButton cpuButton = new JRadioButton("CPU %");
    private transient JRadioButton cumulativeCpuButton = new JRadioButton("Cumulative CPU");
    private transient JRadioButton memButton = new JRadioButton("Memory %");

    public Process(SystemInfo si) {
        super();
        init(si);
    }

    private void init(SystemInfo si) {
        OperatingSystem os = si.getOperatingSystem();
        JLabel procLabel = new JLabel(PROCESSES);
        add(procLabel, BorderLayout.NORTH);

        JPanel settings = new JPanel();

        JLabel cpuChoice = new JLabel("          CPU %:");
        settings.add(cpuChoice);
        cpuOption.add(perProc);
        settings.add(perProc);
        cpuOption.add(perSystem);
        settings.add(perSystem);
        if (SystemInfo.getCurrentPlatform().equals(PlatformEnum.WINDOWS)) {
            perSystem.setSelected(true);
        } else {
            perProc.setSelected(true);
        }

        JLabel sortChoice = new JLabel("          Sort by:");
        settings.add(sortChoice);
        sortOption.add(cpuButton);
        settings.add(cpuButton);
        sortOption.add(cumulativeCpuButton);
        settings.add(cumulativeCpuButton);
        sortOption.add(memButton);
        settings.add(memButton);
        cpuButton.setSelected(true);

        TableModel model = new DefaultTableModel(parseProcesses(os.getProcesses(null, null, 0), si), COLUMNS);
        JTable procTable = new JTable(model);
        JScrollPane scrollV = new JScrollPane(procTable);
        scrollV.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        resizeColumns(procTable.getColumnModel());

        add(scrollV, BorderLayout.CENTER);
        add(settings, BorderLayout.SOUTH);

        Timer timer = new Timer(REFRESH_RATE, e -> {
            DefaultTableModel tableModel = (DefaultTableModel) procTable.getModel();
            Object[][] newData = parseProcesses(os.getProcesses(null, null, 0), si);
            int rowCount = tableModel.getRowCount();
            for (int row = 0; row < newData.length; row++) {
                if (row < rowCount) {
                    // Overwrite row
                    for (int col = 0; col < newData[row].length; col++) {
                        tableModel.setValueAt(newData[row][col], row, col);
                    }
                } else {
                    // Add row
                    tableModel.addRow(newData[row]);
                }
            }
            // Delete any extra rows
            for (int row = rowCount - 1; row >= newData.length; row--) {
                tableModel.removeRow(row);
            }
        });
        timer.start();
    }

    private Object[][] parseProcesses(java.util.List<OSProcess> list, SystemInfo si) {
        long totalMem = si.getHardware().getMemory().getTotal();
        int cpuCount = si.getHardware().getProcessor().getLogicalProcessorCount();
        // Build a map with a value for each process to control the sort
        Map<OSProcess, Double> processSortValueMap = new HashMap<>();
        for (OSProcess p : list) {
            int pid = p.getProcessID();
            // Ignore the Idle process on Windows
            if (pid > 0 || !SystemInfo.getCurrentPlatform().equals(PlatformEnum.WINDOWS)) {
                // Set up for appropriate sort
                if (cpuButton.isSelected()) {
                    processSortValueMap.put(p, p.getProcessCpuLoadBetweenTicks(priorSnapshotMap.get(pid)));
                } else if (cumulativeCpuButton.isSelected()) {
                    processSortValueMap.put(p, p.getProcessCpuLoadCumulative());
                } else {
                    processSortValueMap.put(p, (double) p.getResidentSetSize());
                }
            }
        }
        // Now sort the list by the values
        java.util.List<Map.Entry<OSProcess, Double>> procList = new ArrayList<>(processSortValueMap.entrySet());
        procList.sort(Map.Entry.comparingByValue());
        // Insert into array in reverse order (lowest sort value last)
        int i = procList.size();
        Object[][] procArr = new Object[i][COLUMNS.length];
        // These are in descending CPU order
        for (Map.Entry<OSProcess, Double> e : procList) {
            OSProcess p = e.getKey();
            // Matches order of COLUMNS field
            i--;
            int pid = p.getProcessID();
            procArr[i][0] = pid;
            procArr[i][1] = p.getParentProcessID();
            procArr[i][2] = p.getThreadCount();
            if (perProc.isSelected()) {
                procArr[i][3] = String.format("%.1f",
                        100d * p.getProcessCpuLoadBetweenTicks(priorSnapshotMap.get(pid)) / cpuCount);
                procArr[i][4] = String.format("%.1f", 100d * p.getProcessCpuLoadCumulative() / cpuCount);
            } else {
                procArr[i][3] = String.format("%.1f",
                        100d * p.getProcessCpuLoadBetweenTicks(priorSnapshotMap.get(pid)));
                procArr[i][4] = String.format("%.1f", 100d * p.getProcessCpuLoadCumulative());
            }
            procArr[i][5] = FormatUtil.formatBytes(p.getVirtualSize());
            procArr[i][6] = FormatUtil.formatBytes(p.getResidentSetSize());
            procArr[i][7] = String.format("%.1f", 100d * p.getResidentSetSize() / totalMem);
            procArr[i][8] = p.getName();
        }
        // Re-populate snapshot map
        priorSnapshotMap.clear();
        for (OSProcess p : list) {
            priorSnapshotMap.put(p.getProcessID(), p);
        }
        return procArr;
    }

    private static void resizeColumns(TableColumnModel tableColumnModel) {
        TableColumn column;
        int tW = tableColumnModel.getTotalColumnWidth();
        int cantCols = tableColumnModel.getColumnCount();
        for (int i = 0; i < cantCols; i++) {
            column = tableColumnModel.getColumn(i);
            int pWidth = (int) Math.round(COLUMN_WIDTH_PERCENT[i] * tW);
            column.setPreferredWidth(pWidth);
        }
    }

    public  static  void main(String[] args)
    {
        JFrame frame = new JFrame();
        SystemInfo si = new SystemInfo();
        Process process = new Process(si);

        frame.add(process);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
