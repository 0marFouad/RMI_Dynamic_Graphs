import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Graph implements GraphInterface {
    private ArrayList <ArrayList <Integer>> graph;
    private  String graphEnd = "S";
    private HashMap <Integer , Integer> nodeMap;

    private int  getIndex(int node){
        if(nodeMap.containsKey(node)){
            nodeMap.get(node);
        }
        return createIndex(node);
    }

    private int createIndex(int node){
        int index = graph.size();
        graph.add(new ArrayList<Integer>());
        nodeMap.put(node , index);
        return index;
    }

    private void readFromFile() throws FileNotFoundException {
        File file =
                new File("graph");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            if(!line.equals(graphEnd)){
                Scanner lineSC = new Scanner(line);
                int from = getIndex(lineSC.nextInt());
                int to = getIndex(lineSC.nextInt());
                graph.get(from).add(to);
            }
        }
    }
    private void readFromStandardInput(){

    }
    public Graph() throws FileNotFoundException {
        readFromFile();
    }
    @Override
    public int query(int from, int to) {
        return 0;
    }

    @Override
    public void delete(int from, int to) {

    }

    @Override
    public void add(int from, int to) {

    }
}
