package com.example.linkenup.system;

public class Client {

    public static final String //Database Row references
            ID = "id",
            NAME = "name",
            CNPJ = "cnpj",
            CE = "ce",
            ADDRESS = "address";

    public Integer id;
    public String name;
    public String cnpj;
    public String ce;
    public String address;

    public Client(){ }

    public Client(String name,String cnpj,String ce,String address){
        this.name = name;
        this.cnpj = cnpj;
        this.ce = ce;
        this.address = address;
    }

    public Client(int id,String name,String cnpj,String ce,String address){
        this.name = name;
        this.cnpj = cnpj;
        this.ce = ce;
        this.address = address;
        this.id = id;
    }
}
