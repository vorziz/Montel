package com.montel.Montel.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerClusterItem implements ClusterItem {

    private LatLng latLng;
    private String title;
    private String name;
    private String address;
    private String idpel;
    private String imageURL1;
    private double lat;
    private double lng;
    private  String key;


    public MarkerClusterItem(LatLng latLng,String idpel, String name, String address, String URLimage, String key){
        this.latLng = latLng;
        this.idpel = idpel;
        this.name = name;
        this.address = address;
        this.imageURL1 = URLimage;
        this.key = key;
    }


    public String getKey() {
        return key;
    }

    public String getURLimage() {
        return imageURL1;
    }

    public String getIdpel() {
        return idpel;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }


    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIdpel(String idpel) {
        this.idpel = idpel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setURLimage(String URLimage) {
        this.imageURL1 = URLimage;
    }
}
