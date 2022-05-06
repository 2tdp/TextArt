package com.datnt.textart.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.core.view.WindowCompat;

import com.datnt.textart.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Utils {

    public static void setStatusBarTransparent(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS & WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(activity.getWindow(), false);
            activity.getWindow().getInsetsController().hide(WindowInsets.Type.navigationBars());
        }
    }

    public static void setIntent(Activity activity, String nameActivity) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(activity.getPackageName(), nameActivity));
        activity.startActivity(intent, ActivityOptions.makeCustomAnimation(activity, R.anim.slide_in_right, R.anim.slide_out_left).toBundle());
    }

    public static void setAnimExit(Activity activity){
        activity.overridePendingTransition(R.anim.slide_in_left_small, R.anim.slide_out_right);
    }

    public static void showToast(Activity activity, String msg){
        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static SpannableString underLine(String strUnder) {
        SpannableString underLine = new SpannableString(strUnder);
        underLine.setSpan(new UnderlineSpan(), 0, underLine.length(), 0);

        return underLine;
    }

    private static String makeFilename(Context activity, String namePic) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return getStore(activity) + "/" + "remi-text-art-" + namePic + ".png";
        }
        String subdir;
        String externalRootDir = Environment.getExternalStorageDirectory().getPath();
        if (!externalRootDir.endsWith("/")) {
            externalRootDir += "/";
        }
        subdir = "media/image/";
        String parentdir = externalRootDir + subdir;

        // Create the parent directory
        File parentDirFile = new File(parentdir);
        parentDirFile.mkdirs();

        // If we can't write to that special path, try just writing directly to the sdcard
        if (!parentDirFile.isDirectory()) {
            parentdir = externalRootDir;
        }

        // Turn the title into a filename
        StringBuilder filename = new StringBuilder();
        for (int i = 0; i < "remi-text-art".length(); i++) {
            if (Character.isLetterOrDigit("remi-text-art".charAt(i))) {
                filename.append("remi-text-art".charAt(i));
            }
        }

        // Try to make the filename unique
        String path = null;
        for (int i = 0; i < 100; i++) {
            String testPath;
            if (i > 0)
                testPath = parentdir + filename + i + ".png";
            else
                testPath = parentdir + filename + ".png";

            try {
                RandomAccessFile f = new RandomAccessFile(new File(testPath), "r");
                f.close();
            } catch (Exception e) {
                // Good, the file didn't exist
                path = testPath;
                break;
            }
        }
        return path;
    }

    public static String getStore(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File f = c.getExternalFilesDir(null);
            if (f != null)
                return f.getAbsolutePath();
            else
                return "/storage/emulated/0/Android/data/" + c.getPackageName();
        } else {
            return Environment.getExternalStorageDirectory() + "/Android/data/" + c.getPackageName();
        }
    }

    public static String getMIMEType(String url) {
        String mType = null;
        String mExtension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (mExtension != null) {
            mType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mExtension);
        }
        return mType;
    }

    public static void rateApp(Context context){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
        context.startActivity(intent);
    }

    public static void shareApp(Context context) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Wallpaper Maker");
            String shareMessage = "Let me recommend you this application\nDownload now:\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + context.getPackageName();
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendFeedback(Context context) {
        Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
        selectorIntent.setData(Uri.parse("mailto:"));

        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"tatcachilathuthach92@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Remi TextArt feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "The email body...");
        emailIntent.setSelector(selectorIntent);
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void moreApps(Context context) {
        final String devName = "REMI Studio";
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + devName)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:" + devName)));
        }
    }

    public static void privacyApp(Context context) {
        final String linkPrivacy = "https://myweatherforecastandwidget.blogspot.com/";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkPrivacy));
        context.startActivity(browserIntent);
    }

    public static void shareFile(Context context, Bitmap bitmap, String application) {
        Uri uri = saveImageExternal(context, bitmap);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage(application);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setDataAndType(uri, "image/*");

        context.startActivity(Intent.createChooser(shareIntent, "Remi TextArt"));
    }

    public static Uri saveImageExternal(Context context, Bitmap image) {
        //TODO - Should be processed in another thread
        Uri uri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "remi-textArt.png");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            uri = FileProvider.getUriForFile(context, "com.remi.datnt.borderframe", file);
            ;
        } catch (IOException e) {
            Log.d("TAG", "IOException while trying to write file for sharing: " + e.getMessage());
        }
        return uri;
    }
}
