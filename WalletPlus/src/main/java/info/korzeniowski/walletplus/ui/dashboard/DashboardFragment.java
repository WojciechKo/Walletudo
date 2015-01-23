package info.korzeniowski.walletplus.ui.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.collect.Lists;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.ButterKnife;
import butterknife.InjectView;
import info.korzeniowski.walletplus.R;
import info.korzeniowski.walletplus.WalletPlus;
import info.korzeniowski.walletplus.model.CashFlow;
import info.korzeniowski.walletplus.model.Wallet;
import info.korzeniowski.walletplus.service.CashFlowService;
import info.korzeniowski.walletplus.service.WalletService;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class DashboardFragment extends Fragment {
    public static final String TAG = "dashboard";
    private static final int MAX_NUMBER_OF_POINTS_IN_CHART = 5;

    @InjectView(R.id.totalAmount)
    TextView totalAmount;

    @InjectView(R.id.chart)
    LineChartView chart;

    @Inject
    @Named("local")
    WalletService localWalletService;

    @Inject
    @Named("local")
    CashFlowService localCashFlowService;
    private List<PointValue> chartValues;
    private Double currentAmountSumFromMyWallets;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WalletPlus) getActivity().getApplication()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = View.inflate(getActivity(), R.layout.fragment_dashboard, null);
        ButterKnife.inject(this, view);
        setupViews();
        return view;
    }

    void setupViews() {
        totalAmount.setText(getTotalAmountText());
        chart.setLineChartData(getMainLineChartData());
    }

    private LineChartData getMainLineChartData() {
        List<PointValue> values = Lists.newArrayList();
        List<AxisValue> dateAxisValues = Lists.newArrayList();
        ListIterator<CashFlow> cashFlowListIterator = localCashFlowService.getLastNCashFlows(MAX_NUMBER_OF_POINTS_IN_CHART).listIterator();
        float tempWalletValue = currentAmountSumFromMyWallets.floatValue();

        for (int i = MAX_NUMBER_OF_POINTS_IN_CHART; i > 0; i--) {
            if (!cashFlowListIterator.hasNext()) {
                break;
            }
            CashFlow cashFlow = cashFlowListIterator.next();

            values.add(new PointValue(i, tempWalletValue));
            dateAxisValues.add(new AxisValue(i, getAxisLabel(cashFlow.getDateTime())));
            if (cashFlow.getType() == CashFlow.Type.INCOME) {
                tempWalletValue -= cashFlow.getAmount().floatValue();
            } else if (cashFlow.getType() == CashFlow.Type.EXPANSE) {
                tempWalletValue += cashFlow.getAmount().floatValue();
            }

        }
        Line mainLine = new Line(values);
        mainLine.setColor(getResources().getColor(R.color.green));
        mainLine.setFilled(true);
        mainLine.setHasLabelsOnlyForSelected(true);
        ArrayList<Line> lines = Lists.newArrayList(mainLine);
        LineChartData lineChartData = new LineChartData(lines);

        Axis dateAxis = new Axis(dateAxisValues);
        dateAxis.setHasTiltedLabels(true);
        lineChartData.setAxisXBottom(dateAxis);

        return lineChartData;
    }

    private char[] getAxisLabel(Date dateTime) {
        String dateLabel = DateFormat.getDateFormat(DashboardFragment.this.getActivity()).format(dateTime.getTime());
        return dateLabel.toCharArray();
    }

    private CharSequence getTotalAmountText() {
        currentAmountSumFromMyWallets = getCurrentAmountSumFromMyWallets();
        String totalAmountString = NumberFormat.getCurrencyInstance().format(currentAmountSumFromMyWallets);

        SpannableStringBuilder spanTxt = new SpannableStringBuilder(getString(R.string.totalAmountLabel) + "\n");
        spanTxt.append(totalAmountString);
        spanTxt.setSpan(new RelativeSizeSpan(2f), spanTxt.length() - totalAmountString.length(), spanTxt.length(), 0);
        if (currentAmountSumFromMyWallets < 0) {
            spanTxt.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), spanTxt.length() - totalAmountString.length(), spanTxt.length(), 0);
        } else {
            spanTxt.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green)), spanTxt.length() - totalAmountString.length(), spanTxt.length(), 0);
        }
        return spanTxt;
    }

    private Double getCurrentAmountSumFromMyWallets() {
        List<Wallet> myWallets = localWalletService.getMyWallets();
        Double sum = (double) 0;
        for (Wallet wallet : myWallets) {
            sum += wallet.getCurrentAmount();
        }
        return sum;
    }
}
