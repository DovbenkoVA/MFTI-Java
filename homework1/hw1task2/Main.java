import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] x = new int[n];
        int[] y = new int[n];

        for (int i = 0; i < n; i++) {
            x[i] = scanner.nextInt();
            y[i] = scanner.nextInt();

        }
        scanner.close();

        double polygonArea = 0;
        for (int i = 0; i < n - 1; i++) {
            polygonArea += x[i] * y[i + 1] - x[i + 1] * y[i];
        }
        polygonArea = Math.abs(polygonArea + x[n - 1] * y[0] - x[0] * y[n - 1]) / 2;

        System.out.println(polygonArea);
    }

}

