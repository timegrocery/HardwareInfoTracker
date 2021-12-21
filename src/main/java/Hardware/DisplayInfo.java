package Hardware;

import oshi.SystemInfo;
import oshi.hardware.Display;
import oshi.util.EdidUtil;

import java.util.List;

public class DisplayInfo {
    public static List<Display> GetDisplays() {
        SystemInfo si = new SystemInfo();
        List<Display> displays = si.getHardware().getDisplays();
        if (displays.isEmpty()) {
            return null;
        } else {
            return displays;
        }
    }

    public static String GetDisplayInformation(Display display, int id) {
        if (display == null) {
            return "No display information";
        } else {
            String result;
            byte[] edid = display.getEdid();
            byte[][] desc = EdidUtil.getDescriptors(edid);
            String name = String.valueOf(id);
            for (byte[] b : desc) {
                if (EdidUtil.getDescriptorType(b) == 0xfc) {
                    name = EdidUtil.getDescriptorText(b);
                }
            }
            int hSize = EdidUtil.getHcm(edid);
            int vSize = EdidUtil.getVcm(edid);
            String manufacturerID = EdidUtil.getManufacturerID(edid);
            String productID = EdidUtil.getProductID(edid);
            result = String.format("Display %s: %dcm x %d cm@@@&&&Manufacture ID: %s@@@&&&Product ID: %s@@@&&&",
                    name,
                    hSize,
                    vSize,
                    manufacturerID,
                    productID);
            return result;
        }
    }

    public static String GetAllDisplayInformation() {
        List<Display> displays = GetDisplays();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        if (displays == null) {
            return "No display information";
        }
        for (Display display : displays) {
            sb.append(GetDisplayInformation(display, i));
            if (i++ > 0) {
                sb.append("@@@&&&");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(GetAllDisplayInformation());
    }
}

