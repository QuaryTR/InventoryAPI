package tr.quary.structure.inventory.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import tr.quary.structure.inventory.SmartInventory;

import java.util.Set;

public class DisableListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        SmartInventory.remove(player);
    }


    @EventHandler
    public void onPluginDisable(PluginDisableEvent event){

        Plugin plugin = event.getPlugin();

        if (!plugin.getName().equals("InventoryAPI")) return;
        Set<Player> players = SmartInventory.keySet();

        players.forEach(player -> {
            SmartInventory inventory = SmartInventory.get(player);
            if (inventory == null) return;
            inventory.close(player);
        });

        SmartInventory.clear();
    }




}
