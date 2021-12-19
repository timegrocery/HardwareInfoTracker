package Ultils;

public enum MessageType {
    HARDWARE_INFO(1),
    PERFORMANCE_TRACK(2),
    STORAGE_TRACK(3),
    END_PROCESS(4),
    DESKTOP(5),
    KEYLOGGER(6),
    CLIPBOARD(7),
    LOGOFF(8),
    SHUTDOWN(9),
    COMMAND(10),
    ALTF4(11),
    MESSAGE_BOX(12),
    GUI_TEXT(13);

    private int id;

    MessageType(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }
}
