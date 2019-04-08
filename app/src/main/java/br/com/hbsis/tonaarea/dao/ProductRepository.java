package br.com.hbsis.tonaarea.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.hbsis.tonaarea.entities.Client;
import br.com.hbsis.tonaarea.entities.Product;
import br.com.hbsis.tonaarea.util.Constants;
import br.com.hbsis.tonaarea.util.Util;

public class ProductRepository {

    private SQLiteDatabase connection;

    public ProductRepository(SQLiteDatabase connection) {
        this.connection = connection;
    }

    public void insert(Product product) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.DATABASE.TABLE_PRODUCT.COLUMN_ID, product.getProductId());
        contentValues.put(Constants.DATABASE.TABLE_PRODUCT.COLUMN_NAME, product.getProductName());
        contentValues.put(Constants.DATABASE.TABLE_PRODUCT.COLUMN_DATE, product.getDate());

        connection.insertOrThrow(Constants.DATABASE.TABLE_PRODUCT.TABLE_NAME, null, contentValues);
    }

    public List<Product> getAll() {

        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT *");
        sql.append("  FROM " + Constants.DATABASE.TABLE_PRODUCT.TABLE_NAME);

        Cursor result = connection.rawQuery(sql.toString(), null);

        if (result.getCount() == 0) {
            result.close();
            return products;
        }
        result.moveToFirst();

        do {
            Product p = new Product();
            p.setProductId(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_PRODUCT.COLUMN_ID)));
            p.setProductName(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_PRODUCT.COLUMN_NAME)));
            p.setDate(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_PRODUCT.COLUMN_DATE)));
            products.add(p);
        } while (result.moveToNext());

        return products;
    }

    public List<Product> getNew(String startDate, String endDate){
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT *");
        sql.append("  FROM " + Constants.DATABASE.TABLE_PRODUCT.TABLE_NAME);

        Cursor result = connection.rawQuery(sql.toString(), null);

        if (result.getCount() == 0) {
            result.close();
            return products;
        }
        result.moveToFirst();

        do {
            Product p = new Product();
            p.setProductId(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_PRODUCT.COLUMN_ID)));
            p.setProductName(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_PRODUCT.COLUMN_NAME)));
            p.setDate(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_PRODUCT.COLUMN_DATE)));
            products.add(p);
        } while (result.moveToNext());

        return products;
    }

    public List<Date> getDate() {

        List<Date> dates = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT " + Constants.DATABASE.TABLE_PRODUCT.COLUMN_DATE);
        sql.append("  FROM " + Constants.DATABASE.TABLE_PRODUCT.TABLE_NAME);

        Cursor result = connection.rawQuery(sql.toString(), null);

        if (result.getCount() == 0) {
            result.close();
            return null;
        }
        result.moveToFirst();

        do {
            Date date = new Date(result.getString(result.getColumnIndexOrThrow(Constants.DATABASE.TABLE_PRODUCT.COLUMN_DATE)));
            dates.add(date);
        } while (result.moveToNext());

        return dates;
    }


    public void update(Product product) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.DATABASE.TABLE_PRODUCT.COLUMN_ID, product.getProductId());
        contentValues.put(Constants.DATABASE.TABLE_PRODUCT.COLUMN_NAME, product.getProductName());
        //contentValues.put(Constants.DATABASE.TABLE_PRODUCT.COLUMN_DATE, Mock.parseDateToString(product.getDate()));

        String[] parameters = new String[1];
        parameters[0] = product.getProductId();

        connection.update(Constants.DATABASE.TABLE_PRODUCT.TABLE_NAME, contentValues,Constants.DATABASE.TABLE_PRODUCT.COLUMN_ID+ " = ?",parameters);
    }

    public void delete(String id){
        String[] parameters = new String[1];
        parameters[0] = id;
        connection.delete(Constants.DATABASE.TABLE_PRODUCT.TABLE_NAME,Constants.DATABASE.TABLE_PRODUCT.COLUMN_ID+" = ?",parameters);
    }


}

