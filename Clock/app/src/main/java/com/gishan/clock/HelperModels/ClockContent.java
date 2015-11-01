package com.gishan.clock.HelperModels;

import android.os.Parcel;
import android.os.Parcelable;

//Object for the world clock with getters and setters
public class ClockContent {


    public static class ClockItem implements Parcelable{
        private int id;
        private String country;
        private String time;

        public ClockItem(int id, String country,String time) {
            this.id = id;
            this.country = country;
            this.time = time;
        }

        public ClockItem(Parcel in) {
            this.id = in.readInt();
            this.country = in.readString();
            this.time = in.readString();
        }


        public String getTime() {
            return time;
        }

        public int getId() {
            return id;
        }

        public String getCountry() {
            return country;
        }

        @Override
        public String toString() {
            return country;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(country);
            dest.writeString(time);

        }

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public ClockItem createFromParcel(Parcel in) {
                return new ClockItem(in);
            }

            public ClockItem[] newArray(int size) {
                return new ClockItem[size];
            }
        };


    }
}
