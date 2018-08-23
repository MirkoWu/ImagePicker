package com.mirkowu.imagepicker.callback;

import com.mirkowu.imagepicker.bean.Folder;

import java.util.List;

/**
 * @author by DELL
 * @date on 2018/8/21
 * @describe
 */
public interface CollectionLoaderCallback {

    void onLoadFinish(List<Folder> folderList );
}
