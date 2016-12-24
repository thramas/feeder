package utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v4.util.SimpleArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pukingminion.feeder.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

/**
 * Created by samarth on 12/23/16.
 */

public class AndroidUtilities {
    private static final String TAG = AndroidUtilities.class.getName();
    public static float density = 1;
    public static Point displaySize = new Point();
    public static boolean usingHardwareInput;
    public static DisplayMetrics displayMetrics = new DisplayMetrics();
    private static int width;
    private static int height;
    private static int adjustOwnerClassGuid = 0;


    public static final int MEDIA_TYPE_UNKNOWN = -1;
    public static final int MEDIA_DIR_IMAGE = 0;
    public static final int MEDIA_DIR_AUDIO = 1;
    public static final int MEDIA_DIR_VIDEO = 2;
    public static final int MEDIA_DIR_DATA = 3;
    public static final int MEDIA_DIR_CACHE = 4;

    public static final int MEDIA_TYPE_JPG = 1;
    public static final int MEDIA_TYPE_PNG = 2;
    public static final int MEDIA_TYPE_GIF = 3;
    public static final int MEDIA_TYPE_WEBP = 4;
    public static final int MEDIA_TYPE_TIFF = 5;

    public static final int MEDIA_TYPE_MP4 = 11;
    public static final int MEDIA_TYPE_MP3 = 12;


    private static SimpleArrayMap<Integer, File> mediaDirs = null;

    static {
////        density = MyApplication.applicationContext.getResources().getDisplayMetrics().density;
//        checkDisplaySize();
    }

    private AndroidUtilities() {
        throw new UnsupportedOperationException("Non-instantiable class");
    }


    public static void getMediaDuration(String videoPath) {

    }

    @NonNull
    public static GradientDrawable getGradientDrawable(int bgColor, int cornerRadius, int strokeWidth, int strokeColor) {
        GradientDrawable background = new GradientDrawable();
        background.setColor(bgColor);
        background.setCornerRadius(cornerRadius);
        if(strokeColor == 0 && strokeWidth == 0) return background;
        background.setStroke(strokeWidth, strokeColor);
        return background;
    }

    public static void getMediaSize(int resId) {

    }

    @IntDef({
            MEDIA_DIR_IMAGE,
            MEDIA_DIR_VIDEO,
            MEDIA_DIR_CACHE

    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaDirectoryTypes {
    }

    public static void requestAdjustResize(Activity activity, int classGuid) {
        if (activity == null) {
            return;
        }
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        adjustOwnerClassGuid = classGuid;
    }

    public static void removeAdjustResize(Activity activity, int classGuid) {
        if (activity == null) {
            return;
        }
        if (adjustOwnerClassGuid == classGuid) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
    }

//    public static void checkDisplaySize() {
//        try {
////            Configuration configuration = MyApplication.applicationContext.getResources().getConfiguration();
//            usingHardwareInput = configuration.keyboard != Configuration.KEYBOARD_NOKEYS && configuration.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO;
////            WindowManager manager = (WindowManager) MyApplication.applicationContext.getSystemService(Context.WINDOW_SERVICE);
//            if (manager != null) {
//                Display display = manager.getDefaultDisplay();
//                if (display != null) {
//                    display.getMetrics(displayMetrics);
//                    display.getSize(displaySize);
//                }
//            }
//        } catch (Exception e) {
////            if (Utilities.TOLOG) {
//////                MyLogger.e("roposo_msg", e.toString());
////            }
//        }
//
//    }

//    public static boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager
////                = (ConnectivityManager) MyApplication.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
//    }

    public static void showKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

//    public static void showLongToast(String msg) {
//        Toast.makeText(ActivitySwitchHelper.getContext(), msg, Toast.LENGTH_LONG).show();
//    }
//
//    public static void showShortToast(String msg) {
//        Toast.makeText(ActivitySwitchHelper.getContext(), msg, Toast.LENGTH_SHORT).show();
//    }

    public static boolean isKeyboardShowed(View view) {
        if (view == null) {
            return false;
        }
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputManager.isActive(view);
    }

    public static void hideKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive()) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void addMediaToGallery(String fromPath) {
        if (fromPath == null) {
            return;
        }
        File f = new File(fromPath);
        Uri contentUri = Uri.fromFile(f);
        addMediaToGallery(contentUri);
    }

    public static void addMediaToGallery(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
////        MyApplication.applicationContext.sendBroadcast(mediaScanIntent);
    }

    public static int pixelsToDp(int dp_value) {
        if (dp_value == 0 || density <= 0) {
            return dp_value;
        }

        return (int) (dp_value / density);
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }

    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            MyApplication.applicationHandler.post(runnable);
        } else {
            MyApplication.applicationHandler.postDelayed(runnable, delay);
        }
    }

    public static void cancelRunOnUIThread(Runnable runnable) {
//        MyApplication.applicationHandler.removeCallbacks(runnable);
    }

    public static void setProgressBarAnimationDuration(ProgressBar progressBar, int duration) {
        if (progressBar == null) {
            return;
        }
        try {
            Field mCursorDrawableRes = ProgressBar.class.getDeclaredField("mDuration");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.setInt(progressBar, duration);
        } catch (Exception e) {
////            if (Utilities.TOLOG) {
//////                MyLogger.e("roposo_msg", e.toString());
////            }
        }
    }

    @SuppressLint("NewApi")
    public static void clearDrawableAnimation(View view) {
        if (Build.VERSION.SDK_INT < 21 || view == null) {
            return;
        }
        Drawable drawable;
        if (view instanceof ListView) {
            drawable = ((ListView) view).getSelector();
            if (drawable != null) {
                drawable.setState(StateSet.NOTHING);
            }
        } else {
            drawable = view.getBackground();
            if (drawable != null) {
                drawable.setState(StateSet.NOTHING);
                drawable.jumpToCurrentState();
            }
        }
    }

//    @SuppressLint("NewApi")
//    public static String getPath(final Uri uri) {
//        try {
//            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
////            if (isKitKat && DocumentsContract.isDocumentUri(MyApplication.applicationContext, uri)) {
//                if (isExternalStorageDocument(uri)) {
//                    final String docId = DocumentsContract.getDocumentId(uri);
//                    final String[] split = docId.split(":");
//                    final String type = split[0];
//                    if ("primary".equalsIgnoreCase(type)) {
//                        return Environment.getExternalStorageDirectory() + "/" + split[1];
//                    }
//                } else if (isDownloadsDocument(uri)) {
//                    final String id = DocumentsContract.getDocumentId(uri);
//                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
////                    return getDataColumn(MyApplication.applicationContext, contentUri, null, null);
//                } else if (isMediaDocument(uri)) {
//                    final String docId = DocumentsContract.getDocumentId(uri);
//                    final String[] split = docId.split(":");
//                    final String type = split[0];
//
//                    Uri contentUri = null;
//                    switch (type) {
//                        case "image":
//                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                            break;
//                        case "video":
//                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                            break;
//                        case "audio":
//                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                            break;
//                    }
//
//                    final String selection = "_id=?";
//                    final String[] selectionArgs = new String[]{
//                            split[1]
//                    };
//
////                    return getDataColumn(MyApplication.applicationContext, contentUri, selection, selectionArgs);
//                }
//            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
////                return getDataColumn(MyApplication.applicationContext, uri, null, null);
//            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//                return uri.getPath();
//            }
//        } catch (Exception e) {
//////            if (Utilities.TOLOG) {
////////                MyLogger.e("roposo_msg", e.toString());
//////            }
//        }
//        return null;
//    }

    /**
     * @param bitmap
     * @param path
     * @param format  One of JPEG, PNG or WEBP.
     * @param quality 0 to 100 representing quality of the image saved.
     * @throws IOException
     */
    public static void writeImage(Bitmap bitmap, String path, Bitmap.CompressFormat format, int quality) throws IOException {
        final File thumb = new File(path);
        FileOutputStream fos = new FileOutputStream(thumb);
        bitmap.compress(format, quality, fos);
        fos.flush();
        fos.close();
    }


    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
////            if (Utilities.TOLOG) {
//                Log.e("roposo_msg", e.toString());
////            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /*
     * All videos / photos captured from the app are stored in this directory
     */
    private static File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Roposo");
            if (!storageDir.mkdirs()) {
                if (!storageDir.exists()) {
//                    if (Utilities.TOLOG) {
////                        MyLogger.d("getAlbum", "failed to create directory");
//                    }
                    return null;
                }
            }
        } else {
//            if (Utilities.TOLOG) {
////                MyLogger.d("getAlbum", "External storage is not mounted.");
//            }
        }

        return storageDir;
    }

    public static File generatePicturePath() {
        try {
            File storageDir = getAlbumDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            return new File(storageDir, "IMG_" + timeStamp + ".jpg");
        } catch (Exception e) {
////            Crashlytics.logException(e);
////            MyLogger.e("roposo_msg", e.toString());
        }
        return null;
    }

    public static File generateCachePicturePath() {
        try {
            if (null == mediaDirs) {
                mediaDirs = new SimpleArrayMap<>();
//                createMediaPaths(mediaDirs);
            }

            File mediaDir = mediaDirs.get(MEDIA_DIR_CACHE);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            return new File(mediaDir, "IMG_" + timeStamp + ".jpg");
        } catch (Exception e) {
////            MyLogger.e("roposo_msg", e.toString());
        }
        return null;
    }

    public static File generateCacheVideoPath() {
        try {
            if (null == mediaDirs) {
                mediaDirs = new SimpleArrayMap<>();
//                createMediaPaths(mediaDirs);
            }

            File mediaDir = mediaDirs.get(MEDIA_DIR_CACHE);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            File file = new File(mediaDir, "VID_" + timeStamp + ".mp4");
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (Exception e) {
////            MyLogger.e("roposo_msg", e.toString());
        }
        return null;
    }

    public static File generateVideoPath() {
        try {
            File storageDir = getAlbumDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            return new File(storageDir, "VID_" + timeStamp + ".mp4");
        } catch (Exception e) {
//            if (Utilities.TOLOG) {
////                MyLogger.e("roposo_msg", e.toString());
//            }
        }
        return null;
    }

    public static String formatFileSize(long size) {
        if (size < 1024) {
            return String.format("%d B", size);
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0f);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", size / 1024.0f / 1024.0f);
        } else {
            return String.format("%.1f GB", size / 1024.0f / 1024.0f / 1024.0f);
        }
    }

    public static boolean copyFile(InputStream sourceFile, File destFile) throws IOException {
        OutputStream out = new FileOutputStream(destFile);
        byte[] buf = new byte[4096];
        int len;
        while ((len = sourceFile.read(buf)) > 0) {
            Thread.yield();
            out.write(buf, 0, len);
        }
        out.close();
        return true;
    }

    public static boolean copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileInputStream source = null;
        FileOutputStream destination = null;
        try {
            source = new FileInputStream(sourceFile);
            destination = new FileOutputStream(destFile);
            destination.getChannel().transferFrom(source.getChannel(), 0, source.getChannel().size());
        } catch (Exception e) {
////            MyLogger.e("roposo_msg", e.toString());
            return false;
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
        return true;
    }
//
//    public static boolean isOnLowMemory() {
////        ActivityManager activityManager = (ActivityManager) MyApplication.applicationContext.getSystemService(Context.ACTIVITY_SERVICE);
//        if (null != activityManager) {
//            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
//            activityManager.getMemoryInfo(memoryInfo);
//            return memoryInfo.lowMemory;
//        }
//
//        return false;
//    }

    // Can be used to filter out actual screenshot taken on the device
//    public static Point getRealScreenSize() {
//        Point size = new Point();
//        try {
////            WindowManager windowManager = (WindowManager) MyApplication.applicationContext.getSystemService(Context.WINDOW_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                windowManager.getDefaultDisplay().getRealSize(size);
//            } else {
//                try {
//                    Method mGetRawW = Display.class.getMethod("getRawWidth");
//                    Method mGetRawH = Display.class.getMethod("getRawHeight");
//                    size.set((Integer) mGetRawW.invoke(windowManager.getDefaultDisplay()), (Integer) mGetRawH.invoke(windowManager.getDefaultDisplay()));
//                } catch (Exception e) {
//                    size.set(windowManager.getDefaultDisplay().getWidth(), windowManager.getDefaultDisplay().getHeight());
//////                    if (Utilities.TOLOG) MyLogger.e("roposo_msg", e.toString());
////                }
////            }
//        } catch (Exception e) {
//////            if (Utilities.TOLOG) MyLogger.e("roposo_msg", e.toString());
////        }
////        return size;
//    }


//    @Nullable
//    public static String getDirectoryPath(int mediaType) {
//        if (null == mediaDirs) {
//            mediaDirs = new SimpleArrayMap<>();
//            createMediaPaths(mediaDirs);
//        }
//
//        File mediaDir = mediaDirs.get(mediaType);
//        if (null != mediaDir) {
//            return mediaDir.getAbsolutePath();
//        }
//        return null;
//    }


//    private static void createMediaPaths(@NonNull SimpleArrayMap<Integer, File> mediaDirsOut) {
//
//        // 1. Add External cache directory
//        File cachePath = AndroidUtilities.getCacheDir();
//        if (!cachePath.isDirectory()) {
//            try {
//                cachePath.mkdirs();
//            } catch (Exception e) {
//////                MyLogger.e("roposo_msg", e.getMessage());
//            }
//        }
//
//        // Disable media scanning for cache directory
//        try {
//            new File(cachePath, ".nomedia").createNewFile();
//        } catch (Exception e) {
//////            MyLogger.e("roposo_msg", e.getMessage());
//        }
//
//        mediaDirsOut.put(AndroidUtilities.MEDIA_DIR_CACHE, cachePath);
//
//////        MyLogger.d("roposo_msg", "cache path = " + cachePath);
//
//        // 2. Add Roposo directory
//
//        try {
//            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//                File roposoPath = new File(Environment.getExternalStorageDirectory(), "Roposo");
//                roposoPath.mkdirs();
//
//                if (roposoPath.isDirectory()) {
//
//                    try {
//                        File imagePath = new File(roposoPath, "Roposo Photos");
//                        imagePath.mkdir();
//                        if (imagePath.isDirectory()) {
//                            // TODO anilshar :: ideally we should check if a file can be saved into created directory
//                            mediaDirs.put(AndroidUtilities.MEDIA_DIR_IMAGE, imagePath);
//////                            MyLogger.e("roposo_msg", "image path = " + imagePath);
//                        }
//                    } catch (Exception e) {
//////                        MyLogger.e("roposo_msg", e.getMessage());
//                    }
//
//                    try {
//                        File videoPath = new File(roposoPath, "Roposo Video");
//                        videoPath.mkdir();
//                        if (videoPath.isDirectory()) {
//                            mediaDirsOut.put(AndroidUtilities.MEDIA_DIR_VIDEO, videoPath);
//////                            MyLogger.e("roposo_msg", "video path = " + videoPath);
//                        }
//                    } catch (Exception e) {
//////                        MyLogger.e("roposo_msg", e.getMessage());
//                    }
//
//                    // TODO anilshar :: Uncomment below code when separate folder for audios are used
////                    try {
////                        File audioPath = new File(roposoPath, "Roposo Audio");
////                        audioPath.mkdir();
////                        if (audioPath.isDirectory() ) {
////                            new File(audioPath, ".nomedia").createNewFile();
////                            mediaDirsOut.put(AndroidUtilities.MEDIA_DIR_AUDIO, audioPath);
//////                            MyLogger.e("roposo_msg", "audio path = " + audioPath);
////                        }
////                    } catch (Exception e) {
//////                        MyLogger.e("roposo_msg", e.getMessage());
////                    }
//                }
//            } else {
//                // register a broadcast receiver to find when media gets mounted/unmounted etc.
//                BroadcastReceiver receiver = new BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context arg0, Intent intent) {
////                        MyApplication.applicationContext.unregisterReceiver(this);
//////                        MyLogger.e("roposo_msg", "file system changed");
//                        Runnable r = new Runnable() {
//                            public void run() {
//                                createMediaPaths(mediaDirs);
//                            }
//                        };
//                        if (Intent.ACTION_MEDIA_UNMOUNTED.equals(intent.getAction())) {
//                            AndroidUtilities.runOnUIThread(r, 1000);
//                        } else {
//                            r.run();
//                        }
//                    }
//                };
//
//                IntentFilter filter = new IntentFilter();
//                filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
//                filter.addAction(Intent.ACTION_MEDIA_CHECKING);
//                filter.addAction(Intent.ACTION_MEDIA_EJECT);
//                filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
//                filter.addAction(Intent.ACTION_MEDIA_NOFS);
//                filter.addAction(Intent.ACTION_MEDIA_REMOVED);
//                filter.addAction(Intent.ACTION_MEDIA_SHARED);
//                filter.addAction(Intent.ACTION_MEDIA_UNMOUNTABLE);
//                filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
//                filter.addDataScheme("file");
////                MyApplication.applicationContext.registerReceiver(receiver, filter);
//            }
//        } catch (Exception e) {
//////            MyLogger.e("roposo_msg", e.getMessage());
//        }
//
//    }

    /**
     * One folder for all file types.
     *
     * @return File Path
     */
    public static String getRoposoDirectoryPath() {
        String path = null;
        try {
            File storage = Environment.getExternalStorageDirectory();
            // to check if external is mounted or not -- TO BE SEEN
            File roposoDirectory = new File(storage.getAbsolutePath() + "/Roposo");
            if (!roposoDirectory.exists()) {
                if (!roposoDirectory.mkdirs()) {
                    // directory still not created. Returning null
                    return null;
                }
            }
            path = roposoDirectory.getAbsoluteFile().getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

//    public static File getCacheDir() {
//        String state = null;
//        try {
//            state = Environment.getExternalStorageState();
//        } catch (Exception e) {
//////            MyLogger.e("roposo_msg", e.getMessage());
//        }
//        if (state == null || state.startsWith(Environment.MEDIA_MOUNTED)) {
//            try {
////                File file = MyApplication.applicationContext.getExternalCacheDir();
//                if (file != null) {
//                    return file;
//                }
//            } catch (Exception e) {
//////                MyLogger.e("roposo_msg", e.getMessage());
//            }
//        }
//        try {
////            File file = MyApplication.applicationContext.getCacheDir();
//            if (file != null) {
//                return file;
//            }
//        } catch (Exception e) {
//////            MyLogger.e("roposo_msg", e.getMessage());
//        }
//        return new File("");
//    }

    /*
     * @param: videoPath of local video in disk
     * returns : the file path of the generated video thumbnail
     */
//    public static String generateVideoThumbnail(String videoPath) {
//        String thumbFilePath = null;
//        String thumbPrefix = AndroidUtilities.getDirectoryPath(AndroidUtilities.MEDIA_DIR_CACHE);
//        if (null != thumbPrefix) {
//            thumbFilePath = new File(thumbPrefix, "THUMB_" + System.currentTimeMillis() + ".jpeg").getAbsolutePath();
//            try {
//                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath,
//                        MediaStore.Images.Thumbnails.MINI_KIND);
//                final File thumb = new File(thumbFilePath);
//                if (!thumb.exists()) {
//                    thumb.createNewFile();
//                }
//
//                FileOutputStream fos = new FileOutputStream(thumb);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
//            } catch (Exception e) {
////                Crashlytics.logException(e);
//            }
//        }
//
//        return thumbFilePath;
//    }


    public static String getScreenShotPath() {
        String PATH = null;
        File screenShotDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/Screenshots/");
        if (screenShotDirectory.isDirectory()) {
            PATH = screenShotDirectory.getPath() + "/";
        } else {
            screenShotDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/Screenshots/");
            if (screenShotDirectory.isDirectory()) {
                PATH = screenShotDirectory.getPath() + "/";
            }
        }
        return PATH;
    }

    public static boolean isPackageExisted(Context context, String targetPackage) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

//    public static void showToast(String message) {
//        if (ActivitySwitchHelper.getContext() != null) {
//            Toast.makeText(ActivitySwitchHelper.getContext(), message, Toast.LENGTH_SHORT).show();
//        }
//    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        //http://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Determines the Media type of the file
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static int getFileMediaType(URL url) throws IOException {
        HttpURLConnection connection = ((HttpURLConnection) url.openConnection());
        connection.setRequestProperty("Range", "bytes=" + 0 + "-" + 0);
        connection.connect();
        InputStream uInputStream = connection.getInputStream();
        return getFileMediaType(uInputStream);
    }

    /**
     * Determines the Media type of the file
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static int getFileMediaType(String path) {
        FileInputStream fInStream = null;
        try {
            fInStream = new FileInputStream(path);
            return getFileMediaType(fInStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return MEDIA_TYPE_UNKNOWN;
    }

    /**
     * Reads the first byte of the file and determines the Media type
     * TODO Handle media/file types identifiable by reading a few more bytes.
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static int getFileMediaType(InputStream is) throws IOException {
        int fileType;

        byte[] buf = new byte[1];
        is.read(buf);

        String TAG = "roposo_msg";
        switch (bytesToHex(buf)) {
            //http://www.garykessler.net/library/file_sigs.html
            case "00":
////                MyLogger.d(TAG, "file  is of type: mp4");
                fileType = MEDIA_TYPE_MP4;
                break;
            case "FF":
//                MyLogger.d(TAG, "file  is of type: image/jpeg");
                fileType = MEDIA_TYPE_JPG;
                break;
            case "89":
//                MyLogger.d(TAG, "file  is of type: image/png");
                fileType = MEDIA_TYPE_PNG;
                break;
            case "47":
//                MyLogger.d(TAG, "file  is of type: image/gif");
                fileType = MEDIA_TYPE_GIF;
                break;
            case "49":
            case "4D":
//                MyLogger.d(TAG, "file  is of type: image/tiff");
                fileType = MEDIA_TYPE_TIFF;
                break;
            default:
//                MyLogger.w(TAG, "Unknown file type");
                fileType = MEDIA_TYPE_UNKNOWN;
        }
        return fileType;
    }


    public static boolean isLocalFile(String file_path) {
        if (file_path == null)
            return false;
        File file = new File(file_path);
        if (file.exists())
            return true;
        else
            return false;
    }

    public static String appendUri(String url, String appendQuery) {
        try {
            Uri uri = Uri.parse(url);
            final Set queryParams = uri.getQueryParameterNames();
            if (queryParams.isEmpty()) {
                url += "?" + appendQuery;
            } else {
                if (!url.contains(appendQuery)) {
                    url += "&" + appendQuery;
                }
            }
        } catch (Exception e) {
//            Crashlytics.logException(e);
        }
        return url;
    }

    public static String getFileExt(String path) {
        int indexOfDot = path.lastIndexOf('.');
        return path.substring(indexOfDot + 1);
    }

//    public static int[] queryImageSize(int resId) {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(ActivitySwitchHelper.getContext().getResources(), resId, options);
//        int[] size = new int[2];
//        size[0] = options.outWidth;
//        size[1] = options.outHeight;
//        return size;
//    }

    public static int[] queryImageSize(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int[] size = new int[2];
        size[0] = options.outWidth;
        size[1] = options.outHeight;
        return size;
    }

    // This snippet hides the system bars.
    public static void hideSystemUI(Activity activity) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
/*        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
*//*                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION*//*
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
*//*                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar*//*
                        | View.SYSTEM_UI_FLAG_FULLSCREEN ); // hide status bar*/
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    public static void showSystemUI(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
/*        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
*//*                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION*//*
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);*/
    }

    public static Rect getActivityUsableArea(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        Rect outRect = new Rect();
        decorView.getWindowVisibleDisplayFrame(outRect);
        return outRect;
    }

    public static Matrix transformMatrixScaletoCoordinateSystem(Matrix src, Rect srcCoordinateSystem, Rect targetCoordinateSystem) {
        float[] v = new float[9];
        src.getValues(v);

        Matrix target = new Matrix(src);
        float posX = v[Matrix.MTRANS_X];
        float posY = v[Matrix.MTRANS_Y];

        int srcWidth = srcCoordinateSystem.width();
        int srcHeight = srcCoordinateSystem.height();

        int targetWidth = targetCoordinateSystem.width();
        int targetHeight = targetCoordinateSystem.height();

        posX -= srcWidth / 2;
        posY -= srcHeight / 2;

        float xScale = (float) targetWidth / srcWidth;
        float yScale = (float) targetHeight / srcHeight;

        posX *= xScale;
        posY *= yScale;

        posX += targetWidth / 2;
        posY += targetHeight / 2;

        float[] w = new float[9];
        target.getValues(w);

        w[Matrix.MTRANS_X] = posX;
        w[Matrix.MTRANS_Y] = posY;

        target.setValues(w);

        return target;
    }

    public static Matrix transformMatrixTranslationtoCoordinateSystem(Matrix src, Rect srcCoordinateSystem, Rect targetCoordinateSystem) {
        float[] v = new float[9];
        src.getValues(v);

        Matrix target = new Matrix(src);
        float posX = v[Matrix.MTRANS_X];
        float posY = v[Matrix.MTRANS_Y];

        int originOffsetX = -(targetCoordinateSystem.left - srcCoordinateSystem.left);
        int originOffsetY = -(targetCoordinateSystem.top - srcCoordinateSystem.top);

        posX += originOffsetX;
        posY += originOffsetY;

        float[] w = new float[9];
        target.getValues(w);

        w[Matrix.MTRANS_X] = posX;
        w[Matrix.MTRANS_Y] = posY;

        target.setValues(w);

        return target;
    }

    /**
     * @param srcRect  original Rect to extract original width and height values
     * @param destRect target Rect (the location specifier).
     * @param crop     true for centerCrop and false for centerInside
     * @return
     */
    public static void scaleCenter(Rect srcRect, Rect destRect, Rect computedRect, boolean crop) {
        if (srcRect == null || destRect == null) return;

        int destWidth = destRect.width();
        int destHeight = destRect.height();

        int sourceWidth = srcRect.width();
        int sourceHeight = srcRect.height();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) destWidth / sourceWidth;
        float yScale = (float) destHeight / sourceHeight;

        float scale;
        if (crop) {
            scale = Math.max(xScale, yScale);
        } else {
            scale = Math.min(xScale, yScale);
        }

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (destWidth - scaledWidth) / 2;
        float top = (destHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRectF = new RectF(left, top, left + scaledWidth, top + scaledHeight);
        targetRectF.offset(destRect.left, destRect.top);

        Rect targetRect = new Rect();
        targetRectF.roundOut(targetRect);

        computedRect.set(targetRect);
    }

    public static boolean isPointInView(Float x, Float y, View view) {

        RectF rect = new RectF(view.getX(), view.getY(), view.getX() + view.getWidth(), view.getY() + view.getHeight());
        if (rect.isEmpty()) return false;
        return rect.contains(x, y);

    }

    public static int getMediaTranspose(String path) {
        int transpose;
        if (path.endsWith(".mp4") || path.endsWith(".avi") || path.endsWith(".mov")) {
            transpose = getVideoTranspose(path);
        } else if (path.endsWith("png") || path.endsWith("jpg") || path.endsWith("jpeg")) {
            transpose = getImageTranspose(path);
        } else { //Give video the benefit of doubt for now
            transpose = getVideoTranspose(path);
        }
        return transpose;
    }

    public static int[] getMediaSize(String path) {
        int[] size;
        if (path.endsWith(".mp4") || path.endsWith(".avi") || path.endsWith(".mov")) {
            size = getVideoSize(path);
        } else if (path.endsWith("png") || path.endsWith("jpg") || path.endsWith("jpeg")) {
            size = getImageSize(path);
        } else { //Give video the benefit of doubt for now
            size = getVideoSize(path);
        }
        return size;
    }

    private static int[] getVideoSize(String path) {
        int[] size = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(path);
            try {
                size = new int[2];
                size[0] = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                size[1] = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

                int transpose = getVideoTranspose(path);
                if (transpose == TRANSPOSE_CCLOCK || transpose == TRANSPOSE_CLOCK || transpose == TRANSPOSE_CCLOCK_FLIP || transpose == TRANSPOSE_CLOCK_FLIP) {
                    int temp = size[0];
                    size[0] = size[1];
                    size[1] = temp;
                }
            } catch (Exception e) {
                e.printStackTrace();
                size = null;
            }
        } catch (Exception e) {
            retriever.release();
        }
        return size;
    }

    public static int getFileNameTypeImage(String path) {
        int fileTypeImage = MEDIA_TYPE_UNKNOWN;
        String fileExt = AndroidUtilities.getFileExt(path);
        switch (fileExt) {
            case "mp4":
            case "avi":
            case "mov":
                fileTypeImage = MEDIA_TYPE_MP4;
                break;
            case "png":
                fileTypeImage = MEDIA_TYPE_PNG;
                break;
            case "jpg":
            case "jpeg":
                fileTypeImage = MEDIA_TYPE_JPG;
                break;
        }

        return fileTypeImage;
    }

    public static int[] getImageSize(String imagePath) {
        int[] size = new int[2];
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ei == null) {
            Log.w(TAG, "Invalid image path or inaccessible");
            return null;
        }
        size = AndroidUtilities.queryImageSize(imagePath);
/*        size[0] = ei.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 480);
        size[1] = ei.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 640);*/
        int orientation = getImageOrientation(imagePath);
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            int temp = size[0];
            size[0] = size[1];
            size[1] = temp;
        }
        return size;
    }

    public static int SCALE_CENTER_INSIDE = 0;
    public static int SCALE_CENTER_CROP = 1;
    public static int SCALE_FIT = 2;
    public static int SCALE_FIT_WIDTH = 3;
    public static int SCALE_FIT_HEIGHT = 4;

    public static int TRANSPOSE_NONE = -1;
    public static int TRANSPOSE_CCLOCK_FLIP = 0;
    public static int TRANSPOSE_CLOCK = 1;
    public static int TRANSPOSE_CCLOCK = 2;
    public static int TRANSPOSE_CLOCK_FLIP = 3;
    public static int TRANSPOSE_180 = 4;

    private static int getVideoTranspose(String videoPath) {
        int transpose = TRANSPOSE_NONE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            ;
            try {
                retriever.setDataSource(videoPath);
                String rotationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
                if (rotationStr != null) {
                    int rotation = 0;
                    try {
                        rotation = Integer.valueOf(rotationStr);
                        if (rotation == 90) {
                            transpose = TRANSPOSE_CLOCK;
                        } else if (rotation == -90) {
                            transpose = TRANSPOSE_CCLOCK;
                        }
                    } catch (NumberFormatException e) {
                        transpose = TRANSPOSE_NONE;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                retriever.release();
                transpose = TRANSPOSE_NONE;
            }
        }
        return transpose;
    }

    public static int getImageTranspose(String imagePath) {
        int orientation = getImageOrientation(imagePath);
        int transpose = 0;
        // Rotation from Exif Tag (Embedded inside the image)
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                transpose = TRANSPOSE_CCLOCK;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                transpose = TRANSPOSE_180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                transpose = TRANSPOSE_CLOCK;
                break;
            default:
                transpose = 0;
        }
        return transpose;
    }

    private static int getImageOrientation(String imagePath) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ei == null) {
            Log.d(TAG, "Invalid image path or inaccessible");
            return 0;
        }
        return ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
    }

    public static int sp(int value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, displayMetrics));
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

//    public static void initializeScreenDimensions() {
////        WindowManager manager = (WindowManager) MyApplication.applicationContext.getSystemService(Context.WINDOW_SERVICE);
//        if (manager != null) {
//            Display display = manager.getDefaultDisplay();
//            if (display != null) {
//                display.getMetrics(displayMetrics);
//                width = displayMetrics.widthPixels;
//                height = displayMetrics.heightPixels;
//            }
//        }
//    }

    /**
     * Calculates the tile length of a grid element considering square grid element type.
     *
     * @param numberOfColumns    Number of columns in the grid containing the tiles
     * @param sideMarginInPixels The margin of the grid from sides(left,right) in pixels
     * @param gridMarginInPixels The margin in between the grid in pixels
     * @return The tile length of the square in pixels
     */

    public static int getSquareTileLength(int numberOfColumns, int sideMarginInPixels, int gridMarginInPixels) {
        int dm = AndroidUtilities.displayMetrics.widthPixels;
        int size = 0;
        if (numberOfColumns > 0) {
            size = (int) ((double) (dm - 2 * sideMarginInPixels - (numberOfColumns + 1) * gridMarginInPixels) / (double) numberOfColumns);
        }
        return size;
    }

    /**
     * Calculates the tile dimensions of a grid element.
     * <p>
     * Note: For calculation of defaultSquareTileLength and totalFixedCardHeight a default values of columns,rows
     * are taken so that all the cards get drawn from the perspective of the base card.
     *
     * @param baseRows    Number of rows for baseCase. 0 for no base case.
     * @param baseColumns Number of columns for baseCase. 0 for no base case.
     * @param rows        Number of rows in the grid containing the tiles
     * @param columns     Number of columns in the grid containing the tiles
     * @param sideMargin  The margin of the grid from sides(left,right) in pixels
     * @param gridMargin  The margin in between the grid in pixels
     * @return The tile dimensions of the rectangular grid element in pixels
     */
    public static Pair<Integer, Integer> getGridElementDimensions(int rows, int columns, int sideMargin, int gridMargin, int baseRows, int baseColumns) {
        int defaultSqaureTileLength;
        int totalFixedCardHeight;

        if (baseColumns > 0 && baseRows > 0) {               //Consider Base Case
            defaultSqaureTileLength = getSquareTileLength(baseColumns, sideMargin, gridMargin);
            totalFixedCardHeight = baseRows * defaultSqaureTileLength + (baseRows + 1) * gridMargin;
        } else {
            defaultSqaureTileLength = getSquareTileLength(columns, sideMargin, gridMargin);
            totalFixedCardHeight = rows * defaultSqaureTileLength + (rows + 1) * gridMargin;
        }
        int tileLength = (totalFixedCardHeight - (rows + 1) * gridMargin) / rows;
        int tileWidth = (AndroidUtilities.displayMetrics.widthPixels - (columns + 1) * gridMargin - 2 * sideMargin) / columns;
        if (tileLength > 0 && tileWidth > 0) {
            return new Pair<>(tileLength, tileWidth);
        }
        return null;
    }
}
