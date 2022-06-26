package tr.quary.structure.pagination.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaceAbleSlot {

    public static PlaceAbleSlot array(int... ints){
        return new PlaceAbleSlot(ints);
    }

    public static PlaceAbleSlot list(List<Integer> list){
        return new PlaceAbleSlot(list);
    }

    private List<Integer> list = new ArrayList<>();

    private PlaceAbleSlot(int... ints){
        for (int a: ints) list.add(a);
        Collections.sort(list);
    }

    private PlaceAbleSlot(List<Integer> list){
        this.list = list;
        Collections.sort(list);
    }

    public List<Integer> getSlots(){
        return list;
    }

    public int getSize(){
        return list.size();
    }

    public int get(int slot){
        return list.get(slot);
    }


}
