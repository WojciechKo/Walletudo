package info.korzeniowski.walletplus.model.greendao;

import java.util.List;
import info.korzeniowski.walletplus.model.greendao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import static info.korzeniowski.walletplus.model.Category.findById;
import info.korzeniowski.walletplus.model.Category;

import com.google.common.collect.Lists;
// KEEP INCLUDES END
/**
 * Entity mapped to table Category.
 */
public class GreenCategory {

    private Long id;
    private Long parentId;
    private Long accountId;
    /** Not-null value. */
    private String name;
    private Integer type;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient GreenCategoryDao myDao;

    private GreenCategory parent;
    private Long parent__resolvedKey;

    private List<GreenCategory> children;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public GreenCategory() {
    }

    public GreenCategory(Long id) {
        this.id = id;
    }

    public GreenCategory(Long id, Long parentId, Long accountId, String name, Integer type) {
        this.id = id;
        this.parentId = parentId;
        this.accountId = accountId;
        this.name = name;
        this.type = type;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGreenCategoryDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /** To-one relationship, resolved on first access. */
    public GreenCategory getParent() {
        Long __key = this.parentId;
        if (parent__resolvedKey == null || !parent__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GreenCategoryDao targetDao = daoSession.getGreenCategoryDao();
            GreenCategory parentNew = targetDao.load(__key);
            synchronized (this) {
                parent = parentNew;
            	parent__resolvedKey = __key;
            }
        }
        return parent;
    }

    public void setParent(GreenCategory parent) {
        synchronized (this) {
            this.parent = parent;
            parentId = parent == null ? null : parent.getId();
            parent__resolvedKey = parentId;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<GreenCategory> getChildren() {
        if (children == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GreenCategoryDao targetDao = daoSession.getGreenCategoryDao();
            List<GreenCategory> childrenNew = targetDao._queryGreenCategory_Children(id);
            synchronized (this) {
                if(children == null) {
                    children = childrenNew;
                }
            }
        }
        return children;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetChildren() {
        children = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    public GreenCategory(Category category) {
        if (category == null) return;

        setId(category.getId());
        setName(category.getName());
        setParentId(category.getParentId());
        setType(Category.Type.convertEnumToBitwise(category.getTypes()));
    }

    public static List<Category> deepCopyToCategories(List<GreenCategory> greenCategoryList) {
        List<Category> result = Lists.newArrayList();

        for (GreenCategory original : greenCategoryList) {
            result.add(toCategory(original));
        }

        for (Category copy : result) {
            Category copyParent = findById(result, copy.getParentId());
            if (copyParent != null) {
                copy.setTypes(copyParent.getTypes());
                copyParent.addChild(copy);
            }
            copy.setParent(copyParent);
        }
        return result;
    }

    public static Category toCategory(GreenCategory greenCategory) {
        if (greenCategory == null) return null;

        Category result = new Category();
        result.setId(greenCategory.getId());
        result.setParentId(greenCategory.getParentId());
        result.setName(greenCategory.getName());
        result.setTypes(Category.Type.convertBitwiseToEnumSet(greenCategory.getType()));
        result.setChildren(Lists.<Category>newArrayList());
        return result;
    }
    // KEEP METHODS END

}
