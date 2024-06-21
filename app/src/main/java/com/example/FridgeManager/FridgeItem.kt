package com.example.FridgeManager

import android.os.Parcel
import android.os.Parcelable

data class FridgeItem(
    val imageResId: Int,
    val name: String,
    val number: Int,
    val expiryDate: String
    ):Parcelable {
    constructor(parcel: Parcel) : this(
    parcel.readInt(),
        parcel.readString() ?:"",
    parcel.readInt(),
    parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(imageResId)
        parcel.writeString(name)
        parcel.writeInt(number)
        parcel.writeString(expiryDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FridgeItem> {
        override fun createFromParcel(parcel: Parcel): FridgeItem {
            return FridgeItem(parcel)
        }

        override fun newArray(size: Int): Array<FridgeItem?> {
            return arrayOfNulls(size)
        }
    }
}