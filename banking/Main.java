package banking;


public class Main {
    static String createTable = "CREATE TABLE card (\n" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "number VARCHAR(50),\n" +
            "pin VARCHAR(50),\n" +
            "balance INT DEFAULT 0\n" +
            ");";


    public static void main(String[] args) {
        String fileName = "";
        if (args.length > 0) {
            fileName = args[1];
        }

        Database db = new Database(fileName);

        // db.createNewDatabase(fileName);
        // db.startQuery(createTable);
        BankingApp bankingApp  = new BankingApp(db);
        bankingApp.mainMenu();
    }
}
