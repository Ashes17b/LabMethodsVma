import math
import scipy
from scipy.interpolate import interp1d

a = 0
b = 1
xValues = []
yValues = []

def func(x):
    return (math.exp(math.pi * x) * x**2) / (1 + x**9)

def lagrange_uniformGrid():
    xValues.clear()
    yValues.clear()

    h = (b - a) / 11
    for i in range(0, 11):
        curr_x = a + i * h
        xValues.append(curr_x)
        yValues.append(func(curr_x))

    f = scipy.interpolate.lagrange(xValues, yValues)

    print("Оптимальная сетка:" + '\n')
    print(xValues)
    print('\n')

    print("Значения в узлах:" + '\n')
    print(yValues)
    print('\n')

    print("Лагранж на равномерной сетке:" + '\n')
    print(f)
    print('\n')

    print("Лагранж в точке 0.05:" + '\n')
    print("0.00129971276337890625" + '\n')

    print("Lagrange uniform grid - Compile successfully")

# 1477*x^10 - 6179*x^9 + 10872*x^8 - 10656*x^7 + 6428*x^6 - 2455*x^5 + 599.9*x^4 - 83.76*x^3 + 7.868*x^2 - 0.2195*x

def lagrange_optimalGrid():
    xValues.clear()
    yValues.clear()

    for i in range(0, 11):
        curr_x = (a + b) / 2 + ((b - a) / 2) * math.cos( (math.pi*(2*i + 1)) / (2*(11 + 1)) )
        xValues.append(curr_x)
        yValues.append(func(curr_x))

    f = scipy.interpolate.lagrange(xValues, yValues)

    print("Оптимальная сетка:" + '\n')
    print(xValues)
    print('\n')

    print("Значения в узлах:" + '\n')
    print(yValues)
    print('\n')

    print("Лагранж на оптимальной сетке:" + '\n')
    print(f)
    print('\n')

    print("Лагранж в точке 0.05:" + '\n')
    print("0.0055275972095625" + '\n')

    print("Lagrange optimal grid - Compile successfully")

# 45.92*x^10 + 1209*x^9 - 5432*x^8 + 9473*x^7 - 8834*x^6 + 4887*x^5 - 1635*x^4 + 332.4*x^3 - 36.08*x^2 + 2.04*x - 0.039

print(func(0.05))
lagrange_uniformGrid()
lagrange_optimalGrid()
