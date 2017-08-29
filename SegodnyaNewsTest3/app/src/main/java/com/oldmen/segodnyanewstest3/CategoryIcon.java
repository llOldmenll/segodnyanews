package com.oldmen.segodnyanewstest3;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

public class CategoryIcon extends RealmObject implements Parcelable {

    private int iconBlack;
    private int iconBlue;
    private int iconGray;


    public CategoryIcon() {
    }

    private CategoryIcon(Parcel in) {
        iconBlack = in.readInt();
        iconBlue = in.readInt();
        iconGray = in.readInt();
    }

    public static final Creator<CategoryIcon> CREATOR = new Creator<CategoryIcon>() {
        @Override
        public CategoryIcon createFromParcel(Parcel in) {
            return new CategoryIcon(in);
        }

        @Override
        public CategoryIcon[] newArray(int size) {
            return new CategoryIcon[size];
        }
    };

    public int getIconBlack() {
        return iconBlack;
    }

    void setIconBlack(int iconBlack) {
        this.iconBlack = iconBlack;
    }

    public int getIconBlue() {
        return iconBlue;
    }

    void setIconBlue(int iconBlue) {
        this.iconBlue = iconBlue;
    }

    public int getIconGray() {
        return iconGray;
    }

    void setIconGray(int iconGray) {
        this.iconGray = iconGray;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(iconBlack);
        dest.writeInt(iconBlue);
        dest.writeInt(iconGray);
    }
}
