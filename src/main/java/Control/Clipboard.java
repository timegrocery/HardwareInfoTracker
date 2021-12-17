package Control;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


public class Clipboard {
    public static String GetClipboard() {
        try {
            return (String) Toolkit.getDefaultToolkit()
                    .getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }

    public static void main(String[] args) {
        // copy this b4 running: test_1234_%$$#@_你好——？。tiếng việt
        System.out.println(GetClipboard());
    }
}
