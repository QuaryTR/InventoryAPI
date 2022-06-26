package tr.quary.structure.inventory.imp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tr.quary.structure.inventory.SmartInventory;
import tr.quary.structure.inventory.events.SmartInventoryCloseEvent;
import tr.quary.structure.inventory.events.SmartInventoryOpenEvent;
import tr.quary.structure.inventory.utils.SmartInventoryType;
import tr.quary.structure.inventory.utils.ClickableItem;
import tr.quary.structure.pagination.utils.SlotPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ClassicInventory implements SmartInventory {

    private final String title;
    private final int size;

    private final SmartInventoryType type = SmartInventoryType.CLASSIC;

    private final String uniqueId;
    private final Inventory bukkitInventory;

    private Consumer<SmartInventoryCloseEvent> closer;
    private Consumer<SmartInventoryOpenEvent> opener;

    private final Map<String,Object> values;
    private final ClickableItem[] items;

    public ClassicInventory(final String title,final int size,final String uniqueId){

        this.title = ChatColor.translateAlternateColorCodes('&',title);
        this.size = size;
        this.uniqueId = uniqueId;

        this.items = new ClickableItem[size*9];
        this.values = new ConcurrentHashMap<>();

        this.bukkitInventory = Bukkit.createInventory(null,size*9,this.title);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public SmartInventoryType getType() {
        return type;
    }

    @Override
    public Inventory getBukkitInventory() {
        return bukkitInventory;
    }

    @Override
    public Consumer<SmartInventoryCloseEvent> getCloser() {
        return closer;
    }
    @Override
    public Consumer<SmartInventoryOpenEvent> getOpener() {
        return opener;
    }

    @Override
    public void open(final Player player) {
        SmartInventory.put(player,this);
        player.openInventory(bukkitInventory);
    }
    @Override
    public void close(final Player player) {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        if (inventory != null && inventory.equals(bukkitInventory)) player.closeInventory();
    }

    @Override
    public void whenClose(final Consumer<SmartInventoryCloseEvent> consumer) {
        this.closer = consumer;
    }
    @Override
    public void whenOpen(final Consumer<SmartInventoryOpenEvent> consumer) {
        this.opener = consumer;
    }

    @Override
    public void setItem(int slot, ClickableItem clickableItem) {

        items[slot] = clickableItem;

        if (clickableItem != null) {
            bukkitInventory.setItem(slot,clickableItem.getItemStack());
            clickableItem.putInventory(this, SlotPos.of(1,slot,-1));
        }

        if  (clickableItem == null){
            ClickableItem item = getItem(slot);
            if (item != null) item.removeInventory(this);
            bukkitInventory.setItem(slot,new ItemStack(Material.AIR));
        }
    }

    @Override
    public ClickableItem getItem(int slot) {
        return items[slot];
    }

    @Override
    public ClickableItem[] getItems() {
        return items;
    }

    @Override
    public List<Player> getViewers() {
        List<Player> list = new ArrayList<>();
        bukkitInventory.getViewers().forEach(humanEntity -> {
            if (humanEntity instanceof Player) list.add((Player) humanEntity);
        });
        return list;
    }

    @Override
    public Object getValue(String key) {
        return values.get(key);
    }

    @Override
    public void putValue(String key, Object object) {
        values.put(key,object);
    }
}
