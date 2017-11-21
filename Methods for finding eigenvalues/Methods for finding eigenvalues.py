import random
import numpy as np
import math
import numpy

N = 20

#fucntion converts arr1D to arr2D
def squareform_diagfill(arr1D):
    n = int(np.sqrt(arr1D.size*2))
    if (n*(n+1))//2!=arr1D.size:
        print ("Size of 1D array not suitable for creating a symmetric 2D array!")
        return None
    else:
        R,C = np.triu_indices(n)
        out = np.zeros((n,n),dtype=arr1D.dtype)
        out[R,C] = arr1D
        out[C,R] = arr1D
    return out

#create 2D array
def create_matrix():
    f = open('data.txt', 'w')
    arr1D = np.random.uniform(-100, 100,(210))
    arr1D = np.around(arr1D, decimals = 2)
    arr = squareform_diagfill(arr1D)
    f.write("Matrix A(20x20):\n")
    for row in arr:
        f.write('%s' % (' '.join('%03s' % i for i in row)) + "\n")
    f.close()

#pow Method for VMA
def powMethod():
    pre_lumbda = [1] * N
    cur_lumbda = [0] * N
    preVal = val = 0
    k = 1 # count Iteration

    with open("data.txt") as data:
        arr = [line.split() for line in data]
    arr.remove(['Matrix', 'A(20x20):'])
    arr = numpy.array(arr).astype("float")

    f = open('output_PowMethod.txt', 'w')
    while True:
        for i in range(N):
            cur_lumbda[i] = 0
            for j in range(N):
                cur_lumbda[i] += arr[i][j] * pre_lumbda[j]
        max_element = math.fabs(cur_lumbda[0])
        for i in range(N):
            if math.fabs(cur_lumbda[i]) > max_element:
                max_element = cur_lumbda[i]

        num = 0.0
        denom = 0.0
        for i in range(N):
            num += cur_lumbda[i] * cur_lumbda[i]
            denom += pre_lumbda[i] * pre_lumbda[i]
        val = math.sqrt(num / denom)

        for i in range(N):
            cur_lumbda[i] /= max_element

        f.write('Iteration: ' + str(k) + '\nVector:\n')
        for i in range(N):
            f.write(str(cur_lumbda[i]) + '  ')
        f.write('\n')

        if (val - preVal) < 1e-7:
            f.write('Iteration: ' + str(k) + '\n')
            f.write('Max_lumbda: ' + str(val) + '\n')
            f.write('Vector result:\n')
            for i in range(N):
                f.write(str(cur_lumbda[i]) + '  ')
            f.write('\n')
            print('Pow Method successfully completed')
            break

        for i in range(N):
            pre_lumbda[i] = cur_lumbda[i]
        preVal = val
        k += 1
    f.close()
    return k

#rotation Method for VMA
def rotationMethod():
    k = 0

    with open("data.txt") as data:
        arr = [line.split() for line in data]
    #Preparing matrix arr for method
    arr.remove(['Matrix', 'A(20x20):'])
    arr = numpy.array(arr).astype("float")

    while True:
        l = m = 0
        max_element = 0
        #Finding l and m elements
        for i in range(N):
            for j in range(N):
                x = math.fabs(arr[i][j])
                if i != j and x > max_element:
                    max_element = x
                    l = i
                    m = j

        #Finding angle fi
        angle = 0.5 * math.atan((2*arr[l][m]) / (arr[l][l] - arr[m][m]))

        #Building matrix U
        U1D = np.zeros(210)
        U = squareform_diagfill(U1D)
        for i in range(N):
            for j in range(N):
                if i == j:
                    U[i][j] = 1
        U[l][l] = math.cos(angle)
        U[l][m] = -math.sin(angle)
        U[m][m] = math.cos(angle)
        U[m][l] = math.sin(angle)

        #Doint iteration A(k + 1) = U(k).transpose * A(k) * U(k)
        arr = np.matmul(U.transpose(), arr)
        arr = np.matmul(arr, U)

        if max_element < 1e-7:
            f = open('output_RotationMethod.txt', 'w')
            f.write('End method' + ' iteration: ' + str(k) + "\n")
            for row in arr:
                f.write('%s' % (' '.join('%03s' % i for i in row)) + "\n")
            f.close()
            print('Rotation Method successfully completed')
            break

        k += 1

powMethod()
rotationMethod()
