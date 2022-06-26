package tr.quary.structure.inventory.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import tr.quary.structure.inventory.SmartInventory;
import tr.quary.structure.inventory.events.SmartInventoryClickEvent;
import tr.quary.structure.inventory.events.SmartInventoryCloseEvent;
import tr.quary.structure.inventory.events.SmartInventoryOpenEvent;
import tr.quary.structure.inventory.utils.ClickableItem;
import tr.quary.structure.inventory.utils.SmartInventoryType;
import tr.quary.structure.pagination.Pagination;
import tr.quary.structure.pagination.utils.SlotPos;

import java.util.function.Consumer;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event){

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;

        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        SmartInventory inventory = SmartInventory.get(player);
        if (inventory == null) return;

        SlotPos slotPos = SlotPos.of(1,event.getSlot(),-1);
        ClickableItem clickableItem = inventory.getItem(event.getSlot());

        if (inventory.getType().equals(SmartInventoryType.PAGINATION)){
            Pagination pagination = inventory.getPagination();
            int page = pagination.getPages().indexOf(clickedInventory) + 1;
            slotPos = SlotPos.of(page,event.getSlot(),-1);

            if (pagination.getPlaceAbleSlots().getSlots().contains(event.getSlot()))
                clickableItem = pagination.getItem(slotPos);
        }

        if (clickedInventory.equals(player.getInventory())){event.setCancelled(true);return;}

        SmartInventoryClickEvent clickEvent = new SmartInventoryClickEvent(player,inventory,event,slotPos);
        Bukkit.getPluginManager().callEvent(clickEvent);
        if (clickEvent.isCancelled()) {event.setCancelled(true); return;}

        if (clickableItem == null) return;
        event.setCancelled(true);
        Consumer<SmartInventoryClickEvent> click = clickableItem.getClick();
        if (click != null) click.accept(clickEvent);
    }



    @EventHandler
    public void onClose(InventoryCloseEvent event){

        if (!(event.getPlayer() instanceof Player)) return;

        Player player = (Player) event.getPlayer();
        SmartInventory inventory = SmartInventory.get(player);

        if (inventory == null) return;

        SmartInventoryCloseEvent closeEvent = new SmartInventoryCloseEvent(player,inventory,event);
        Bukkit.getPluginManager().callEvent(closeEvent);

        if (closeEvent.isCancelled()) inventory.open(player);
        else SmartInventory.remove(player);

        if (inventory.getCloser() != null) inventory.getCloser().accept(closeEvent);
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event){

        if (!(event.getPlayer() instanceof Player)) return;

        Player player = (Player) event.getPlayer();
        SmartInventory inventory = SmartInventory.get(player);

        if (inventory == null) return;

        SmartInventoryOpenEvent openEvent = new SmartInventoryOpenEvent(player,inventory,event);
        Bukkit.getPluginManager().callEvent(openEvent);
        if (openEvent.isCancelled()) event.setCancelled(true);

        if (inventory.getOpener() != null) inventory.getOpener().accept(openEvent);
    }


}
