package com.wenshi_egypt.wenshi.helpers;

/**
 * Created by michaelanis on 3/13/18.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.wenshi_egypt.wenshi.PostImageActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class Scanner {

    public Bitmap decodeBitmapUri(PostImageActivity ctx, Uri uri) throws FileNotFoundException {
        int targetW = 512;
        int targetH = 512;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = true;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bmp = BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);

        return bmp;

    }


}