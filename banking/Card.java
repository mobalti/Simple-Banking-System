package banking;

import java.util.Random;

public class Card {
    private static final Random random = new Random();


    static String generatePin() {
        return String.format("%04d", random.nextInt(10000));
    }

    static String generateCardNumber() {
        String cardNumber =" ";
        String controlNumber = "4000004"
                .concat(String.format("%08d", random.nextInt(100_000_000)));
        int sum = 0;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < controlNumber.length(); i = i + 2) {
            int oddNumber = Integer.parseInt(String.valueOf(controlNumber.charAt(i)));
            oddNumber *= 2;
            if (oddNumber > 9) {
                oddNumber -= 9;
            }
            sb.append(oddNumber);
            if (i + 1 != controlNumber.length()) {
                int evenNumber = Integer.parseInt(String.valueOf(controlNumber.charAt(i + 1)));
                sb.append(evenNumber);
            }
        }


        for (int i = 0; i < sb.length(); i++) {
            sum += Integer.parseInt(String.valueOf(sb.charAt(i)));
        }


        int checksum;
        for (int i = 0; i < 10; i++) {
            checksum = i;
            if ((sum + checksum) % 10 == 0) {
                cardNumber = controlNumber.concat(String.valueOf(checksum));
                break;
            }
        }
        return cardNumber;
    }


    public static boolean isValidCardNumber(String cardNumber) {
        int checksum = Integer.parseInt(String.valueOf(cardNumber.charAt(cardNumber.length() - 1)));
        cardNumber = cardNumber.substring(0,cardNumber.length() - 1);
        int sum = 0;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cardNumber.length(); i = i + 2) {
            int oddNumber = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
            oddNumber *= 2;
            if (oddNumber > 9) {
                oddNumber -= 9;
            }
            sb.append(oddNumber);
            if (i + 1 != cardNumber.length()) {
                int evenNumber = Integer.parseInt(String.valueOf(cardNumber.charAt(i + 1)));
                sb.append(evenNumber);
            }
        }


        for (int i = 0; i < sb.length(); i++) {
            sum += Integer.parseInt(String.valueOf(sb.charAt(i)));
        }

        return (sum + checksum) % 10 == 0;
    }
}
