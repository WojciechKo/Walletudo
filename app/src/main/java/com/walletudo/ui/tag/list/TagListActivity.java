package com.walletudo.ui.tag.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.walletudo.R;
import com.walletudo.ui.BaseActivity;
import com.walletudo.ui.NavigationDrawerHelper;
import com.walletudo.ui.tag.details.TagDetailsActivity;

public class TagListActivity extends BaseActivity {
    public static final String TAG = TagListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFinishing()) {
            return;
        }

        setContentView(R.layout.activity_drawer);

        if (null == savedInstanceState) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new TagListFragment())
                    .commit();
        }

        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_new) {
            getSupportFragmentManager().findFragmentById(R.id.container).startActivityForResult(new Intent(this, TagDetailsActivity.class), TagDetailsActivity.REQUEST_CODE_ADD_TAG);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected NavigationDrawerHelper.DrawerItemType getSelfNavDrawerItem() {
        return NavigationDrawerHelper.DrawerItemType.TAG;
    }
}
