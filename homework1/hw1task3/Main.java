import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] A = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = sc.nextInt();
        }
        int m = sc.nextInt();
        int[] B = new int[m];
        for (int i = 0; i < m; i++) {
            B[i] = sc.nextInt();
        }
        int k = sc.nextInt();

        sc.close();

        int result = 0;
        int i = 0;
        int j = m - 1;

        while (i < n && j >= 0) {
            int sumAB = A[i] + B[j];
            if (sumAB == k) {
                result++;
                j--;
                i++;
            } else if (sumAB < k) {
                i++;
            } else {
                j--;
            }
        }

        System.out.println(result);

    }

}
