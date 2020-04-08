package Testing;

import Graph_Service.Graph_BFS;
import Graph_Service.Graph_BellmanFord;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;

import static org.junit.Assert.assertTrue;

public class SingleThreadedGraphBellmanFord {
    @org.junit.Test
    public void Test_Exist_Edge() throws FileNotFoundException, RemoteException {
        Graph_BellmanFord g = new Graph_BellmanFord("src/Testing/graph_add.txt");
        int distance = g.query(1,2);
        assertTrue(distance==1);
    }

    @org.junit.Test
    public void Test_Not_Exist_Edge() throws FileNotFoundException, RemoteException {
        Graph_BellmanFord g = new Graph_BellmanFord("src/Testing/graph_add.txt");
        int distance = g.query(1,4);
        assertTrue(distance==0);
    }

    @org.junit.Test
    public void Test_Add_Edge1() throws FileNotFoundException, RemoteException {
        Graph_BellmanFord g = new Graph_BellmanFord("src/Testing/graph_add.txt");
        g.add(2, 4);
        int distance = g.query(1,4);
        assertTrue(distance==2);
    }

    @org.junit.Test
    public void Test_Add_Edge2() throws FileNotFoundException, RemoteException {
        Graph_BellmanFord g = new Graph_BellmanFord("src/Testing/graph_add.txt");
        g.add(4, 2);
        g.add(2, 4);
        int distance = g.query(1,4);
        assertTrue(distance==2);
    }

    @org.junit.Test
    public void Test_Add_Edge3() throws FileNotFoundException, RemoteException {
        Graph_BellmanFord g = new Graph_BellmanFord("src/Testing/graph_add.txt");
        g.add(2, 4);
        g.add(1, 4);
        int distance = g.query(1,4);
        assertTrue(distance==1);
    }

    @org.junit.Test
    public void Test_Delete_Edge1() throws FileNotFoundException, RemoteException {
        Graph_BellmanFord g = new Graph_BellmanFord("src/Testing/graph_add.txt");
        g.add(2, 4);
        g.add(1, 4);
        g.delete(1,4);
        int distance = g.query(1,4);
        assertTrue(distance==2);
    }

    @org.junit.Test
    public void Test_Delete_Edge2() throws FileNotFoundException, RemoteException {
        Graph_BellmanFord g = new Graph_BellmanFord("src/Testing/graph_add.txt");
        g.delete(1,2);
        int distance = g.query(1,2);
        assertTrue(distance==2);
    }

    @org.junit.Test
    public void Test_Hard_Graph1() throws FileNotFoundException, RemoteException {
        Graph_BellmanFord g = new Graph_BellmanFord("src/Testing/graph_complex.txt");
        int distance = g.query(1,7);
        assertTrue(distance==2);
    }

    @org.junit.Test
    public void Test_Hard_Graph2() throws FileNotFoundException, RemoteException {
        Graph_BellmanFord g = new Graph_BellmanFord("src/Testing/graph_complex.txt");
        int distance = g.query(1,5);
        assertTrue(distance==3);
    }

    @org.junit.Test
    public void Test_Hard_Graph3() throws FileNotFoundException, RemoteException {
        Graph_BellmanFord g = new Graph_BellmanFord("src/Testing/graph_complex.txt");
        g.delete(2,4);
        g.add(4,6);
        int distance = g.query(6,4);
        assertTrue(distance==Integer.MAX_VALUE);
    }

    @org.junit.Test
    public void Test_Custom_Graph1() throws FileNotFoundException, RemoteException {
        Graph_BellmanFord g = new Graph_BellmanFord("src/Testing/graph_test.txt");
        int distance = g.query(5,6);
        assertTrue(distance==Integer.MAX_VALUE);
        distance = g.query(0,9);
        assertTrue(distance==2);
        g.add(1,3);
        distance = g.query(1,10);
        assertTrue(distance==Integer.MAX_VALUE);
        distance = g.query(2,10);
        assertTrue(distance==Integer.MAX_VALUE);
        distance = g.query(10,6);
        assertTrue(distance==2);
        g.add(5,10);
        distance = g.query(5,6);
        assertTrue(distance==3);
    }
}
