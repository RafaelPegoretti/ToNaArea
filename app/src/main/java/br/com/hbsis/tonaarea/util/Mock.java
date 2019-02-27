package br.com.hbsis.tonaarea.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.hbsis.tonaarea.entities.Audit;

public class Mock {

    public static String convertPhotoBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] photo = baos.toByteArray();
        return Base64.encodeToString(photo, Base64.NO_WRAP);
    }

    public static Bitmap converteBase64Photo(String s) {
        byte[] photo = Base64.decode(s, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(photo, 0, photo.length);
    }

    public static Integer getPositionList(List<Audit> list, Audit audit){
        int position = -1;
        for (int i = 0; i<list.size();i++) {
            if (list.get(i).equals(audit)){
                position = i;
                break;
            }
        }
        return position;
    }

    public static String parseDateToString(Date date) {
        SimpleDateFormat sd = new SimpleDateFormat();
        return sd.format(date);
    }

    public static Date parseStringToDate(String s){
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            return new Date(sd.parse(s).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
