
다익스트라 알고리즘은 특정한 하나의 정점에서 다른 모든 정점으로 가는 최단 경로를 탐색하는 알고리즘이다. 매 순간 가장 가중치가 적은 간선을 선택하여 탐색한다는 점에서 기본적으로 그리디 알고리즘으로 분류된다. 

- 간선의 가중치에 음수가 포함되어 있을 경우 다익스트라 알고리즘을 사용할 수 없다.
- 음의 간선이 포함된 단일 출발점 최단 경로를 구해야 할 때는 **벨만-포드(Bellman-Ford) 알고리즘**을 사용해야 한다.
- 만약 하나의 정점이 아닌, “모든 정점에서 모든 정점”으로의 최단 경로를 구하고 싶다면 **플로이드-워셜(Floyd-Warshall) 알고리즘**을 사용한다.

최소 신장 트리(MST)를 구하는 프림(Prim) 알고리즘과 동작 방식이 유사하다. 또한 너비 우선 탐색(BFS)과도 비슷하게 동작하는데, 우선 순위 큐를 사용하여 가중치가 가장 적은 간선을 먼저 탐색한다.

## 다익스트라 알고리즘 동작 과정

![](https://velog.velcdn.com/images/hariaus/post/3124ff58-42d2-4f91-8e66-a608b5725a7d/image.png)



위 그래프에서 A부터 D까지 가는 과정에 대해 알아볼 것이다. 

### 1. A의 모든 간선을 탐색한다.

시작 정점인 A에 연결된 모든 간선을 탐색한다. 방문하지 않은 정점들에 대해 해당 간선의 비용이 기존 비용보다 작다면, 최단 거리 배열을 갱신하고 우선순위 큐에 (도착 정점, 가중치)를 담는다.

예를 들어 A와 연결된 B와 C가 초기화되며, 이 중 C로 가는 간선의 비용이 가장 짧다고 가정하면 우선순위 큐에 의해 C가 먼저 처리될 준비를 한다.

![](https://velog.velcdn.com/images/hariaus/post/7ec31367-871d-4d0d-8224-17e03b01c62d/image.png)


### 2. C의 모든 간선을 탐색한다.

우선 순위 큐에서 가장 가중치가 적은 정점 C를 꺼내어 인접 간선을 탐색한다. 

이때 B로 가는 경로가 기존의 A → B 직접 경로보다 A → C → B를 거쳐가는 것이 더 짧다면, 최단 거리 배열의 B 값을 이 새로운 최소 가중치로 갱신하고 큐에 담는다. 처음 발견한 D가 있다면 이 역시 큐에 담는다. 

![](https://velog.velcdn.com/images/hariaus/post/bd076e50-0c9b-422f-90a9-b480a4c66066/image.png)


### 3. B의 모든 간선을 탐색한다.

다음으로 큐에서 꺼낸 B의 간선을 탐색한다. 

도착지인 D로 가는 경로를 계산할 때, 기존에 알던 경로 A → C → D 보다 A → C → B → D로 가는 경로가 더 짧다면 최단 거리 배열의 D 값을 최종적으로 갱신한다. 

### 4. 위 과정을 Queue가 빌 때까지 반복한다.

위의 과정을 우선순위 큐가 빌 때까지 반복한다. 계속해서 가장 작은 가중치를 가진 경로를 빼내며 탐색하고, 새로운 경로가 기존에 저장된 최단 거리보다 짧을 때만 배열의 값을 갱신해준다.

## 최종 코드 (Java)

```java
static int[] 다익스트라(int vCount, int eCount, ArrayList<Node>[] nodes, int start) {
		int[] arr = new int[vCount + 1];
		for (int i = 1; i < vCount + 1; i++) {
			arr[i] = Integer.MAX_VALUE;
		}
		arr[start] = 0;
		
		PriorityQueue<Node> queue = new PriorityQueue<>();
		queue.add(new Node(start, 0));
		
		while (!queue.isEmpty()) {
			Node node = queue.poll();
			int v = node.v;
			int w = node.w;
			
			if (w > arr[v]) continue;
			
			for (Node nextNode : nodes[v]) {
				int nv = nextNode.v;
				int nw = nextNode.w;
				if (w + nw < arr[nv]) {
					arr[nv] = w + nw;
					queue.add(new Node(nv, arr[nv]));
				}
				
			}
		}
		
		return arr;
	}
	
class Node implements Comparable<Node> {
	int v;
	int w;
	
	Node(int v, int w) {
		this.v = v;
		this.w = w;
	}
	
	@Override
	public int compareTo(Node node) {
		return this.w - node.w;
	}
}

```
