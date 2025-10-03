import sys
input = sys.stdin.readline

direction_map = {
    'F': {'F': 'F', 'R': 'R', 'L': 'L', 'B': 'B'},
    'R': {'F': 'R', 'R': 'B', 'L': 'F', 'B': 'L'},
    'L': {'F': 'L', 'R': 'F', 'L': 'B', 'B': 'R'},
    'B': {'F': 'B', 'R': 'L', 'L': 'R', 'B': 'F'}
}

dx = {'F': 1, 'B' : -1, 'R' : 0, 'L' : 0}
dy = {'F': 0, 'B' : 0, 'R' : 1, 'L' : -1}

match_direction = {'F': 0, 'B' : 1, 'R' : 2, 'L' : 3}
match_number = {0 : "F", 1 : "B", 2 : "R", 3 : "L"}

from collections import deque

'''def solve(m, visited):
    queue = deque()
    queue.append((0, 0, 'B'))

    while queue:
        x, y, d = queue.popleft()
        md, count = m[x][y][0], int(m[x][y][1])
        
        nd = direction_map[d][md]
        nx, ny = x + dx[nd] * count, y + dy[nd] * count

        visited[x][y][nd] += 1
        if visited[x][y][nd] > 1:
            return (x, y)

        queue.append((nx, ny, nd))'''

def solve(m, visited):
    queue = deque()
    queue.append((0, 0, 'B'))

    while queue:
        x, y, d = queue.popleft()
        md, count = m[x][y][0], int(m[x][y][1])
        
        nd = match_number[(match_direction[md] + match_direction[d]) % 4]
        nx, ny = x + dx[nd] * count, y + dy[nd] * count

        visited[x][y][nd] += 1
        if visited[x][y][nd] > 1:
            return (x, y)

        queue.append((nx, ny, nd))
            
t = int(input())
for _ in range(t):
    n = int(input())
    m = [list(map(str, input().split())) for _ in range(n)]
    visited = [[{'F': 0, 'B' : 0, 'R' : 0, 'L' : 0} for __ in range(n)] for __ in range(n)]
    x, y = solve(m, visited)
    print(x, y)

    