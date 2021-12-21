package Hardware;

import Ultils.StringUltils;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

public class CPU {
    String cpuName;
    String cpuPacket;
    String cpuCore;
    String logicalCpu;
    String identifier;
    String processorID;
    String microarchitecture;

    public CPU(String cpuName, String cpuPacket, String cpuCore, String logicalCpu, String identifier, String processorID, String microarchitecture) {
        this.cpuName = StringUltils.NormalizeSpaces(cpuName);
        this.cpuPacket = StringUltils.NormalizeSpaces(cpuPacket);
        this.cpuCore = StringUltils.NormalizeSpaces(cpuCore);
        this.logicalCpu = StringUltils.NormalizeSpaces(logicalCpu);
        this.identifier = StringUltils.NormalizeSpaces(identifier);
        this.processorID = StringUltils.NormalizeSpaces(processorID);
        this.microarchitecture = StringUltils.NormalizeSpaces(microarchitecture);
    }

    public String getCpuName() {
        return cpuName;
    }

    public void setCpuName(String cpuName) {
        this.cpuName = cpuName;
    }

    public String getCpuPacket() {
        return cpuPacket;
    }

    public void setCpuPacket(String cpuPacket) {
        this.cpuPacket = cpuPacket;
    }

    public String getCpuCore() {
        return cpuCore;
    }

    public void setCpuCore(String cpuCore) {
        this.cpuCore = cpuCore;
    }

    public String getLogicalCpu() {
        return logicalCpu;
    }

    public void setLogicalCpu(String logicalCpu) {
        this.logicalCpu = logicalCpu;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getProcessorID() {
        return processorID;
    }

    public void setProcessorID(String processorID) {
        this.processorID = processorID;
    }

    public String getMicroarchitecture() {
        return microarchitecture;
    }

    public void setMicroarchitecture(String microarchitecture) {
        this.microarchitecture = microarchitecture;
    }

    @Override
    public String toString() {

        return "CPU Name: " + cpuName + "@@@&&&" +
                "CPU Packet: " + cpuPacket + "@@@&&&" +
                "Core: " + cpuCore + "@@@&&&" +
                "Logical CPU: " + logicalCpu + "@@@&&&" +
                identifier + "@@@&&&" +
                processorID + "@@@&&&" +
                microarchitecture + "@@@&&&";
    }

    public static String getCpuInformation() {
        SystemInfo si = new SystemInfo();
        CentralProcessor proc = si.getHardware().getProcessor();
        String temp = proc.toString();
        String[] templist = temp.split("\n");
        return new CPU(templist[0], templist[1], templist[2], templist[3], templist[4], templist[5], templist[6]).toString();
    }

    public static void main(String[] args) {
        System.out.println(getCpuInformation());
    }

}
