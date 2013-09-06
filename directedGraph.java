import java.util.*;

class directedGraph {
    
    /* The graph is stored in a hash table (called HashMap in Java).
       The vertices are identified by a String, which is used as key in the
       hash table. The information associated to each vertex v is a LinkedList of
       Strings, describing all vertices that v links to v */
    
    HashMap vertices;
    
    /* The key of that hash table is a String (URL) and the value is a boolean indicating
       if the vertex has been set to visited or not */
    HashMap visited;
    
    /* We use this hash table to store page ranks */
    HashMap pageRank;
    
    // Constructor, creates an empty graph
    public directedGraph() {
        vertices = new HashMap();
        visited = new HashMap();
        pageRank = new HashMap();
    } 
    

    // Adds a vertex v to the graph
    public void addVertex( String v ) {
        // if the vertex is already present, don't add it
        if ( vertices.containsKey(v) ) return;
        vertices.put( v, new LinkedList() );
    } // end of addVertex
    
    
    // Adds an edge from v1 to v2
    public void addEdge(String v1, String v2) {
        // if key1 or key2 are not in the graph, add them.
        if (!vertices.containsKey(v1)) addVertex(v1);
        if (!vertices.containsKey(v2)) addVertex(v2);

        // if the edge is not already in the list of neighbors of key1, add it
        // notice that we need to cast the result of the get method 
        LinkedList edges = (LinkedList)vertices.get(v1);
        if (!edges.contains(v2)) edges.addLast(v2);
    } // end of addEdge 
    
    
    // Returns a LinkedList of vertices that are the neighbors of key
    public LinkedList getNeighbors(String v) {
        return (LinkedList) vertices.get(v);
    } // end of getNeighbors
    
    
    // Returns a LinkedList of all vertices in the graph
    public LinkedList getVertices() {
        Iterator i = vertices.keySet().iterator();
        // since keySet return a Set, we need to transfer it to a LinkedList 
        LinkedList l = new LinkedList();
        while ( i.hasNext() ) l.addLast( i.next() );
        return l;
    } // end of getVertices
    
    // Returns the list of vertices that have links to v
    LinkedList getEdgesInto( String v ) {
        LinkedList l = new LinkedList();
        Iterator i = vertices.keySet().iterator();
        while (i.hasNext()) {
            String s = (String) i.next();
            if ( ( (LinkedList)vertices.get(s) ).contains(v)) l.addLast(s);
        }
        return l;
    } // end of getEdgesInto
    
    // Returns the numbers of edges going out of vertex v
    int getOutDegree(String v) {
        return ( (LinkedList)vertices.get(v) ).size();
    }        
    
    // returns the page rank of a given vertex. If the vertex doesn't exist, returns 0
    double getPageRank(String url) {
        if ( pageRank.containsKey(url) ) return ( (Double)pageRank.get(url) ).doubleValue();
        else return 0;
    }


    // sets the pageRank of a given vertex
    void setPageRank(String url, double pr) {
        pageRank.put(url,new Double(pr));
    }


    // returns the visited status of a given url
    boolean getVisited(String url) {
        if (visited.containsKey(url) && ( (Boolean)visited.get(url) ).booleanValue()) return true;
        else return false;
    }

    // sets the visited status of a given url
    void setVisited(String url, boolean b) {
        visited.put(url,new Boolean(b));
    }

    public String toString() {
        return "Vertices: \n" + vertices.toString() + 
            "\nVisited:\n"+visited.toString()+
            "\npageRanks:\n"+pageRank;
    }
}
        
    
        
        
        
        
