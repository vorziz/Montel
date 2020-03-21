package com.montel.Montel.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.Gson;
import com.montel.Montel.utils.MarkerClusterItem;

import io.realm.RealmObject;



public class Customer {

    public String key;
    public String type;
    public String noMeter;

    public String name;
    public String noIDPL;
    public String rates;
    public String powerType;
    public String noReport;
    public String address;
    public String violation;
    public String powerClass;
    public String type_violation;
    public String violationType;
    public String lat;
    public String employe;
    public String coordinat;
    public String imageURL1;
    public String imageURL2;
    public String imageURL3;
    public String phone;
    public String dateCreate;
    public String dateUpdate;
    public String unitPelayanan;


    public Customer(){

    }


    // You can add those functions as LiveTemplate !
    public String toJsonCustomer() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Customer fromJsonCustomer(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Customer.class);
    }





    // GET

    public String getKey() {
        return key;
    }

    public String getViolationType() {
        return violationType;
    }

    public String getEmploye() {
        return employe;
    }


    public String getType_violation() {
        return type_violation;
    }

    public String getPowerClass() {
        return powerClass;
    }

    public String getNoMeter() {
        return noMeter;
    }

    public String getNoReport() {
        return noReport;
    }

    public String getPowerType() {
        return powerType;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public String getRates() {
        return rates;
    }

    public String getType() {
        return type;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public String getPhone() {
        return phone;
    }

    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public String getViolation(){
        return violation;
    }

    public String getImageURL1() {
        return imageURL1;
    }

    public String getImageURL2() {
        return imageURL2;
    }

    public String getImageURL3() {
        return imageURL3;
    }

    public String getLat(){
        return lat;
    }

    public String getCoordinat() {
        return coordinat;
    }

    public String getNoIDPL() {
        return noIDPL;
    }

    public String getUnitPelayanan() {
        return unitPelayanan;
    }
    //SET


    public void setPowerClass(String powerClass) {
        this.powerClass = powerClass;
    }

    public void setUnitPelayanan(String unitPelayanan) {
        this.unitPelayanan = unitPelayanan;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setEmploye(String employe) {
        this.employe = employe;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNoMeter(String noMeter) {
        this.noMeter = noMeter;
    }

    public void setNoReport(String noReport) {
        this.noReport = noReport;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }

    public void setPowerType(String powerType) {
        this.powerType = powerType;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public void setImageURL1(String imageURL1) {
        this.imageURL1 = imageURL1;
    }

    public void setImageURL2(String imageURL2) {
        this.imageURL2 = imageURL2;
    }

    public void setImageURL3(String imageURL3) {
        this.imageURL3 = imageURL3;
    }

    public void setCoordinat(String coordinat) {
        this.coordinat = coordinat;
    }

    public void setNoIDPL(String noIDPL) {
        this.noIDPL = noIDPL;
    }


}