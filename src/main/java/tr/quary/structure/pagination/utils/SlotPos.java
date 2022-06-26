package tr.quary.structure.pagination.utils;

public class SlotPos {


    public static SlotPos of(int page,int slot,int number){
        return new SlotPos(page,slot,number);
    }

    private final int page;
    private final int slot;
    private final int number;

    private SlotPos(int page,int slot,int number){
        this.page = page;
        this.slot = slot;
        this.number = number;
    }

    public int getPage(){return page;}

    public int getSlot(){return slot;}

    public int toInt(){return number;}


}
