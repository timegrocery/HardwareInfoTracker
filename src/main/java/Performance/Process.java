package Performance;

import oshi.SystemInfo;
import oshi.software.os.OSProcess;

public class Process {
    public static String getProcessList() {
        SystemInfo si = new SystemInfo();
        float totalMem = si.getHardware().getMemory().getTotal();
        int cpuCount = si.getHardware().getProcessor().getLogicalProcessorCount();
        StringBuilder result = new StringBuilder();
        result.append("Process name_____PID_____PPID_____Thread count_____Memory (%)").append("@@@&&&");
        for (OSProcess osProcess : si.getOperatingSystem().getProcesses()) {
            String process = String.format("%s_____%d_____%d_____%d_____%.3f",
                    osProcess.getName(), osProcess.getProcessID(), osProcess.getParentProcessID(), osProcess.getThreadCount(), osProcess.getResidentSetSize()/totalMem*100.0);
            result.append(process).append("@@@&&&");
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(getProcessList());
    }
}
