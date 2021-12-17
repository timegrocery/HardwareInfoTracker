package Control;

import oshi.util.ExecutingCommand;

import java.util.List;

public class SendCommand {
    public static String SendCommand(String command) {
        List<String> cmdRet = ExecutingCommand.runNative(command);
        StringBuilder result = new StringBuilder();
        for(String s : cmdRet) {
            result.append(String.format("%s\n",s));
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(SendCommand("tasklist"));
    }
}
