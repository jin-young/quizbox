package byteland;

import java.util.LinkedList;
import java.util.List;

public class City {
    private int id;
    private String troopsType;
    private int cost;
    private List<Load> connectedLoads;
    
    public City(int id, String type, int cost) {
        this.id = id;
        this.troopsType = type;
        this.cost = cost;
        this.connectedLoads = new LinkedList<Load>();
    }
    
    public int id() {
        return this.id;
    }
    
    public String troopsType() {
        return this.troopsType;
    }
    
    public int eliminationCost() {
        return this.cost;
    }
    
    public void addLoad(Load l) {
        if(!connectedLoads.contains(l)) connectedLoads.add(l);
    }
    
    public List<Load> loads() {
        return this.connectedLoads;
    }
}
