package com.oldmen.segodnyanewstest3;


import android.os.Parcel;
import android.os.Parcelable;

public class ItemInfo implements Parcelable {

    private String title;
    private String imageUrl;
    private String articleUrl;
    private String date;
    private String category;


    public ItemInfo(String title, String imageUrl, String articleUrl, String date, String category) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.articleUrl = articleUrl;
        this.date = date;
        this.category = category;
    }

    protected ItemInfo(Parcel in) {
        title = in.readString();
        imageUrl = in.readString();
        articleUrl = in.readString();
        date = in.readString();
        category = in.readString();
    }

    public static final Creator<ItemInfo> CREATOR = new Creator<ItemInfo>() {
        @Override
        public ItemInfo createFromParcel(Parcel in) {
            return new ItemInfo(in);
        }

        @Override
        public ItemInfo[] newArray(int size) {
            return new ItemInfo[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(imageUrl);
        parcel.writeString(articleUrl);
        parcel.writeString(date);
        parcel.writeString(category);
    }
}
