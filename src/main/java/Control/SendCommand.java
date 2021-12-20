package Control;

import java.io.*;

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
            Process proc = runtime.exec("shutdown -h -t 0");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static String SendCommand(String command) {
        Runtime rt = Runtime.getRuntime();
        String[] commands = {command};
        StringBuilder result = new StringBuilder();
        try {
            Process proc = rt.exec(commands);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                result.append(s).append("\n");
            }

            while ((s = stdError.readLine()) != null) {
                result.append(s).append("\n");
            }

        } catch (Exception e) {
            System.out.println("Failed to execute a command");
            return "Unknown command";
        }
        return result.toString();
    }

    public static void main(String[] args) {

        System.out.println(SendCommand("test"));
    }
}
