public abstract class BankAccount {
    protected int id;
    protected String code;
    protected String owner;
    protected String createDate;

    public BankAccount(int id, String code, String owner, String createDate) {
        this.id = id;
        this.code = code;
        this.owner = owner;
        this.createDate = createDate;
    }

    public int getId() { return id; }
    public String getCode() { return code; }
    public String getOwner() { return owner; }
    public String getCreateDate() { return createDate; }

    public abstract String toCSV();
    @Override
    public abstract String toString();
}

