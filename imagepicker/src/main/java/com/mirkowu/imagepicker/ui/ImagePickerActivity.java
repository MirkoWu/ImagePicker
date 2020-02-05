package com.mirkowu.imagepicker.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mirkowu.imagepicker.R;
import com.mirkowu.imagepicker.callback.PickerCallback;

import java.io.File;
import java.util.ArrayList;

/**
 * Multi image selector
 * Created by Nereo on 2015/4/7.
 * Updated by nereo on 2016/1/19.
 * Updated by nereo on 2016/5/18.
 */
public class ImagePickerActivity extends AppCompatActivity
        implements PickerCallback {

    // Single choice
    public static final int MODE_SINGLE = 0;
    // Multi choice
    public static final int MODE_MULTI = 1;

    /**
     * Max image size，int，{@link #DEFAULT_IMAGE_SIZE} by default
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * Select mode，{@link #MODE_MULTI} by default
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * Whether show camera，true by default
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * Result data set，ArrayList&lt;String&gt;
     */
    public static final String EXTRA_RESULT = "select_result";
    /**
     * Original data set
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";
    // Default image size
    public static final int DEFAULT_IMAGE_SIZE = 9;
    public static final int DEFAULT_SINGLE_SIZE = 1;

    private ArrayList<String> mSelectedList = new ArrayList<>();
    private Button mSubmitButton;
    private int mMaxSelectCount = DEFAULT_IMAGE_SIZE;
    private ImagePickerFragment mPickerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.IVP_NO_ACTIONBAR);
        setContentView(R.layout.ivp_activity_default);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final Intent intent = getIntent();
        mMaxSelectCount = intent.getIntExtra(EXTRA_SELECT_COUNT, DEFAULT_IMAGE_SIZE);
        final int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        final boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            mSelectedList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        }

        mSubmitButton = (Button) findViewById(R.id.commit);
        if (mode == MODE_SINGLE) {
            mMaxSelectCount = DEFAULT_SINGLE_SIZE;
        }

        updateDoneText(mSelectedList);
        mSubmitButton.setVisibility(View.VISIBLE);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitResult();
            }
        });

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt(ImagePickerFragment.EXTRA_SELECT_COUNT, mMaxSelectCount);
            bundle.putInt(ImagePickerFragment.EXTRA_SELECT_MODE, mode);
            bundle.putBoolean(ImagePickerFragment.EXTRA_SHOW_CAMERA, isShow);
            bundle.putStringArrayList(ImagePickerFragment.EXTRA_DEFAULT_SELECTED_LIST, mSelectedList);

            mPickerFragment = ImagePickerFragment.newInstance(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.image_grid, mPickerFragment)
                    .commit();
        }
    }

    /**
     * 提交结果
     */
    public void submitResult() {
        mSelectedList = mPickerFragment.getSelectedList();
        if (mSelectedList != null && mSelectedList.size() > 0) {
            // Notify success
            Intent data = new Intent();
            data.putStringArrayListExtra(EXTRA_RESULT, mSelectedList);
            setResult(RESULT_OK, data);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Update done button by select image data
     *
     * @param mSelectedList selected image data
     */
    public void updateDoneText(ArrayList<String> mSelectedList) {
        int size = 0;
        if (mSelectedList == null || mSelectedList.size() <= 0) {
            mSubmitButton.setText(R.string.ivp_action_done);
            mSubmitButton.setEnabled(false);
        } else {
            size = mSelectedList.size();
            mSubmitButton.setEnabled(true);
        }
        mSubmitButton.setText(getString(R.string.ivp_action_button_string,
                getString(R.string.ivp_action_done), size, mMaxSelectCount));
    }

//    @Override
//    public void onSingleImageSelected(String path) {
////        Intent data = new Intent();
////        mSelectedList.add(path);
////        data.putStringArrayListExtra(EXTRA_RESULT, mSelectedList);
////        setResult(RESULT_OK, data);
////        finish();
//
//        mSelectedList.clear();
//        mSelectedList.add(path);
//        updateDoneText(mSelectedList);
//    }
//
//    @Override
//    public void onImageSelected(String path) {
//        if (!mSelectedList.contains(path)) {
//            mSelectedList.add(path);
//        }
//        updateDoneText(mSelectedList);
//    }
//
//    @Override
//    public void onImageUnselected(String path) {
//        if (mSelectedList.contains(path)) {
//            mSelectedList.remove(path);
//        }
//        updateDoneText(mSelectedList);
//    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            // notify system the image has change
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));

            mSelectedList = mPickerFragment.getSelectedList();
            if (mSelectedList != null) {
                mSelectedList = new ArrayList<>();
            }
            mSelectedList.add(imageFile.getAbsolutePath());

            Intent data = new Intent();
            data.putStringArrayListExtra(EXTRA_RESULT, mSelectedList);
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
