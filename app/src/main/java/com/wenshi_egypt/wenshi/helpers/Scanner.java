package com.wenshi_egypt.wenshi.helpers;

/**
 * Created by michaelanis on 3/13/18.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.wenshi_egypt.wenshi.PostImageActivity;

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
        int scaleFactor = Math.max(photoW / targetW, photoH / targetH);
        if(scaleFactor < 2)
            scaleFactor = 4;
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Log.i("IMAGE SIZE",""+photoH +""+ photoW +""+""+scaleFactor);


        return BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
    }
}