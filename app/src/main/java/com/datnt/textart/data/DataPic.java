package com.datnt.textart.data;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.datnt.textart.R;
import com.datnt.textart.model.BucketPicModel;
import com.datnt.textart.model.PicModel;

import java.util.ArrayList;

public class DataPic {

    private static final String[] EXTERNAL_COLUMNS_PIC = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATA,
            "\"" + MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "\""
    };

    private static final String[] EXTERNAL_COLUMNS_PIC_API_Q = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATA
    };

    public static ArrayList<BucketPicModel> getBucketPictureList(Context context) {

        ArrayList<BucketPicModel> lstBucket = new ArrayList<>();
        ArrayList<PicModel> lstPic, lstAll = new ArrayList<>();
        Uri CONTENT_URI;
        String[] COLUMNS;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            CONTENT_URI = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
            COLUMNS = EXTERNAL_COLUMNS_PIC_API_Q;
        } else {
            CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            COLUMNS = EXTERNAL_COLUMNS_PIC;
        }

        try (Cursor cursor = context.getContentResolver().query(
                CONTENT_URI,
                COLUMNS,
                null,
                null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER)) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            int dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
            int bucketColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT);
            int widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            while (cursor.moveToNext()) {
                String id = cursor.getString(idColumn);
                String title = cursor.getString(titleColumn);
                String date = cursor.getString(dateColumn);
                String bucket = cursor.getString(bucketColumn);
                String height = cursor.getString(heightColumn);
                String width = cursor.getString(widthColumn);
                String size = cursor.getString(sizeColumn);
                String data = cursor.getString(dataColumn);

                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.parseLong(id));

                lstAll.add(new PicModel(id, title, date, bucket, height, width, size, data, contentUri.toString(), false));
                boolean check = false;
                if (lstBucket.isEmpty()) {
                    lstPic = new ArrayList<>();
                    if (bucket != null) {
                        lstPic.add(new PicModel(id, title, date, bucket, height, width, size, data, contentUri.toString(), false));
                        lstBucket.add(new BucketPicModel(lstPic, bucket));
                    } else {
                        lstPic.add(new PicModel(id, title, date, "", height, width, size, data, contentUri.toString(), false));
                        lstBucket.add(new BucketPicModel(lstPic, ""));
                    }
                } else {
                    for (int i = 0; i < lstBucket.size(); i++) {
                        if (bucket == null) {
                            lstBucket.get(i).getLstPic().add(new PicModel(id, title, date, "", height, width, size, data, contentUri.toString(), false));
                            check = true;
                            break;
                        }
                        if (bucket.equals(lstBucket.get(i).getBucket())) {
                            lstBucket.get(i).getLstPic().add(new PicModel(id, title, date, bucket, height, width, size, data, contentUri.toString(), false));
                            check = true;
                            break;
                        }
                    }
                    if (!check) {
                        lstPic = new ArrayList<>();
                        lstPic.add(new PicModel(id, title, date, bucket, height, width, size, data, contentUri.toString(), false));
                        lstBucket.add(new BucketPicModel(lstPic, bucket));
                    }
                }
            }

            lstBucket.add(0, new BucketPicModel(lstAll, context.getString(R.string.all)));
        }
        return lstBucket;
    }

    public static ArrayList<PicModel> getAllPictureList(Context context) {

        ArrayList<PicModel> lstPic = new ArrayList<>();
        Uri CONTENT_URI;
        String[] COLUMNS;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            CONTENT_URI = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
            COLUMNS = EXTERNAL_COLUMNS_PIC_API_Q;
        } else {
            CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            COLUMNS = EXTERNAL_COLUMNS_PIC;
        }

        try (Cursor cursor = context.getContentResolver().query(
                CONTENT_URI,
                COLUMNS,
                null,
                null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER)) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            int dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
            int bucketColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT);
            int widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            while (cursor.moveToNext()) {
                String id = cursor.getString(idColumn);
                String title = cursor.getString(titleColumn);
                String date = cursor.getString(dateColumn);
                String bucket = cursor.getString(bucketColumn);
                String height = cursor.getString(heightColumn);
                String width = cursor.getString(widthColumn);
                String size = cursor.getString(sizeColumn);
                String data = cursor.getString(dataColumn);

                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.parseLong(id));

                lstPic.add(new PicModel(id, title, date, bucket, height, width, size, data, contentUri.toString(), false));
            }
        }
        return lstPic;
    }
}
