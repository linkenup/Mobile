package com.example.linkenup.system;

import java.io.Serializable;

public class Screen implements Serializable {
    public static final String //Database Row references
            ID = "id",
            FK_SOFTWARE = "softwareID",
            NAME = "name",
            FUNCTIONS = "functions",
            URI = "uri";

    public Integer id;
    public Integer fkSoftware;
    public String name;
    public String functions;
    public String uri;

    public Screen(){}

    public Screen(String name, String functions, String uri){
        this.name=name;
        this.functions=functions;
        this.uri = uri;
    }

    public Screen(int id,int fkSoftware, String name, String functions, String uri){
        this.name=name;
        this.functions=functions;
        this.id=id;
        this.fkSoftware=fkSoftware;
        this.uri = uri;
    }

    public Screen(int fkSoftware, String name, String functions, String uri){
        this.name=name;
        this.functions=functions;
        this.fkSoftware=fkSoftware;
        this.uri = uri;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFkSoftware() {
        return fkSoftware;
    }

    public void setFkSoftware(Integer fkSoftware) {
        this.fkSoftware = fkSoftware;
    }

    public String getFunctions() {
        return functions;
    }

    public void setFunctions(String functions) {
        this.functions = functions;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
