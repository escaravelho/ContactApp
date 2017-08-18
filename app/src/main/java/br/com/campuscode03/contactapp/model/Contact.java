package br.com.campuscode03.contactapp.model;


import com.google.gson.annotations.SerializedName;

public class Contact {

    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("phone")
    String phone;


    public Contact(int id, String name, String phone){

        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public int getId(){ return id; }

    public void setId(int id){ this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
