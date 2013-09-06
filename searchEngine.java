import java.util.*;
import java.io.*;

// This class implements a google-like search engine
public class searchEngine {

    public HashMap wordIndex;                  // this will contain a set of pairs (String, LinkedList of Strings)        
    public directedGraph internet;             // this is our internet graph
    public static double DAMPING_FACTOR = 0.5; // this value is suggested by the authors of Google   
        
        
    // Constructor initializes everything to empty data structures
    // It also sets the location of the internet files. 2013
    searchEngine() {
        // Below is the directory that contains all the internet files
        htmlParsing.internetFilesLocation = "internetFiles";
        wordIndex = new HashMap();                
        internet = new directedGraph();                                
    } // end of constructor
        
        
    // Returns a String description of a searchEngine
    public String toString () {
        return "wordIndex:\n" + wordIndex + "\ninternet:\n" + internet;
    }


    // This does a graph traversal of the internet, starting at the given url.
    // For each new vertex seen, it updates the wordIndex, the internet graph,
    // and the set of visited vertices.
        
    void traverseInternet(String url) throws Exception{
	
		try {
		
		/* remove "//" to see all scanned URLs printed to screen (fun) */
		//System.out.println(url);
		
		/*parse words found on website and store in new linked list */
		LinkedList webContentWords = new LinkedList(htmlParsing.getContent(url));

		/* parse URLs and store in new linked list */
		LinkedList webContentURLs = new LinkedList(htmlParsing.getLinks(url));

		/* Create iterators to go through linked lists */
		Iterator wordIterator = webContentWords.iterator();
		Iterator urlIterator = webContentURLs.iterator();
		
		
		/* put in global hash the pairs 'word, linkedlist of urls' */
		String word;
		
		/* iterate through words found on webpage */
		while (wordIterator.hasNext()) {
			word = (String) wordIterator.next();
			
			if (wordIndex.containsKey(word)) { // word exists as a key in hash
				((LinkedList) wordIndex.get(word)).add(url); //append url to end of linkedlist mapped to the word
			}
			else { // word doesn't exist as a key in hash, so create linkedlist and put the pair 'word, list' in hash
				LinkedList urlList = new LinkedList();
				urlList.add(url);
				wordIndex.put(word, urlList);
			}
		}
		
		/*add vertex to directedGraph if not already visited */
		if (!internet.getVisited(url)) {
			internet.addVertex(url);
			internet.setVisited(url, true);
		}
		
		/*add edges to directedGraph and do recursive call */
		while (urlIterator.hasNext()) {
			String nextURL = (String) urlIterator.next();
			
			if (!internet.getVisited(nextURL)) { //add vertex to directedGraph if nextURL not already visited
				internet.addVertex(nextURL);
				internet.setVisited(nextURL, true);
			}
			
			internet.addEdge(url, nextURL);
			
			traverseInternet(nextURL); // recursive call
		}
		
		} /* end of try */
		catch (Exception e) {} // if exception, do nothing
        
    } /* end of traverseInternet */


    /* This computes the pageRanks for every vertex in the internet graph.
       It will only be called after the internet graph has been constructed using 
       traverseInternet.
       Use the iterative procedure described in the text of the assignment to
       compute the pageRanks for every vertices in the graph. 
                      
       This method will probably fit in about 30 lines.
    */
    void computePageRanks() {
	
		/* create linkedlist of all vertices of directedGraph */
		LinkedList vertices = new LinkedList(internet.getVertices());
		
		/* create iterator to go through each vertices */
		Iterator vertIterator = vertices.iterator();
		
		/* initialize all pageranks to 1 */
		while (vertIterator.hasNext()) {
			internet.setPageRank((String) vertIterator.next(),1);
		}
		
		/* compute pageRank until convergence (100 times) */
		for (int i=0; i<100; i++) {
			vertIterator = vertices.iterator(); //reset iterator
			
			/* iterate through each vertex */
			while (vertIterator.hasNext()) {
			
				String vertex = (String) vertIterator.next(); //take one vertex from the iterator
				
				/* create linkedlist of all vertices that have edges toward the vertex */
				LinkedList edgesInto = new LinkedList(internet.getEdgesInto(vertex));
				/* create iterator to go through each vertice that have edge toward a given vertex */
				Iterator edgeIterator = edgesInto.iterator();
				
				
				/* compute pageRank */
				double pageRank = 0;
				
				while (edgeIterator.hasNext()) { // iterate through each edge going into the vertex and compute part of pageRank
					String edge = (String) edgeIterator.next();
					pageRank += (internet.getPageRank(edge) / internet.getOutDegree(edge));
				}
				
				pageRank = (1-DAMPING_FACTOR) + DAMPING_FACTOR * pageRank;
				internet.setPageRank(vertex, pageRank);
				
			} /* end while */
			
		} /* end for */
		
    } /* end of computePageRanks */
                
        
    /* Returns the URL of the page with the high page-rank containing the query word
       Returns the String "" if no web site contains the query.
       This method can only be called after the computePageRanks method has been executed.
       Start by obtaining the list of URLs containing the query word. Then return the URL 
       with the highest pageRank.
       This method should take about 25 lines of code.
    */
    String getBestURL(String query) {
        /* variables which will store the best URL to be returned and temporary url*/
		String bestURL;
		String tempURL;
		
		/* linkedlist to store the value of the hashmap. If query non-existent, return "" */
		if (wordIndex.get(query) == null) return "";
		LinkedList queryWebsites = new LinkedList((LinkedList) wordIndex.get(query));
		
		/* iterator to go through linked list */
		Iterator queryWebsitesIterator = queryWebsites.iterator();
		
		/* initialize bestURL and pageRank to first value of linked list */
		bestURL = (String) queryWebsitesIterator.next();
		double pageRank = internet.getPageRank(bestURL);
		
		/* update pageRank and bestURL if other values in linked list have higher pageRank */
		while (queryWebsitesIterator.hasNext()) {
			tempURL = (String) queryWebsitesIterator.next();
			double tempPageRank = internet.getPageRank(tempURL);
			if (pageRank < tempPageRank) {
				pageRank = tempPageRank;
				bestURL = tempURL;
			}
		}
		
		return bestURL;
		
    } // end of getBestURL
        

        

    // You shouldn't need to modify the main method, except maybe for debugging purposes
    public static void main(String args[]) throws Exception{                

        // create an object of type searchEngine
        searchEngine google = new searchEngine();

        // to debug your program, start with.
        //google.traverseInternet("http://www.cs.mcgill.ca/~blanchem/250/a.html");

        // When your program is working on the small example, move on to
        google.traverseInternet("http://www.cs.mcgill.ca/");

        // this is just for debugging purposes
       // System.out.println(google);

        google.computePageRanks();

        // this is just for debugging purposes
        //System.out.println(google);
                
        BufferedReader stndin = new BufferedReader( new InputStreamReader(System.in) );
        String query;
        do {
            System.out.print( "Enter query: " );
            query = stndin.readLine();
            if ( query != null && query.length() > 0 ) {
                System.out.println("Best site = " + google.getBestURL(query) + " (page rank = " + google.internet.getPageRank(google.getBestURL(query)) + ")");
            }
        } while ( query != null && query.length() > 0 );                                
    } // end of main
}
