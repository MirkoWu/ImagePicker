package com.mirkowu.imagepicker.callback;

import java.io.File;

/**
 * @author by DELL
 * @date on 2018/8/21
 * @describe
 */
public interface PickerCallback {
//    void onSingleImageSelected(String path);
//
//    void onImageSelected(String path);
//
//    void onImageUnselected(String path);

    void onCameraShot(File imageFile);
}
