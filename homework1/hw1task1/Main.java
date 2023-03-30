import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] A = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = sc.nextInt();
        }

        int[] B = new int[n];
        for (int i = 0; i < n; i++) {
            B[i] = sc.nextInt();
        }
        sc.close();

        int i0 = 0;
        int j0 = 0;
        int imax = 0;
        int maxSum = A[i0] + B[j0];
        for (int i = 0; i < n; i++) {
            if (A[imax] < A[i]) imax = i;
            int tmpSum = A[imax] + B[i];
            if (tmpSum > maxSum) {
                maxSum = tmpSum;
                i0 = imax;
                j0 = i;
            }
        }
        System.out.println("" + i0 + " " + j0);
    }

}
