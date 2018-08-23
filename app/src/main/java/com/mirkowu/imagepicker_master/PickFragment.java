package com.mirkowu.imagepicker_master;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mirkowu.imagepicker.ImagePicker;
import com.mirkowu.imagepicker.view.ImagePickerRecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PickFragment extends Fragment implements ImagePickerRecyclerView.OnImagePickEventListener, View.OnClickListener {
    ImagePicker picker;
    ImagePickerRecyclerView mRecyclerViewPicker;

    public PickFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pick, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        picker = ImagePicker.getInstance();
        mRecyclerViewPicker = (ImagePickerRecyclerView) view.findViewById(R.id.mRecyclerView22);
        mRecyclerViewPicker.setOnImagePickEventListener(this);

        List<String> list = new ArrayList<>();
        list.add("http://pic58.nipic.com/file/20150110/11284670_104043775000_2.jpg");
        list.add("http://pic40.nipic.com/20140412/11857649_170524977000_2.jpg");
        mRecyclerViewPicker.setData(list);
        view.findViewById(R.id.btn022).setOnClickListener(this);
        view.findViewById(R.id.btn122).setOnClickListener(this);
        view.findViewById(R.id.btn222).setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn022:
                picker.showCameraAction(this);
                return;
            case R.id.btn122:
                picker.single().showCamera(true);
                break;
            case R.id.btn222:
                picker.maxCount(9).showCamera(true);
                break;
        }
        picker.start(this);
    }

    //   ArrayList<String> selectPath=new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        picker.onActivityResult(requestCode, resultCode, data, new ImagePicker.OnPickResultListener() {
            @Override
            public void onPickResult(ArrayList<String> imageList) {
                mRecyclerViewPicker.setData(imageList);
            }
        });

    }

    @Override
    public void onItemClick(int position, boolean isAdd) {
        if (isAdd) {
            picker.maxCount(9).showCamera(true)
                    .origin((ArrayList<String>) mRecyclerViewPicker.getAdapter().getData())
                    .start(this);
        } else {
            ImagePicker.previewImageWithSave(getActivity(), getActivity().getExternalCacheDir().getAbsolutePath(),
                    (ArrayList<String>) mRecyclerViewPicker.getAdapter().getData(), position);
        }
    }

    @Override
    public void onItemDelete(int position) {
        mRecyclerViewPicker.getAdapter().remove(position);
    }
}
