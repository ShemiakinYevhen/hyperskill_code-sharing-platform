import java.math.BigDecimal;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BigDecimal bigDecimal = new BigDecimal(scanner.nextLine());
        bigDecimal = bigDecimal.multiply(new BigDecimal(scanner.nextLine()));
        System.out.println(bigDecimal.toPlainString());
    }
}