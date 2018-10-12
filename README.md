# ImagePicker
仿微信实现多图选择。支持单选和多选两种模式


[English Doc](README_en.md)


-------------------

### 快速开始
* 第0步
把模块 `ImagePicker` 作为你的项目依赖添加到工程中. 在项目`build.gradle` 中:
```java

dependencies {
    compile 'com.mirkowu:imagepicker:0.0.7'
}
```

* 第1步 
在你的 `AndroidManifest.xml` 中做如下声明:
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.CAMERA"/>

```

* 第2步
在你的代码中简单调用(), eg.

``` java
// Multi image selector form an Activity
ImagePicker.create(Context)
        .start(Activity);

//可直接调用相机
ImagePicker.create(Context).showCameraAction(Activity/fragment)
```

详细可使用的Api.
``` java
ImagePicker.create(Context)
        .showCamera(boolean) // 是否显示相机. 默认为显示
        .count(int) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
        .single() // 单选模式
        .multi() // 多选模式, 默认模式;
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

同样支持老版本的 `Intent` 调用方法:
```java
Intent intent = new Intent(mContext, ImagePickerActivity.class);
// 是否显示调用相机拍照
intent.putExtra(ImagePickerActivity.EXTRA_SHOW_CAMERA, true);
// 最大图片选择数量
intent.putExtra(ImagePickerActivity.EXTRA_SELECT_COUNT, 9);
// 设置模式 (支持 单选/ImagePickerActivity.MODE_SINGLE 或者 多选/ImagePickerActivity.MODE_MULTI)
intent.putExtra(ImagePickerActivity.EXTRA_SELECT_MODE, ImagePickerActivity.MODE_MULTI);
// 默认选择图片,回填选项(支持String ArrayList)
intent.putStringArrayListExtra(ImagePickerActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultDataArray);
startActivityForResult(intent, REQUEST_IMAGE);
```

* 第3步
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

* 第4步
没第4步了，就这样就OK啦~ :)

-------------------

* 保存图片
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
        <bundle class="putBo"></bundle>olean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
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

-------------------

### 更新日志
* 2018-5-24
    1. 更新Glide到4.7.1
    2. 升级Gilde到4.4
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