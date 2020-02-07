# ImagePicker
[![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Download](https://api.bintray.com/packages/mirkowu/maven/ImagePicker/images/download.svg) ](https://bintray.com/mirkowu/maven/ImagePicker/)

仿微信实现多图选择。支持单选和多选两种模式


[English Doc](README_en.md)


-------------------

### 快速开始
* 第0步
把模块 `ImagePicker` 作为你的项目依赖添加到工程中. 在项目`build.gradle` 中:
```java

dependencies {
    implementation 'com.mirkowu:ImagePicker:3.0.1'
    
    //glide 版本最好一致 
    implementation 'com.github.bumptech.glide:glide:4.9.0'
}
```

* 第1步
在你的代码中简单调用(), eg.

``` java
// Multi image selector form an Activity
ImagePicker.getInstance(Context)
        .single() // 单选模式        
        .maxCount(9)//多选模式
        .start(Activity);

//可直接调用相机
ImagePicker.getInstance(Context).showCameraAction(Activity/fragment)
```

详细可使用的Api.
``` java
ImagePicker.getInstance(Context)
        .showCamera(boolean) // 是否显示相机. 默认为显示
        .single() // 单选模式
        .maxCount(9)//多选模式 自定义最大选择数量
        .origin(ArrayList<String>) // 默认已选择图片. 只有在选择模式为多选时有效
        .start(Activity/Fragment;
        
        
     //选择操作之后

    @Override
    public void onItemClick(int position, boolean isAdd) {//点击预览
        if (isAdd) {//是否是添加图片事件
            selector.multi().count(9).showCamera(true)
                    .origin((ArrayList<String>) mRecyclerView.getAdapter().getData())
                    .start(this, 1);
        } else {
            ImagePicker.previewImage(this, (ArrayList<String>) mRecyclerView.getAdapter().getData(), position);

        }
    }

    @Override
    public void onItemDelete(int position) {//点击删除
        mRecyclerView.getAdapter().remove(position);
    }
        
```

* 第2步
在你的 `onActivityResult` 方法中接受结果. 例如:
```java
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selector.onActivityResult(requestCode, resultCode, data, new ImagePicker.OnPickResultListener() {
            @Override
            public void onPickResult(ArrayList<String> imageList) {//选择的图片列表
                mRecyclerView.setData(imageList);
            }
        });
    }
```

* 第3步
没第3步了，就这样就OK啦~ :)

-------------------

###  保存图片
 可以调用该方法保存图片 ，传入路径为空则不会出现保存按钮
```java 
    public static void previewImageWithSave(Context context, String savePath, ArrayList<String> originData, int currentPos) {
            Intent intent = new Intent(context, ImagePreviewActivity.class);
            intent.putExtra(ImagePreviewActivity.CURRENTPOS, currentPos);
            intent.putExtra(ImagePreviewActivity.SAVEPATH, savePath);
            intent.putStringArrayListExtra(ImagePreviewActivity.ORIGINDATA, originData);
            context.startActivity(intent);
        }
```

### 自定义显示
* 自定义Activity
```java
class CustomerActivity extends Activity implements MultiImageSelectorFragment.Callback{
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		// 你自己的逻辑...
        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
        // 添加主Fragment到Activity
        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
                .commit();
	}
	@Override
    public void onSingleImageSelected(String path) {
        // 当选择模式设定为 单选/MODE_SINGLE, 这个方法就会接受到Fragment返回的数据
    }

    @Override
    public void onImageSelected(String path) {
        // 一个图片被选择是触发，这里可以自定义的自己的Actionbar行为
    }

    @Override
    public void onImageUnselected(String path) {
        // 一个图片被反选是触发，这里可以自定义的自己的Actionbar行为
    }

    @Override
    public void onCameraShot(File imageFile) {
        // 当设置了使用摄像头，用户拍照后会返回照片文件
    }
}
```
* 具体可以参考`ImagePickerActivity.java`的实现

* 图片默认使用Glide加载，需要自定义图片加载器的，可以根据下面这个自己从写 记得 `setImageEngine()`
```java
/**
 *  默认Glide 图片加载
 */
public class Glide4Loader implements ImageEngine {

    public void load(Context context, ImageView image, String url) {
        Glide.with(context).load(url)
                .apply(RequestOptions
                        .placeholderOf(com.mirkowu.imagepicker.R.drawable.ivp_default_error)
                        .error(com.mirkowu.imagepicker.R.drawable.ivp_default_error)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                )
                .into(image);
    }

    public void loadThumbnail(Context context, ImageView image, String url, int width) {
        Glide.with(context).load(url)
                .apply(RequestOptions
                        .placeholderOf(com.mirkowu.imagepicker.R.drawable.ivp_default_image)
                        .error(com.mirkowu.imagepicker.R.drawable.ivp_default_error)
                        .override(width, width)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(image);
    }

    @Override
    public void loadPicked(Context context, ImageView image, String url, int width, int height) {
        Glide.with(context).load(url)
                .apply(RequestOptions
                        .placeholderOf(com.mirkowu.imagepicker.R.drawable.ivp_default_image)
                        .error(com.mirkowu.imagepicker.R.drawable.ivp_default_error)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(image);
    }


    @Override
    public void pause(Context context) {
        // Glide.with(context).pauseRequests();
    }

    @Override
    public void resume(Context context) {
        // Glide.with(context).resumeRequests();
    }


    @Override
    public Bitmap loadAsBitmap(Context context, String url) throws ExecutionException, InterruptedException {
        return Glide.with(context)
                .asBitmap()
                .load(url)
                .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get();
    }


}
```
-------------------

### 更新日志
* 2020-2-7
    1. 更新预览界面下载图标
* 2020-2-5
    1. 修复因传送数据过大导致预览时闪退的Bug
    2. 升级到Androidx版本
    3. 升级Gradle到4.6
* 2018-5-24
    1. 更新Glide到4.7.1
    2. 升级Gradle到4.4
* 2017-10-27
    1. 升级Gradle 3.0
    2. 修改上传方式
    3. 支持保存图片后，相册刷新
* 2017-7-21
    1. 使用 rxpermission 申请权限 简化步骤
    2. 更改回调方式
* 2017-5-31
    1. 添加保存图片功能
* 2017-4-7
    1. 新增. Android 7.0 拍照支持
    2. 新增. 选择完图片可放置图片的控件ImagePickerRecyelerView
    3. 新增. 预览页面

-------------------

### 感谢

* [bumptech-glide](https://github.com/bumptech/glide)  A powerful image downloading and caching library for Android 

* 本项目参考[ MultiImageSelector](https://jitpack.io/#lovetuzitong/MultiImageSelector)

-------------------

### License
>The MIT License (MIT)

>Copyright (c) 2015 mirkowu

>Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

>The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.