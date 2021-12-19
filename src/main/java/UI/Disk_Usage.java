package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;

import oshi.PlatformEnum;
import oshi.SystemInfo;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.util.FormatUtil;

public class Disk_Usage extends OshiJPanel {
    private static final long serialVersionUID = 1L;

    private static final String USED = "Used";
    private static final String AVAILABLE = "Available";
    public static final int GUI_WIDTH = 800;
    public static final int GUI_HEIGHT = 500;

    public static final int REFRESH_RATE = 15_000;

    public Disk_Usage(SystemInfo si) {
        super();
    }

    public void init(FileSystem fs, List<OSFileStore> fileStores) {
        @SuppressWarnings("unchecked")
        DefaultPieDataset[] fsData = new DefaultPieDataset[fileStores.size()];
        JFreeChart[] fsCharts = new JFreeChart[fsData.length];

        JPanel fsPanel = new JPanel();
        fsPanel.setLayout(new GridBagLayout());
        GridBagConstraints fsConstraints = new GridBagConstraints();
        fsConstraints.weightx = 1d;
        fsConstraints.weighty = 1d;
        fsConstraints.fill = GridBagConstraints.BOTH;

        int modBase = (int) (fileStores.size() * (GUI_HEIGHT + GUI_WIDTH)
                / (GUI_WIDTH * Math.sqrt(fileStores.size())));
        for (int i = 0; i < fileStores.size(); i++) {
            fsData[i] = new DefaultPieDataset();
            fsCharts[i] = ChartFactory.createPieChart(null, fsData[i], true, true, false);
            configurePlot(fsCharts[i]);
            fsConstraints.gridx = i % modBase;
            fsConstraints.gridy = i / modBase;
            fsPanel.add(new ChartPanel(fsCharts[i]), fsConstraints);
        }
        updateDatasets(fs, fsData, fsCharts);

        add(fsPanel, BorderLayout.CENTER);

        Timer timer = new Timer(REFRESH_RATE, e -> {
            if (!updateDatasets(fs, fsData, fsCharts)) {
                ((Timer) e.getSource()).stop();
                fsPanel.removeAll();
                init(fs,fileStores);
                fsPanel.revalidate();
                fsPanel.repaint();
            }
        });
        timer.start();
    }

    private static boolean updateDatasets(FileSystem fs, DefaultPieDataset[] fsData, JFreeChart[] fsCharts) {
        List<OSFileStore> fileStores = fs.getFileStores();
        if (fileStores.size() != fsData.length) {
            return false;
        }
        int i = 0;
        for (OSFileStore store : fileStores) {
            fsCharts[i].setTitle(store.getName());
            List<TextTitle> subtitles = new ArrayList<>();
            if (SystemInfo.getCurrentPlatform().equals(PlatformEnum.WINDOWS)) {
                subtitles.add(new TextTitle(store.getLabel()));
            }
            long usable = store.getUsableSpace();
            long total = store.getTotalSpace();
            subtitles.add(new TextTitle(
                    "Available: " + FormatUtil.formatBytes(usable) + "/" + FormatUtil.formatBytes(total)));
            fsCharts[i].setSubtitles(subtitles);
            fsData[i].setValue(USED, (double) total - usable);
            fsData[i].setValue(AVAILABLE, usable);
            i++;
        }
        return true;
    }

    private static void configurePlot(JFreeChart chart) {
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

    public static void main(String[] args)
    {
        SystemInfo si = new SystemInfo();
        FileSystem fs = si.getOperatingSystem().getFileSystem();

        List<OSFileStore> fileStores = fs.getFileStores();

        for (OSFileStore osFileStore : fileStores)
        {
            System.out.println(osFileStore.toString());
        }
    }

}
