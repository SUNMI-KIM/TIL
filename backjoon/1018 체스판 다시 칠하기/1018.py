import sys
input = sys.stdin.readline

def check(x, y, board):
    ans = 0

    for nx in range(x, x + 8):
        for ny in range(y, y + 8):
            if (nx + ny) % 2 == 0 and board[nx][ny] != "W":
                ans += 1
            elif (nx + ny) % 2 != 0 and board[nx][ny] != "B":
                ans += 1

    return min(ans, 64 - ans)

n, m = map(int, input().split())
board = [list(str(input().strip())) for _ in range(n)]
ans = float("inf")
for x in range(n - 7):
    for y in range(m - 7):
        ans = min(check(x, y, board), ans)
print(ans)