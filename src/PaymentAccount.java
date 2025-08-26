public class PaymentAccount extends BankAccount {
    private String cardNumber;
    private double balance;

    public PaymentAccount(int id, String code, String owner, String createDate, String cardNumber, double balance) {
        super(id, code, owner, createDate);
        this.cardNumber = cardNumber;
        this.balance = balance;
    }

    @Override
    public String toCSV() {
        return String.format("%d,%s,%s,%s,%s,%.2f", id, code, owner, createDate, cardNumber, balance);
    }

    @Override
    public String toString() {
        return String.format("[Thanh toán] ID: %d | Mã: %s | Chủ: %s | Ngày tạo: %s | Số thẻ: %s | Số dư: %.2f", id, code, owner, createDate, cardNumber, balance);
    }
}

