import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Graph implements GraphInterface {
    private ArrayList <HashSet <Integer>> graph;
    private  String graphEnd = "S";
    private HashMap <Integer , Integer> nodeMap;
    private static ReentrantLock lock;

    private int  getIndex(int node){
        if(nodeMap.containsKey(node)){
            return nodeMap.get(node);
        }
        return createIndex(node);
    }

    private int createIndex(int node){
        int index = graph.size();
        graph.add(new HashSet<>());
        nodeMap.put(node , index);
        return index;
    }

    private void readFromFile() throws FileNotFoundException {
        File file = new File("graph");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            if(!line.equals(graphEnd)){
                Scanner lineSC = new Scanner(line);
                int from = lineSC.nextInt();
                int to = lineSC.nextInt();
                add(from,to);
            }
        }
    }
    private void readFromStandardInput(){
        Scanner sc = new Scanner(System.in);
        String line;
        while(!(line = sc.nextLine()).equals(graphEnd)){
            Scanner scLine = new Scanner(line);
            int from = scLine.nextInt();
            int to = scLine.nextInt();
            add(from,to);
        }
    }
    public Graph(Boolean isFile) throws FileNotFoundException {
        lock = new ReentrantLock();
        nodeMap = new HashMap <>();
        graph = new ArrayList<>();
        if(isFile){
            readFromFile();
        }else{
            readFromStandardInput();
        }
        System.out.println(graph);
        System.out.println("R\n");
    }

    private int BFS(int from, int to){
        Queue<Integer> q = new LinkedList<>();
        q.add(from);
        int height = 0;
        HashSet<Integer> visited = new HashSet<>();
        visited.add(from);
        while(!q.isEmpty()){
            int size = q.size();
            for(int i=0;i<size;i++){
                int x = q.poll();
                if(x == to){
                    return height;
                }
                Iterator<Integer> itr = graph.get(getIndex(x)).iterator();
                while(itr.hasNext()){
                    int new_node = itr.next();
                    if(!visited.contains(new_node)){
                        q.add(new_node);
                        visited.add(new_node);
                    }
                }
            }
            height++;
        }
        return 0;
    }

    @Override
    public int query(int from, int to) {
        if(!nodeMap.containsKey(from) || !nodeMap.containsKey(to)){
            return 0;
        }
        return BFS(from, to);
    }

    @Override
    public void delete(int from, int to) {
        lock.lock();
        if(nodeMap.containsKey(from) && nodeMap.containsKey(to)){
            int idx = nodeMap.get(from);
            graph.get(idx).remove(to);
        }
        lock.unlock();
    }

    @Override
    public void add(int from, int to) {
        lock.lock();
        int idx_from = getIndex(from);
        getIndex(to);
        graph.get(idx_from).add(to);
        lock.unlock();
    }
}