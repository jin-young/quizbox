package byteland;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Run {
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
        for (CityElimination elimination : cases) {
            //elimination.setEnableCache(false);
            long start = Calendar.getInstance().getTimeInMillis();
            System.out.println(elimination.minCost());
            System.out.println((Calendar.getInstance().getTimeInMillis() - start)/1000f + " sec");
        }
    }

}
