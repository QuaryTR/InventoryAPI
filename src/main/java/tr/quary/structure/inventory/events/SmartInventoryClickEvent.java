package tr.quary.structure.inventory.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import tr.quary.structure.inventory.SmartInventory;
import tr.quary.structure.pagination.utils.SlotPos;

public class SmartInventoryClickEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final SmartInventory smartInventory;
    private final InventoryClickEvent event;
    private final SlotPos slotPos;
    private boolean cancelled = false;

    public SmartInventoryClickEvent(Player player, SmartInventory smartInventory, InventoryClickEvent event,SlotPos slotPos) {
        this.player = player;
        this.event = event;
        this.smartInventory = smartInventory;
        this.slotPos = slotPos;
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

    public InventoryClickEvent getClickEvent() {
        return this.event;
    }

    public SlotPos getSlotPos(){return slotPos;}

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
