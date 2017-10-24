import java.util.Random;

public class main {
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

        System.out.println("Create vector of exact solutions:");
        double[] x = new double[n];
        for (int i = 0; i < x.length; i++) {
            x[i] = -100 + (200) * r.nextDouble();
            x[i] = Math.rint(x[i] * 100.0) / 100.0;
        }
        output(x);

        System.out.println("Give up vector f:");
        double[] f = new double[n];
        for (int i = 0; i < f.length; i++)
            for (int j = 0; j < f.length; j++)
                f[i] += arr[j][i] * x[j];
        output(f);

        System.out.println("Transpose matrix, ArrT");
        double[][] arrT = transposeMatrix(arr);
        System.out.println("ArrT*Arr:");
        double[][] arrT_arr = multiplicar(arrT, arr);
        output(arrT_arr);
        System.out.println("ArrT*f");
        double[] ArrT_f = new double[n];
        for (int i = 0; i < f.length; i++)
            for (int j = 0; j < f.length; j++)
                ArrT_f[i] += arrT[j][i] * x[j];
        output(ArrT_f);

        System.out.println("Vector answers:");
        double[] answer = mthdSmplOper(n, arr, x, Math.pow(10, -3));
        output(answer);
    }

    public static double[] mthdSmplOper(int n, double[][] matrix, double[] sol, double eps) {
        double[] preX = new double[n];

        double norm = 0;
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum = 0;
            for (int j = 0; j < n; j++)
                sum += matrix[i][j];
            if (sum > norm) norm = sum;
        }


        double[][] B = new double [n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (i == j) B[i][j] = 1 - (matrix[i][j] / norm);
                else B[i][j] = - (matrix[i][j] / norm);

        double[] G = new double [n];
        for (int i = 0; i < n; i++)
            G[i] = sol[i] / norm;

        int k = 0;
        while (true) {
            k++;
            double[] curX = new double[n];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    curX [i] += B[i][j] * preX[j];

            for (int i = 0; i < n; i++)
                curX[i] += G[i];

            double error = 0.0;
            for (int i = 0; i < n; i++)
                if (error < Math.abs(curX[i] - preX[i])) error = Math.abs(curX[i] - preX[i]);

            if (error < eps)  break;
            preX = curX;
        }

        return preX;
    }
}
