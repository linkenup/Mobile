package com.example.linkenup.system;

import java.util.ArrayList;
import java.util.List;

public class Software {
    public static final String //Database Row references
            ID = "id",
            NAME = "name",
            DESCRIPTION = "description",
            SUPPORTS = "supports";

    public Integer id;
    public String name;
    public String description;
    public String supports;
    public List<Screen> screenList;

    public Software(){ }

    public Software(String name, String description, String supports)
    {
        this.name=name;
        this.description=description;
        this.supports=supports;
        this.screenList=new ArrayList<Screen>();
    }

    public Software(int id,String name, String description, String supports)
    {
        this.name=name;
        this.description=description;
        this.supports=supports;
        this.id=id;
        this.screenList=new ArrayList<Screen>();
    }

}
