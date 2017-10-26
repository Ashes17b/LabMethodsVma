import java.util.Random;

public class main {
    private static int k = 0;
    private static double ke = 0;
    private static double q = 0.1;
    private static double qOptimal = q;
    private static int remIteration = Integer.MAX_VALUE;

    public static void output(double[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++)
                System.out.print(arr[i][j] + " ");
            System.out.println();
        }
        System.out.println();
    }
    public static void output(double[] vector) {
        for (int i = 0; i < vector.length; i++)
            System.out.println(vector[i]);
        System.out.println();
    }
    public static void output(double[] a, double[] b) {
        for (int i = 0; i < a.length; i++)
            System.out.println("X = " + a[i] + " Answer = " + b[i]);
    }

    public static double[][] transposeMatrix(double [][] arr){
        double[][] temp = new double[arr[0].length][arr.length];
        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr[0].length; j++)
                temp[j][i] = arr[i][j];
        return temp;
    }

    public static double[][] multiplicar(double[][] arr, double[][] arrT) {
        double[][] C = new double[arr.length][arr.length];
        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr.length; j++)
                C[i][j] = 0;

        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr.length; j++)
                for (int k = 0; k < arr.length; k++) {
                    C[i][j] += arr[i][k] * arrT[k][j];
                    C[i][j] = Math.rint(C[i][j] * 100.0) / 100.0;
                }

        return C;
    }
    public static double[] multiplicar(double[][] arr, double[] x) {
        int n = arr.length;
        double[] f = new double[n];
        for (int j = 0; j < f.length; j++)
            for (int i = 0; i < f.length; i++)
                f[j] += arr[j][i] * x[i];

        return f;
    }

    public static double norma(double[] a) {
        double result = 0;
        for (int i = 0; i < a.length; i++)
            if (Math.abs(a[i]) > result)
                result = Math.abs(a[i]);

        return result;
    }
    public static double norma(double[][] a) {
        double result = 0;
        for (int i = 0; i < a.length; i++) {
            double sum = 0;
            for (int j = 0; j < a.length; j++)
                sum += Math.abs(a[i][j]);
            if (result < sum)
                result = sum;
        }

        return result;
    }

    public static void main(String[] args) {
        int n = 20;
        Random r = new Random();

        System.out.println("Create matrix with diagonal prevalence:");
        double[][] arr = new double[n][n];
        for (int i = 0; i < arr.length; i++) {
            double sum = 0;
            double tmp = -100 + (200) * r.nextDouble();
            int rememberPos = 0;
            for (int j = 0; j < arr.length; j++) {
                if (i != j) {
                    while(Math.abs(tmp) > (99.99 / n - 1))
                        tmp = -100 + (200) * r.nextDouble();
                    arr[i][j] = Math.rint(tmp * 100.0) / 100.0;
                    sum += Math.abs(arr[i][j]);
                    tmp = -100 + (200) * r.nextDouble();
                }
                else
                    rememberPos = j;
            }
            tmp = -100 + (200) * r.nextDouble();
            while(Math.abs(tmp) < sum)
                tmp = -100 + (200) * r.nextDouble();
            arr[rememberPos][rememberPos] = Math.rint(tmp * 100.0) / 100.0;
        }
        output(arr);

        double[] x = new double[n];
        for (int i = 0; i < x.length; i++) {
            x[i] = -100 + (200) * r.nextDouble();
            x[i] = Math.rint(x[i] * 100.0) / 100.0;
        }

        System.out.println("Calculate vector f:");
        double[] f = multiplicar(arr, x);
        output(f);

        System.out.println("Transpose matrix, ArrT");
        double[][] arrT = transposeMatrix(arr);
        System.out.println("Calculate ArrT*Arr:");
        double[][] arrT_arr = multiplicar(arrT, arr);
        output(arrT_arr);
        System.out.println("Calculate ArrT*f");
        double[] arrT_f = multiplicar(arrT, f);
        output(arrT_f);

        commonStep(arr, f);
        commonStep(arrT_arr, arrT_f);

        mthdSmpl(arr, f, 1e-7, x);

        for (q = 0.1; q < 2; q += 0.1) {
            q = Math.rint(q * 10.0) / 10.0;
            mthdRelax(arrT_arr, arrT_f, 1e-7, x);
        }
        System.out.println("qOptimal = " + qOptimal);
        double tmp = qOptimal;
        for (q = tmp + 0.01; q < tmp + 0.10; q += 0.01) {
            q = Math.rint(q * 100.0) / 100.0;
            mthdRelax(arrT_arr, arrT_f, 1e-7, x);
        }
        System.out.println("qNextOptimal = " + qOptimal);
    }

    public static double[][] calculateB(double[][] a) {
        int n = a.length;
        double[][] B = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (j != i)
                    B[i][j] = -(a[i][j] / a[i][i]);
                else
                    B[i][j] = 0;
            }

        return B;
    }
    public static double[] calculateG(double[][] a, double[] f) {
        int n = a.length;
        double[] g = new double[n];
        for (int i = 0; i < n; i++)
            g[i] = f[i] / a[i][i];

        return g;
    }

    private static void commonStep(double[][] arr, double[] f) {
        /** Finding b[i][j] for matrix B */
        System.out.println("Matrix B:");
        double[][] B = calculateB(arr);
        output(B);

        /** Finding g[i] of vector G*/
        System.out.println("Vector g:");
        double[] g = calculateG(arr, f);
        output(g);
    }

    private static void mthdSmpl(double[][] arr, double[] f, double eps, double[] answer) {
        int n = arr.length;
        double[] x = new double[n];

        double[][] B = calculateB(arr);
        double[] g = calculateG(arr, f);

        while(true) {
            k++;
            double[] curX = new double[n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++)
                    if (j != i)
                        curX[i] += B[i][j] * x[j];
                curX[i] += g[i];
            }

            double[] error = new double[n];
            for (int i = 0; i < n; i++)
                error[i] = curX[i] - x[i];

            double[] test = new double[n];
            for (int i = 0; i < n; i++)
                test[i] = curX[i] - answer[i];

            /** Method evaluation
            double rightGrade = (Math.pow(norma(B), k + 1) * norma(g)) / (1 - norma(B));
            System.out.println("Norma(test) = " + norma(test));
            System.out.println("RightGrade = " + rightGrade);
            if (norma(test) <= rightGrade)
                System.out.println("Good");
            else
                System.out.println("Bad"); */

            if (norma(error) <= eps)
                break;

            x = curX;
        }

        /** Calculation k(e) */
        ke = Math.log10((eps * (1 - norma(B))) / norma(g)) / Math.log10(norma(B));

        System.out.println("\nIterations: " + k);
        System.out.println("Norma ||B|| = " + norma(B));
        System.out.println("Norma ||g|| = " + norma(g));
        System.out.println("K(e) iterations: " + ke);
        System.out.println("Calculate vector answers:");
        output(answer, x);
    }

    private static void mthdRelax(double[][] arrT_arr, double[] arrT_f, double eps, double[] answer) {
        int n = arrT_arr.length;
        double[] x = new double[n];

        double[][] B = calculateB(arrT_arr);
        double[] g = calculateG(arrT_arr, arrT_f);

        k = 0;
        while (true) {
            k++;
            double[] curX = new double[n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (j < i)
                        curX[i] += B[i][j] * curX[j];
                    else
                        curX[i] += B[i][j] * x[j];
                }
                curX[i] += g[i];
                curX[i] = (1 - q) * x[i] + q * curX[i];
            }

            double[] error = new double[n];
            for (int i = 0; i < n; i++)
                error[i] = curX[i] - x[i];

            if ((norma(error) / q) <= eps)
                break;

            x = curX;
        }

        if (remIteration >= k) {
            remIteration = k;
            qOptimal = q;
        }

        System.out.println("\nIterations = " + k);
        System.out.println("q  = " + q);
        System.out.println("Calculate vector answers:");
        output(answer, x);
    }
}