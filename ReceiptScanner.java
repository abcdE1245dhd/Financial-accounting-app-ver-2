
package proledger;

import net.sourceforge.tess4j.Tesseract;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;

public class ReceiptScanner {

    // OCR SCANNING
    public static String scan(File file) throws Exception {

        Tesseract scanner = new Tesseract();

        scanner.setDatapath(
            "C:/Program Files/Tesseract-OCR/tessdata"
        );

        scanner.setLanguage("eng");

        scanner.setPageSegMode(6);

        return scanner.doOCR(file);
    }

    // EXTRACT MERCHANT NAME
    public static String extractMerchant(String text) {

        String[] lines = text.split("\\R");

        for (String line : lines) {

            String value = line.trim();

            if (value.length() >= 3 &&
                value.length() <= 60 &&
                value.matches(".*[a-zA-Z].*")) {

                return value;
            }
        }

        return "Unknown Merchant";
    }

    // EXTRACT DATE
    public static String extractDate(String text) {

        Pattern pattern = Pattern.compile(
            "\\b(\\d{4}-\\d{2}-\\d{2}|\\d{2}/\\d{2}/\\d{4})\\b"
        );

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {

            String value = matcher.group(1);

            try {

                if (value.contains("-")) {
                    return LocalDate.parse(value).toString();
                }

                return LocalDate.parse(
                    value,
                    java.time.format.DateTimeFormatter.ofPattern(
                        "MM/dd/yyyy"
                    )
                ).toString();

            } catch (Exception ignored) {}
        }

        return LocalDate.now().toString();
    }

    // EXTRACT TOTAL AMOUNT
    public static double extractAmount(String text) {

        Pattern pattern = Pattern.compile(
            "(?im)^\\s*(?:grand\\s+total|total\\s+amount|total|amount\\s+due)\\s*[:\\-]?\\s*(?:PHP|₱|P)?\\s*([0-9,]+\\.\\d{2})\\s*$"
        );

        Matcher matcher = pattern.matcher(text);

        double amount = 0;

        while (matcher.find()) {

            try {

                amount = Double.parseDouble(
                    matcher.group(1).replace(",", "")
                );

            } catch (NumberFormatException ignored) {}
        }

        return amount;
    }

    // AUTOMATIC EXPENSE CATEGORY
    public static String extractCategory(String text) {

        String value = text.toLowerCase();

        if (contains(value,
            "restaurant", "food", "grocery",
            "supermarket", "coffee", "bakery")) {

            return "Food Expenses";
        }

        if (contains(value,
            "gasoline", "fuel", "petron",
            "shell", "transport", "grab", "fare")) {

            return "Transportation Expenses";
        }

        if (contains(value,
            "electricity", "water", "meralco",
            "utility", "internet")) {

            return "Utilities Expenses";
        }

        if (contains(value,
            "school", "bookstore", "notebook",
            "school supplies")) {

            return "School Expenses";
        }

        if (contains(value,
            "office", "printer", "paper", "ink")) {

            return "Office Supplies";
        }

        if (contains(value, "rent", "rental")) {

            return "Rent Expenses";
        }

        return "Uncategorized Receipts";
    }

    // KEYWORD DETECTOR
    private static boolean contains(
        String text, String... keywords
    ) {

        for (String keyword : keywords) {

            if (text.contains(keyword)) {
                return true;
            }
        }

        return false;
    }

}
