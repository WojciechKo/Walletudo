package com.walletudo.test;

import com.walletudo.InMemoryServiceComponent;
import com.walletudo.service.CashFlowService;
import com.walletudo.service.StatisticService;
import com.walletudo.service.TagService;
import com.walletudo.service.WalletService;

import javax.inject.Inject;

public class ServiceInjectedUnitTest {
    @Inject
    protected WalletService walletService;

    @Inject
    protected CashFlowService cashFlowService;

    @Inject
    protected TagService tagService;

    @Inject
    protected StatisticService statisticService;

    public void setUp() {
        InMemoryServiceComponent.Initializer.init().inject(this);
    }
}
