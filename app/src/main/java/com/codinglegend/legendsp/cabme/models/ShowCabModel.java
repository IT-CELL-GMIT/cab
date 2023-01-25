package com.codinglegend.legendsp.cabme.models;

public class ShowCabModel {

    String
      cabId,
      cabFromAddress,
      cabToAdress,
      cabTime,
      cabOwner;

    public ShowCabModel(String cabId, String cabFromAddress, String cabToAdress, String cabTime, String cabOwner) {
        this.cabId = cabId;
        this.cabFromAddress = cabFromAddress;
        this.cabToAdress = cabToAdress;
        this.cabTime = cabTime;
        this.cabOwner = cabOwner;
    }

    public String getCabId() {
        return cabId;
    }

    public void setCabId(String cabId) {
        this.cabId = cabId;
    }

    public String getCabFromAddress() {
        return cabFromAddress;
    }

    public void setCabFromAddress(String cabFromAddress) {
        this.cabFromAddress = cabFromAddress;
    }

    public String getCabToAdress() {
        return cabToAdress;
    }

    public void setCabToAdress(String cabToAdress) {
        this.cabToAdress = cabToAdress;
    }

    public String getCabTime() {
        return cabTime;
    }

    public void setCabTime(String cabTime) {
        this.cabTime = cabTime;
    }

    public String getCabOwner() {
        return cabOwner;
    }

    public void setCabOwner(String cabOwner) {
        this.cabOwner = cabOwner;
    }
}
