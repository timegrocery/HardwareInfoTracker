package Hardware.GPU;

import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPUInfo {
    public static ArrayList<GPU> GetGPUInformation() {
        SystemInfo si = new SystemInfo();
        ArrayList<GPU> result = new ArrayList<>();
        List<GraphicsCard> vga_list = si.getHardware().getGraphicsCards();

        for (GraphicsCard vga : vga_list) {
            String stringVga = vga.toString();
            Pattern pattern = Pattern.compile("name=(.*?), deviceId=(.*?), vendor=(.*?), vRam=(.*?),");
            Matcher matcher = pattern.matcher(stringVga);
            if (matcher.find()) {
                GPU gpu = new GPU(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4));
                result.add(gpu);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        for (GPU g : GetGPUInformation()) {
            System.out.println(g.toString());
        }
    }
}
