package Performance;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.VirtualMemory;

public class MemoryUsage {
    public static Double GetPhysicMemAvailable(SystemInfo si){
        GlobalMemory globalMemory= si.getHardware().getMemory();
        return (double)globalMemory.getAvailable();
    }
    public static Double GetPhysicMemUsed(SystemInfo si){
        GlobalMemory globalMemory= si.getHardware().getMemory();
        return (double)globalMemory.getTotal()-globalMemory.getAvailable();
    }
    public static Double GetVirtualMemUsed(SystemInfo si){
        VirtualMemory virtualMemory = si.getHardware().getMemory().getVirtualMemory();
        return (double)virtualMemory.getSwapUsed();
    }
    public static Double GetVirtualMemAvailable(SystemInfo si){
        VirtualMemory virtualMemory = si.getHardware().getMemory().getVirtualMemory();
        return (double) virtualMemory.getSwapTotal() - virtualMemory.getSwapUsed();
    }

    public static String getMemory(){
        SystemInfo si = new SystemInfo();
        Double physicalMemUsed = GetPhysicMemUsed(si)/1024/1024;
        Double physicalMemAvailable = GetPhysicMemAvailable(si)/1024/1024;
        Double virtualMemUsed = GetVirtualMemUsed(si)/1024/1024;
        Double virtualMemAvailable = GetVirtualMemAvailable(si)/1024/1024;

        Double totalPhysicalMem = physicalMemAvailable + physicalMemUsed;
        Double totalVirtualMem = virtualMemAvailable + virtualMemUsed;

        StringBuilder result = new StringBuilder();

        result.append(String.format("Physical memory used: %.1fMb %.1f%%", physicalMemUsed, physicalMemUsed/totalPhysicalMem*100)).append("@@@&&&");
        result.append(String.format("Physical memory available: %.1fMb %.1f%%", physicalMemAvailable, physicalMemAvailable/totalPhysicalMem*100)).append("@@@&&&");
        result.append(String.format("Total physical memory: %.0fMb", totalPhysicalMem)).append("@@@&&&");
        result.append(String.format("Virtual memory used: %.1fMb %.1f%%", virtualMemUsed, virtualMemUsed/totalVirtualMem*100)).append("@@@&&&");
        result.append(String.format("Virtual memory available: %.1fMb %.1f%%", virtualMemAvailable, virtualMemAvailable/totalVirtualMem*100)).append("@@@&&&");
        result.append(String.format("Total virtual memory: %.0fMb", totalVirtualMem)).append("@@@&&&");
        return result.toString();
    }

    public static void main(String[] args) {

        System.out.println(MemoryUsage.getMemory());
    }
}
