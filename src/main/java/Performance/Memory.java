package Performance;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

public class Memory {
    public static long GetTotal() {
        SystemInfo si = new SystemInfo();
        GlobalMemory memory = si.getHardware().getMemory();
        return memory.getTotal()/1048576; // in Mb
    }

    public static long GetAvailable() {
        SystemInfo si = new SystemInfo();
        GlobalMemory memory = si.getHardware().getMemory();
        return memory.getAvailable()/1048576; // in Mb
    }

    public static long GetUsed() {
        return GetTotal() - GetAvailable();
    }

    public static void main(String[] args) {
        System.out.println("Total: " + GetTotal() + "Mb");
        System.out.println("Available: " + GetAvailable() + "Mb");
        System.out.println("Used: " + GetUsed() + "Mb");
    }
}
