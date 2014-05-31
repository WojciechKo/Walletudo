package info.korzeniowski.walletplus.model;

import com.google.common.collect.Lists;

import java.util.List;

public class Wallet {
    public enum Type {MY_WALLET, CONTRACTOR;}

    private Long id;
    private String name;
    private Double amount;
    private Type type;

    public Wallet() {
    }

    public Wallet(Wallet wallet) {
        setId(wallet.getId());
        setName(wallet.getName());
        setAmount(wallet.getAmount());
        setType(wallet.getType());
    }

    public Long getId() {
        return id;
    }

    public Wallet setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Wallet setName(String name) {
        this.name = name;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public Wallet setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public Type getType() {
        return type;
    }

    public Wallet setType(Type type) {
        this.type = type;
        return this;
    }

    public static Wallet findById(List<Wallet> wallets, Long id) {
        for (Wallet wallet : wallets) {
            if (id.equals(wallet.getId())) {
                return wallet;
            }
        }
        return null;
    }

    public static List<Wallet> copyList(List<Wallet> wallets) {
        List<Wallet> copy = Lists.newArrayListWithExpectedSize(wallets.size());
        for(Wallet wallet : wallets) {
            copy.add(new Wallet(wallet));
        }
        return copy;
    }


}
