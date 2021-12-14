package CPU;

import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.util.EdidUtil;

import java.util.List;


public class HW_Info {

    public static String GetDisplay(SystemInfo si) {
        StringBuilder sb = new StringBuilder();
        List<Display> displays = si.getHardware().getDisplays();
        if (displays.isEmpty()) {
            sb.append("None detected.");
        } else {
            int i = 0;
            for (Display display : displays) {
                byte[] edid = display.getEdid();
                byte[][] desc = EdidUtil.getDescriptors(edid);
                String name = "Display " + i;
                for (byte[] b : desc) {
                    if (EdidUtil.getDescriptorType(b) == 0xfc) {
                        name = EdidUtil.getDescriptorText(b);
                    }
                }
                if (i++ > 0) {
                    sb.append('\n');
                }
                sb.append(name).append(": ");
                int hSize = EdidUtil.getHcm(edid);
                int vSize = EdidUtil.getVcm(edid);
                sb.append(String.format("%d x %d cm (%.1f x %.1f in)", hSize, vSize, hSize / 2.54, vSize / 2.54));
            }
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

    public static  void main(String[] args) {
        SystemInfo si = new SystemInfo();
        GlobalMemory memory = si.getHardware().getMemory();

        System.out.println(GetDisplay(si));
        System.out.println(UpdatePhysTitle(memory));
        System.out.println(UpdateVirtTitle(memory));
    }
}