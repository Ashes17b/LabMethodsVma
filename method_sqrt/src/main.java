import java.util.Random;

public class main {
    public static void output(double[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++)
                System.out.print(arr[i][j] + "  ");
            System.out.println();
        }
        System.out.println();
    }

    public static void output(double[] x) {
        for (int i = 0; i < x.length; i++)
            System.out.println(x[i]);
        System.out.println();
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
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                C[i][j] = 0;
            }
        }

        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr.length; j++)
                for (int k = 0; k < arr.length; k++) {
                    C[i][j] += arr[i][k] * arrT[k][j];
                    C[i][j] = Math.rint(C[i][j] * 100.0) / 100.0;
                }

        return C;
    }

    public static void main(String[] args) {
        int n = 20;
        double[][] arr = new double[n+1][n+1];

        Random r = new Random();
        //Create Matrix A
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                arr[i][j] = -100 + (200) * r.nextDouble();
                arr[i][j] = Math.rint(arr[i][j] * 100.0) / 100.0;
            }
        }
        System.out.println("Matrix Arr:");
        output(arr);

        double[][] arrT = transposeMatrix(arr);
        System.out.println("Matrix ArrT:");
        output(arrT);

        double[][] multiArr = multiplicar(arr, arrT);
        System.out.println("Matrix Arr*ArrT:");
        output(multiArr);

        System.out.println("Vector X:");
        double[] x = new double[n+1];
        for (int i = 0; i < x.length; i++) {
            x[i] = -100 + (200) * r.nextDouble();
            x[i] = Math.rint(x[i] * 100.0) / 100.0;
        }
        output(x);

        System.out.println("Vector F:");
        double f[] = new double[n+1];
        for (int j = 0; j <= n; j++)
            for (int i = 0; i <= n; i++)
                f[j] += multiArr[i][j] * x[i];
        output(f);

        double[] answer = methodSQRT(multiArr, f, n+1);
        System.out.println("Vector New_Answer:");
        output(answer);
        System.out.println("Vector Old_Answer:");
        output(x);
    }

    static double[] methodSQRT(double[][] arr, double[] f, int n) {
        double[][] s = new double[n][n];
        double[] x = new double[n];
        double[] y = new double[n];

        s[0][0] = Math.sqrt(arr[0][0]);
        for (int j = 1; j < n; j++) //s[1][j]
            s[0][j] = arr[0][j] / s[0][0];

        for (int i = 1; i < n; i++) //s[i][j]
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    double sum = 0;
                    for (int k = 0; k < i; k++)
                        sum += Math.pow(s[k][i], 2);
                    s[i][i] = Math.sqrt(arr[i][i] - sum);
                }
                if (i > j)
                    s[i][j] = 0;
                if (i < j) {
                    double sum = 0;
                    for (int k = 0; k < i; k++)
                        sum += s[k][i] * s[k][j];
                    s[i][j] = (arr[i][j] - sum) / s[i][i];
                }
            }
        System.out.println("Matrix S:");
        output(s);
        System.out.println("Matrix S*St");
        double temp[][] = transposeMatrix(s);
        output(multiplicar(temp, s));

        y[0] = f[0] / s[0][0];    // вычисление вектора y
        for (int i = 1; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < i; j++)
                sum += s[j][i] * y[j];
            y[i] = (f[i] - sum) / s[i][i];
        }

        x[n-1] = y[n-1] / s[n-1][n-1];  // вычисление решения x
        for (int i = n - 2; i >= 0; i--) {
            double sum = 0;
            for (int j = i; j < n; j++)
                sum += s[i][j] * x[j];
            x[i] = (y[i] - sum) / s[i][i];
        }

        System.out.println("Vector Y:");
        output(y);
        return x;
    }
}
