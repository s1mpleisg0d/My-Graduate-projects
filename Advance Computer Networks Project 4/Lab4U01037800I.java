//import package
package PacketRouting;

import java.io.*;
import java.util.*;
//The class Packet routing

public class Lab4U01037800I {
	// allocates an amount of memory needed to store the object
	private static int dist[] = new int[100001];
	private static int p[] = new int[100001];
	// create array object
	private static ArrayList<ArrayList<Edge>> edges = new ArrayList<>();
	private static final long obj = 1000000000;

	private static class Edge {
		int to, cost;// Edge Cost

		/**
		 *
		 * Edge
		 *
		 * @param t the t
		 * @param c the c
		 * @return public
		 */
		public Edge(int t, int c) {
			to = t;
			cost = c;
		}
	}

	/*
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// read input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// read the first line of input,which contains the no of nodes and links
		String[] s = br.readLine().split(" ");
		int n = Integer.parseInt(s[0]);
		int m = Integer.parseInt(s[1]);
		for (int i = 0; i <= n; i++)
			// get all edges who are connected to this node
			edges.add(new ArrayList<>());
		// Initialize the variable
		int i = 0;
		while (i < m) {
			// read the remaining input lines which contain the links
			s = br.readLine().split(" ");
			int a = Integer.parseInt(s[0]);
			int b = Integer.parseInt(s[1]);
			int c = Integer.parseInt(s[2]);
			edges.get(a).add(new Edge(b, c));
			i++;
		}

		for (i = 0; i <= n; i++)
			dist[i] = (int) obj;
		// create a priority queue to store the nodes
		PriorityQueue<Edge> queue = new PriorityQueue<>(n, new Comparator<Edge>() {
			/**
			 *
			 * Compare
			 *
			 * @param a the a
			 * @param b the b
			 * @return int
			 */
			public int compare(Edge a, Edge b) {
				return a.cost - b.cost;
			}
		});

		queue.add(new Edge(1, 0));
		dist[1] = 0;
		// check priority queue empty or not
		while (!queue.isEmpty()) {
			Edge current = queue.poll();

			if (current.cost > dist[current.to])
				continue;
			// find Least Cost Path Routing
			for (Edge edge : edges.get(current.to)) {
				if (dist[edge.to] > dist[current.to] + edge.cost) {
					dist[edge.to] = dist[current.to] + edge.cost;
					p[edge.to] = current.to;
					queue.add(new Edge(edge.to, dist[edge.to]));
				}
			}
		}
		// assign node to cost
		int c = n;
		// create array list for Least Cost Path Routing
		ArrayList<Integer> LCPR = new ArrayList<>();
		while (c != 0) {
			LCPR.add(c);
			c = p[c];
		}
		// print the number of nodes in the route
		System.out.println(LCPR.size());
		Collections.reverse(LCPR);
		for (int i1 : LCPR)
			// print the nodes in the route in increasing order
			System.out.print(i1 + " ");
	}
}