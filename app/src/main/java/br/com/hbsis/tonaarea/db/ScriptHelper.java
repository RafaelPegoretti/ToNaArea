package br.com.hbsis.tonaarea.db;

import br.com.hbsis.tonaarea.util.Constants;

public class ScriptHelper {

    public static String createTableClient(){
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS "+Constants.DATABASE.TABLE_CLIENT.TABLE_NAME+" ( ");
        sql.append(Constants.DATABASE.TABLE_CLIENT.COLUMN_ID+" id VARCHAR(100) PRIMARY KEY NOT NULL,");
        sql.append(Constants.DATABASE.TABLE_CLIENT.COLUMN_NAME+" VARCHAR (250),");
        sql.append(Constants.DATABASE.TABLE_CLIENT.COLUMN_CODE+" VARCHAR(100),");
        sql.append(Constants.DATABASE.TABLE_CLIENT.COLUMN_ACTIVE+" INTEGER,");
        sql.append(Constants.DATABASE.TABLE_CLIENT.COLUMN_REVENDA_NAME+" VARCHAR(100),  ");
        sql.append(Constants.DATABASE.TABLE_CLIENT.COLUMN_DATE+" VARCHAR(100))  ");
        return sql.toString();
    }

    public static String createTableProduct(){
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS "+Constants.DATABASE.TABLE_PRODUCT.TABLE_NAME+" ( ");
        sql.append(Constants.DATABASE.TABLE_PRODUCT.COLUMN_ID+" VARCHAR(100) PRIMARY KEY NOT NULL,");
        sql.append(Constants.DATABASE.TABLE_PRODUCT.COLUMN_NAME+" VARCHAR (250),");
        sql.append(Constants.DATABASE.TABLE_PRODUCT.COLUMN_DATE+" VARCHAR (250))");
        return sql.toString();
    }


}
