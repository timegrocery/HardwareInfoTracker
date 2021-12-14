package Hardware.CPU;

import Ultils.Ultils;

public class CPU {
    String cpuName;
    String cpuPacket;
    String cpuCore;
    String logicalCpu;
    String identifier;
    String processorID;
    String microarchitecture;

    public CPU(String cpuName, String cpuPacket, String cpuCore, String logicalCpu, String identifier, String processorID, String microarchitecture) {
        this.cpuName = Ultils.NormalizeSpaces(cpuName);
        this.cpuPacket = Ultils.NormalizeSpaces(cpuPacket);
        this.cpuCore = Ultils.NormalizeSpaces(cpuCore);
        this.logicalCpu = Ultils.NormalizeSpaces(logicalCpu);
        this.identifier = Ultils.NormalizeSpaces(identifier);
        this.processorID = Ultils.NormalizeSpaces(processorID);
        this.microarchitecture = Ultils.NormalizeSpaces(microarchitecture);
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

        return cpuName + '\n' +
                cpuPacket + '\n' +
                cpuCore + '\n' +
                logicalCpu + '\n' +
                identifier + '\n' +
                processorID + '\n' +
                microarchitecture + "\n";
    }

}
