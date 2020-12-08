package com.example.linkenup.system;

import java.io.Serializable;

public class Director implements Serializable {
    public static final String //Database Row references
            ID = "id",
            FK_CLIENT = "fkClient",
            NAME = "name",
            RG = "rg",
            CPF = "cpf",
            PROFESSION = "profession",
            ADDRESS = "address",
            NATIONALITY = "nationality",
            CIVIL_STATE = "civilState";

    public Integer  id;
    public String   name;
    public Integer  fkClient;
    public String   rg;
    public String   cpf;
    public String   profession;
    public String   nationality;
    public String   address;
    public String   civilState;

    public Director(){}

    public Director(String name, String rg, String cpf, String profession, String address, String nationality, String civilState){
        this.name = name;
        this.rg=rg;
        this.cpf=cpf;
        this.profession=profession;
        this.address = address;
        this.nationality=nationality;
        this.civilState=civilState;
    }

    public Director(int id, int fkClient, String name, String rg, String cpf, String profession, String address, String nationality, String civilState){
        this.name = name;
        this.rg=rg;
        this.cpf=cpf;
        this.profession=profession;
        this.address = address;
        this.nationality=nationality;
        this.civilState=civilState;
        this.fkClient = fkClient;
        this.id = id;
    }

    public Director(int fkClient, String name, String rg, String cpf, String profession, String address, String nationality, String civilState){
        this.name = name;
        this.rg=rg;
        this.cpf=cpf;
        this.profession=profession;
        this.nationality=nationality;
        this.address = address;
        this.civilState=civilState;
        this.fkClient = fkClient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setFkClient(Integer fkClient) {
        this.fkClient = fkClient;
    }

    public Integer getFkClient() {
        return fkClient;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNationality() {
        return nationality;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProfession() {
        return profession;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getRg() {
        return rg;
    }

    public void setCivilState(String civilState) {
        this.civilState = civilState;
    }

    public String getCivilState() {
        return civilState;
    }
}
