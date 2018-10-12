# ImagePIcker
Image selector for Android device. Support single choice and multi-choice.


[中文文档](README_zh.md)


-------------------

### Quick Start
* Step 0
Add module `ImagePicker` as your dependence. in your `build.gradle` :
```java

dependencies {
   compile 'com.mirkowu:imagepicker:0.0.7'
}
```

* Step 1 
Set your `AndroidManifest.xml` as below:
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.CAMERA"/>

```

* Step 2
Call image selector simplest in your code, eg. ( From `version-1.1` )

``` java
// Multi image selector form an Activity
ImagePicker.create(Context)
        .start(Activity);
//take photo  direct
ImagePicker.create(Context).showCameraAction(Activity/fragment)
```

Detail Api.
``` java
ImagePicker.create(Context)
        .showCamera(boolean) // show camera or not. true by default
        .count(int) // max select image size, 9 by default. used width #.multi()
        .single() // single mode
        .multi() // multi mode, default mode;
        .origin(ArrayList<String>) // original select data set, used width #.multi()
        .start(Activity/Fragment);
        
          //selected 
        
            @Override
            public void onItemClick(int position, boolean isAdd) {//click to preview
                if (isAdd) {//is add image event
                    selector.multi().count(9).showCamera(true)
                            .origin((ArrayList<String>) mRecyclerView.getAdapter().getData())
                            .start(this, 1);
                } else {
                    ImagePicker.previewImage(this, (ArrayList<String>) mRecyclerView.getAdapter().getData(), position);
        
                }
            }
        
            @Override
            public void onItemDelete(int position) {//delete evevt
                mRecyclerView.getAdapter().remove(position);
            }
```

Also support traditional `Intent` :
``` java
Intent intent = new Intent(mContext, ImagePickerActivity.class);
// whether show camera
intent.putExtra(ImagePickerActivity.EXTRA_SHOW_CAMERA, true);
// max select image amount
intent.putExtra(ImagePickerActivity.EXTRA_SELECT_COUNT, 9);
// select mode (ImagePickerActivity.MODE_SINGLE OR ImagePickerActivity.MODE_MULTI)
intent.putExtra(ImagePickerActivity.EXTRA_SELECT_MODE, ImagePickerActivity.MODE_MULTI);
// default select images (support array list)
intent.putStringArrayListExtra(ImagePickerActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultDataArray);
startActivityForResult(intent, REQUEST_IMAGE);
```

* Step 3
Receive result in your `onActivityResult` Method. eg.
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

* Step 4
No more steps, just enjoy. :)

-------------------
### Custom Activity Style
*Use the func can save image in previewActivity
```java
  public static void previewImageWithSave(Context context, String savePath, ArrayList<String> originData, int currentPos) {
            Intent intent = new Intent(context, ImagePreviewActivity.class);
            intent.putExtra(ImagePreviewActivity.CURRENTPOS, currentPos);
            intent.putExtra(ImagePreviewActivity.SAVEPATH, savePath);
            intent.putStringArrayListExtra(ImagePreviewActivity.ORIGINDATA, originData);
            context.startActivity(intent);
        }
```
### Custom Activity Style
* Custome your own Activity
```java
class CustomerActivity extends Activity implements ImagePickerFragment.Callback{
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		// customer logic here...
		Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
        // Add fragment to your Activity
        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
                .commit();
	}
	@Override
    public void onSingleImageSelected(String path) {
        // When select mode set to MODE_SINGLE, this method will received result from fragment
    }

    @Override
    public void onImageSelected(String path) {
        // You can specify your ActionBar behavior here 
    }

    @Override
    public void onImageUnselected(String path) {
        // You can specify your ActionBar behavior here 
    }

    @Override
    public void onCameraShot(File imageFile) {
        // When user take phone by camera, this method will be called.
    }
}
```



-------------------

### Change Log
* 2018-5-24
    1. update Glide to 4.7.1
    2. update Gradle to 4.4
* 2017-10-27
    1. update to Gradle 3.0
    2. modify the plugin for upload to jcenter
    3. dir scan after saving image
* 2017-7-21
    1. use "rxpermission" Simplified procedures for application authority
    2. modify callback method
* 2017-5-31
    1. add save image method 
* 2017-4-7
    1. Added. takephoto Android 7.0 support
    2. Added. the widget for contain selected images   ImagePickerRecyelerView
    3. Added. priview activity

-------------------

### Thanks
* [bumptech-glide](https://github.com/bumptech/glide) A powerful image downloading and caching library for Android 

* the progect fixed from [MultiImageSelector](https://jitpack.io/#lovetuzitong/MultiImageSelector)

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
