package info.korzeniowski.walletplus.ui.wallet.list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.text.MessageFormat;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.ButterKnife;
import butterknife.InjectView;
import info.korzeniowski.walletplus.MainActivity;
import info.korzeniowski.walletplus.R;
import info.korzeniowski.walletplus.WalletPlus;
import info.korzeniowski.walletplus.service.CashFlowService;
import info.korzeniowski.walletplus.service.WalletService;
import info.korzeniowski.walletplus.ui.wallet.details.WalletDetailsFragment;
import info.korzeniowski.walletplus.widget.DividerItemDecoration;

public class WalletListFragment extends Fragment {
    public static final String TAG = "walletList";

    @InjectView(R.id.recycler_view)
    RecyclerView list;

    @Inject
    @Named("local")
    WalletService localWalletService;

    @Inject
    @Named("local")
    CashFlowService localCashFlowService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WalletPlus) getActivity().getApplication()).inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.inject(this, view);
        setupViews();
        return view;
    }

    void setupViews() {
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setItemAnimator(new DefaultItemAnimator());
        list.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        final RecyclerView.Adapter adapter = new WalletListAdapter(getActivity(), localWalletService.getMyWallets(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWalletDetailsFragment(list.getAdapter().getItemId(list.getChildPosition(v)));
            }
        });
        list.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(info.korzeniowski.walletplus.R.menu.action_new, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_new) {
            startWalletDetailsFragment(0L);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startWalletDetailsFragment(Long id) {
        Fragment fragment = new WalletDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(WalletDetailsFragment.WALLET_ID, id);
        fragment.setArguments(bundle);
        ((MainActivity) getActivity()).setContentFragment(fragment, true, WalletDetailsFragment.TAG);
    }

    private void selectedOptionDelete(Long id) {
        showDeleteConfirmationAlert(id);
    }

    private void showDeleteConfirmationAlert(final Long walletId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage(getConfirmationMessage(walletId))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tryDelete(walletId);
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();
    }

    private String getConfirmationMessage(Long walletId) {
        int count = (int) localCashFlowService.countAssignedToWallet(walletId);
        String msg = getActivity().getString(R.string.walletDeleteConfirmation);
        return MessageFormat.format(msg, count);
    }

    private void tryDelete(Long id) {
        localWalletService.deleteById(id);
    }
}