package com.lovelilu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.widget.Toast;


import com.lovelilu.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PictureUtil {


    /**
     * 把bitmap转换成String
     *
     * @param filePath
     * @return
     */
    public static byte[] bitmapToString(String filePath) {

        Bitmap bm = getSmallBitmap(filePath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] b = baos.toByteArray();

        return b;

    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 根据路径删除图片
     *
     * @param path
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 添加到图库
     */
    public static void galleryAddPic(Context context, String pathAndName) {
        /*
        Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(path);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		context.sendBroadcast(mediaScanIntent);
		*/
        //4.4系统以后上述方法失效，遍历图库的方法改为：
        MediaScannerConnection.scanFile(context, new String[]{pathAndName}, null, null);
    }

    /**
     * 获取保存图片的目录
     *
     * @return
     */
    public static File getAlbumDir() {
        File dir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                getAlbumName());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取保存 隐患检查的图片文件夹名称
     *
     * @return
     */
    public static String getAlbumName() {
        return "sheguantong";
    }

    static boolean isSave = false;


    /**
     * 保存图片到本地
     *
     * @param ctx
     * @param mbBitmap
     */
    public static String savePicture(Context ctx, Bitmap mbBitmap) {

        String path = Environment.getExternalStorageDirectory() + "/ILoveLilu";
        String fileName = getDateFileName() + ".png";
        File bitmapFile = new File(path, fileName);
        FileOutputStream fos = null;
        //判断图片是否已经保存
        if (isSave) {
            //图片已经存在
//            Toast.makeText(ctx, R.string.picture_exits, Toast.LENGTH_SHORT).show();
        } else {
            try {

                //在android4.0的手机上直接创建某个文件的路径一直报这个错：open failed: ENOENT (No such file or directory).
                //在网上查了很多资料，没找到解决方案，尝试了多次终于找到解决办法:
                //如果在FileOutputStream创建一个流文件路径时或者是对一个File文件路径直接操作时，可先创建文件的路径，然后在创建文件名就不会在报该错误
                //以下是解决方案:
                //makeRootDirectory(path + fileName);

                fos = new FileOutputStream(bitmapFile);
                mbBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                //通知系统去遍历图库
                galleryAddPic(ctx, path + "/" + fileName);

//                Toast.makeText(ctx, R.string.picture_save_success + bitmapFile.getAbsolutePath() + bitmapFile.getName(), Toast.LENGTH_SHORT).show();

                fos.flush();
                fos.close();

                isSave = true;


                return path + fileName;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return path + fileName;
    }

    /**
     * 用时间来命名当前保存的文件
     *
     * @return
     */
    public static String getDateFileName() {
        String rel = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        rel = formatter.format(curDate);
        return rel;
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }
}
