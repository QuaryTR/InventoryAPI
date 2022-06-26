package tr.quary.structure.pagination;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tr.quary.structure.inventory.SmartInventory;
import tr.quary.structure.inventory.utils.ClickableItem;
import tr.quary.structure.pagination.utils.PlaceAbleSlot;
import tr.quary.structure.pagination.utils.SlotPos;

import java.util.ArrayList;
import java.util.List;

public interface Pagination {

    SmartInventory getInventory();

    PlaceAbleSlot getPlaceAbleSlots();

    List<Inventory> getPages();

    Inventory getPage(int page);

    void openPage(int page, Player player);

    Inventory getLastPage();

    Inventory getFirstPage();

    List<ClickableItem> getItems();

    ClickableItem getItem(SlotPos slotPos);

    List<ClickableItem> getItemsFromPage(int page);

    void removeItem(SlotPos slotPos);

    void removeItem(ClickableItem clickableItem);

    void addItem(ClickableItem clickableItem);

    boolean contains(ClickableItem clickableItem);

    boolean contains(int page,ClickableItem clickableItem);

    SlotPos getSlotsPosOfItem(ClickableItem clickableItem);

    SlotPos getSlotsPosFromInt(int number);

    SlotPos getFirstAvailablePos();

    Inventory getStructureInventory();


    class PaginationImp implements Pagination{

        private final SmartInventory smartInventory;
        private final PlaceAbleSlot placeAbleSlot;

        private final Inventory bukkitStructureInventory;

        private final List<Inventory> pages;

        private final List<ClickableItem> items;

        public PaginationImp(SmartInventory inventory,PlaceAbleSlot placeAbleSlot,Inventory bukkitInventory){

            this.smartInventory = inventory;
            this.placeAbleSlot = placeAbleSlot;
            this.bukkitStructureInventory = bukkitInventory;

            this.pages = new ArrayList<>();
            this.items = new ArrayList<>();

            Inventory firstPage = Bukkit.createInventory(null,inventory.getSize()*9,inventory.getTitle());
            firstPage.setContents(bukkitInventory.getContents());
            pages.add(firstPage);
        }


        @Override
        public SmartInventory getInventory() {
            return smartInventory;
        }

        @Override
        public Inventory getStructureInventory(){
            return this.bukkitStructureInventory;
        }

        @Override
        public PlaceAbleSlot getPlaceAbleSlots() {
            return placeAbleSlot;
        }


        @Override
        public List<Inventory> getPages() {
            return pages;
        }
        @Override
        public Inventory getPage(int page) {
            return pages.get(page-1);
        }
        @Override
        public void openPage(int page,Player player) {
            SmartInventory.put(player,smartInventory);
            player.openInventory(getPage(page));
        }
        @Override
        public Inventory getLastPage() {
            return getPage(pages.size());
        }
        @Override
        public Inventory getFirstPage() {
            return getPage(1);
        }


        @Override
        public List<ClickableItem> getItems() {
            return items;
        }
        @Override
        public ClickableItem getItem(SlotPos slotPos) {
            int index = placeAbleSlot.getSlots().indexOf(slotPos.getSlot());
            int var = ((slotPos.getPage()-1) * placeAbleSlot.getSize()) + (index+1);
            return items.get(var-1);
        }
        @Override
        public List<ClickableItem> getItemsFromPage(int page) {

            int var = (page-1)*placeAbleSlot.getSize();

            List<ClickableItem> pageItems = new ArrayList<>();

            for (int a=var; a<var+placeAbleSlot.getSize(); a++){
                if (a>=items.size()) break;
                pageItems.add(items.get(a));
            }

            return pageItems;
        }

        @Override
        public boolean contains(ClickableItem clickableItem) {
            return items.contains(clickableItem);
        }

        @Override
        public boolean contains(int page, ClickableItem clickableItem) {
            return getItemsFromPage(page).contains(clickableItem);
        }

        @Override
        public SlotPos getSlotsPosOfItem(ClickableItem clickableItem) {
            return getSlotsPosFromInt(items.indexOf(clickableItem)+1);
        }
        @Override
        public SlotPos getSlotsPosFromInt(int number) {

            int page = number / placeAbleSlot.getSize();
            int var = number % placeAbleSlot.getSize();

            int slot;

            if (var != 0) {
                page+=1;
                slot = placeAbleSlot.get(var-1);
            }else slot = placeAbleSlot.get(placeAbleSlot.getSize()-1);

            if (page == 0) return null;
            else return SlotPos.of(page,slot,number);
        }

        @Override
        public SlotPos getFirstAvailablePos() {
            return getSlotsPosFromInt(items.size() +1);
        }

        @Override
        public void removeItem(SlotPos slotPos) {

            ClickableItem removedItem = getItem(slotPos);
            removedItem.removeInventory(smartInventory);

            items.remove(slotPos.toInt()-1);
            List<ClickableItem> sublist = items.subList(slotPos.toInt()-1,items.size());

            sublist.forEach(clickableItem -> {
                SlotPos pos = getSlotsPosOfItem(clickableItem);
                getPage(pos.getPage()).setItem(pos.getSlot(),clickableItem.getItemStack());
            });

            SlotPos deletedItemPos = getFirstAvailablePos();
            if (deletedItemPos.getSlot() == placeAbleSlot.get(0)) pages.remove(getLastPage());
            else getPage(deletedItemPos.getPage()).setItem(deletedItemPos.getSlot(),new ItemStack(Material.AIR));
        }

        @Override
        public void removeItem(ClickableItem clickableItem) {
            clickableItem.removeInventory(smartInventory);
            SlotPos slotPos = getSlotsPosOfItem(clickableItem);
            removeItem(slotPos);
        }

        @Override
        public void addItem(ClickableItem clickableItem) {

            SlotPos slotPos = getFirstAvailablePos();

            if (slotPos.getPage() > pages.size()) {
                Inventory newInventory = Bukkit.createInventory(null,smartInventory.getSize()*9,smartInventory.getTitle());
                newInventory.setContents(bukkitStructureInventory.getContents());
                pages.add(newInventory);
            }

            items.add(clickableItem);
            Inventory page = getPage(slotPos.getPage());
            page.setItem(slotPos.getSlot(),clickableItem.getItemStack());
            clickableItem.putInventory(smartInventory,slotPos);
        }


    }


}
