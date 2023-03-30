import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        scanner.close();

        int[] participants = new int[n];
        for (int i = 0; i < n; i++) {
            participants[i] = i + 1;
        }
        int result = 0;
        for (int i : participants) {
            result = (result + k) % i;
        }
        result++;
        System.out.println(result);
    }
}
