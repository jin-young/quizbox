package byteland;

public class Load {
    private City u;
    private City v;
    
    public Load(City u, City v) {
        this.u = u;
        this.v = v;
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
