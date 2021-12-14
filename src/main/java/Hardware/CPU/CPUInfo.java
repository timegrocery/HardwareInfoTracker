package Hardware.CPU;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

public class CPUInfo {
    public static CPU getCpuInformation() {
        SystemInfo si = new SystemInfo();
        CentralProcessor proc = si.getHardware().getProcessor();
        String temp = proc.toString();
        String[] templist = temp.split("\n");
        return new CPU(templist[0], templist[1], templist[2], templist[3], templist[4], templist[5], templist[6]);
    }

    public static void main(String[] args) {
        System.out.println(getCpuInformation());
    }
}