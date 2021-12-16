package Hardware;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.PhysicalMemory;

import java.util.List;

public class RAM {
    public static String GetPhysicalMemory() {
        SystemInfo si = new SystemInfo();
        GlobalMemory memory = si.getHardware().getMemory();
        List<PhysicalMemory> physicalMemoryList = memory.getPhysicalMemory();
        if (physicalMemoryList == null || physicalMemoryList.isEmpty()) {
            return "No physical memory found"; // just make sure
        }
        StringBuilder result = new StringBuilder();
        long totalCapacity = 0;
        for(PhysicalMemory physicalMemory: physicalMemoryList) {
            String bankLabel = physicalMemory.getBankLabel();
            long capacity = physicalMemory.getCapacity();
            capacity /= 1048576; // capacity in Mb;
            totalCapacity += capacity;
            long clockSpeed = physicalMemory.getClockSpeed();
            clockSpeed /= 1_000_000; // clock speed in Hz;
            String manufacturer = physicalMemory.getManufacturer();
            String memoryType = physicalMemory.getMemoryType();
            String temp = String.format("Bank Label: %s\nCapacity: %dMb\nClock Speed: %dHz\nManufacturer: %s\nMemory Type: %s\n", bankLabel, capacity, clockSpeed, manufacturer, memoryType);
            result.append(temp);
        }
        String totalCapacityString = String.format("Total Capacity: %dMb", totalCapacity);
        result.append(totalCapacityString);
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(GetPhysicalMemory());
    }
}
