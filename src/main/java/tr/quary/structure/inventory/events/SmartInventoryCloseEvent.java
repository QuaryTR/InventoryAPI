package tr.quary.structure.inventory.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;
import tr.quary.structure.inventory.SmartInventory;

public class SmartInventoryCloseEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final SmartInventory smartInventory;
    private final InventoryCloseEvent event;
    private boolean cancelled = false;

    public SmartInventoryCloseEvent(Player player, SmartInventory smartInventory, InventoryCloseEvent event) {
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

    public InventoryCloseEvent getEvent() {
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
