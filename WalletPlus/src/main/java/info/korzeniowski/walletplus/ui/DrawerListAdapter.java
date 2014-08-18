package info.korzeniowski.walletplus.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import javax.inject.Inject;

import info.korzeniowski.walletplus.R;

/**
 * Adapter for items from Main Drawer Menu
 */
public class DrawerListAdapter extends BaseAdapter {

    @Inject
    MainDrawerContent mainDrawerContent;

    @Inject
    Context context;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.main_drawer_item, null);
            holder = createHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        fillViewWithItem(holder, getItem(position));
        return convertView;
    }

    private ViewHolder createHolder(View convertView) {
        ViewHolder holder = new ViewHolder();

        holder.menuName = (TextView) convertView;

        convertView.setTag(holder);
        return holder;
    }

    private void fillViewWithItem(ViewHolder holder, MainDrawerItem item) {
        holder.menuName.setText(item.getTitle());
        holder.menuName.setCompoundDrawablesWithIntrinsicBounds(item.getIcon(), 0, 0, 0);
    }

    @Override
    public int getCount() {
        return mainDrawerContent.getCount();
    }

    @Override
    public MainDrawerItem getItem(int position) {
        return mainDrawerContent.getDrawerItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        protected TextView menuName;
    }
}