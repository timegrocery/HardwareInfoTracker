package Hardware;

import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

public class OS {
    public static String GetOSVersionInfo() {
        SystemInfo si = new SystemInfo();
        OperatingSystem operatingSystem = si.getOperatingSystem();
        int bitness = operatingSystem.getBitness();
        OperatingSystem.OSVersionInfo osVersionInfo = operatingSystem.getVersionInfo();
        StringBuilder result = new StringBuilder();

        String version, codeName, buildNumber;

        version = osVersionInfo.getVersion();
        if (version != null && !version.isEmpty() && !version.isBlank()) {
            result.append(String.format("Windows %s ", version));
        }
        if (bitness != 0) {
            result.append(String.format("%d bit ", bitness));
        }
        codeName = osVersionInfo.getCodeName();
        if (codeName != null && !codeName.isBlank() && !codeName.isEmpty()) {
            result.append(String.format("Code %s ", codeName));
        }

        buildNumber = osVersionInfo.getBuildNumber();
        if (buildNumber != null && !buildNumber.isBlank() && !buildNumber.isEmpty()) {
            result.append(String.format("Build %s ", buildNumber));
        }

        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(GetOSVersionInfo());
    }
}
