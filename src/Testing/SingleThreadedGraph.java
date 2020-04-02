package Testing;
import Graph_Service.Graph;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;

import static org.junit.Assert.*;

public class SingleThreadedGraph {
    @org.junit.Test
    public void Test_Exist_Edge() throws FileNotFoundException, RemoteException {
        Graph g = new Graph("src/Testing/graph_add.txt");
        int distance = g.query(1,2);
        assertTrue(distance==1);
    }

    @org.junit.Test
    public void Test_Not_Exist_Edge() throws FileNotFoundException, RemoteException {
        Graph g = new Graph("src/Testing/graph_add.txt");
        int distance = g.query(1,4);
        assertTrue(distance==0);
    }

    @org.junit.Test
    public void Test_Add_Edge1() throws FileNotFoundException, RemoteException {
        Graph g = new Graph("src/Testing/graph_add.txt");
        g.add(2, 4);
        int distance = g.query(1,4);
        assertTrue(distance==2);
    }

    @org.junit.Test
    public void Test_Add_Edge2() throws FileNotFoundException, RemoteException {
        Graph g = new Graph("src/Testing/graph_add.txt");
        g.add(4, 2);
        int distance = g.query(1,4);
        assertTrue(distance==0);
    }

    @org.junit.Test
    public void Test_Add_Edge3() throws FileNotFoundException, RemoteException {
        Graph g = new Graph("src/Testing/graph_add.txt");
        g.add(2, 4);
        g.add(1, 4);
        int distance = g.query(1,4);
        assertTrue(distance==1);
    }

    @org.junit.Test
    public void Test_Delete_Edge1() throws FileNotFoundException, RemoteException {
        Graph g = new Graph("src/Testing/graph_add.txt");
        g.add(2, 4);
        g.add(1, 4);
        g.delete(1,4);
        int distance = g.query(1,4);
        assertTrue(distance==2);
    }

    @org.junit.Test
    public void Test_Delete_Edge2() throws FileNotFoundException, RemoteException {
        Graph g = new Graph("src/Testing/graph_add.txt");
        g.delete(1,2);
        int distance = g.query(1,2);
        assertTrue(distance==2);
    }

    @org.junit.Test
    public void Test_Hard_Graph1() throws FileNotFoundException, RemoteException {
        Graph g = new Graph("src/Testing/graph_complex.txt");
        int distance = g.query(1,7);
        assertTrue(distance==2);
    }

    @org.junit.Test
    public void Test_Hard_Graph2() throws FileNotFoundException, RemoteException {
        Graph g = new Graph("src/Testing/graph_complex.txt");
        int distance = g.query(1,5);
        assertTrue(distance==3);
    }
}
