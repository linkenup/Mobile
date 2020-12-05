package com.example.linkenup.system;

public class Worker{
    public static final String //Database Row references
            ID = "id",
            NAME = "name",
            RG = "rg",
            CPF = "cpf",
            CTPS = "ctps",
            PROFESSION = "profession",
            NATIONALITY = "nationality",
            ADDRESS = "address",
            CIVIL_STATE = "civilState";

    public Integer id;
    public String name;
    public String profession;
    public String rg;
    public String cpf;
    public String ctps;
    public String nationality;
    public String address;
    public String civilState;

    public Worker() {}

    public Worker(String name,String rg,String cpf,String ctps,String profession,String nationality,String address,String civilState){
        this.name = name;
        this.rg=rg;
        this.cpf=cpf;
        this.ctps=ctps;
        this.profession=profession;
        this.nationality=nationality;
        this.address=address;
        this.civilState=civilState;
    }
    public Worker(int id, String name,String rg,String cpf,String ctps,String profession,String nationality,String address,String civilState){
        this.id = id;
        this.name = name;
        this.rg=rg;
        this.cpf=cpf;
        this.ctps=ctps;
        this.profession=profession;
        this.nationality=nationality;
        this.address=address;
        this.civilState=civilState;
    }
}
