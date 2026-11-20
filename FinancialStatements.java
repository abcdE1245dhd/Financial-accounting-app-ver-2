
package proledger;

public class FinancialStatements {

    public static String generateIncomeStatement()
        throws Exception {

        double income = Database.getTotal("INCOME");
        double expenses = Database.getTotal("EXPENSE");

        double netIncome = income - expenses;

        return """
            ==================================
                  PROLEDGER
                  INCOME STATEMENT
            ==================================

            Total Income:    PHP %.2f

            Total Expenses:  PHP %.2f

            ----------------------------------
            NET INCOME:      PHP %.2f

            ==================================
            """.formatted(income, expenses, netIncome);
    }
}
