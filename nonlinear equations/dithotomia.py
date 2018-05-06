import math
#common features
resultMethods = 0

def func(x):
    return pow(2, x) + 5*x - 3

def der_func(x):
    return pow(2, x) * math.log(2) + 5

#Method Dithotomia
def methodDithotomia(a, b):
    f = open("outputDithotomia.txt", "w")
    x0 = a
    x1 = b
    k = 1
    while abs(x0 - x1) > 1e-7:
        x2 = (x0 + x1) / 2

        f_x0 = func(x0)
        f_x1 = func(x1)
        f_x2 = func (x2)

        if f_x0 * f_x2 < 0:
            x1 = x2
        elif f_x2 * f_x1 < 0:
            x0 = x2

        resultMethods = x2
        f.write("Iteration: " + str(k) + '\n')
        f.write("Decide: " + str(x2) + '\n')
        f.write('\n')
        k += 1
    print(resultMethods)
    f.close()

#Method simple operation
def fi(x):
    return (3 - pow(2, x)) / 5

def der_fi(x):
    return -1 / 5 * (pow(2, x) * math.log(2))

def der_der_fi(x):
    return -0.0960906*2^x

def methodSimpleOperation(a, b):
    f = open("outputSimpleOperation.txt", "w")
    x0 = a + (a - b) / 2  #Точка на координатной прямой
    x1 = fi(x0)
    k = 1
    while abs(x0 - x1) > 1e-7:
        x0 = x1
        x1 = fi(x0)

        resultMethods = x1
        f.write("Iteration: " + str(k) + '\n')
        f.write("Decide: " + str(x1) + '\n')
        f.write('\n')
        k += 1
    print(resultMethods)
    f.close()

#Method Newton
def methodNewton(a, b):
    f = open("outputNewton.txt", "w")
    x0 = b
    x1 = x0 - func(x0) / der_func(x0)
    k = 1
    while abs(x0 - x1) > 1e-7:
        x0 = x1
        x1 = x0 - func(x0) / der_func(x0)

        resultMethods = x1
        f.write("Iteration: " + str(k) + '\n')
        f.write("Decide: " + str(x1) + '\n')
        f.write('\n')
        k += 1
    print(resultMethods)
    f.close()

methodDithotomia(0, 1)
methodSimpleOperation(0, 1)
methodNewton(0, 1)
q = abs(der_fi(1))
print("q = " + str(q))
keps = math.log(1e-7/0.5) / math.log(q)
print(keps)
keps = round(keps)
print("k(e) = " + str(keps + 1))
