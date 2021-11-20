import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

public class HW_info {
    public static String GetCPU(SystemInfo si)
    {
        StringBuilder sb = new StringBuilder();
        CentralProcessor proc = si.getHardware().getProcessor();
        sb.append(proc.toString());
        return sb.toString();
    }

    public static  void main(String[] args)
    {
        SystemInfo si = new SystemInfo();
        String s = GetCPU(si);
        System.out.println(s);
    }
}
