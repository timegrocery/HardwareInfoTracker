package Ultils;

public enum MessageType {
    HARDWARE_INFO(1),
    PERFORMANCE_TRACK(2),
    END_PROCESS(3),
    DESKTOP(4),
    KEYLOGGER(5),
    CLIPBOARD(6),
    LOGOFF(7),
    SHUTDOWN(8),
    COMMAND(9),
    ALTF4(10),
    MESSAGE_BOX(11),
    GUI_TEXT(12);

    private int id;

    MessageType(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }
}
