package com.example.biometrikauhtorization.models

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import java.util.*

//class ClassUser : Parcelable {

//    var surname: String? = null
//    var forename: String? = null
//    var gender: String? = null
//    private var dateOfBirth: String? = null
//    private var documentType: String? = null
//    private var documentNumber: String? = null
//    private var expireDate: String? = null
//    var nationality: String? = null
//    var issuer: String? = null
//    var opt1: String? = null
//    var opt2: String? = null
//
//    constructor() {}
//    constructor(source: Parcel) {
//        surname = source.readString()
//        forename = source.readString()
//        gender = source.readString()
//        dateOfBirth = source.readString()
//        documentType = source.readString()
//        documentNumber = source.readString()
//        expireDate = source.readString()
//        nationality = source.readString()
//        issuer = source.readString()
//        opt1 = source.readString()
//        opt2 = source.readString()
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    override fun writeToParcel(dest: Parcel, flags: Int) {
//        dest.writeString(surname)
//        dest.writeString(forename)
//        dest.writeString(gender)
//        dest.writeString(dateOfBirth)
//        dest.writeString(documentType)
//        dest.writeString(documentNumber)
//        dest.writeString(expireDate)
//        dest.writeString(nationality)
//        dest.writeString(issuer)
//        dest.writeString(opt1)
//        dest.writeString(opt2)
//    }
//
//    fun getDateOfBirth(): String? {
//        return dateOfBirth
//    }
//
//    fun setDateOfBirth(dateOfBirth: Date?) {
//        if (dateOfBirth != null) {
//            this.dateOfBirth = dateOfBirth.year
//                .toString() + "/" + dateOfBirth.month + "/" + dateOfBirth.day
//        }
//    }
//
//    fun getDocumentType(): String? {
//        return documentType
//    }
//
//    fun setDocumentType(documentType: MRTDDocumentType?) {
//        if (documentType != null) {
//            this.documentType = documentType.toString().replace("MRTD_TYPE_", "")
//        }
//    }
//
//    fun getDocumentNumber(): String? {
//        return documentNumber
//    }
//
//    fun setDocumentNumber(documentNumber: String?) {
//        if (documentNumber != null) {
//            this.documentNumber = documentNumber.replace("<", "")
//        }
//    }
//
//    fun getExpireDate(): String? {
//        return expireDate
//    }
//
//    fun setExpireDate(expireDate: Date) {
//        if (dateOfBirth != null) {
//            this.expireDate = expireDate.year
//                .toString() + "/" + expireDate.month + "/" + expireDate.day
//        }
//    }
//
//    override fun toString(): String {
//        return """
//            Surname: $surname
//            Forename: $forename
//            Gender: $gender
//            DateOfBirth: $dateOfBirth
//            DocumentType: $documentType
//            DocumentNumber: $documentNumber
//            ExpireDate: $expireDate
//            Nationality:$nationality
//            Issuer:$issuer
//            Opt1: $opt1
//            Opt2: $opt2
//            """.trimIndent()
//    }
//
//    companion object {
//        val CREATOR: Creator<IdentificationDetail> = object : Creator<IdentificationDetail?> {
//            override fun createFromParcel(source: Parcel): IdentificationDetail? {
//                return IdentificationDetail(source)
//            }
//
//            override fun newArray(size: Int): Array<IdentificationDetail?> {
//                return arrayOfNulls(size)
//            }
//        }
//    }
//}