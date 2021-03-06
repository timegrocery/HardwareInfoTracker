package Hardware;

import oshi.PlatformEnum;
import oshi.SystemInfo;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.util.FormatUtil;

import java.util.List;

public class Disk {
    public static String GetDiskInfo() {
        SystemInfo si = new SystemInfo();
        StringBuilder result = new StringBuilder();
        FileSystem fileSystem = si.getOperatingSystem().getFileSystem();
        List<OSFileStore> fileStores = fileSystem.getFileStores();
        for (OSFileStore fileStore: fileStores) {
            String name = String.format(fileStore.getName());
            result.append(name);
            if (SystemInfo.getCurrentPlatform().equals(PlatformEnum.WINDOWS)) {
                result.append(" " + fileStore.getLabel());
            }
            long usable = fileStore.getUsableSpace();
            long total = fileStore.getTotalSpace();
            result.append("@@@&&&Available: " + FormatUtil.formatBytes(usable) + "/" + FormatUtil.formatBytes(total) + "@@@&&&");
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(GetDiskInfo());
    }
}
