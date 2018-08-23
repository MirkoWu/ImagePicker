package com.mirkowu.imagepicker.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.concurrent.ExecutionException;

/**
 * Created by MirkoWu on 2017/4/6 0006.
 */

public interface ImageEngine {


    /**
     * 预览加载大图时用到的方法
     *
     * @param context
     * @param image
     * @param url
     */
    void load(Context context, ImageView image, String url);

    /**
     * 加载 文件夹，图片列表等 用到的方法 可重写size
     *
     * @param context
     * @param image
     * @param url
     * @param width   size宽度
     */
    void loadThumbnail(Context context, ImageView image, String url, int width);

    /**
     * 停止加载
     *
     * @param context
     */
    void pause(Context context);

    /**
     * 开始加载
     *
     * @param context
     */
    void resume(Context context);

    /**
     * 下载网络图片时用到的方法 没有用到可以不用管
     *
     * @param context
     * @param url
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    Bitmap loadAsBitmap(Context context, String url) throws ExecutionException, InterruptedException;


}
