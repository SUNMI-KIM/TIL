import sys
input = sys.stdin.readline

t = int(input())

def solve(n, m, p, ladder):
    x, y = m - 1, (p - 1) * 2
    while x >= 0:
        if y - 2 >= 0 and ladder[x][y - 1] == '+':
            y -= 2
        elif y + 2 < n * 2 - 1 and ladder[x][y + 1] == '+':
            y += 2
        x -= 1
    print((y // 2) + 1)

for _ in range(t):
    n, m, d = map(int, input().split())
    ladder = [list(input().rstrip('\n')) for _ in range(m)]
    solve(n, m, d, ladder)