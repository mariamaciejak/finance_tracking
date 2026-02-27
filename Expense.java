public class Expense {
    double amount;
    String category;
    String description;
    String date;

    public Expense(double amount, String category, String description, String date) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getDate() { return date; }

    // Convert expense to a pipe-delimited string for file storage
    public String toFileString() {
        return amount + "|" + category + "|" + description + "|" + date;
    }

    // Recreate an Expense object from a stored file line
    public static Expense fromFileString(String line) {
        String[] parts = line.split("\\|");
        double amount = Double.parseDouble(parts[0]);
        String category = parts[1];
        String description = parts[2];
        String date = parts[3];
        return new Expense(amount, category, description, date);
    }
}

