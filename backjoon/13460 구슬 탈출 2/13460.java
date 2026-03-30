import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class _13460 {

	static int[] dx = { 0, 0, 1, -1 };
	static int[] dy = { 1, -1, 0, 0 };

	static int n;
	static int m;
	static char[][] arr;

	static int min;

	public static void main(String[] args) throws Exception {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		arr = new char[n][];
		min = Integer.MAX_VALUE;

		Pair red = null;
		Pair blue = null;

		for (int i = 0; i < n; i++) {
			arr[i] = br.readLine().toCharArray();
			for (int r = 0; r < m; r++) {
				if (arr[i][r] == 'B') {
					blue = new Pair(i, r);
					arr[i][r] = '.';
				} else if (arr[i][r] == 'R') {
					red = new Pair(i, r);
					arr[i][r] = '.';
				}
			}
		}
		solve(1, red, blue);
		if (min == Integer.MAX_VALUE)
			min = -1;
		System.out.println(min);
	}

	static void solve(int depth, Pair red, Pair blue) {
		if (depth >= min) return;
	
		if (depth > 10)
			return;

		for (int i = 0; i < 4; i++) {
			Pair preRed = new Pair(red.x, red.y);
			Pair preBlue = new Pair(blue.x, blue.y);

			// 여기에 판별 로직 적용
			boolean status = lean(i, preRed, preBlue);
			if (status) {
				if (arr[preBlue.x][preBlue.y] == 'O') continue;
				if (arr[preRed.x][preRed.y] == 'O') {
					min = Math.min(min, depth);
					continue;
				}
			}
			solve(depth + 1, preRed, preBlue);
		}
	}

	static boolean lean(int i, Pair red, Pair blue) {
		boolean status = false;

		int red_count = 0;
		while (true) {
			int nx = red.x + dx[i];
			int ny = red.y + dy[i];
			if (nx < 0 || nx >= n || ny < 0 || ny >= m)
				break;
			if (arr[nx][ny] == '#')
				break;
			if (arr[nx][ny] == 'O') {
				status = true;
				red.x = nx;
				red.y = ny;
				break;
			}
			red_count++;
			red.x = nx;
			red.y = ny;
		}

		int blue_count = 0;
		while (true) {
			int nx = blue.x + dx[i];
			int ny = blue.y + dy[i];
			if (nx < 0 || nx >= n || ny < 0 || ny >= m)
				break;
			if (arr[nx][ny] == '#')
				break;
			if (arr[nx][ny] == 'O') {
				status = true;
				blue.x = nx;
				blue.y = ny;
				break;
			}
			blue_count++;
			blue.x = nx;
			blue.y = ny;
		}

		if (red.x == blue.x && red.y == blue.y) {
		    if (arr[red.x][red.y] != 'O') {
		        if (red_count > blue_count) {
		            red.x -= dx[i]; 
		            red.y -= dy[i];
		        } else {
		            blue.x -= dx[i];
		            blue.y -= dy[i];
		        }
		    }
		}
		return status;
	}
}

class Pair {
	int x;
	int y;

	Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}
}