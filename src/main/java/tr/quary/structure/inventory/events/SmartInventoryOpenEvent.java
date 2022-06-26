package tr.quary.structure.inventory.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryOpenEvent;
import tr.quary.structure.inventory.SmartInventory;

public class SmartInventoryOpenEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final SmartInventory smartInventory;
    private final InventoryOpenEvent event;
    private boolean cancelled = false;

    public SmartInventoryOpenEvent(Player player, SmartInventory smartInventory, InventoryOpenEvent event) {
        this.player = player;
        this.smartInventory = smartInventory;
        this.event = event;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return this.player;
    }

    public SmartInventory getInventory() {
        return this.smartInventory;
    }

    public InventoryOpenEvent getEvent() {
        return this.event;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public HandlerList getHandlers() {
        return handlers;
    }


}
