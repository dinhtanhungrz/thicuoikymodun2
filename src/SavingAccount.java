public class SavingAccount extends BankAccount {
    private double amount;
    private String depositDate;
    private double interest;
    private int term;

    public SavingAccount(int id, String code, String owner, String createDate, double amount, String depositDate, double interest, int term) {
        super(id, code, owner, createDate);
        this.amount = amount;
        this.depositDate = depositDate;
        this.interest = interest;
        this.term = term;
    }

    @Override
    public String toCSV() {
        return String.format("%d,%s,%s,%s,%.2f,%s,%.2f,%d", id, code, owner, createDate, amount, depositDate, interest, term);
    }

    @Override
    public String toString() {
        return String.format("[Tiết kiệm] ID: %d | Mã: %s | Chủ: %s | Ngày tạo: %s | Số tiền: %.2f | Ngày gửi: %s | Lãi suất: %.2f | Kỳ hạn: %d tháng", id, code, owner, createDate, amount, depositDate, interest, term);
    }
}

