package tr.quary.structure.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import tr.quary.structure.inventory.events.SmartInventoryCloseEvent;
import tr.quary.structure.inventory.events.SmartInventoryOpenEvent;
import tr.quary.structure.inventory.utils.SmartInventoryType;
import tr.quary.structure.inventory.utils.ClickableItem;
import tr.quary.structure.pagination.Pagination;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public interface SmartInventory {

    class InventoryHolder{
        static Map<Player,SmartInventory> SMART_INVENTORY_MAP = new ConcurrentHashMap<>();
    }

    static void put(final Player player,final SmartInventory inventory){
        InventoryHolder.SMART_INVENTORY_MAP.put(player,inventory);
    }

    static void clear(){
        InventoryHolder.SMART_INVENTORY_MAP.clear();
    }

    static Set<Player> keySet(){return InventoryHolder.SMART_INVENTORY_MAP.keySet();}

    static void remove(final Player player){
        InventoryHolder.SMART_INVENTORY_MAP.remove(player);
    }

    static SmartInventory get(final Player player){
        return InventoryHolder.SMART_INVENTORY_MAP.get(player);
    }

    String getTitle();

    int getSize();

    String getUniqueId();

    SmartInventoryType getType();

    Consumer<SmartInventoryCloseEvent> getCloser();

    Consumer<SmartInventoryOpenEvent> getOpener();

    void open(Player player);

    void close(Player player);

    void whenClose(Consumer<SmartInventoryCloseEvent> consumer);

    void whenOpen(Consumer<SmartInventoryOpenEvent> consumer);

    void setItem(int slot, ClickableItem clickableItem);

    ClickableItem getItem(int slot);

    ClickableItem[] getItems();

    List<Player> getViewers();

    Object getValue(String key);

    void putValue(String key,Object object);

    default Pagination getPagination() {return null;}

    default Inventory getBukkitInventory() {
        return null;
    }


}
