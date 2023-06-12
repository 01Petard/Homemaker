package tudan.Tools;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

/**
 * Created by fengliang
 * on 2017/7/6.
 */

public class SparseParcelableArray<E> extends SparseArray<E> implements Parcelable {
    public SparseParcelableArray(){
        super();
    }
    public SparseParcelableArray(Parcel in) {
    }

    public static final Creator<SparseParcelableArray> CREATOR = new Creator<SparseParcelableArray>() {
        @Override
        public SparseParcelableArray createFromParcel(Parcel in) {
            return new SparseParcelableArray(in);
        }

        @Override
        public SparseParcelableArray[] newArray(int size) {
            return new SparseParcelableArray[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
