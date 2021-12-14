package CPU;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GraphicsCard;
import oshi.util.ExecutingCommand;

import java.util.List;


public class HW_Info {

    public static String GetCPU(SystemInfo si)
    {
        StringBuilder sb = new StringBuilder();
        CentralProcessor proc = si.getHardware().getProcessor();
        sb.append(proc.toString());
        return sb.toString();
    }

    public static String GetGPU(SystemInfo si) {
        StringBuilder sb = new StringBuilder();
        List<GraphicsCard> vgas = si.getHardware().getGraphicsCards();

        for (GraphicsCard vga : vgas)
        {
            sb.append(vga);
            sb.append("\n");
        }
        return  sb.toString();
    }

    public static  void main(String[] args) {
        SystemInfo si = new SystemInfo();
        String s = GetCPU(si);
        System.out.println(GetGPU(si));
        System.out.println(s);
    }
}