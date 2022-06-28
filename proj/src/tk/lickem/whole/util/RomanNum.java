package tk.lickem.whole.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RomanNum {

    I(1, "I"),
    II(2, "II"),
    III(3, "III"),
    IV(4, "IV"),
    V(5, "V");

    private int number;
    private String display;

    public static String convertTo(int num) {
        for(RomanNum val : values()) if(val.number == num) return val.display;
        return "ERR";
    }
}
