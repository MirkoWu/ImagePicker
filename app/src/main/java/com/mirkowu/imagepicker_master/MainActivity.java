package com.mirkowu.imagepicker_master;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mirkowu.imagepicker.ImagePicker;
import com.mirkowu.imagepicker.view.ImagePickerRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImagePickerRecyclerView.OnImagePickEventListener {

    ImagePicker selector;
    ImagePickerRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selector = ImagePicker.getInstance().setImageEngine(new Glide4Loader());
        mRecyclerView = (ImagePickerRecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setOnImagePickEventListener(this);

        List<String> list = new ArrayList<>();
        list.add("http://pic58.nipic.com/file/20150110/11284670_104043775000_2.jpg");
        list.add("http://pic40.nipic.com/20140412/11857649_170524977000_2.jpg");
        mRecyclerView.setData(list);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new PickFragment()).commit();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn0:
                // selector.showCameraAction(this);
              //  mRecyclerView.setItemShape(ImagePickerRecyclerView.ITEM_SHAPE_FIT);
                mRecyclerView.setItemShape(ImagePickerRecyclerView.ITEM_SHAPE_SIZE,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mRecyclerView.setImageScaleType(ImageView.ScaleType.FIT_XY);
                mRecyclerView.setSpanCount(1);

                break;
            case R.id.btn1:
                // selector.single().showCamera(true).start(this);

                mRecyclerView.setItemShape(ImagePickerRecyclerView.ITEM_SHAPE_SQUARE);
                mRecyclerView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                mRecyclerView.setSpanCount(2);

                break;
            case R.id.btn2:
                selector.maxCount(9).showCamera(true).start(this);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selector.onActivityResult(requestCode, resultCode, data, new ImagePicker.OnPickResultListener() {
            @Override
            public void onPickResult(ArrayList<String> imageList) {
                for (String s : imageList) {
                    Log.d("path", "" + s);
                }
                mRecyclerView.setData(imageList);
            }
        });
    }

    @Override
    public void onItemClick(int position, boolean isAdd) {
        if (isAdd) {
            selector.maxCount(9).showCamera(true)
                    .origin((ArrayList<String>) mRecyclerView.getAdapter().getData())
                    .start(this);
        } else {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test2";
            Log.d("path==", path + "  fsef");
            ImagePicker.previewImageWithSave(this, path /*getApplicationContext().getExternalCacheDir().getAbsolutePath()*/,
                    (ArrayList<String>) mRecyclerView.getAdapter().getData(), position);
        }
    }

    @Override
    public void onItemDelete(int position) {
        mRecyclerView.getAdapter().remove(position);
    }
}
