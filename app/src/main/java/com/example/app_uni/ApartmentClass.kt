package com.example.app_uni

import android.os.Bundle


class ApartmentClass(
    var apartmentNumber:Int,
    var apartmentBuilding:Int,
    var apartmentStreet:String,
    var responsibleForQA:String,
    var apartmentCompletitionStatus:Int,
    var raportDataset: List<RaportActivity.RaportItem> = listOf(
        RaportActivity.RaportItem("Отделка окна", false),
        RaportActivity.RaportItem("Установка розеток", false),
        RaportActivity.RaportItem("Монтаж обоев", false),
        RaportActivity.RaportItem("Монтаж потолка", false),
        RaportActivity.RaportItem("Монтаж освещения", false),
        RaportActivity.RaportItem("Установка плиток в санузле", false),
        RaportActivity.RaportItem("Укладка ломината", false)
    ),
    var bluePrintURL:String)
    {

    fun getApartmentInfo():List<Any>{
        return listOf(this.apartmentNumber, this.apartmentBuilding, this.apartmentStreet, this.responsibleForQA, this.apartmentCompletitionStatus, this.raportDataset, bluePrintURL)
    }
    fun setApartmentInfo(newApartmentNumber:Int=this.apartmentNumber,
                         newApartmentBuilding:Int=this.apartmentBuilding,
                         newApartmentStreet:String=this.apartmentStreet,
                         newResponsibleForQA:String=this.responsibleForQA,
                         newApartmentCompletitionStatus:Int=this.apartmentCompletitionStatus){
        this.apartmentNumber=newApartmentNumber
        this.apartmentBuilding=newApartmentBuilding
        this.apartmentStreet=newApartmentStreet
        this.responsibleForQA=newResponsibleForQA
        this.apartmentCompletitionStatus=newApartmentCompletitionStatus
    }
    fun toBundle(): Bundle {
        var bundle = Bundle()
        bundle.putInt("apartmentNumber", apartmentNumber)
        bundle.putInt("apartmentBuilding", apartmentBuilding)
        bundle.putString("apartmentStreet", apartmentStreet)
        bundle.putString("responsibleForQA", responsibleForQA)
        bundle.putInt("apartmentCompletitionStatus", apartmentCompletitionStatus)
        bundle.putString("bluePrintURL", bluePrintURL)
        bundle.putBoolean(raportDataset[0].Task, raportDataset[0].Status)
        bundle.putBoolean(raportDataset[1].Task, raportDataset[1].Status)
        bundle.putBoolean(raportDataset[2].Task, raportDataset[2].Status)
        bundle.putBoolean(raportDataset[3].Task, raportDataset[3].Status)
        bundle.putBoolean(raportDataset[4].Task, raportDataset[4].Status)
        bundle.putBoolean(raportDataset[5].Task, raportDataset[5].Status)
        bundle.putBoolean(raportDataset[6].Task, raportDataset[6].Status)
        return bundle
    }
}


