package Ultils;

public class OSUtils {
    public final static boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    public final static boolean isMac = System.getProperty("os.name").toLowerCase().startsWith("mac os");


}
