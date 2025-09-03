n = int(input())
a = list(map(int, input().split()))

m = int(input())
b = list(map(int, input().split()))

a.sort()

def solve(arr, num):
    start, end = 0, len(arr) - 1

    while start <= end:
        mid = (start + end) // 2

        if arr[mid] == num:
            return 1
        
        elif arr[mid] < num:
            start = mid + 1
        
        else:
            end = mid - 1
    
    return 0

for num in b:
    print(solve(a, num))