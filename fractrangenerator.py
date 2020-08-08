dimx = 30
dimy = 30

# generate primes to use as variables
primes = [2, 3]
def nextPrime():
    p = primes[-1]
    while True:
        p+=2
        if checkPrime(p):
            primes.append(p)
            return
def checkPrime(p):
    for q in primes:
        if p%q == 0:
            return False
    return True
while(len(primes) < 3*dimx*dimy+2):
    nextPrime()

variables = []
for i in range(3):
    variables.append([])
    for j in range(dimx):
        variables[i].append([])
        for k in range(dimy):
            variables[i][j].append(primes[i*dimx*dimy + j*dimy + k + 2])


program = ""
program += str(primes[0]) + "/" + str(primes[1]) + ", "

for i in range(dimx):
    for j in range(dimy):
        nominator = primes[1]*variables[2][i][j]
        for x in (1, 0, -1):
            xindex = i+x
            if xindex==-1 or xindex==dimx:
                continue
            for y in (1, 0, -1):
                yindex = j+y
                if yindex==-1 or yindex==dimy or (x==0 and y==0):
                    continue
                nominator *= variables[1][xindex][yindex]
            
        program += str(nominator) + "/" + str(variables[0][i][j] * primes[0]) + ", "

program += "1/"+str(primes[0]) + ", "

for i in range(dimx):
    for j in range(dimy):
        var0 = variables[0][i][j]
        var1 = variables[1][i][j]
        var2 = variables[2][i][j]
        
        program += "1/" + str(var1**7) + ", "
        program += "1/" + str(var1**6) + ", "
        program += "1/" + str(var1**4) + ", "
        program += str(var0) + "/" + str(var1**3) + ", "
        program += str(var0) + "/" + str(var1**2*var2) + ", "
        program += "1/" + str(var1) + ", "
        program += "1/" + str(var2) + ", "
        
        
print(program[:-2])

file = open("fractrancode.txt", "w+")
file.write(program[:-2])
file.close()









        
