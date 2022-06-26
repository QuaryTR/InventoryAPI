package tr.quary.structure;

import org.bukkit.plugin.java.JavaPlugin;
import tr.quary.structure.inventory.listeners.DisableListener;
import tr.quary.structure.inventory.listeners.InventoryListener;


public class Starter extends JavaPlugin{

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new InventoryListener(),this);
        getServer().getPluginManager().registerEvents(new DisableListener(),this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
