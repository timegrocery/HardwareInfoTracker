package Ultils;

import org.jetbrains.annotations.NotNull;

public class Ultils {
    public static @NotNull
    String NormalizeSpaces(String text){
        return text.trim().replaceAll(" +", " ");
    }
}
