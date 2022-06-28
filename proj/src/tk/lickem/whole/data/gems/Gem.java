package tk.lickem.whole.data.gems;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Gem {

    private final String owner;
    private final int amount;
    private final long creation;
}
