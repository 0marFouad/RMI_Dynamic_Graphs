import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import Graph_Service.Graph_BFS;
import Graph_Service.Graph_BellmanFord;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, RemoteException {
        Registry registry  = LocateRegistry.createRegistry(6000);
        registry.rebind("graph_bfs", new Graph_BFS(true));
        registry.rebind("graph_bellman", new Graph_BellmanFord(true));
    }
}