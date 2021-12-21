package Control;

import Ultils.StringUltils;
import oshi.util.ExecutingCommand;

import java.io.*;
import java.util.List;

public class SendCommand {

    public static void SendCommand_ShutDown() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec("shutdown -s -t 0");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static void SenCommand_LogOff() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec("shutdown -l");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static String SendCommand(String command) {
        StringBuilder result = new StringBuilder();
        try {
            List<String> cmdRet = ExecutingCommand.runNative(command);
            for(String s: cmdRet) {
                result.append(s).append("@@@&&&");
            }
        } catch (Exception e) {
            System.out.println("Failed to execute a command");
        }
        command = command.replace("@", "").replace("&", "").replace("%", "");
        if (StringUltils.NormalizeSpaces(result.toString()).isEmpty()||StringUltils.NormalizeSpaces(result.toString()).isBlank()) {
            return String.format("'%s' is not recognized as an internal or external command, operable program or batch file", command);
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(SendCommand("taskkill /f /im brave.exe"));
    }
}
