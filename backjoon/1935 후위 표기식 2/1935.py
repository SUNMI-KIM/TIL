import sys
input = sys.stdin.readline

n = int(input())
formula = input().rstrip()
dic = {}
for i in range(n):
    dic[chr(ord("A") + i)] = input().rstrip()

def solve(formula, dic):
    stack = []
    
    for f in formula:
        if f == "*":
            stack.append(stack.pop() * stack.pop())
        elif f == "/":
            second = stack.pop()
            first = stack.pop()
            stack.append(first / second)
        elif f == "+":
            stack.append(stack.pop() + stack.pop())
        elif f == "-":
            second = stack.pop()
            first = stack.pop()
            stack.append(first - second)
        else:
            stack.append(dic[f])
    
    return stack[0]

def eval_solve(formula, dic):
    stack = []
    
    for f in formula:
        if f in dic:
            stack.append(dic[f])
        else:
            second = stack.pop()
            first = stack.pop()
            stack.append(eval(f"{first}{f}{second}"))
    return stack[0]

print(f"{solve(formula, dic):.2f}")