package tr.quary.structure.inventory.imp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tr.quary.structure.inventory.utils.SmartInventoryType;
import tr.quary.structure.pagination.Pagination;
import tr.quary.structure.inventory.SmartInventory;
import tr.quary.structure.inventory.events.SmartInventoryCloseEvent;
import tr.quary.structure.inventory.events.SmartInventoryOpenEvent;
import tr.quary.structure.inventory.utils.ClickableItem;
import tr.quary.structure.pagination.utils.PlaceAbleSlot;
import tr.quary.structure.pagination.utils.SlotPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class PaginationInventory implements SmartInventory {

    private final String title;
    private final int size;

    private final SmartInventoryType type = SmartInventoryType.PAGINATION;
    private final String uniqueId;
    private Consumer<SmartInventoryCloseEvent> closer;
    private Consumer<SmartInventoryOpenEvent> opener;

    private final Pagination pagination;
    private final Map<String,Object> values;
    private final ClickableItem[] items;

    public PaginationInventory(final String title, final int size, final String uniqueId, final PlaceAbleSlot placeAbleSlot){

        this.title = ChatColor.translateAlternateColorCodes('&',title);
        this.size = size;
        this.uniqueId = uniqueId;

        this.items = new ClickableItem[size*9];
        this.values = new ConcurrentHashMap<>();

        Inventory bukkitInventory = Bukkit.createInventory(null,size*9,this.title);
        pagination = new Pagination.PaginationImp(this,placeAbleSlot,bukkitInventory);
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
        player.openInventory(pagination.getFirstPage());
    }

    @Override
    public void close(final Player player) {
        player.closeInventory();
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
    public void setItem(int slot, ClickableItem clickableItem){

        List<Integer> placeAbleSlots = getPagination().getPlaceAbleSlots().getSlots();
        if (placeAbleSlots.contains(slot)) return;
        List<Inventory> pages = pagination.getPages();

        if (clickableItem == null){

            ClickableItem item = getItem(slot);
            if (item != null) item.removeInventory(this);

            pages.forEach(inventory ->inventory.setItem(slot,new ItemStack(Material.AIR)));
            pagination.getStructureInventory().setItem(slot,new ItemStack(Material.AIR));
            items[slot] = null;
            return;
        }

        clickableItem.putInventory(this, SlotPos.of(-1,slot,-1));
        pages.forEach(inventory -> inventory.setItem(slot,clickableItem.getItemStack()));
        pagination.getStructureInventory().setItem(slot,clickableItem.getItemStack());
        items[slot] = clickableItem;
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

        pagination.getPages().forEach(page -> page.getViewers().forEach(humanEntity -> {
            if (!(humanEntity instanceof Player)) return;
            Player p = (Player) humanEntity;
            if (!list.contains(p)) list.add(p);
        }));

        return list;
    }

    @Override
    public Pagination getPagination() {
        return pagination;
    }

    @Override
    public Object getValue(String key) {
        return values.getOrDefault(key,null);
    }

    @Override
    public void putValue(final String key, final Object object) {
        values.put(key,object);
    }
}
