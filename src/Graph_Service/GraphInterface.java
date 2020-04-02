package Graph_Service;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GraphInterface extends Remote {
    int query(int from, int to) throws RemoteException;
    void delete(int from, int to) throws RemoteException;
    void add(int from , int to) throws RemoteException;
}