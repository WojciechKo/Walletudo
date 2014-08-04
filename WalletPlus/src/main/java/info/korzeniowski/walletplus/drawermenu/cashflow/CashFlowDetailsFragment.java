package info.korzeniowski.walletplus.drawermenu.cashflow;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import info.korzeniowski.walletplus.R;
import info.korzeniowski.walletplus.WalletPlus;
import info.korzeniowski.walletplus.service.CashFlowService;
import info.korzeniowski.walletplus.service.CategoryService;
import info.korzeniowski.walletplus.service.WalletService;
import info.korzeniowski.walletplus.drawermenu.category.CategoryExpandableListAdapter;
import info.korzeniowski.walletplus.model.CashFlow;
import info.korzeniowski.walletplus.model.Category;
import info.korzeniowski.walletplus.model.Wallet;

@EFragment(R.layout.cashflow_details_fragment)
@OptionsMenu(R.menu.action_save)
public class CashFlowDetailsFragment extends Fragment {

    static final public String CASH_FLOW_ID = "CASH_FLOW_ID";

    @ViewById
    Spinner fromWallet;

    @ViewById
    Spinner toWallet;

    @ViewById
    EditText amount;

    @ViewById
    Button category;

    @ViewById
    EditText comment;

    @ViewById
    Button datePicker;

    @ViewById
    Button timePicker;

    @ViewById
    Switch recordType;

    @Inject @Named("local")
    CashFlowService localCashFlowService;

    @Inject @Named("local")
    WalletService localWalletService;

    @Inject @Named("local")
    CategoryService localCategoryService;

    @Inject @Named("amount")
    NumberFormat amountFormat;

    private CashFlow cashFlow;
    private Long cashFlowId;
    private DetailsType type;
    private Calendar calendar;
    private Category previousCategory;

    private List<Wallet> fromWalletList;
    private List<Wallet> toWalletList;
    private List<Category> categoryList;

    @AfterInject
    void daggerInject() {
        ((WalletPlus) getActivity().getApplication()).inject(this);
    }

    @AfterViews
    void setupViews() {
        initFields();
        setupAdapters();
        setupListeners();
        fillViewsWithData();
    }

    private void initFields() {
        cashFlowId = getArguments().getLong(CASH_FLOW_ID);
        type = cashFlowId == 0L ? DetailsType.ADD : DetailsType.EDIT;
        calendar = Calendar.getInstance();
        if (type.equals(DetailsType.ADD)) {
            cashFlow = new CashFlow();
        } else if (type.equals(DetailsType.EDIT)) {
            cashFlow = localCashFlowService.findById(cashFlowId);
            calendar.setTime(cashFlow.getDateTime());
        }
        fromWalletList = Lists.newArrayList();
        toWalletList = Lists.newArrayList();
        categoryList = Lists.newArrayList();
    }

    private void setupAdapters() {
        toWallet.setAdapter(new WalletAdapter(getActivity(), toWalletList));
        fromWallet.setAdapter(new WalletAdapter(getActivity(), fromWalletList));
    }

    private void setupListeners() {
        amount.addTextChangedListener(new TextWatcher() {
                  @Override
                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                  }

                  @Override
                  public void onTextChanged(CharSequence s, int start, int before, int count) {

                  }

                  @Override
                  public void afterTextChanged(Editable s) {
                      if (isDecimal(s)) {
                          int numberOfDigitsToDelete = getNumberOfDigitsToDelete(s);
                          s.delete(s.length() - numberOfDigitsToDelete, s.length());
                      }
                  }

                  private int getNumberOfDigitsToDelete(Editable s) {
                      int allowedNumberOfDigitsAfterComa = 2;
                      int indexOfComa = s.toString().indexOf('.');
                      if (indexOfComa < s.length() - 1 - allowedNumberOfDigitsAfterComa) {
                          return s.length() - indexOfComa - 1 - allowedNumberOfDigitsAfterComa;
                      }
                      return 0;
                  }

                  private boolean isDecimal(Editable s) {
                      return s.toString().contains(".");
                  }
             }
        );
    }

    private void fillViewsWithData() {
        refreshDatePicker();
        refreshTimePicker();
        if (cashFlow.getAmount() != null) {
            amount.setText(amountFormat.format(cashFlow.getAmount()));
        }
        comment.setText(cashFlow.getComment());
        recordType.setChecked(cashFlow.isExpanse());
        fillListsWithData();
        fromWallet.setSelection(fromWalletList.indexOf(cashFlow.getFromWallet()));
        toWallet.setSelection(toWalletList.indexOf(cashFlow.getToWallet()));
        notifyWalletAdapters();
        if (cashFlow.getCategory() != null) {
            category.setText(cashFlow.getCategory().getName());
        }
    }

    private void fillListsWithData() {
        if (isExpanseType()) {
            fromWalletList.addAll(localWalletService.getMyWallets());
            toWalletList.addAll(localWalletService.getContractors());
            categoryList.addAll(localCategoryService.getMainExpenseTypeCategories());
        } else {
            fromWalletList.addAll(localWalletService.getContractors());
            toWalletList.addAll(localWalletService.getMyWallets());
            categoryList.addAll(localCategoryService.getMainIncomeTypeCategories());
        }
    }


    private void notifyWalletAdapters() {
        ((WalletAdapter) fromWallet.getAdapter()).notifyDataSetChanged();
        ((WalletAdapter) toWallet.getAdapter()).notifyDataSetChanged();
    }

    @CheckedChange
    void recordTypeCheckedChanged() {
        swapWalletLists();
        handleCategoryWhenSwapType();
        notifyWalletAdapters();
    }

    private void swapWalletLists() {
        int selectedFromWalletPosition = fromWallet.getSelectedItemPosition();
        int selectedToWalletPosition = toWallet.getSelectedItemPosition();

        List<Wallet> tempList = Lists.newArrayList(fromWalletList);
        fromWalletList.clear();
        fromWalletList.addAll(toWalletList);
        toWalletList.clear();
        toWalletList.addAll(tempList);

        toWallet.setSelection(selectedFromWalletPosition);
        fromWallet.setSelection(selectedToWalletPosition);
    }

    private void handleCategoryWhenSwapType() {
        Category temp = previousCategory;
        previousCategory = cashFlow.getCategory();
        cashFlow.setCategory(temp);

        if (cashFlow.getCategory() == null) {
            category.setText(R.string.cashflowCategoryHint);
        } else {
            category.setText(cashFlow.getCategory().getName());
        }
    }

    public boolean isExpanseType() {
        //TODO: do it better
        return recordType.isChecked();
    }

    @Click
    void categoryClicked() {
        ExpandableListView expandableListView = new ExpandableListView(getActivity());
        expandableListView.setAdapter(new CategoryExpandableListAdapter(getActivity(), categoryList));

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Choose category:")
                .setView(expandableListView)
                .create();

        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                long packedPosition = ((ExpandableListView) parent).getExpandableListPosition(position);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);

                ExpandableListAdapter expandableListAdapter = ((ExpandableListView) parent).getExpandableListAdapter();
                if (childPosition == -1)
                    cashFlow.setCategory((Category) expandableListAdapter.getGroup(groupPosition));
                else
                    cashFlow.setCategory((Category) expandableListAdapter.getChild(groupPosition, childPosition));

                category.setText(cashFlow.getCategory().getName());
                alertDialog.dismiss();
                return true;
            }
        });

        alertDialog.show();
    }

    private void refreshDatePicker() {
        datePicker.setText(DateFormat.getDateFormat(getActivity()).format(calendar.getTime()));
    }

    private void refreshTimePicker() {
        timePicker.setText(DateFormat.getTimeFormat(getActivity()).format(calendar.getTime()));
    }

    public void getDataFromViews() {
        cashFlow.setAmount(Float.parseFloat(amount.getText().toString()));
        cashFlow.setId(cashFlowId);
        cashFlow.setDateTime(calendar.getTime());
        cashFlow.setFromWallet((Wallet) fromWallet.getSelectedItem());
        cashFlow.setToWallet((Wallet) toWallet.getSelectedItem());
        cashFlow.setComment(comment.getText().toString());
    }

    @Click
    void datePickerClicked() {
        new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        refreshDatePicker();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    @Click
    void timePickerClicked() {
        new TimePickerDialog(
                getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        refreshTimePicker();
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getActivity())
        ).show();
    }

    @OptionsItem(R.id.menu_save)
    void actionSave() {
        if (preValidations()) {
            getDataFromViews();
            boolean success = false;
            if (DetailsType.ADD.equals(type)) {
                success = tryInsert();
            } else if (DetailsType.EDIT.equals(type)) {
                success = tryUpdate();
            }
            if (success) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }

    private boolean tryInsert() {
        localCashFlowService.insert(cashFlow);
        return true;
    }

    private boolean tryUpdate() {
        localCashFlowService.update(cashFlow);
        return true;
    }

    private boolean preValidations() {
        return validateAmount();
    }

    private boolean validateAmount() {
        if (Strings.isNullOrEmpty(amount.getText().toString())) {
            amount.setError("Amount can't be empty.");
            return false;
        }
        return true;
    }

    private enum DetailsType {ADD, EDIT}

    private class WalletAdapter extends BaseAdapter {
        List<Wallet> wallets;
        Context context;

        private WalletAdapter(Context context, List<Wallet> list) {
            super();
            this.context = context;
            wallets = list;
        }

        @Override
        public int getCount() {
            return wallets.size();
        }

        @Override
        public Wallet getItem(int position) {
            return wallets.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView;
            if (convertView == null) {
                textView = new TextView(context);
                textView.setTextSize(getResources().getDimension(R.dimen.smallFontSize));
            } else {
                textView = (TextView) convertView;
            }
            textView.setText(getItem(position).getName());
            return textView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getView(position, convertView, parent);
        }
    }

}