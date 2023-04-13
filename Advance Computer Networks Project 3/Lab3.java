import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Lab3 {
    static class Edge {
        private final int path, weight;

        public Edge(int path, int weight) {
            this.path = path;
            this.weight = weight;
        }

        public int getPath() {
            return path;
        }

        public int getWeight() {
            return weight;
        }
    }

    public static void readFile(Scanner sc, int[] p, List<Edge>[] edges, int N, int M) {
        for (int i = 0; i < N; i++) {
            edges[i] = new ArrayList<>();
        }

        String[] tokens = sc.nextLine().split(" ");

        for (int i = 0; i < N; i++) {
            p[i] = Integer.parseInt(tokens[i]);
            p[i]--;
        }

        for (int i = 0; i < M; i++) {
            tokens = sc.nextLine().split(" ");
            edges[Integer.parseInt(tokens[0]) - 1].add(new Edge(Integer.parseInt(tokens[1]) - 1, Integer.parseInt(tokens[2])));
            edges[Integer.parseInt(tokens[1]) - 1].add(new Edge(Integer.parseInt(tokens[0]) - 1, Integer.parseInt(tokens[2])));
        }

        sc.close();
    }


    public static boolean isValid(int l, int[] v, int[] p, List<Edge>[] edges) {
        Arrays.fill(v, -1);
        int counter = 0;

        for (int i = 0; i < v.length; i++) {
            if (v[i] < 0) {
                solve(i, counter++, l, v, edges);
            }
        }

        for (int i = 0; i < p.length; i++) {
            if (v[i] != v[p[i]]) {
                return false;
            }
        }
        return true;
    }

    public static void solve(int curr, int value, int low, int[] v, List<Edge>[] edges) {
        if (v[curr] == value)
            return;
        v[curr] = value;
        for (Edge e : edges[curr]) {
            if (e.getWeight() >= low) {
                solve(e.getPath(), value, low, v, edges);
            }
        }
    }

    public static void writeFile(int output) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File("storageout.txt"));
        pw.println(output);
        pw.close();
    }

    //main method for execution
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("storage.txt"));
        String[] tokens = sc.nextLine().split(" ");

        int N = Integer.parseInt(tokens[0]);
        int M = Integer.parseInt(tokens[1]);

        int[] p = new int[N];
        int[] v = new int[N];
        ArrayList<Edge>[] edges = new ArrayList[N];

        readFile(sc, p, edges, N, M);

        int low = 0;
        int high = Integer.MAX_VALUE;
        int mid;
        while (low != high) {
            mid = (low + high + 1) / 2;
            if (isValid(mid, v, p, edges)) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }

        writeFile(low);
    }
}