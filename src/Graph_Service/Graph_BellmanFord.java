package Graph_Service;
import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReentrantLock;

public class Graph_BellmanFord extends UnicastRemoteObject implements GraphInterface {
    private  String graphEnd = "S";
    CopyOnWriteArraySet<Integer> list_of_nodes;
    private ConcurrentHashMap <Integer , ConcurrentHashMap<Integer, Integer>> graph;
    private static ReentrantLock lock;
    private ConcurrentHashMap<Integer,CopyOnWriteArraySet<Integer>> parents;


    private void readFromFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
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
    private void readFromStandardInput() {
        Scanner sc = new Scanner(System.in);
        String line;
        while(!(line = sc.nextLine()).equals(graphEnd)){
            Scanner scLine = new Scanner(line);
            int from = scLine.nextInt();
            int to = scLine.nextInt();
            add(from,to);
        }
    }

    public Graph_BellmanFord(Boolean isFile) throws FileNotFoundException, RemoteException {
        super();
        parents = new ConcurrentHashMap<>();
        lock = new ReentrantLock();
        list_of_nodes = new CopyOnWriteArraySet<>();
        graph = new ConcurrentHashMap<>();
        if(isFile){
            readFromFile("graph");
        }else{
            readFromStandardInput();
        }
    }

    public Graph_BellmanFord(String filename) throws FileNotFoundException, RemoteException {
        super();
        parents = new ConcurrentHashMap<>();
        lock = new ReentrantLock();
        list_of_nodes = new CopyOnWriteArraySet<>();
        graph = new ConcurrentHashMap<>();
        readFromFile(filename);
    }


    private void updateParent(int from, CopyOnWriteArraySet<Integer> visited){
        if(visited.contains(from)){
            return;
        }
        visited.add(from);
        ConcurrentHashMap<Integer, Integer> new_list = getEmptyList(from);
        new_list.put(from,0);
        CopyOnWriteArrayList<ConcurrentHashMap<Integer, Integer>> direct_neighbours = new CopyOnWriteArrayList<>();
        for (ConcurrentHashMap.Entry<Integer, Integer> entry : graph.get(from).entrySet()) {
            if(entry.getValue() == 1){
                direct_neighbours.add(graph.get(entry.getKey()));
            }
        }
//        for(ConcurrentHashMap.Entry<Integer, Integer> entry : graph.get(from).entrySet()){
//            if(entry.getKey() != from){
//                new_list.put(entry.getKey(), entry.getValue());
//            }
//        }
        for(int i=0;i<direct_neighbours.size();i++){
            for (ConcurrentHashMap.Entry<Integer, Integer> entry : direct_neighbours.get(i).entrySet()) {
                Integer new_value = entry.getValue();
                Integer old_value = new_list.get(entry.getKey());
                if(new_value < Integer.MAX_VALUE){
                    new_value++;
                }
                new_list.put(entry.getKey(), Math.min(old_value, new_value));
            }
        }
        boolean isChanged = false;
        for(ConcurrentHashMap.Entry<Integer, Integer> entry : graph.get(from).entrySet()){
            if(new_list.get(entry.getKey()) != entry.getValue()){
                isChanged = true;
                break;
            }
        }
        graph.put(from, new_list);
        if(isChanged){
            if(parents.get(from) != null){
                Iterator itr = parents.get(from).iterator();
                while (itr.hasNext()) {
                    Integer p = (Integer) itr.next();
                    updateParent(p, visited);
                }
            }
        }
    }


    @Override
    public int query(int from, int to) {
        if(!list_of_nodes.contains(from) || !list_of_nodes.contains(to)){
            return 0;
        }
        return graph.get(from).get(to);
    }

    @Override
    public void delete(int from, int to) {
        lock.lock();
        if(list_of_nodes.contains(from) && list_of_nodes.contains(to)){
            graph.get(from).put(to, Integer.MAX_VALUE);
            if(parents.get(from) != null){
                Iterator itr = parents.get(from).iterator();
                while (itr.hasNext()) {
                    Integer p = (Integer) itr.next();
                    updateParent(p, new CopyOnWriteArraySet<>());
                }
            }
            updateParent(from, new CopyOnWriteArraySet<>());
        }
        lock.unlock();
    }

    private ConcurrentHashMap<Integer, Integer> getEmptyList(int newely_added){
        ConcurrentHashMap<Integer, Integer> mp = new ConcurrentHashMap<>();
        Iterator<Integer> itr = list_of_nodes.iterator();
        while(itr.hasNext()){
            int new_node = itr.next();
            mp.put(new_node, Integer.MAX_VALUE);
        }
        mp.put(newely_added,0);
        return mp;
    }

    private void addNode(int node_id, ConcurrentHashMap<Integer, Integer> list){
        Iterator<Integer> itr = list_of_nodes.iterator();
        while(itr.hasNext()){
            int new_node = itr.next();
            if(new_node != node_id)
                graph.get(new_node).put(node_id, Integer.MAX_VALUE);
        }
        graph.put(node_id, list);
        list_of_nodes.add(node_id);
    }

    @Override
    public void add(int from, int to) {
        lock.lock();
        if(!list_of_nodes.contains(from) && !list_of_nodes.contains(to)){
            ConcurrentHashMap<Integer, Integer> list_from = getEmptyList(from);
            ConcurrentHashMap<Integer, Integer> list_to = getEmptyList(to);
            CopyOnWriteArraySet<Integer> children = new CopyOnWriteArraySet<>();
            children.add(from);
            parents.put(to,children);
            addNode(from, list_from);
            addNode(to, list_to);
            graph.get(from).put(to,1);
            graph.get(to).put(from,Integer.MAX_VALUE);
        }else if(!list_of_nodes.contains(from)){
            ConcurrentHashMap<Integer, Integer> list_from = getEmptyList(from);
            Iterator<Integer> itr = list_of_nodes.iterator();
            while(itr.hasNext()){
                int new_node = itr.next();
                graph.get(new_node).put(from, Integer.MAX_VALUE);
            }
            for (ConcurrentHashMap.Entry<Integer, Integer> entry : graph.get(to).entrySet()) {
                if(entry.getValue() == Integer.MAX_VALUE){
                    list_from.put(entry.getKey(), Math.min(entry.getValue(), list_from.get(entry.getKey())));
                }else{
                    list_from.put(entry.getKey(), Math.min(entry.getValue() + 1, list_from.get(entry.getKey())));
                }
            }
            if(parents.get(to) != null){
                parents.get(to).add(from);
            }else{
                CopyOnWriteArraySet<Integer> x = new CopyOnWriteArraySet<>();
                x.add(from);
                parents.put(to,x);
            }
            list_from.put(to,0);
            graph.put(from,list_from);
        }else if(!list_of_nodes.contains(to)){
            ConcurrentHashMap<Integer, Integer> list_to = getEmptyList(to);
            CopyOnWriteArraySet<Integer> children = new CopyOnWriteArraySet<>();
            children.add(from);
            parents.put(to,children);
            addNode(to, list_to);
            graph.put(to,list_to);
            graph.get(from).put(to,1);
            if(parents.get(from) != null){
                Iterator itr = parents.get(from).iterator();
                while (itr.hasNext()) {
                    Integer p = (Integer) itr.next();
                    updateParent(p, new CopyOnWriteArraySet<>());
                }
            }
        }else{
            if(graph.get(from).get(to) == 1){
                return;
            }else{
                graph.get(from).put(to,1);
                if(parents.get(to) != null){
                    parents.get(to).add(from);
                }else{
                    CopyOnWriteArraySet x = new CopyOnWriteArraySet();
                    x.add(from);
                    parents.put(to, x);
                }
                if(parents.get(from) != null){
                    Iterator itr = parents.get(from).iterator();
                    while (itr.hasNext()) {
                        Integer p = (Integer) itr.next();
                        updateParent(p, new CopyOnWriteArraySet<>());
                    }
                }
                updateParent(from, new CopyOnWriteArraySet<>());
            }
        }
        list_of_nodes.add(from);
        list_of_nodes.add(to);
        lock.unlock();
    }
}
