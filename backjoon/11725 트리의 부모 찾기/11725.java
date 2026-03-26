import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class _11725 {

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();

		int n = Integer.parseInt(br.readLine());
		List<Integer>[] graph = new ArrayList[n + 1]; 
		for (int i = 1; i < n + 1; i++) {
			graph[i] = new ArrayList<>();
		}
		
		for (int i = 0; i < n - 1; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int n1 = Integer.parseInt(st.nextToken());
			int n2 = Integer.parseInt(st.nextToken());
			graph[n1].add(n2);
			graph[n2].add(n1);
		}
		
		
		int[] parent = solve(graph, n);
		for (int i = 2; i < n + 1; i++) {
			sb.append(parent[i]).append("\n");
		}
		System.out.println(sb.toString());
	}
	
	static int[] solve(List<Integer>[] graph, int n) {
		Queue<Integer> queue = new ArrayDeque<>();
		int[] parent = new int[n + 1];
		queue.add(1);
		
		while (!queue.isEmpty()) {
			int node = queue.poll();
			for (int next_node : graph[node]) {
				if (parent[next_node] > 0) continue;
				parent[next_node] = node;
				queue.add(next_node);
			}
		}
		return parent;
	}
}
