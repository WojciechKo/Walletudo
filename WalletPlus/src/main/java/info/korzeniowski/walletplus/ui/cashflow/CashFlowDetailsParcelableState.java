package info.korzeniowski.walletplus.ui.cashflow;

import android.os.Parcel;
import android.os.Parcelable;

import info.korzeniowski.walletplus.model.Category;
import info.korzeniowski.walletplus.model.Wallet;

public class CashFlowDetailsParcelableState implements Parcelable {
    private Long id;
    private Float amount;
    private String comment;
    private Wallet incomeFromWallet;
    private Wallet incomeToWallet;
    private Wallet expanseFromWallet;
    private Wallet expanseToWallet;
    private Category incomeCategory;
    private Category expanseCategory;
    private Long date;
    private boolean init;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeFloat(amount);
        dest.writeString(comment);
        dest.writeParcelable(incomeFromWallet, flags);
        dest.writeParcelable(incomeToWallet, flags);
        dest.writeParcelable(expanseFromWallet, flags);
        dest.writeParcelable(expanseToWallet, flags);
        dest.writeParcelable(incomeCategory, flags);
        dest.writeParcelable(expanseCategory, flags);
        dest.writeLong(date);
        dest.writeByte((byte) (init ? 1 : 0));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Wallet getIncomeFromWallet() {
        return incomeFromWallet;
    }

    public void setIncomeFromWallet(Wallet incomeFromWallet) {
        this.incomeFromWallet = incomeFromWallet;
    }

    public Wallet getIncomeToWallet() {
        return incomeToWallet;
    }

    public void setIncomeToWallet(Wallet incomeToWallet) {
        this.incomeToWallet = incomeToWallet;
    }

    public Wallet getExpanseFromWallet() {
        return expanseFromWallet;
    }

    public void setExpanseFromWallet(Wallet expanseFromWallet) {
        this.expanseFromWallet = expanseFromWallet;
    }

    public Wallet getExpanseToWallet() {
        return expanseToWallet;
    }

    public void setExpanseToWallet(Wallet expanseToWallet) {
        this.expanseToWallet = expanseToWallet;
    }

    public Category getIncomeCategory() {
        return incomeCategory;
    }

    public void setIncomeCategory(Category incomeCategory) {
        this.incomeCategory = incomeCategory;
    }

    public Category getExpanseCategory() {
        return expanseCategory;
    }

    public void setExpanseCategory(Category expanseCategory) {
        this.expanseCategory = expanseCategory;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }
}