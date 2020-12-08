package com.example.linkenup.code;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.linkenup.R;
import com.example.linkenup.system.Client;
import com.example.linkenup.system.Contract;
import com.example.linkenup.system.Director;
import com.example.linkenup.system.Screen;
import com.example.linkenup.system.Software;
import com.example.linkenup.system.Worker;

public class DatabaseHelper extends SQLiteOpenHelper {
    static final String insertTag = "DatabaseInsert";
    static final String updateTag = "DatabaseUpdate";


    public static final String DATABASE_NAME = "LinkenUp.db";
    public static final String
            TABLE_CLIENT = "Client",
            TABLE_WORKER = "Worker",
            TABLE_SOFTWARE = "Software",
            TABLE_SCREEN = "Screen",
            TABLE_DIRECTOR = "Director",
            TABLE_CONTRACT = "Contract";
    public Context context;


    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+TABLE_CLIENT+"(" +
                Client.ID + " integer primary key, " +
                Client.NAME + " text not null, " +
                Client.CNPJ + " text not null unique, " +
                Client.CE + " text not null, " +
                Client.ADDRESS + " text not null"+
                ")");

        db.execSQL("create table "+TABLE_WORKER+"(" +
                Worker.ID + " integer primary key, " +
                Worker.NAME + " text not null, " +
                Worker.RG + " text not null, " +
                Worker.CPF + " text not null unique, " +
                Worker.CTPS + " text not null, " +
                Worker.PROFESSION + " text not null, " +
                Worker.NATIONALITY + " text not null, " +
                Worker.ADDRESS + " text not null, " +
                Worker.CIVIL_STATE + " text" +
                ")");

        db.execSQL("create table "+TABLE_SOFTWARE+"(" +
                Software.ID + " integer primary key," +
                Software.NAME + " text not null," +
                Software.DESCRIPTION + " text not null," +
                Software.SUPPORTS + " text" +
                ")");

        String screenSoftwareForeginKey = "foreign key ("+Screen.FK_SOFTWARE+") references "+TABLE_SOFTWARE+"("+Software.ID+") on delete cascade";
        db.execSQL("create table "+TABLE_SCREEN+"(" +
                Screen.ID + " integer primary key, " +
                Screen.FK_SOFTWARE + " integer not null, " +
                Screen.NAME + " text not null, " +
                Screen.FUNCTIONS + " text, " +
                Screen.URI + " text, " +
                screenSoftwareForeginKey+
                ")");

        String directorClientForeginKey = "foreign key ("+Director.FK_CLIENT+") references "+TABLE_CLIENT+"("+Client.ID+") on delete cascade";
        db.execSQL("create table "+TABLE_DIRECTOR+"(" +
                Director.ID + " integer primary key, " +
                Director.FK_CLIENT + " integer not null, " +
                Client.NAME +  " text not null, " +
                Director.CPF + " text not null unique, " +
                Director.RG + " text not null, " +
                Director.PROFESSION + " text not null, " +
                Director.NATIONALITY + " text not null, " +
                Director.ADDRESS + "  text not null, " +
                Director.CIVIL_STATE + "  text not null, " +
                directorClientForeginKey+
                ")");

        String contractClientForeginKey = "foreign key ("+Contract.FK_CLIENT+") references "+TABLE_CLIENT+"("+Client.ID+")  on delete restrict, ";
        String contractSoftwareForeginKey = "foreign key ("+Contract.FK_SOFTWARE+") references "+TABLE_SOFTWARE+"("+Software.ID+")  on delete restrict, ";
        String contractWorkerDirectorForeginKey = "foreign key ("+Contract.FK_WORKER_DIRECTOR+") references "+TABLE_WORKER+"("+Worker.ID+")  on delete restrict, ";
        String contractWorkerConsultantForeginKey = "foreign key ("+Contract.FK_WORKER_CONSULTANT+") references "+TABLE_WORKER+"("+Worker.ID+")  on delete restrict, ";
        String contractDirectorForeginKey = "foreign key ("+Contract.FK_DIRECTOR+") references "+TABLE_DIRECTOR+"("+Director.ID+")  on delete restrict ";
        db.execSQL("create table "+ TABLE_CONTRACT + "(" +
                Contract.ID + " integer primary key, " +
                Contract.FK_CLIENT + " integer not null, " +
                Contract.FK_SOFTWARE + " integer not null, " +
                Contract.FK_WORKER_DIRECTOR + " integer not null, " +
                Contract.FK_WORKER_CONSULTANT + " integer not null, " +
                Contract.FK_DIRECTOR + " integer not null, " +
                Contract.MONTH_VALUE + " integer not null, " +
                Contract.BANK + " text not null, " +
                Contract.AGENCY + " text not null, " +
                Contract.ACCOUNT + "  text not null, " +
                Contract.DAYS_CONSULTANT + "  integer not null, " +
                Contract.HOURS_CONSULTANT + " integger not null, " +
                Contract.BEGIN_HOUR + " integer not null, " +
                Contract.END_HOUR + " integer not null, " +
                contractClientForeginKey+
                contractSoftwareForeginKey+
                contractWorkerDirectorForeginKey+
                contractWorkerConsultantForeginKey+
                contractDirectorForeginKey+
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_CONTRACT);
        db.execSQL("drop table if exists "+TABLE_DIRECTOR);
        db.execSQL("drop table if exists "+TABLE_SCREEN);
        db.execSQL("drop table if exists "+TABLE_SOFTWARE);
        db.execSQL("drop table if exists "+TABLE_CLIENT);
        db.execSQL("drop table if exists "+TABLE_WORKER);
        onCreate(db);
    }

    private int getMaxID(String table)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select MAX(id) as maxID from " + table,null);
        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return 0;
        }
        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex("maxID"));
        if(!cursor.isClosed())cursor.close();
        return result;
    }

    /*
    public int insert(){
        int ocupiedID = getMaxID(TABLE_);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(.ID,ocupiedID+1);
        values.put(.,.);
        db.insert(TABLE_,null,values);
        return ocupiedID + 1;
    }
    */

    public int insertClient(Client client){
        int ocupiedID = getMaxID(TABLE_CLIENT);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Client.ID,ocupiedID+1);
        values.put(Client.NAME,client.name);
        values.put(Client.CNPJ,client.cnpj);
        values.put(Client.CE,client.ce);
        values.put(Client.ADDRESS,client.address);
        db.insert(TABLE_CLIENT,null,values);
        Log.d(insertTag,"id: "+String.valueOf(ocupiedID)+"\n"+client.name);
        return ocupiedID+1;
    }

    public int insertWorker(Worker worker){
        int ocupiedID = getMaxID(TABLE_WORKER);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Worker.ID,ocupiedID+1);
        values.put(Worker.NAME,worker.name);
        values.put(Worker.RG,worker.rg);
        values.put(Worker.CPF,worker.cpf);
        values.put(Worker.CTPS,worker.ctps);
        values.put(Worker.PROFESSION,worker.profession);
        values.put(Worker.NATIONALITY,worker.nationality);
        values.put(Worker.ADDRESS,worker.address);
        values.put(Worker.CIVIL_STATE,worker.civilState);
        db.insert(TABLE_WORKER,null,values);
        Log.d(insertTag,"id: "+String.valueOf(ocupiedID)+"\n"+worker.name);
        return ocupiedID+1;
    }

    public int insertSoftware(Software software){
        int ocupiedID = getMaxID(TABLE_SOFTWARE);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Software.ID,ocupiedID+1);
        values.put(Software.NAME,software.name);
        values.put(Software.DESCRIPTION,software.description);
        values.put(Software.SUPPORTS,software.supports);
        db.insert(TABLE_SOFTWARE,null,values);
        for(int i =0; i< software.screenList.size();i++){
            Toast.makeText(context,"screen",Toast.LENGTH_LONG).show();
            int screenOcupiedID = getMaxID(TABLE_SCREEN);
            values = new ContentValues();
            values.put(Screen.ID,screenOcupiedID+1);
            values.put(Screen.FK_SOFTWARE,ocupiedID+1);
            values.put(Screen.NAME,software.screenList.get(i).name);
            values.put(Screen.FUNCTIONS,software.screenList.get(i).functions);
            values.put(Screen.URI,software.screenList.get(i).uri);
            db.insert(TABLE_SCREEN,null,values);
        }
        Log.d(insertTag,"id: "+String.valueOf(ocupiedID)+"\n"+software.name);

        return ocupiedID + 1;
    }

    public int insertDirector(Director director){
        int ocupiedID = getMaxID(TABLE_DIRECTOR);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Director.ID,ocupiedID+1);
        values.put(Director.FK_CLIENT,director.fkClient);
        values.put(Director.RG,director.rg);
        values.put(Director.CPF,director.cpf);
        values.put(Director.NAME,director.name);
        values.put(Director.CPF,director.cpf);
        values.put(Director.PROFESSION,director.profession);
        values.put(Director.NATIONALITY,director.nationality);
        values.put(Director.ADDRESS,director.address);
        values.put(Director.CIVIL_STATE,director.civilState);
        db.insert(TABLE_DIRECTOR,null,values);
        Log.d(insertTag,"id: "+String.valueOf(ocupiedID)+"\n"+director.name);
        return ocupiedID + 1;
    }

    public int insertScreen(Screen screen){
        int ocupiedID = getMaxID(TABLE_SCREEN);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Screen.ID,ocupiedID+1);
        values.put(Screen.FK_SOFTWARE,screen.fkSoftware);
        values.put(Screen.NAME,screen.name);
        values.put(Screen.FUNCTIONS,screen.functions);
        values.put(Screen.URI,screen.uri!=null?screen.uri:"NULL");
        db.insert(TABLE_SCREEN,null,values);
        Log.d(insertTag,"id: "+String.valueOf(ocupiedID)+"\n"+screen.name);
        return ocupiedID + 1;
    }

    public int insertContract(Contract contract){
        int ocupiedID = getMaxID(TABLE_CONTRACT);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.ID,ocupiedID+1);
        values.put(Contract.FK_CLIENT,contract.fkClient);
        values.put(Contract.FK_SOFTWARE,contract.fkSoftware);
        values.put(Contract.FK_WORKER_DIRECTOR,contract.fkWorkerDirector);
        values.put(Contract.FK_WORKER_CONSULTANT,contract.fkWorkerConsultant);
        values.put(Contract.FK_DIRECTOR,contract.fkDirector);
        values.put(Contract.MONTH_VALUE,contract.monthValue);
        values.put(Contract.BANK,contract.bank);
        values.put(Contract.AGENCY,contract.agency);
        values.put(Contract.ACCOUNT,contract.account);
        values.put(Contract.DAYS_CONSULTANT,contract.daysConsultant);
        values.put(Contract.HOURS_CONSULTANT,contract.hoursConsultant);
        values.put(Contract.BEGIN_HOUR,contract.beginHour);
        values.put(Contract.END_HOUR,contract.endHour);
        Log.d(insertTag,"ContractID: "+String.valueOf(ocupiedID));
        db.insert(TABLE_CONTRACT,null,values);
        return ocupiedID + 1;
    }

    /*public  get(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_+" where "+.ID+"="+String.valueOf(id),null);
        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        cursor.moveToFirst();
         result = new (
                cursor.getInt(cursor.getColumnIndex(.ID)),
                cursor.get(cursor.getColumnIndex(.)),

        );
        return result;
    }*/

    public Client getClient(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_CLIENT+" where "+Client.ID+"="+String.valueOf(id),null);
        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        cursor.moveToFirst();
        Client result = new Client(
                cursor.getInt(cursor.getColumnIndex(Client.ID)),
                cursor.getString(cursor.getColumnIndex(Client.NAME)),
                cursor.getString(cursor.getColumnIndex(Client.CNPJ)),
                cursor.getString(cursor.getColumnIndex(Client.CE)),
                cursor.getString(cursor.getColumnIndex(Client.ADDRESS))
        );
        return result;
    }

    public Client[] getAllClient(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_CLIENT,null);
        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        Client[] result = new Client[cursor.getCount()];
        cursor.moveToFirst();
        while(!(cursor.isAfterLast())){
            result[cursor.getPosition()]= new Client(
                    cursor.getInt(cursor.getColumnIndex(Client.ID)),
                    cursor.getString(cursor.getColumnIndex(Client.NAME)),
                    cursor.getString(cursor.getColumnIndex(Client.CNPJ)),
                    cursor.getString(cursor.getColumnIndex(Client.CE)),
                    cursor.getString(cursor.getColumnIndex(Client.ADDRESS))
                    );
            cursor.moveToNext();
        }
        return result;
    }

    public Worker getWorker(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_WORKER+" where "+Worker.ID+"="+String.valueOf(id),null);
        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        cursor.moveToFirst();
        Worker result = new Worker(
                cursor.getInt(cursor.getColumnIndex(Worker.ID)),
                cursor.getString(cursor.getColumnIndex(Worker.NAME)),
                cursor.getString(cursor.getColumnIndex(Worker.RG)),
                cursor.getString(cursor.getColumnIndex(Worker.CPF)),
                cursor.getString(cursor.getColumnIndex(Worker.CTPS)),
                cursor.getString(cursor.getColumnIndex(Worker.PROFESSION)),
                cursor.getString(cursor.getColumnIndex(Worker.NATIONALITY)),
                cursor.getString(cursor.getColumnIndex(Worker.ADDRESS)),
                cursor.getString(cursor.getColumnIndex(Worker.CIVIL_STATE))
        );
        return result;
    }

    public Worker[] getAllWorker(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_WORKER,null);
        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        Worker[] result = new Worker[cursor.getCount()];
        cursor.moveToFirst();
        while(!(cursor.isAfterLast())){
            result[cursor.getPosition()]= new Worker(
                    cursor.getInt(cursor.getColumnIndex(Worker.ID)),
                    cursor.getString(cursor.getColumnIndex(Worker.NAME)),
                    cursor.getString(cursor.getColumnIndex(Worker.RG)),
                    cursor.getString(cursor.getColumnIndex(Worker.CPF)),
                    cursor.getString(cursor.getColumnIndex(Worker.CTPS)),
                    cursor.getString(cursor.getColumnIndex(Worker.PROFESSION)),
                    cursor.getString(cursor.getColumnIndex(Worker.NATIONALITY)),
                    cursor.getString(cursor.getColumnIndex(Worker.ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(Worker.CIVIL_STATE))
                    );
            cursor.moveToNext();
        }
        return result;
    }

    public Software getSoftware(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_SOFTWARE+" where "+Software.ID+"="+String.valueOf(id),null);
        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        cursor.moveToFirst();
        Software result = new Software(
                cursor.getInt(cursor.getColumnIndex(Software.ID)),
                cursor.getString(cursor.getColumnIndex(Software.NAME)),
                cursor.getString(cursor.getColumnIndex(Software.DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(Software.SUPPORTS))
        );
        return result;
    }

    public Director getDirector(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_DIRECTOR+" where "+Director.ID+"="+String.valueOf(id),null);
        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        cursor.moveToFirst();
        Director result = new Director(
                cursor.getInt(cursor.getColumnIndex(Director.ID)),
                cursor.getInt(cursor.getColumnIndex(Director.FK_CLIENT)),
                cursor.getString(cursor.getColumnIndex(Director.NAME)),
                cursor.getString(cursor.getColumnIndex(Director.RG)),
                cursor.getString(cursor.getColumnIndex(Director.CPF)),
                cursor.getString(cursor.getColumnIndex(Director.PROFESSION)),
                cursor.getString(cursor.getColumnIndex(Director.ADDRESS)),
                cursor.getString(cursor.getColumnIndex(Director.NATIONALITY)),
                cursor.getString(cursor.getColumnIndex(Director.CIVIL_STATE))
        );
        return result;
    }

    public Director[] getAllDirector(int clientID){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_DIRECTOR+" where "+Director.FK_CLIENT+" = "+String.valueOf(clientID),null);
        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        Director[] result = new Director[cursor.getCount()];
        cursor.moveToFirst();
        while(!(cursor.isAfterLast())){
            result[cursor.getPosition()]= new Director(
                    cursor.getInt(cursor.getColumnIndex(Director.ID)),
                    cursor.getInt(cursor.getColumnIndex(Director.FK_CLIENT)),
                    cursor.getString(cursor.getColumnIndex(Director.NAME)),
                    cursor.getString(cursor.getColumnIndex(Director.RG)),
                    cursor.getString(cursor.getColumnIndex(Director.CPF)),
                    cursor.getString(cursor.getColumnIndex(Director.PROFESSION)),
                    cursor.getString(cursor.getColumnIndex(Director.ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(Director.NATIONALITY)),
                    cursor.getString(cursor.getColumnIndex(Director.CIVIL_STATE))
            );
            cursor.moveToNext();
        }
        return result;
    }

    public Director[] getAllDirector(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_DIRECTOR,null);
        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        Director[] result = new Director[cursor.getCount()];
        cursor.moveToFirst();
        while(!(cursor.isAfterLast())){
            result[cursor.getPosition()]= new Director(
                    cursor.getInt(cursor.getColumnIndex(Director.ID)),
                    cursor.getInt(cursor.getColumnIndex(Director.FK_CLIENT)),
                    cursor.getString(cursor.getColumnIndex(Director.NAME)),
                    cursor.getString(cursor.getColumnIndex(Director.RG)),
                    cursor.getString(cursor.getColumnIndex(Director.CPF)),
                    cursor.getString(cursor.getColumnIndex(Director.PROFESSION)),
                    cursor.getString(cursor.getColumnIndex(Director.ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(Director.NATIONALITY)),
                    cursor.getString(cursor.getColumnIndex(Director.CIVIL_STATE))
            );
            cursor.moveToNext();
        }
        return result;
    }

    public Screen getScreen(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_SCREEN+" where "+Screen.ID+"="+String.valueOf(id),null);
        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        cursor.moveToFirst();
        Screen result = new Screen(
                cursor.getInt(cursor.getColumnIndex(Screen.ID)),
                cursor.getInt(cursor.getColumnIndex(Screen.FK_SOFTWARE)),
                cursor.getString(cursor.getColumnIndex(Screen.NAME)),
                cursor.getString(cursor.getColumnIndex(Screen.FUNCTIONS)),
                cursor.getString(cursor.getColumnIndex(Screen.URI))
                );
        return result;
    }

    public Screen[] getAllScreen(int softwareID){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_SCREEN+" where "+Screen.FK_SOFTWARE+" = "+String.valueOf(softwareID),null);
        if(!(cursor.getCount() >0))
        {
            Toast.makeText(context,"select * from "+TABLE_SCREEN+" where "+Screen.FK_SOFTWARE+" = "+String.valueOf(softwareID),Toast.LENGTH_LONG).show();
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        Screen[] result = new Screen[cursor.getCount()];
        cursor.moveToFirst();
        while(!(cursor.isAfterLast())){
            result[cursor.getPosition()]= new Screen(
                    cursor.getInt(cursor.getColumnIndex(Screen.ID)),
                    cursor.getInt(cursor.getColumnIndex(Screen.FK_SOFTWARE)),
                    cursor.getString(cursor.getColumnIndex(Screen.NAME)),
                    cursor.getString(cursor.getColumnIndex(Screen.FUNCTIONS)),
                    cursor.getString(cursor.getColumnIndex(Screen.URI))
                    );
            cursor.moveToNext();
        }
        if(!cursor.isClosed())cursor.close();
        return result;
    }

    public Contract getContract(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_CONTRACT+" where "+Contract.ID+"="+String.valueOf(id),null);
        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        cursor.moveToFirst();
        Contract result = new Contract(
                cursor.getInt(cursor.getColumnIndex(Contract.ID)),
                cursor.getInt(cursor.getColumnIndex(Contract.FK_CLIENT)),
                cursor.getInt(cursor.getColumnIndex(Contract.FK_SOFTWARE)),
                cursor.getInt(cursor.getColumnIndex(Contract.FK_WORKER_DIRECTOR)),
                cursor.getInt(cursor.getColumnIndex(Contract.FK_WORKER_CONSULTANT)),
                cursor.getInt(cursor.getColumnIndex(Contract.FK_DIRECTOR)),
                cursor.getInt(cursor.getColumnIndex(Contract.MONTH_VALUE)),
                cursor.getString(cursor.getColumnIndex(Contract.BANK)),
                cursor.getString(cursor.getColumnIndex(Contract.AGENCY)),
                cursor.getString(cursor.getColumnIndex(Contract.ACCOUNT)),
                cursor.getInt(cursor.getColumnIndex(Contract.DAYS_CONSULTANT)),
                cursor.getInt(cursor.getColumnIndex(Contract.HOURS_CONSULTANT)),
                cursor.getInt(cursor.getColumnIndex(Contract.BEGIN_HOUR)),
                cursor.getInt(cursor.getColumnIndex(Contract.END_HOUR))
                );
        return result;
    }

    public Contract[] getAllContract(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_CONTRACT,null);
        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        Contract[] result = new Contract[cursor.getCount()];
        cursor.moveToFirst();
        while(!(cursor.isAfterLast())){
            result[cursor.getPosition()]= new Contract(
                    cursor.getInt(cursor.getColumnIndex(Contract.ID)),
                    cursor.getInt(cursor.getColumnIndex(Contract.FK_CLIENT)),
                    cursor.getInt(cursor.getColumnIndex(Contract.FK_SOFTWARE)),
                    cursor.getInt(cursor.getColumnIndex(Contract.FK_WORKER_DIRECTOR)),
                    cursor.getInt(cursor.getColumnIndex(Contract.FK_WORKER_CONSULTANT)),
                    cursor.getInt(cursor.getColumnIndex(Contract.FK_DIRECTOR)),
                    cursor.getInt(cursor.getColumnIndex(Contract.MONTH_VALUE)),
                    cursor.getString(cursor.getColumnIndex(Contract.BANK)),
                    cursor.getString(cursor.getColumnIndex(Contract.AGENCY)),
                    cursor.getString(cursor.getColumnIndex(Contract.ACCOUNT)),
                    cursor.getInt(cursor.getColumnIndex(Contract.DAYS_CONSULTANT)),
                    cursor.getInt(cursor.getColumnIndex(Contract.HOURS_CONSULTANT)),
                    cursor.getInt(cursor.getColumnIndex(Contract.BEGIN_HOUR)),
                    cursor.getInt(cursor.getColumnIndex(Contract.END_HOUR))
                    );
            cursor.moveToNext();
        }
        return result;
    }

    public Client[] findClient(String searchValue,String nullableRow){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;

        if(nullableRow==null) cursor = db.rawQuery("select * from "+TABLE_CLIENT+
                " where cast("+Client.ID+" as text) like '%"+searchValue+"%' " +
                        " or "+Client.NAME+" like '%"+searchValue+"%' "+
                        " or "+Client.CNPJ+" like '%"+searchValue+"%' "+
                        " or "+Client.CE+" like '%"+searchValue+"%' "+
                        " or "+Client.ADDRESS+" like '%"+searchValue+"%' "
                ,null);

        else if(nullableRow!=Client.ID) cursor = db.rawQuery("select * from "+TABLE_CLIENT+
                        " where "+nullableRow+" like '%"+searchValue+"%'"
                ,null);

        else cursor = db.rawQuery("select * from "+TABLE_CLIENT+
                    "where cast("+Client.ID+" as text) like '%"+searchValue+"%'"
                    ,null);

        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        Client[] result = new Client[cursor.getCount()];
        cursor.moveToFirst();
        while(!(cursor.isAfterLast())){
            result[cursor.getPosition()]= new Client(
                    cursor.getInt(cursor.getColumnIndex(Client.ID)),
                    cursor.getString(cursor.getColumnIndex(Client.NAME)),
                    cursor.getString(cursor.getColumnIndex(Client.CNPJ)),
                    cursor.getString(cursor.getColumnIndex(Client.CE)),
                    cursor.getString(cursor.getColumnIndex(Client.ADDRESS))
            );
            cursor.moveToNext();
        }
        return result;
    }

    public void sqliteUpgrade(){
        this.onUpgrade(this.getWritableDatabase(),0,0);
    }

    public Software[] getAllSoftware(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_SOFTWARE,null);
        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        Software[] result = new Software[cursor.getCount()];
        cursor.moveToFirst();
        while(!(cursor.isAfterLast())){
            result[cursor.getPosition()]= new Software(
                    cursor.getInt(cursor.getColumnIndex(Software.ID)),
                    cursor.getString(cursor.getColumnIndex(Software.NAME)),
                    cursor.getString(cursor.getColumnIndex(Software.DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(Software.SUPPORTS))
            );
            cursor.moveToNext();
        }
        return result;
    }

    public Worker[] findWorker(String searchValue,String nullableRow){
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor;

            if(nullableRow==null) cursor = db.rawQuery("select * from "+TABLE_WORKER+
                            " where cast("+Worker.ID+" as text) like '%"+searchValue+"%'" +
                            " or "+Worker.CPF+" like '%"+searchValue+"%'"+
                            " or "+Worker.RG+" like '%"+searchValue+"%'"+
                            " or "+Worker.CTPS+" like '%"+searchValue+"%'"+
                            " or "+Worker.ADDRESS+" like '%"+searchValue+"%'"+
                            " or "+Worker.NATIONALITY+" like '%"+searchValue+"%'"+
                            " or "+Worker.CIVIL_STATE+" like '%"+searchValue+"%'"+
                            " or "+Worker.PROFESSION+" like '%"+searchValue+"%'"+
                            " or "+Worker.NAME+" like '%"+searchValue+"%'"
                    ,null);

            else if(nullableRow!=Worker.ID) cursor = db.rawQuery("select * from "+TABLE_WORKER+
                            " where "+nullableRow+" like '%"+searchValue+"%'"
                    ,null);

            else cursor = db.rawQuery("select * from "+TABLE_WORKER+
                                " where cast("+Worker.ID+" as text) like '%"+searchValue+"%'"
                        ,null);

            if(!(cursor.getCount() >0))
            {
                if(!cursor.isClosed())cursor.close();
                return null;
            }
            Worker[] result = new Worker[cursor.getCount()];
            cursor.moveToFirst();
            while(!(cursor.isAfterLast())){
                result[cursor.getPosition()]= new Worker(
                        cursor.getInt(cursor.getColumnIndex(Worker.ID)),
                        cursor.getString(cursor.getColumnIndex(Worker.NAME)),
                        cursor.getString(cursor.getColumnIndex(Worker.RG)),
                        cursor.getString(cursor.getColumnIndex(Worker.CPF)),
                        cursor.getString(cursor.getColumnIndex(Worker.CTPS)),
                        cursor.getString(cursor.getColumnIndex(Worker.PROFESSION)),
                        cursor.getString(cursor.getColumnIndex(Worker.NATIONALITY)),
                        cursor.getString(cursor.getColumnIndex(Worker.ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(Worker.CIVIL_STATE))
                );
                cursor.moveToNext();
            }
            if(!cursor.isClosed())cursor.close();
            return result;
        }

    public Director[] findDirector(String searchValue,String nullableRow){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;

        if(nullableRow==null) cursor = db.rawQuery("select * from "+TABLE_DIRECTOR+
                        " where cast("+Director.ID+" as text) like '%"+searchValue+"%'" +
                        " or "+Director.CPF+" like '%"+searchValue+"%'"+
                        " or "+Director.RG+" like '%"+searchValue+"%'"+
                        " or "+Director.ADDRESS+" like '%"+searchValue+"%'"+
                        " or "+Director.NATIONALITY+" like '"+searchValue+"%'"+
                        " or "+Director.CIVIL_STATE+" like '%"+searchValue+"%'"+
                        " or "+Director.PROFESSION+" like '%"+searchValue+"%'"+
                        " or "+Director.NAME+" like '%"+searchValue+"%'"
                ,null);

        else if(nullableRow!=Director.ID&&nullableRow!=Director.FK_CLIENT) cursor = db.rawQuery("select * from "+TABLE_DIRECTOR+
                        " where "+nullableRow+" like '%"+searchValue+"%'"
                ,null);

        else cursor = db.rawQuery("select * from "+TABLE_DIRECTOR+
                            "where cast("+nullableRow+" as text) like '%"+searchValue+"%'"
                    ,null);

        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        Director[] result = new Director[cursor.getCount()];
        cursor.moveToFirst();
        while(!(cursor.isAfterLast())){
            result[cursor.getPosition()]= new Director(
                    cursor.getInt(cursor.getColumnIndex(Director.ID)),
                    cursor.getInt(cursor.getColumnIndex(Director.FK_CLIENT)),
                    cursor.getString(cursor.getColumnIndex(Director.NAME)),
                    cursor.getString(cursor.getColumnIndex(Director.RG)),
                    cursor.getString(cursor.getColumnIndex(Director.CPF)),
                    cursor.getString(cursor.getColumnIndex(Director.PROFESSION)),
                    cursor.getString(cursor.getColumnIndex(Director.NATIONALITY)),
                    cursor.getString(cursor.getColumnIndex(Director.ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(Director.CIVIL_STATE))
            );
            cursor.moveToNext();
        }
        return result;
    }

    public Contract[] findContract(String searchValue,String nullableRow){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;

        if(nullableRow==null) cursor = db.rawQuery("select * from "+TABLE_CONTRACT+
                        " where cast("+Contract.ID+" as text) like '%"+searchValue+"%'" +
                        " or cast("+Contract.FK_CLIENT+" as text) like '%"+searchValue+"%'" +
                        " or cast("+Contract.FK_SOFTWARE+" as text) like '%"+searchValue+"%'" +
                        " or cast("+Contract.FK_WORKER_DIRECTOR+" as text) like '%"+searchValue+"%'" +
                        " or cast("+Contract.FK_WORKER_CONSULTANT+" as text) like '%"+searchValue+"%'" +
                        " or cast("+Contract.FK_DIRECTOR+" as text) like '%"+searchValue+"%'" +
                        " or "+Contract.BANK+" like '%"+searchValue+"%'"+
                        " or "+Contract.AGENCY+" like '%"+searchValue+"%'"+
                        " or "+Contract.ACCOUNT+" like '%"+searchValue+"%'"+
                        " or cast("+Contract.MONTH_VALUE+" as text) like '%"+searchValue+"%'"+
                        " or cast("+Contract.DAYS_CONSULTANT+" as text) like '%"+searchValue+"%'" +
                        " or cast("+Contract.HOURS_CONSULTANT+" as text) like '%"+searchValue+"%'" +
                        " or cast("+Contract.BEGIN_HOUR+" as text) like '%"+searchValue+"%'" +
                        " or cast("+Contract.END_HOUR+" as text) like '%"+searchValue+"%'"
                ,null);

        else if(nullableRow==Contract.BANK || nullableRow==Contract.AGENCY || nullableRow==Contract.ACCOUNT)
            cursor = db.rawQuery("select * from "+TABLE_CONTRACT+
                        " where "+nullableRow+" like '%"+searchValue+"%'"
                ,null);

        else if(nullableRow==Contract.FK_DIRECTOR||nullableRow==Contract.FK_CLIENT||nullableRow==Contract.FK_SOFTWARE||nullableRow==Contract.FK_WORKER_CONSULTANT||nullableRow==Contract.FK_WORKER_DIRECTOR)
            cursor = db.rawQuery("select * from "+TABLE_CONTRACT+
                            " where cast("+nullableRow+" as text) like '"+searchValue+"'"
                    ,null);
        else
            cursor = db.rawQuery("select * from "+TABLE_CONTRACT+
                            " where cast("+nullableRow+" as text) like '%"+searchValue+"%'"
                    ,null);

        if(cursor == null){
            return null;
        }
        if(!(cursor.getCount() >0)||cursor.isClosed())
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        Contract[] result = new Contract[cursor.getCount()];
        cursor.moveToFirst();
        while(!(cursor.isAfterLast())){
            result[cursor.getPosition()]= new Contract(
                    cursor.getInt(cursor.getColumnIndex(Contract.ID)),
                    cursor.getInt(cursor.getColumnIndex(Contract.FK_CLIENT)),
                    cursor.getInt(cursor.getColumnIndex(Contract.FK_SOFTWARE)),
                    cursor.getInt(cursor.getColumnIndex(Contract.FK_WORKER_DIRECTOR)),
                    cursor.getInt(cursor.getColumnIndex(Contract.FK_WORKER_CONSULTANT)),
                    cursor.getInt(cursor.getColumnIndex(Contract.FK_DIRECTOR)),
                    cursor.getInt(cursor.getColumnIndex(Contract.MONTH_VALUE)),
                    cursor.getString(cursor.getColumnIndex(Contract.BANK)),
                    cursor.getString(cursor.getColumnIndex(Contract.AGENCY)),
                    cursor.getString(cursor.getColumnIndex(Contract.ACCOUNT)),
                    cursor.getInt(cursor.getColumnIndex(Contract.DAYS_CONSULTANT)),
                    cursor.getInt(cursor.getColumnIndex(Contract.HOURS_CONSULTANT)),
                    cursor.getInt(cursor.getColumnIndex(Contract.BEGIN_HOUR)),
                    cursor.getInt(cursor.getColumnIndex(Contract.END_HOUR))
            );
            cursor.moveToNext();
        }
        if(!cursor.isClosed())cursor.close();
        return result;
    }

    public Contract[] fkContract(int fkValue, String fkRow){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;

        if(fkRow==Contract.FK_CLIENT||fkRow==Contract.FK_SOFTWARE|| fkRow==Contract.FK_DIRECTOR || fkRow==Contract.FK_WORKER_DIRECTOR ||fkRow==Contract.FK_WORKER_CONSULTANT)
            cursor = db.rawQuery("select * from "+TABLE_CONTRACT+" where "+fkRow+" = "+String.valueOf(fkValue),null);
        else return null;

        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        Contract[] result = new Contract[cursor.getCount()];
        cursor.moveToFirst();
        while(!(cursor.isAfterLast())){
            result[cursor.getPosition()]= new Contract(
                    cursor.getInt(cursor.getColumnIndex(Contract.ID)),
                    cursor.getInt(cursor.getColumnIndex(Contract.FK_CLIENT)),
                    cursor.getInt(cursor.getColumnIndex(Contract.FK_SOFTWARE)),
                    cursor.getInt(cursor.getColumnIndex(Contract.FK_WORKER_DIRECTOR)),
                    cursor.getInt(cursor.getColumnIndex(Contract.FK_WORKER_CONSULTANT)),
                    cursor.getInt(cursor.getColumnIndex(Contract.FK_DIRECTOR)),
                    cursor.getInt(cursor.getColumnIndex(Contract.MONTH_VALUE)),
                    cursor.getString(cursor.getColumnIndex(Contract.BANK)),
                    cursor.getString(cursor.getColumnIndex(Contract.AGENCY)),
                    cursor.getString(cursor.getColumnIndex(Contract.ACCOUNT)),
                    cursor.getInt(cursor.getColumnIndex(Contract.DAYS_CONSULTANT)),
                    cursor.getInt(cursor.getColumnIndex(Contract.HOURS_CONSULTANT)),
                    cursor.getInt(cursor.getColumnIndex(Contract.BEGIN_HOUR)),
                    cursor.getInt(cursor.getColumnIndex(Contract.END_HOUR))
            );
            cursor.moveToNext();
            if(!cursor.isClosed())cursor.close();
        }
        return result;
    }

    public int countScreen(Integer softwareID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_SCREEN + " where " + Screen.FK_SOFTWARE + " = " + String.valueOf(softwareID), null);
        if (!(cursor.getCount() > 0)) {
         return 0;
        }
        int result = cursor.getCount();
        if(!cursor.isClosed())cursor.close();
        return result;
    }

    public int updateClient(Client client) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Client.NAME,client.name);
        values.put(Client.CNPJ,client.cnpj);
        values.put(Client.CE,client.ce);
        values.put(Client.ADDRESS,client.address);
        Log.d(updateTag,"id: "+String.valueOf(client.id)+"\n"+client.name);
        return db.update(TABLE_CLIENT,values,Client.ID+" = "+String.valueOf(client.id),null);
    }

    public int updateContract(Contract contract) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.ID,contract.id);
        values.put(Contract.FK_CLIENT,contract.fkClient);
        values.put(Contract.FK_SOFTWARE,contract.fkSoftware);
        values.put(Contract.FK_WORKER_DIRECTOR,contract.fkWorkerDirector);
        values.put(Contract.FK_WORKER_CONSULTANT,contract.fkWorkerConsultant);
        values.put(Contract.FK_DIRECTOR,contract.fkDirector);
        values.put(Contract.MONTH_VALUE,contract.monthValue);
        values.put(Contract.BANK,contract.bank);
        values.put(Contract.AGENCY,contract.agency);
        values.put(Contract.ACCOUNT,contract.account);
        values.put(Contract.DAYS_CONSULTANT,contract.daysConsultant);
        values.put(Contract.HOURS_CONSULTANT,contract.hoursConsultant);
        values.put(Contract.BEGIN_HOUR,contract.beginHour);
        values.put(Contract.END_HOUR,contract.endHour);
        Log.d(updateTag,"id: "+String.valueOf(contract.id)+" contract");
        return db.update(TABLE_CONTRACT,values,Contract.ID+" = "+String.valueOf(contract.id),null);
    }

    public boolean removeClient(int id) {
        SQLiteDatabase db = getWritableDatabase();
        if(getAllDirector(id)!=null||fkContract(id,Contract.FK_CLIENT)!=null)return false;
        db.delete(TABLE_CLIENT,Client.ID+" = "+String.valueOf(id),null);
        return  true;
    }

    public boolean removeContract(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CONTRACT,Contract.ID+" = "+String.valueOf(id),null);
        return  true;
    }

    public int countDirectorContracts(int directorID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select id from "+TABLE_CONTRACT+" where "+Contract.FK_DIRECTOR+" = "+String.valueOf(directorID),null);
        int result = cursor.getCount();
        if(!cursor.isClosed()) cursor.close();
        return result;
    }

    public boolean removeDirector(int id) {
        SQLiteDatabase db = getWritableDatabase();
        if(fkContract(id,Contract.FK_DIRECTOR)!=null)return false;
        db.delete(TABLE_DIRECTOR,Director.ID+" = "+String.valueOf(id),null);
        return  true;
    }

    public boolean removeSoftware(int id) {
        SQLiteDatabase db = getWritableDatabase();
        Contract[] contract = fkContract(id,Contract.FK_SOFTWARE);
        if(contract!=null)return false;
        for (Screen screen:getAllScreen(id)) {
            db.delete(TABLE_SCREEN,Screen.ID+" = "+String.valueOf(screen.id),null);
        }
        db.delete(TABLE_SOFTWARE,Software.ID+" = "+String.valueOf(id),null);

        if(getAllScreen(id)!=null)return false;
        return  true;
    }

    public boolean removeScreen(int id){
        SQLiteDatabase db = getWritableDatabase();
        Screen screen = getScreen(id);
        if(!(countScreen(screen.fkSoftware)>1)) return false;
        db.delete(TABLE_SCREEN,Screen.ID +" = "+String.valueOf(id),null);
        return true;
    }

    public boolean removeWorker(int id) {
        SQLiteDatabase db = getWritableDatabase();
        if(fkContract(id,Contract.FK_WORKER_CONSULTANT)!=null||fkContract(id,Contract.FK_WORKER_DIRECTOR)!=null)return false;
        db.delete(TABLE_WORKER,Worker.ID+" = "+String.valueOf(id),null);
        return  true;
    }

    public int updateWorker(Worker worker) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Worker.ID,worker.id);
        values.put(Worker.NAME,worker.name);
        values.put(Worker.RG,worker.rg);
        values.put(Worker.CPF,worker.cpf);
        values.put(Worker.CTPS,worker.ctps);
        values.put(Worker.PROFESSION,worker.profession);
        values.put(Worker.NATIONALITY,worker.nationality);
        values.put(Worker.ADDRESS,worker.address);
        values.put(Worker.CIVIL_STATE,worker.civilState);
        return db.update(TABLE_WORKER,values,Worker.ID+" = "+String.valueOf(worker.id),null);
    }

    public int updateSoftware(Software software) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Software.ID,software.id);
        values.put(Software.NAME,software.name);
        values.put(Software.DESCRIPTION,software.description);
        values.put(Software.SUPPORTS,software.supports);
        return db.update(TABLE_SOFTWARE,values,Software.ID+" = "+software.id,null);
    }

    public int updateDirector(Director director) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Director.ID,director.id);
        values.put(Director.FK_CLIENT,director.fkClient);
        values.put(Director.RG,director.rg);
        values.put(Director.CPF,director.cpf);
        values.put(Director.NAME,director.name);
        values.put(Director.CPF,director.cpf);
        values.put(Director.PROFESSION,director.profession);
        values.put(Director.NATIONALITY,director.nationality);
        values.put(Director.ADDRESS,director.address);
        values.put(Director.CIVIL_STATE,director.civilState);
        return db.update(TABLE_DIRECTOR,values, Director.ID +" = "+String.valueOf(director.id),null);

    }

    public Software[] findSoftware(String searchValue, String nullableRow) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;

        if(nullableRow==null) cursor = db.rawQuery("select * from "+TABLE_SOFTWARE+
                        " where cast("+Software.ID+" as text) like '%"+searchValue+"%' " +
                        " or "+Software.NAME+" like '%"+searchValue+"%' "+
                        " or "+Software.SUPPORTS+" like '%"+searchValue+"%' "+
                        " or "+Software.DESCRIPTION+" like '%"+searchValue+"%' "
                ,null);

        else if(nullableRow!=Software.ID) cursor = db.rawQuery("select * from "+TABLE_SOFTWARE+
                        " where "+nullableRow+" like '%"+searchValue+"%'"
                ,null);

        else cursor = db.rawQuery("select * from "+TABLE_SOFTWARE+
                            " where cast("+Software.ID+" as text) like '%"+searchValue+"%'"
                    ,null);

        if(!(cursor.getCount() >0))
        {
            if(!cursor.isClosed())cursor.close();
            return null;
        }
        Software[] result = new Software[cursor.getCount()];
        cursor.moveToFirst();
        while(!(cursor.isAfterLast())){
            result[cursor.getPosition()]= new Software(
                    cursor.getInt(cursor.getColumnIndex(Software.ID)),
                    cursor.getString(cursor.getColumnIndex(Software.NAME)),
                    cursor.getString(cursor.getColumnIndex(Software.DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(Software.SUPPORTS))
            );
            cursor.moveToNext();
        }
        if(!cursor.isClosed())cursor.close();
        return result;
    }

    public int updateScreen(Screen screen) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Screen.ID,screen.id);
        values.put(Screen.FK_SOFTWARE,screen.fkSoftware);
        values.put(Screen.NAME,screen.name);
        values.put(Screen.FUNCTIONS,screen.functions);
        values.put(Screen.URI,screen.uri!=null?screen.uri:"NULL");
        return db.update(TABLE_SCREEN, values, Screen.ID+" = "+String.valueOf(screen.id),null);
    }

    public void insertOLD() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(
                "insert into worker( " +
                        "                id, " +
                        "                name, " +
                        "                rg, " +
                        "                cpf, " +
                        "                ctps, " +
                        "                profession, " +
                        "                nationality, " +
                        "                address, " +
                        "                civilState " +
                        "             ) values " +
                        "        (1, " +
                        "        'Bruno Marciano', " +
                        "        '59.183.341-93', " +
                        "        '678.923.123-4', " +
                        "        '091232 88234-SP', " +
                        "        'Analista', " +
                        "        'Brasileiro', " +
                        "        '02961-210', " +
                        "        'Solteiro' " +
                        "        ), " +
                        " " +
                        "        (2, " +
                        "        'Renan Damprelli', " +
                        "        '53.284.123-93', " +
                        "        '123.421.173-1', " +
                        "        '094521 34133-SP', " +
                        "        'Designer', " +
                        "        'Brasileiro', " +
                        "        'Rua Tarcon, 81 Perus bro', " +
                        "        'Solteiro' " +
                        "        );");

        db.execSQL(
                "insert into client(id,name,cnpj,ce,address) values  " +
                        "        (1, " +
                        "        'teste-me-exclue', " +
                        "        '99.999.999/9999-99', " +
                        "        '99.999999-9', " +
                        "        '05125-040' " +
                        "        ), " +
                        "         " +
                        "        (2, " +
                        "        'Goal', " +
                        "        '33.425.291/9891-72', " +
                        "        '51.667721-9', " +
                        "        'Villa Lobbos São Paulo' " +
                        "        ), " +
                        "         " +
                        "        (3, " +
                        "        'Cherry Bomb', " +
                        "        '51.222.931/9090-22', " +
                        "        '51.667721-9', " +
                        "        'Parque São Domingos São Paulo' " +
                        "        );");

        db.execSQL(
                "insert into software (id,name,description,supports) values  " +
                        "        (1, " +
                        "        'Site Esportivo', " +
                        "        'Site onepage responsivo com tema esportivo', " +
                        "        NULL " +
                        "        ), " +
                        "        (2, " +
                        "        'Transporte C#local', " +
                        "        'Sistema C# de gerenciamento de empresas de transportes', " +
                        "        NULL " +
                        "        );"
                            );
        db.execSQL(
                "        insert into screen (id,softwareID,name,functions,uri) values " +
                "        (1, " +
                "        1, " +
                "        'Principal', " +
                "        'Site onepage', " +
                "        NULL " +
                "        ), " +
                " " +
                "        (2, " +
                "        2, " +
                "        'Home', " +
                "        'Navegação', " +
                "        NULL " +
                "        );");

        db.execSQL("insert into director ( " +
                "        id, " +
                "        fkClient, " +
                "        name, " +
                "        rg, " +
                "        cpf, " +
                "        profession, " +
                "        address, " +
                "        nationality, " +
                "        civilState) " +
                "        values " +
                "         " +
                "        (1,1,  " +
                "        'TESTE', " +
                "        '99.999.999-99', " +
                "        '999.999.999-9', " +
                "        'Testador', " +
                "        '88888-888', " +
                "        'Brasileiro', " +
                "        'Viuvo'), " +
                "         " +
                "        (2,2, " +
                "        'Roberto Silva', " +
                "        '93.123.453.22', " +
                "        '514.291.330-3', " +
                "        'Gerente de estabelecimento comercial', " +
                "        '90912-123', " +
                "        'Brasileiro', " +
                "        'Viuvo' " +
                "        ), " +
                " " +
                "        (3,3, " +
                "        'Ronaldo Antonino', " +
                "        '94.213.522.33', " +
                "        '313.124.356-4', " +
                "        'Gerente de empresa de transportes', " +
                "        '29019-321', " +
                "        'Brasileiro', " +
                "        'Viuvo' " +
                "        ); ");

        db.execSQL("insert into contract ( " +
                "            id, " +
                "            fkClient, " +
                "            fkSoftware, " +
                "            fkWorkerDirector, " +
                "            fkWorkerConsultant, " +
                "            fkDirector, " +
                "            monthValue, " +
                "            bank, " +
                "            agency, " +
                "            account, " +
                "            daysConsultant, " +
                "            hoursConsultant, " +
                "            beginHour, " +
                "            endHour) " +
                "        values " +
                "        (1, " +//id
                "        2, " +//client
                "        1, " +//software
                "        2, " +//work
                "        2, " +//work
                "        2, " +//dir
                "        55, " +//month
                "        'Banco Brasil', " +//bank
                "        'AgenciaNacional', " +//agency
                "        '00123', " +//account
                "        4, " +
                "        8, " +
                "        6, " +
                "        18 " +
                "        ), " +
                " " +
                "        (2, " +
                "        3, " +
                "        2, " +
                "        1, " +
                "        2, " +
                "        3, " +
                "        105, " +
                "        'Italu', " +
                "        'NoNationalAgency', " +
                "        '99321', " +
                "        2, " +
                "        6, " +
                "        7, " +
                "        17 " +
                "        ), " +
                " " +
                "        (3, " +
                "        1, " +
                "        2, " +
                "        2, " +
                "        1, " +
                "        1, " +
                "        80, " +
                "        'Banco Nacional', " +
                "        'Agenciadorasil', " +
                "        '022031', " +
                "        4, " +
                "        12, " +
                "        9, " +
                "        20 " +
                "        );");
    }
}