import java.util.*;
import java.io.*;

public class Main {
     
    // In-memory list of expenses
    static ArrayList<Expense> expenses = new ArrayList<>();

    // File used for persistent storage
    static final String FILE_NAME = "expenses.txt";

    public static void main(String[] args) {
        loadExpenses();
        Scanner scanner = new Scanner(System.in);
        int choice;

        // Main menu loop

        do {
            System.out.println("\n=== Finance Tracking ===");
            System.out.println("1. Add expense");
            System.out.println("2. View expenses");
            System.out.println("3. Show total spending");
            System.out.println("4. Monthly category summary");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: addExpense(scanner); break;
                case 2: viewExpenses(); break;
                case 3: showTotal(); break;
                case 4: showMonthlySummary(); break;
                case 5: saveExpenses(); System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid choice.");
            }

        } while (choice != 5);

        scanner.close();
    }

    static void addExpense(Scanner scanner) {
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        expenses.add(new Expense(amount, category, description, date));
        System.out.println("Expense added!");
    }

    static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);
            System.out.printf("%d. %s | %s | £%.2f | %s\n", i + 1, e.getDate(), e.getCategory(), e.getAmount(), e.getDescription());
        }
    }

    static void showTotal() {
        double total = 0;
        for (Expense e : expenses) total += e.getAmount();
        System.out.printf("Total spent: £%.2f\n", total);
    }

    static void showMonthlySummary() {
        Map<String, Map<String, Double>> monthlyTotals = new HashMap<>();

        for (Expense e : expenses) {
            String month = e.getDate().substring(0, 7); // YYYY-MM
            monthlyTotals.putIfAbsent(month, new HashMap<>());
            Map<String, Double> categoryTotals = monthlyTotals.get(month);
            categoryTotals.put(e.getCategory(), categoryTotals.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
        }

        if (monthlyTotals.isEmpty()) {
            System.out.println("No expenses to summarize.");
            return;
        }

        for (String month : monthlyTotals.keySet()) {
            System.out.println("\n--- " + month + " ---");
            Map<String, Double> categoryTotals = monthlyTotals.get(month);
            double monthTotal = 0;
            for (String cat : categoryTotals.keySet()) {
                double amt = categoryTotals.get(cat);
                monthTotal += amt;
                System.out.printf("%s: £%.2f\n", cat, amt);
            }
            System.out.printf("Total: £%.2f\n", monthTotal);
        }
    }

    static void saveExpenses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                writer.println(e.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving expenses.");
        }
    }

    static void loadExpenses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                expenses.add(Expense.fromFileString(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading expenses.");
        }
    }
}

