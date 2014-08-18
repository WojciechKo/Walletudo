package info.korzeniowski.walletplus.ui;

import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Simple class for holding menu items in MainDrawerMenu.
 */
public class MainDrawerItem {
    private final String title;
    private final int icon;
    private final Class<? extends Fragment> fragment;

    public MainDrawerItem(String title, int icon, Class<? extends Fragment> fragment) {
        this.title = title;
        this.icon = icon;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }

    public Fragment getFragment() {
        try {
            return fragment.newInstance();
        } catch (InstantiationException e) {
            Log.w("WalletPlus", "Nie można stworzyć obiektu Fragment");
        } catch (IllegalAccessException e) {
            Log.w("WalletPlus", "Nie można stworzyć obiektu Fragment");
        }
        return null;
    }
}