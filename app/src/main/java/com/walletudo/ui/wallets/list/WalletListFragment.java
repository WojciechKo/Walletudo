package com.walletudo.ui.wallets.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.walletudo.R;
import com.walletudo.Walletudo;
import com.walletudo.model.Wallet;
import com.walletudo.service.WalletService;
import com.walletudo.ui.wallets.details.WalletDetailsActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WalletListFragment extends Fragment {
    public static final String TAG = WalletListFragment.class.getSimpleName();

    @InjectView(R.id.swipe_list)
    ListView list;

    @Inject
    WalletService walletService;

    private List<Wallet> walletList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Walletudo) getActivity().getApplication()).component().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_wallet_list, container, false);
        ButterKnife.inject(this, view);
        setupList();
        return view;
    }

    private void setupList() {
        walletList = walletService.getAll();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WalletDetailsActivity.class);
                intent.putExtra(WalletDetailsActivity.EXTRAS_WALLET_ID, list.getAdapter().getItemId(position));
                startActivityForResult(intent, WalletDetailsActivity.REQUEST_CODE_EDIT_WALLET);
            }
        });
        list.setAdapter(new WalletListAdapter(getActivity(), walletList));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WalletDetailsActivity.REQUEST_CODE_ADD_WALLET) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    setupList();
                    return;
            }
        } else if (requestCode == WalletDetailsActivity.REQUEST_CODE_EDIT_WALLET) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    setupList();
                    return;
                case WalletDetailsActivity.RESULT_DELETED:
                    setupList();
                    return;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
