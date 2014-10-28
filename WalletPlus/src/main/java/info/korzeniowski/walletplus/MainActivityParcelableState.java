package info.korzeniowski.walletplus;

import android.os.Parcel;
import android.os.Parcelable;

public class MainActivityParcelableState implements Parcelable {
    public static final String TAG = "mainActivityState";

    private String appName;
    private String selectedFragmentTitle;
    private String fragmentTag;
    private int selectedDrawerPosition;

    public static final Parcelable.Creator<MainActivityParcelableState> CREATOR = new Parcelable.Creator<MainActivityParcelableState>() {
        public MainActivityParcelableState createFromParcel(Parcel in) {
            return new MainActivityParcelableState(in);
        }

        public MainActivityParcelableState[] newArray(int size) {
            return new MainActivityParcelableState[size];
        }
    };

    public MainActivityParcelableState() {

    }

    public MainActivityParcelableState(Parcel in) {
        appName = in.readString();
        selectedFragmentTitle = in.readString();
        fragmentTag = in.readString();
        selectedDrawerPosition = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appName);
        dest.writeString(selectedFragmentTitle);
        dest.writeString(fragmentTag);
        dest.writeInt(selectedDrawerPosition);
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getSelectedFragmentTitle() {
        return selectedFragmentTitle;
    }

    public void setSelectedFragmentTitle(String selectedFragmentTitle) {
        this.selectedFragmentTitle = selectedFragmentTitle;
    }

    public String getFragmentTag() {
        return fragmentTag;
    }

    public void setFragmentTag(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }

    public int getSelectedDrawerPosition() {
        return selectedDrawerPosition;
    }

    public void setSelectedDrawerPosition(int selectedDrawerPosition) {
        this.selectedDrawerPosition = selectedDrawerPosition;
    }
}