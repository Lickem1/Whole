package tk.lickem.whole.data.backpack;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BackPackSize {

    NINE(9),
    EIGHTTEEN(18),
    TWENTY_SEVEN(27),
    THIRTY_SIX(36);

    private final int numerator;
}
