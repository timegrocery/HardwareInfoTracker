package Hardware;

import Ultils.StringUltils;
import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPU {
    String name;
    String deviceID;
    String vendor;
    String vRam;

    public GPU(String name, String deviceID, String vendor, String vRam) {
        this.name = StringUltils.NormalizeSpaces(name);
        this.deviceID = StringUltils.NormalizeSpaces(deviceID);
        this.vendor = StringUltils.NormalizeSpaces(vendor);
        this.vRam = StringUltils.NormalizeSpaces(vRam);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getvRam() {
        return vRam;
    }

    public void setvRam(String vRam) {
        this.vRam = vRam;
    }

    @Override
    public String toString() {

        return name + '\n' +
                deviceID + '\n' +
                vendor + '\n' +
                vRam + '\n';
    }

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
