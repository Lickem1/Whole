package tk.lickem.whole;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import tk.lickem.whole.manager.DynamicManger;

@Getter
public class Whole extends JavaPlugin {

    @Getter private static Whole whole;
    private final Gson gson = new GsonBuilder().create();
    private DynamicManger dynamicManger;

    public void onEnable() {
        whole = this;
        this.dynamicManger = new DynamicManger();
        System.out.println("Enabled Whole 1.0.0");


        DynamicManger.init();

    }

    public void onDisable() {
        whole = null;
    }
}
