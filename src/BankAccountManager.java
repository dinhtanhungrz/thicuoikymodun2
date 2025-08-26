
import java.util.Scanner;

public class BankAccountManager {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankAccountService service = new BankAccountService(sc);

        while (true) {
            System.out.println("\n===== QUẢN LÝ TÀI KHOẢN NGÂN HÀNG =====");
            System.out.println("1. Thêm mới tài khoản");
            System.out.println("2. Xóa tài khoản");
            System.out.println("3. Xem danh sách tài khoản");
            System.out.println("4. Tìm kiếm tài khoản");
            System.out.println("5. Thoát");
            System.out.print("Lựa chọn của bạn: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    service.addBankAccount();
                    break;
                case "2":
                    service.deleteAccount();
                    break;
                case "3":
                    service.displayAccounts();
                    break;
                case "4":
                    service.searchAccount();
                    break;
                case "5":
                    System.out.println("Thoát chương trình. Tạm biệt!");
                    sc.close();
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng nhập từ 1-5.");
            }
        }
    }
}
