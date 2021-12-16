package Hardware;

import oshi.SystemInfo;
import oshi.hardware.LogicalVolumeGroup;

import java.util.List;
import java.util.Set;

public class Disk {
    public static String GetDiskInfo() {
        SystemInfo si = new SystemInfo();
        StringBuilder result = new StringBuilder();
        List<LogicalVolumeGroup> logicalVolumeGroupList = si.getHardware().getLogicalVolumeGroups();
        for (LogicalVolumeGroup logicalVolumeGroup: logicalVolumeGroupList) {
            Set<String> physicalVolumes = logicalVolumeGroup.getPhysicalVolumes();
            for (String s : physicalVolumes) {
                result.append(s);
            }
            //result.append(logicalVolumeGroup.getName());
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(GetDiskInfo());
    }
}
