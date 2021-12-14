package Hardware.GPU;

import Ultils.Ultils;

public class GPU {
    String name;
    String deviceID;
    String vendor;
    String vRam;

    public GPU(String name, String deviceID, String vendor, String vRam) {
        this.name = Ultils.NormalizeSpaces(name);
        this.deviceID = Ultils.NormalizeSpaces(deviceID);
        this.vendor = Ultils.NormalizeSpaces(vendor);
        this.vRam = Ultils.NormalizeSpaces(vRam);
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
}
