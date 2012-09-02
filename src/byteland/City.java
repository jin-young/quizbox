package byteland;

public class City {
    private int id;
    private String troopsType;
    private int cost;
    
    public City(int id, String type, int cost) {
        this.id = id;
        this.troopsType = type;
        this.cost = cost;
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
}
