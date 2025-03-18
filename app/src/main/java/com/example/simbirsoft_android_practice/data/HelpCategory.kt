package com.example.simbirsoft_android_practice.data

import android.os.Parcel
import android.os.Parcelable

data class HelpCategory(
    val id: Int,
    val title: String,
    val iconUrl: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(iconUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HelpCategory> {
        override fun createFromParcel(parcel: Parcel): HelpCategory {
            return HelpCategory(parcel)
        }

        override fun newArray(size: Int): Array<HelpCategory?> {
            return arrayOfNulls(size)
        }
    }
}
