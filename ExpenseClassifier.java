
package proledger;

public class ExpenseClassifier {

    public static String classify(String text) {

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
            "office", "printer", "paper",
            "ink")) {
            return "Office Supplies";
        }

        if (contains(value, "rent", "rental")) {
            return "Rent Expenses";
        }

        return "Uncategorized Receipts";
    }

    private static boolean contains(
        String text, String... keywords
    ) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) return true;
        }

        return false;
    }
}
