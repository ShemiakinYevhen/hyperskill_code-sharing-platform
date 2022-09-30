import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String ticketNumber = scanner.nextLine();

        int rightSum = 0;
        int leftSum = 0;

        for (int i = 0; i < 3; i++) {
            rightSum += ticketNumber.charAt(i);
            leftSum += ticketNumber.charAt(i + 3);
        }

        String message = rightSum == leftSum ? "Lucky" : "Regular";

        System.out.println(message);
    }
}