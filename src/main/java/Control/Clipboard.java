package Control;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class Clipboard {
    public static String GetClipboard() {
        String result = "";
        java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if ( hasTransferableText) {
            try {
                result = (String)contents.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                return "ufe. Cannot get Intellij embedded text";
            }
        }
        return result.replace("\n", "@@@&&&");
    }

    public static void main(String[] args) {
        System.out.println(GetClipboard());
    }
}
