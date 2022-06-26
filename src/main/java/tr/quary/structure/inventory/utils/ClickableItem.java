package tr.quary.structure.inventory.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tr.quary.structure.inventory.SmartInventory;
import tr.quary.structure.inventory.events.SmartInventoryClickEvent;
import tr.quary.structure.pagination.Pagination;
import tr.quary.structure.pagination.utils.SlotPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ClickableItem {

    public static ClickableItem of(ItemStack itemStack,Consumer<SmartInventoryClickEvent> clickEvent){
        return new ClickableItem(itemStack,clickEvent);
    }

    public static ClickableItem empty(ItemStack itemStack){
        return ClickableItem.of(itemStack,click->{});
    }

    private ItemStack realItemStack;
    private Consumer<SmartInventoryClickEvent> clickEvent;
    private final Map<String,Object> values;

    private final Map<SmartInventory,SlotPos> slotPosMap;
    private final UUID uniqueId;

    public ClickableItem(ItemStack realItemStack,Consumer<SmartInventoryClickEvent> clickEvent){

        this.realItemStack = realItemStack;
        this.values = new ConcurrentHashMap<>();
        this.slotPosMap = new ConcurrentHashMap<>();
        this.uniqueId = UUID.randomUUID();
        this.clickEvent = clickEvent;
    }

    public UUID getUniqueId(){
        return uniqueId;
    }
    public ItemStack getItemStack(){
        return realItemStack;
    }
    public Consumer<SmartInventoryClickEvent> getClick(){
        return clickEvent;
    }

    public void setClick(Consumer<SmartInventoryClickEvent> clickEvent){this.clickEvent = clickEvent;}

    public Object getValue(String key){
        return values.getOrDefault(key,null);
    }

    public void putValue(String key,Object value){
        values.put(key,value);
    }

    public void removeValue(String key){
        values.remove(key);
    }

    public void putInventory(SmartInventory inventory, SlotPos slotPos){
        slotPosMap.put(inventory,slotPos);
    }

    public void removeInventory(SmartInventory inventory){
        slotPosMap.remove(inventory);
    }


    public void changeItemStack(ItemStack itemStack,SmartInventory... inventories){

        this.realItemStack = itemStack;

        if (inventories.length != 0){
            for (SmartInventory inv : inventories) this.setInventory(inv);
        }
        if (inventories.length == 0){
            slotPosMap.keySet().forEach(this::setInventory);
        }

    }

    public void changeName(String name,SmartInventory... inventories){

        ItemMeta meta = realItemStack.getItemMeta();
        meta.setDisplayName(name);
        realItemStack.setItemMeta(meta);

        if (inventories.length != 0){
            for (SmartInventory inv : inventories) this.setInventory(inv);
        }
        if (inventories.length == 0){
            slotPosMap.keySet().forEach(this::setInventory);
        }
    }

    public void changeLore(List<String> lore, SmartInventory... inventories){

        ItemMeta meta = realItemStack.getItemMeta();
        meta.setLore(lore);
        realItemStack.setItemMeta(meta);

        if (inventories.length != 0){
            for (SmartInventory inv : inventories) this.setInventory(inv);
        }
        if (inventories.length == 0){
            slotPosMap.keySet().forEach(this::setInventory);
        }
    }

    public void addLore(List<String> lore, SmartInventory... inventories){

        ItemMeta meta = realItemStack.getItemMeta();
        if (!meta.hasLore()) meta.setLore(lore);
        else {
            List<String> list = new ArrayList<>(meta.getLore());
            list.addAll(lore);
            meta.setLore(list);
        }
        realItemStack.setItemMeta(meta);

        if (inventories.length != 0){
            for (SmartInventory inv : inventories) this.setInventory(inv);
        }
        if (inventories.length == 0){
            slotPosMap.keySet().forEach(this::setInventory);
        }
    }

    private void setInventory(SmartInventory inventory){

        SlotPos slotPos = slotPosMap.get(inventory);
        if (slotPos == null) return;

        if (inventory.getType().equals(SmartInventoryType.CLASSIC)){
            inventory.getBukkitInventory().setItem(slotPos.getSlot(),realItemStack.clone());
        }

        if (inventory.getType().equals(SmartInventoryType.PAGINATION)){
            Pagination pagination = inventory.getPagination();
            if (slotPos.getPage() == -1){
                pagination.getPages().forEach(inv -> inv.setItem(slotPos.getSlot(),realItemStack.clone()));
            }else pagination.getPage(slotPos.getPage()).setItem(slotPos.getSlot(),realItemStack.clone());
        }
    }

}
