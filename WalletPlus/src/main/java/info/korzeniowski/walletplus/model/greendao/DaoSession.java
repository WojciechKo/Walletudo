package info.korzeniowski.walletplus.model.greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig greenAccountDaoConfig;
    private final DaoConfig greenCategoryDaoConfig;
    private final DaoConfig greenWalletDaoConfig;
    private final DaoConfig greenCashFlowDaoConfig;

    private final GreenAccountDao greenAccountDao;
    private final GreenCategoryDao greenCategoryDao;
    private final GreenWalletDao greenWalletDao;
    private final GreenCashFlowDao greenCashFlowDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        greenAccountDaoConfig = daoConfigMap.get(GreenAccountDao.class).clone();
        greenAccountDaoConfig.initIdentityScope(type);

        greenCategoryDaoConfig = daoConfigMap.get(GreenCategoryDao.class).clone();
        greenCategoryDaoConfig.initIdentityScope(type);

        greenWalletDaoConfig = daoConfigMap.get(GreenWalletDao.class).clone();
        greenWalletDaoConfig.initIdentityScope(type);

        greenCashFlowDaoConfig = daoConfigMap.get(GreenCashFlowDao.class).clone();
        greenCashFlowDaoConfig.initIdentityScope(type);

        greenAccountDao = new GreenAccountDao(greenAccountDaoConfig, this);
        greenCategoryDao = new GreenCategoryDao(greenCategoryDaoConfig, this);
        greenWalletDao = new GreenWalletDao(greenWalletDaoConfig, this);
        greenCashFlowDao = new GreenCashFlowDao(greenCashFlowDaoConfig, this);

        registerDao(GreenAccount.class, greenAccountDao);
        registerDao(GreenCategory.class, greenCategoryDao);
        registerDao(GreenWallet.class, greenWalletDao);
        registerDao(GreenCashFlow.class, greenCashFlowDao);
    }
    
    public void clear() {
        greenAccountDaoConfig.getIdentityScope().clear();
        greenCategoryDaoConfig.getIdentityScope().clear();
        greenWalletDaoConfig.getIdentityScope().clear();
        greenCashFlowDaoConfig.getIdentityScope().clear();
    }

    public GreenAccountDao getGreenAccountDao() {
        return greenAccountDao;
    }

    public GreenCategoryDao getGreenCategoryDao() {
        return greenCategoryDao;
    }

    public GreenWalletDao getGreenWalletDao() {
        return greenWalletDao;
    }

    public GreenCashFlowDao getGreenCashFlowDao() {
        return greenCashFlowDao;
    }

}
