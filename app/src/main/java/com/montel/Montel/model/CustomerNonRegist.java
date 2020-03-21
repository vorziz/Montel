package com.montel.Montel.model;

import com.google.gson.Gson;

public class CustomerNonRegist {



    public String type;
    public String noIDPEL;
    public String name;
    public String powerClass;
    public String powerType;
    public String noReport;
    public String address;
    public String violation;
    public String type_violation;
    public String employe;
    public String lat;
    public String coordinat;
    public String image1;
    public String image2;
    public String image3;
    public String phone;
    public String dateCreate;
    public String dateUpdate;
    public String key;
    public String unitPelayanan;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public CustomerNonRegist(String key,
                              String noIDPEL,
                              String golongan,
                              String daya,
                              String nama,
                              String alamat,
                              String koordinat,
                              String no_hp,
                              String no_berita_acara,
                              String jenis_pelanggaran,
                              String pelanggaran,
                              String petugas,
                              String unitPelayanan,
                              String type,
                              String dateCreate,
                              String dateUpdate) {
        this.key = key;
        this.noIDPEL = noIDPEL;
        this.powerClass = golongan;
        this.powerType = daya;
        this.name = nama;
        this.address = alamat;
        this.coordinat = koordinat;
        this.phone = no_hp;
        this.noReport = no_berita_acara;
        this.type_violation = jenis_pelanggaran;
        this.violation = pelanggaran;
        this.employe = petugas;
        this.unitPelayanan = unitPelayanan;
        this.type = type;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;

    }

    // You can add those functions as LiveTemplate !
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static CustomerNonRegist fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, CustomerNonRegist.class);
    }


        // GET


    public String getUnitPelayanan() {
        return unitPelayanan;
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

        public String getImage1() {
            return image1;
        }

        public String getImage2() {
            return image2;
        }

        public String getImage3() {
            return image3;
        }

        public String getLat(){
            return lat;
        }




        //SET


        public void setAddress(String address) {
            this.address = address;
        }

        public void setName(String name) {
            this.name = name;
        }



        public void setNoReport(String noReport) {
            this.noReport = noReport;
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

        public void setImage1(String image1) {
            this.image1 = image1;
        }

        public void setImage2(String image2) {
            this.image2 = image2;
        }

        public void setImage3(String image3) {
            this.image3 = image3;
        }




}
