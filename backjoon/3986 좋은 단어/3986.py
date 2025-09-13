import sys
input = sys.stdin.readline

def solve(string):
    stack = []  
    
    for s in string:
        if stack and stack[-1] == s:  
            stack.pop() 
        else:
            stack.append(s)  

    return 1 if not stack else 0  

n = int(input())
ans = 0
for _ in range(n):
    string = input().rstrip()
    ans += solve(string)
print(ans)