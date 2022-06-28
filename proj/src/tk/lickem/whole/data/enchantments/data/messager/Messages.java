package tk.lickem.whole.data.enchantments.data.messager;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;

@Getter
@AllArgsConstructor
public enum Messages {

    DANK("This is such a dank message!"),
    WOAH("Woah this is so cool!"),
    NO_WAY("NO way you got this enchant!"),
    COOL_BEANS("You must be pretty annoyed to get this cool bean!"),
    GAY("You're gay and that's okay!");

    private final String message;

    public static String random() {
        return values()[new Random().nextInt(values().length)].getMessage();
    }
}
