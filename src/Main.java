
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import Graph_Service.Graph;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, RemoteException {
        Registry registry  = LocateRegistry.createRegistry(6000);
        registry.rebind("graph", new Graph(true));
    }
}