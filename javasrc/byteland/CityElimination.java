package byteland;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CityElimination {
    private Map<Integer, City> cities = new HashMap<Integer, City>();
    private List<Load> loads = new LinkedList<Load>();
    private Map<Entry<List<City>,List<Load>>, Integer> cache = new HashMap<Entry<List<City>,List<Load>>, Integer>();
    private boolean enableCache = false;
    
    public void setEnableCache(boolean enableCache) {
        this.enableCache = enableCache;
    }

    public Map<Integer, City> getCities() {
        return this.cities;
    }
    
    public List<Load> getLoads() {
        return this.loads;
    }
    
    public void addCity(int id, City city) {
        cities.put(id, city);
    }
    
    public void addLoad(int idU, int idV) {
        City u = cities.get(idU);
        City v = cities.get(idV);
        if(!u.troopsType().equals(v.troopsType())) {
            loads.add(new Load(loads.size()+1,cities.get(idU), cities.get(idV)));
        }
    }
    
    public int minCost() {
        return minCost(new LinkedList<City>(this.cities.values()), this.loads);
    }
    
    public int minCost(List<City> cities, List<Load> loads) {
        if(loads.size() == 0) return 0;
        
        Map.Entry<List<City>,List<Load>> key = null;
        if(enableCache) {
            key = new AbstractMap.SimpleEntry<List<City>,List<Load>>(cities, loads);
            if(cache.containsKey(key)) return cache.get(key);
        }
        
        City candiateCity = cities.remove(0);
        
        //First of all, let assume that removing first city in city list is optimal solution.
        //Then optimal value would be sum of the selected city's elimination cost and 
        //sub-optimal value minCost(remainedCities, remainedLoads).
        List<City> remainedCities = new LinkedList<City>(cities);
        List<Load> remaindLoads = new LinkedList<Load>();
        for(Load l : loads) {
            if(!candiateCity.loads().contains(l)) 
                remaindLoads.add(l);
        }
        int costElemination = candiateCity.eliminationCost() +  minCost(remainedCities, remaindLoads);
        
        remaindLoads.clear();
        remainedCities.clear();
        
        //Second, let assume that keeping the first city in city list is optimal solution.
        //Then, since we only deal with loads which have different troop type at the each end,
        //we should remove cities which are connected to the first city. 
        //All loads which is connected to the cities also should be removed.
        List<City> connectedCities = new LinkedList<City>();
        List<Load> loadsFromConnectedCities = new LinkedList<Load>();
        
        int anotherSideElimiationCost = 0;
        for(Load l : candiateCity.loads()) {
            City city = (candiateCity == l.cityU()) ? l.cityV() : l.cityU();
            if(cities.contains(city)) { //connected city could be removed already, so check it first
                connectedCities.add(city);
                anotherSideElimiationCost += city.eliminationCost();
                loadsFromConnectedCities.addAll(city.loads());
            }
        }
        
        for(City c : cities) {
            if(!connectedCities.contains(c)) remainedCities.add(c);
        }
        for(Load l : loads) {
            if(!loadsFromConnectedCities.contains(l)) remaindLoads.add(l);
        }
        
        int noEliminationCost = anotherSideElimiationCost + minCost(remainedCities, remaindLoads);
        
        if(enableCache) cache.put(key, Math.min(costElemination, noEliminationCost));
        
        return Math.min(costElemination, noEliminationCost);
    }
    
    public int minCostBrute(List<Load> loads) {
        int cost = Integer.MAX_VALUE;
        if(loads.isEmpty()) return 0;
        
        Set<City> cities = new HashSet<City>();
        for(Load load : loads) {
            cities.add(load.cityU());
            cities.add(load.cityV());
        }
        
        List<Load> targetLoads = null;
        for(City c : cities) {
            int costTemp = c.eliminationCost();
            targetLoads = new LinkedList<Load>();
            
            for(Load nextLoad : loads) {
                if(!nextLoad.endWith(c)) {
                    targetLoads.add(nextLoad);
                }
            }
            costTemp += minCostBrute(targetLoads);
            cost = Math.min(cost, costTemp);
        }
        
        return cost;
    }
}
