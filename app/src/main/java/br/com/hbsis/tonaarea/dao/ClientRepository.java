package br.com.hbsis.tonaarea.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.hbsis.tonaarea.entities.Client;
import br.com.hbsis.tonaarea.util.Constants;

public class ClientRepository {

    private SQLiteDatabase connection;

    public ClientRepository(SQLiteDatabase connection) {
        this.connection = connection;
    }

    public void insert(Client client) {

        int active;
        if (client.isActive()) {
            active = 1;
        } else {
            active = 0;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.DATABASE.TABLE_CLIENT.COLUMN_ID, client.getClientId());
        contentValues.put(Constants.DATABASE.TABLE_CLIENT.COLUMN_NAME, client.getClientName());
        contentValues.put(Constants.DATABASE.TABLE_CLIENT.COLUMN_CODE, client.getClienteCode());
        contentValues.put(Constants.DATABASE.TABLE_CLIENT.COLUMN_ACTIVE, active);
        contentValues.put(Constants.DATABASE.TABLE_CLIENT.COLUMN_REVENDA_NAME, client.getRevendaName());
        contentValues.put(Constants.DATABASE.TABLE_CLIENT.COLUMN_DATE, client.getDate());

        connection.insertOrThrow(Constants.DATABASE.TABLE_CLIENT.TABLE_NAME, null, contentValues);
    }

    public List<Client> getAll() {

        List<Client> clients = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT *");
        sql.append("  FROM " + Constants.DATABASE.TABLE_CLIENT.TABLE_NAME);

        Cursor result = connection.rawQuery(sql.toString(), null);

        if (result.getCount() == 0) {
            result.close();
            return clients;
        }
        result.moveToFirst();

        do {
            Client c = new Client();
            c.setClientId(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_CLIENT.COLUMN_ID)));
            c.setClientName(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_CLIENT.COLUMN_NAME)));
            c.setClienteCode(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_CLIENT.COLUMN_CODE)));
            c.setRevendaName(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_CLIENT.COLUMN_REVENDA_NAME)));
            c.setDate(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_CLIENT.COLUMN_DATE)));

            int i = result.getInt(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_CLIENT.COLUMN_ACTIVE));
            if (i == 0) {
                c.setActive(false);
            } else if (i == 1) {
                c.setActive(true);
            }
            clients.add(c);
        } while (result.moveToNext());

        return clients;
    }

    public List<Client> getByCode(int code){
        List<Client> clients = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT *");
        sql.append("  FROM " + Constants.DATABASE.TABLE_CLIENT.TABLE_NAME);
        sql.append(" WHERE "+Constants.DATABASE.TABLE_CLIENT.COLUMN_CODE+" = "+code);

        Cursor result = connection.rawQuery(sql.toString(), null);

        if (result.getCount() == 0) {
            result.close();
            return clients;
        }
        result.moveToFirst();

        do {
            Client c = new Client();
            c.setClientId(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_CLIENT.COLUMN_ID)));
            c.setClientName(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_CLIENT.COLUMN_NAME)));
            c.setClienteCode(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_CLIENT.COLUMN_CODE)));
            c.setRevendaName(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_CLIENT.COLUMN_REVENDA_NAME)));
            c.setDate(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_CLIENT.COLUMN_DATE)));

            int i = result.getInt(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_CLIENT.COLUMN_ACTIVE));
            if (i == 0) {
                c.setActive(false);
            } else if (i == 1) {
                c.setActive(true);
            }
            clients.add(c);
        } while (result.moveToNext());

        return clients;
    }


    public List<Date> getDate(){

        List<Date> dates = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT "+Constants.DATABASE.TABLE_CLIENT.COLUMN_DATE);
        sql.append("  FROM " + Constants.DATABASE.TABLE_CLIENT.TABLE_NAME);

        Cursor result = connection.rawQuery(sql.toString(), null);

        if (result.getCount() == 0) {
            result.close();
            return null;
        }
        result.moveToFirst();

        do{
            Date date = new Date(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_CLIENT.COLUMN_DATE)));
            dates.add(date);
        } while (result.moveToNext());

        return dates;
    }

    public void update(Client client){
        int active;
        if (client.isActive()) {
            active = 1;
        } else {
            active = 0;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.DATABASE.TABLE_CLIENT.COLUMN_ID, client.getClientId());
        contentValues.put(Constants.DATABASE.TABLE_CLIENT.COLUMN_NAME, client.getClientName());
        contentValues.put(Constants.DATABASE.TABLE_CLIENT.COLUMN_CODE, client.getClienteCode());
        contentValues.put(Constants.DATABASE.TABLE_CLIENT.COLUMN_ACTIVE, active);
        contentValues.put(Constants.DATABASE.TABLE_CLIENT.COLUMN_REVENDA_NAME, client.getRevendaName());
        contentValues.put(Constants.DATABASE.TABLE_CLIENT.COLUMN_DATE, client.getDate());

        String[] parameters = new String[1];
        parameters[0] = client.getClientId();

        connection.update(Constants.DATABASE.TABLE_CLIENT.TABLE_NAME, contentValues, Constants.DATABASE.TABLE_CLIENT.COLUMN_ID+" = ?",parameters);
    }

    public void delete(String id){
        String[] parameters = new String[1];
        parameters[0] = id;
        connection.delete(Constants.DATABASE.TABLE_CLIENT.TABLE_NAME,Constants.DATABASE.TABLE_CLIENT.COLUMN_ID+" = ?",parameters);
    }


}
