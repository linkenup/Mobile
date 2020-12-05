package com.example.linkenup.system;

public class Contract {
    public static final String //Database Row references
            ID = "id",
            FK_CLIENT = "fkClient",
            FK_SOFTWARE = "fkSoftware",
            FK_WORKER_DIRECTOR = "fkWorkerDirector",
            FK_WORKER_CONSULTANT = "fkWorkerConsultant",
            FK_DIRECTOR = "fkDirector",
            MONTH_VALUE = "monthValue",
            BANK = "bank",
            AGENCY = "agency",
            ACCOUNT = "account",
            DAYS_CONSULTANT = "daysConsultant",
            HOURS_CONSULTANT = "hoursConsultant",
            BEGIN_HOUR = "beginHour",
            END_HOUR = "endHour";


    public Integer id;
    public Integer
            fkClient,
            fkSoftware,
            fkWorkerDirector,
            fkWorkerConsultant,
            fkDirector;
    public Integer monthValue;
    public String bank;
    public String agency;
    public String account;
    public Integer daysConsultant;
    public Integer hoursConsultant;
    public Integer
            beginHour,
            endHour;

    public Contract() {}

    public Contract(int fkClient,
                    int fkSoftware,
                    int fkWorkerDirector,
                    int fkWorkerConsultant,
                    int fkDirector,
                    int monthValue,
                    String bank,String agency,String account,int daysConsultant,int hoursConsultant,int beginHour,int endHour)
    {
        this.fkClient=fkClient;
        this.fkSoftware=fkSoftware;
        this.fkWorkerDirector=fkWorkerDirector;
        this.fkWorkerConsultant=fkWorkerConsultant;
        this.fkDirector=fkDirector;
        this.monthValue=monthValue;
        this.bank=bank;
        this.agency=agency;
        this.account=account;
        this.daysConsultant=daysConsultant;
        this.hoursConsultant=hoursConsultant;
        this.beginHour=beginHour;
        this.endHour=endHour;
    }

    public Contract(int id, int fkClient,int fkSoftware,int fkWorkerDirector,int fkWorkerConsultant,int fkDirector,int monthValue,String bank,String agency,String account,int daysConsultant,int hoursConsultant,int beginHour,int endHour)
    {
        this.id = id;
        this.fkClient=fkClient;
        this.fkSoftware=fkSoftware;
        this.fkWorkerDirector=fkWorkerDirector;
        this.fkWorkerConsultant=fkWorkerConsultant;
        this.fkDirector=fkDirector;
        this.monthValue=monthValue;
        this.bank=bank;
        this.agency=agency;
        this.account=account;
        this.daysConsultant=daysConsultant;
        this.hoursConsultant=hoursConsultant;
        this.beginHour=beginHour;
        this.endHour=endHour;
    }

}
