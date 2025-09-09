import sys
input = sys.stdin.readline

n, k = map(int, input().split())
visited = set()
arr = ["?"] * (n)

def solve(n, k, arr):
    start = 0
    for _ in range(k):
        s, char = map(str, input().split())
        start = (int(s) + start) % n
        
        # 1) 해당 칸이 이미 채워져 있는데 다른 글자가 들어오려는 경우 → 모순
        if arr[start] != "?" and arr[start] != char:
            print("!")
            return

        # 2) 해당 칸은 비어 있는데, 이번에 쓰려는 글자가 이미 다른 칸에서 쓰인 경우 → 모순
        if arr[start] == "?" and char in visited:
            print("!")
            return
        
        visited.add(char)
        arr[start] = char
    
    for i in range(0, n):
        print(arr[(start - i) % n], end = "")
    return

solve(n, k, arr)