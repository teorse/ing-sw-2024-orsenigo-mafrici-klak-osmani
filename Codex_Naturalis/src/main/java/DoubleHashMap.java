import java.util.HashMap;
import java.util.Map;

public class DoubleHashMap {
    private Map<Integer, Map<Integer, CardPlacement>> map;

    public DoubleHashMap(){
        this.map = new HashMap<>();
    }

    public void put(int x, int y, CardPlacement card){
        Map<Integer, CardPlacement> innerMap;
        if(!map.containsKey(x)){
            map.put(x, new HashMap<>());
        }
        innerMap = map.get(x);
        innerMap.put(y, card);
    }

    public boolean containsKey(int x, int y){
        Map<Integer, CardPlacement> innerMap;
        if(map.containsKey(x)){
            innerMap = map.get(x);
            if(innerMap.containsKey(y))
                return true;
        }
        return false;
    }

    public CardPlacement get(int x, int y){
        Map<Integer, CardPlacement> innerMap;
        innerMap = map.get(x);
        return innerMap.get(y);
    }

    public CardPlacement remove(int x, int y){
        CardPlacement r;
        Map<Integer, CardPlacement> innerMap;
        innerMap = map.get(x);
        r = innerMap.remove(y);
        if(innerMap.isEmpty())
            map.remove(x);
        return r;
    }
}