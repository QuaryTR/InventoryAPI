package tr.quary.structure.inventory;

import tr.quary.structure.inventory.imp.ClassicInventory;
import tr.quary.structure.inventory.imp.PaginationInventory;
import tr.quary.structure.pagination.utils.PlaceAbleSlot;

public class InventoryBuilder {

    private PlaceAbleSlot placeAbleSlot;
    private String title;
    private int size;

    private String uniqueId;


    public InventoryBuilder pagination(final PlaceAbleSlot placeAbleSlot){
        this.placeAbleSlot = placeAbleSlot;
        return this;
    }

    public InventoryBuilder classic(){
        return this;
    }

    public InventoryBuilder size(int size){
        this.size = size;
        return this;
    }

    public InventoryBuilder title(String title){
        this.title = title;
        return this;
    }

    public InventoryBuilder uniqueId(String uniqueId){
        this.uniqueId = uniqueId;
        return this;
    }

    public SmartInventory create(){
        if (placeAbleSlot == null) return new ClassicInventory(title,size,uniqueId);
        else return new PaginationInventory(title,size,uniqueId,placeAbleSlot);
    }







}
