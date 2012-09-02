package byteland;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

class Pair {
    private List<Integer> cityIds;
    private String loads;
    
    public Pair(List<Integer> cityIds, String loads) {
        this.cityIds = cityIds;
        this.loads = loads;
    }
    
    public boolean equals(Object o) {
        Pair t = (Pair)o;
        return this.cityIds.equals(t.getCityIds()) && loads.equals(t.getLoads());
    }
    
    public List<Integer> getCityIds() {
        return this.cityIds;
    }
    
    public String getLoads() {
        return this.loads;
    }
    
    public int hashCode() {
        String temp = "";
        for(int value: this.cityIds) {
            temp += Integer.toString(value);
        }
        temp += this.loads;
        return temp.hashCode();
    }
}
public class CityElimination {
    private Map<Integer, City> cities = new HashMap<Integer, City>();
    private List<Load> loads = new LinkedList<Load>();
    private Map<Pair, Integer> cache = new HashMap<Pair, Integer>();
    
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
        if(!u.troopsType().equals(v.troopsType()))
            loads.add(new Load(cities.get(idU), cities.get(idV)));
    }
    
    public static List<CityElimination> buildGraph(String filePath) {
        File f = new File(filePath);
        BufferedReader bis = null;
        List<CityElimination> testCases = new LinkedList<CityElimination>();
        
        try {
            bis = new BufferedReader(new FileReader(f));
            
            int numOfTestCase = new Scanner(bis.readLine()).nextInt();
            for(int idx=1;idx<=numOfTestCase;idx++) {
                CityElimination ele = new CityElimination();
                
                String testConfig = bis.readLine();
                Scanner config = new Scanner(testConfig);
                int numOfCity = config.nextInt();
                int numOfLoad = config.nextInt();
                
                for(int idxCity=1;idxCity<=numOfCity;idxCity++) {
                    String cityConfig = bis.readLine();
                    config = new Scanner(cityConfig);
                    ele.addCity(idxCity, new City(idxCity, config.next(), config.nextInt()));
                }
                
                for(int idxLoad=1;idxLoad<=numOfLoad;idxLoad++) {
                    String loadConfig = bis.readLine();
                    config = new Scanner(loadConfig);
                    ele.addLoad(config.nextInt(), config.nextInt());
                }
                testCases.add(ele);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bis != null) { try {bis.close();} catch (IOException e) {
                e.printStackTrace();
            } finally {} }
        }
        
        return testCases;
    }
    
    public static void main(String[] args) {
        List<CityElimination> cases = buildGraph(args[0]);
        for (CityElimination elemination : cases) {
            
            List<City> cities = new LinkedList<City>();
            for(Load load : elemination.getLoads()) {
                if(!cities.contains(load.cityU())) cities.add(load.cityU());
                if(!cities.contains(load.cityV())) cities.add(load.cityV());
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z");
            System.out.println("Start: " + sdf.format(Calendar.getInstance().getTime()));
            System.out.println(elemination.minCostTryDynamic(cities, elemination.getLoads()));
            System.out.println("End: " + sdf.format(Calendar.getInstance().getTime()));
        }
    }
    
    public int minCostTryDynamic(List<City> cities, List<Load> loads) {
        if(loads.size() == 0) return 0;
        if(cities.size() == 0 && loads.size() > 0) return Integer.MAX_VALUE;

        List<Integer> cityIds = new LinkedList<Integer>();
        for(City c: cities) cityIds.add(c.id());
        
        String loadString = "";
        for(Load l: loads) loadString += l.cityU().id() + "-" + l.cityV().id() + ",";
        
        Pair key = new Pair(cityIds, loadString);
        if(cache.get(key) != null) {
            /*
            if(cache.get(key) != Integer.MAX_VALUE){
            System.out.print("HIT : ");
            System.out.print(key.getCityIds());
            System.out.print(" / ");
            System.out.print(key.getLoads());
            System.out.println(". Value: " + cache.get(key));
            }
            */
            return cache.get(key);
        } else {
            /*
            System.out.print("MISS : ");
            System.out.print(key.getCityIds());
            System.out.print(" / ");
            System.out.println(key.getLoads());
            */
        }
        
        City candiateCity = cities.remove(0);
        
        List<Load> remaindLoads = new LinkedList<Load>();
        for(Load nextLoad : loads) {
            if(!nextLoad.endWith(candiateCity)) remaindLoads.add(nextLoad);
        }
        
        List<City> remainedCities = new LinkedList<City>();
        for(City city: cities) remainedCities.add(city);
        
        int eleminationSubCost = minCostTryDynamic(remainedCities, remaindLoads);
        int costElemination = (eleminationSubCost == Integer.MAX_VALUE) ? Integer.MAX_VALUE : candiateCity.eliminationCost() + eleminationSubCost;
        int minCost = Math.min(costElemination, minCostTryDynamic(cities, loads));
        
        cache.put(key, minCost);
        
        return minCost;
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
