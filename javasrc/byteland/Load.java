package byteland;

public class Load {
    private City u;
    private City v;
    public int getId() {
        return id;
    }

    private int id;
    
    public Load(int id, City u, City v) {
        this.id = id;
        this.u = u;
        this.v = v;
        u.addLoad(this);
        v.addLoad(this);
    }
    
    public City cityU() {
        return this.u;
    }
    
    public City cityV() {
        return this.v;
    }
    
    public boolean endWith(City city) {
        return u.id() == city.id() || v.id() == city.id();
    }
    
    public boolean isConnectDiffType() {
        return !u.troopsType().equals(v.troopsType());
    }
}
