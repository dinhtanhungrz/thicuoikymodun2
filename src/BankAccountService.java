import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class BankAccountService {
    private static final String FILE_PATH = "data/bank_accounts.csv";
    private final Scanner sc;

    public BankAccountService(Scanner sc) {
        this.sc = sc;
    }

    // ================= THÊM MỚI TÀI KHOẢN =================
    public void addBankAccount() {
        try {
            List<String> lines = readAllLines();
            int newId = getNextId(lines);

            System.out.println("\n=== THÊM MỚI TÀI KHOẢN NGÂN HÀNG ===");

            String code = readRequiredString("Nhập mã tài khoản: ");
            String owner = readRequiredString("Nhập tên chủ tài khoản: ");
            String createDate = readDate("Nhập ngày tạo tài khoản (yyyy-MM-dd): ");
            int type = readAccountType();

            StringBuilder sb = new StringBuilder();
            sb.append(newId).append(",").append(code).append(",").append(owner).append(",").append(createDate);

            if (type == 1) { // Tiết kiệm
                double amount = readPositiveDouble("Nhập số tiền gửi tiết kiệm: ");
                String depositDate = readDate("Nhập ngày gửi tiết kiệm (yyyy-MM-dd): ");
                double interest = readPositiveDouble("Nhập lãi suất (%): ");
                int term = readPositiveInt("Nhập kỳ hạn (số tháng): ");

                sb.append(",").append(amount)
                        .append(",").append(depositDate)
                        .append(",").append(interest)
                        .append(",").append(term);
            } else { // Thanh toán
                String cardNumber = readRequiredString("Nhập số thẻ: ");
                double balance = readPositiveDouble("Nhập số tiền trong tài khoản: ");

                sb.append(",").append(cardNumber)
                        .append(",").append(balance);
            }

            appendLine(sb.toString());
            System.out.println("Thêm tài khoản thành công!");
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc/ghi file: " + e.getMessage());
        }
    }

    // ================= HÀM HỖ TRỢ =================
    private List<String> readAllLines() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            boolean dirCreated = file.getParentFile().mkdirs();
            if (!dirCreated && !file.getParentFile().exists()) {
                throw new IOException("Không thể tạo thư mục dữ liệu: " + file.getParent());
            }
            boolean fileCreated = file.createNewFile();
            if (!fileCreated && !file.exists()) {
                throw new IOException("Không thể tạo file dữ liệu: " + FILE_PATH);
            }
        }
        return new ArrayList<>(Files.readAllLines(Paths.get(FILE_PATH)));
    }

    private int getNextId(List<String> lines) {
        if (lines.isEmpty()) return 1;
        String lastLine = lines.get(lines.size() - 1);
        String[] parts = lastLine.split(",");
        try {
            return Integer.parseInt(parts[0]) + 1;
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private void appendLine(String line) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(line);
            bw.newLine();
        }
    }

    // ================= HÀM ĐỌC VÀ VALIDATE DỮ LIỆU =================
    private String readRequiredString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Trường này không được bỏ trống!");
        }
    }

    private double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(sc.nextLine());
                if (value > 0) return value;
                System.out.println("Giá trị phải là số dương!");
            } catch (NumberFormatException e) {
                System.out.println("Giá trị không hợp lệ, nhập số!");
            }
        }
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(sc.nextLine());
                if (value > 0) return value;
                System.out.println("Giá trị phải là số nguyên dương!");
            } catch (NumberFormatException e) {
                System.out.println("Giá trị không hợp lệ, nhập số nguyên!");
            }
        }
    }

    private String readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                LocalDate.parse(input);
                return input;
            } catch (DateTimeParseException e) {
                System.out.println("Định dạng ngày không hợp lệ! Vui lòng nhập yyyy-MM-dd.");
            }
        }
    }

    private int readAccountType() {
        while (true) {
            System.out.print("Chọn loại tài khoản (1. Tiết kiệm, 2. Thanh toán): ");
            String input = sc.nextLine().trim();
            if (input.equals("1") || input.equals("2")) return Integer.parseInt(input);
            System.out.println("Loại tài khoản không hợp lệ, nhập 1 hoặc 2.");
        }
    }

    private List<BankAccount> getAllAccounts() throws IOException {
        List<String> lines = readAllLines();
        List<BankAccount> accounts = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 8) {
                // SavingAccount
                accounts.add(new SavingAccount(
                    Integer.parseInt(parts[0]), parts[1], parts[2], parts[3],
                    Double.parseDouble(parts[4]), parts[5], Double.parseDouble(parts[6]), Integer.parseInt(parts[7])
                ));
            } else if (parts.length == 6) {
                // PaymentAccount
                accounts.add(new PaymentAccount(
                    Integer.parseInt(parts[0]), parts[1], parts[2], parts[3],
                    parts[4], Double.parseDouble(parts[5])
                ));
            }
        }
        return accounts;
    }

    public void displayAccounts() {
        try {
            List<BankAccount> accounts = getAllAccounts();
            if (accounts.isEmpty()) {
                System.out.println("Không có tài khoản nào.");
            } else {
                System.out.println("\n--- DANH SÁCH TÀI KHOẢN ---");
                for (BankAccount acc : accounts) {
                    System.out.println(acc);
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file: " + e.getMessage());
        }
    }

    public void searchAccount() {
        System.out.print("Nhập từ khóa tìm kiếm (mã hoặc tên chủ): ");
        String keyword = sc.nextLine().trim().toLowerCase();
        try {
            List<BankAccount> accounts = getAllAccounts();
            List<BankAccount> found = new ArrayList<>();
            for (BankAccount acc : accounts) {
                if (acc.getCode().toLowerCase().contains(keyword) || acc.getOwner().toLowerCase().contains(keyword)) {
                    found.add(acc);
                }
            }
            if (found.isEmpty()) {
                System.out.println("Không tìm thấy tài khoản phù hợp.");
            } else {
                System.out.println("\n--- KẾT QUẢ TÌM KIẾM ---");
                for (BankAccount acc : found) {
                    System.out.println(acc);
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file: " + e.getMessage());
        }
    }

    public void deleteAccount() {
        while (true) {
            System.out.print("Nhập mã tài khoản cần xóa: ");
            String code = sc.nextLine().trim();
            try {
                List<BankAccount> accounts = getAllAccounts();
                BankAccount toDelete = null;
                for (BankAccount acc : accounts) {
                    if (acc.getCode().equals(code)) {
                        toDelete = acc;
                        break;
                    }
                }
                if (toDelete == null) {
                    throw new NotFoundBankAccountException("Tài khoản không tồn tại.");
                }
                System.out.print("Bạn có chắc muốn xóa tài khoản này? (Yes/No): ");
                String confirm = sc.nextLine().trim().toLowerCase();
                if (confirm.equals("yes")) {
                    accounts.remove(toDelete);
                    // Save back to CSV
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
                        for (BankAccount acc : accounts) {
                            bw.write(acc.toCSV());
                            bw.newLine();
                        }
                    }
                    System.out.println("Đã xóa tài khoản thành công!");
                    displayAccounts();
                    break;
                } else if (confirm.equals("no")) {
                    System.out.println("Hủy xóa. Quay về menu chính.");
                    break;
                } else {
                    System.out.println("Vui lòng nhập Yes hoặc No.");
                }
            } catch (NotFoundBankAccountException e) {
                System.out.println(e.getMessage());
                System.out.println("Nhấn Enter để quay lại menu chính.");
                sc.nextLine();
                break;
            } catch (IOException e) {
                System.out.println("Lỗi khi xử lý file: " + e.getMessage());
                break;
            }
        }
    }
}
