package com.afirez.wevideo.common.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.afirez.wevideo.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by afirez on 18-3-29.
 */

public class ImageUtils {
    private static final float VER_POSTER_RATIO = 0.73f;
    private static final float HOR_POSTER_RATIO = 1.5f;

    public static void load(ImageView view, String url) {
        if (view != null && url != null) {
            Glide.with(view.getContext()).load(url).into(view);
        }
    }

    public static void load(ImageView view, String url, int width, int height) {
        if (view != null && url != null && width > 0 && height > 0) {
            if (width > height) {
                Glide.with(view.getContext())
                        .load(url) //加载图片url
                        .diskCacheStrategy(DiskCacheStrategy.ALL)// 设置缓存
                        .error(R.drawable.ic_loading_hor)//出错时使用默认图
                        .fitCenter()//设置图片居中, centerCrop会截断大图,不会自适应, fitCenter居中自适应
                        .override(height, width)//重写宽高
                        .into(view);//加载imageview上
            } else {
                Glide.with(view.getContext())
                        .load(url) //加载图片url
                        .diskCacheStrategy(DiskCacheStrategy.ALL)// 设置缓存
                        .error(R.drawable.ic_loading_hor)//出错时使用默认图
                        .centerCrop()//设置图片居中
                        .override(width, height)//重写宽高
                        .into(view);//加载imageview上
            }
        }
    }

    /**
     * 让图片获得最佳比例
     *
     * @param context
     * @param columns
     * @return
     */
    public static Point getVerPostSize(Context context, int columns) {
        int width = getScreenWidthPixel(context) / columns;
        width = width - dp(context, 8);
        int height = Math.round((float) width / VER_POSTER_RATIO);
        Point point = new Point();
        point.x = width;
        point.y = height;
        return point;
    }

    public static Point getHorPostSize(Context context, int columns) {
        int width = getScreenWidthPixel(context) / columns;
        width = width - dp(context, 4);
        int height = Math.round((float) width / HOR_POSTER_RATIO);
        Point point = new Point();
        point.x = width;
        point.y = height;
        return point;
    }

    public static int dp(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int getScreenWidthPixel(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        return width;
    }
}
