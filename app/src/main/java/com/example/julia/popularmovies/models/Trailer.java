/*
 * Copyright 2016.  Julia Kozhukhovskaya
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.julia.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.julia.popularmovies.Config;

import org.json.JSONException;
import org.json.JSONObject;

public class Trailer implements Parcelable {

    private String mId;
    private String mKey;
    private String mName;
    private String mSite;
    private String mType;

    public Trailer(JSONObject trailer) throws JSONException {
        mId = trailer.getString(Config.TMD_ID);
        mKey = trailer.getString(Config.TMD_TRAILER_KEY);
        mName = trailer.getString(Config.TMD_TRAILER_NAME);
        mSite = trailer.getString(Config.TMD_TRAILER_SITE);
        mType = trailer.getString(Config.TMD_TRAILER_TYPE);
    }

    private Trailer(Parcel in) {
        mId = in.readString();
        mKey = in.readString();
        mName = in.readString();
        mSite = in.readString();
        mType = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public String getId() {
        return mId;
    }

    public String getKey() { return mKey; }

    public String getName() { return mName; }

    public String getTrailerUrl() {
        return Config.TMD_TRAILER_YOUTUBE_WATCH + mKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mKey);
        dest.writeString(mName);
        dest.writeString(mSite);
        dest.writeString(mType);
    }
}