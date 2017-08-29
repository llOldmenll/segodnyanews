package com.oldmen.segodnyanewstest3;


import android.os.Parcel;
import android.os.Parcelable;

public class CategoryState implements Parcelable {

    private int imgSrc;
    private int txtColorRes;

    CategoryState() {
    }

    protected CategoryState(Parcel in) {
        imgSrc = in.readInt();
        txtColorRes = in.readInt();
    }

    public static final Creator<CategoryState> CREATOR = new Creator<CategoryState>() {
        @Override
        public CategoryState createFromParcel(Parcel in) {
            return new CategoryState(in);
        }

        @Override
        public CategoryState[] newArray(int size) {
            return new CategoryState[size];
        }
    };

    public int getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(int imgSrc) {
        this.imgSrc = imgSrc;
    }

    public int getTxtColorRes() {
        return txtColorRes;
    }

    public void setTxtColorRes(int txtColorRes) {
        this.txtColorRes = txtColorRes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imgSrc);
        dest.writeInt(txtColorRes);
    }
}
