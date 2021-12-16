package Ultils;

import org.jetbrains.annotations.NotNull;

public class StringUltils {
    public static @NotNull
    String NormalizeSpaces(String text){
        return text.trim().replaceAll(" +", " ");
    }
}
