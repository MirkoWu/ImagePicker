package com.mirkowu.imagepicker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.mirkowu.imagepicker.ImagePicker;
import com.mirkowu.imagepicker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MirkoWu on 2017/4/6 0006.
 */

public class ImagePickerRecyclerView extends RecyclerView {
    private static final String TAG = "ImagePickerRecyclerView";
    int maxNum = 9;
    int mGridWidth;
    int columnNum;
    boolean showDelete;
    Drawable add;
    Drawable del;
    ImagePickedAdapter adapter;

    public ImagePickerRecyclerView(Context context) {
        this(context, null);
    }

    public ImagePickerRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImagePickerRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImagePickerRecyclerView, defStyle, 0);
        maxNum = a.getInteger(R.styleable.ImagePickerRecyclerView_ivp_maxNum, 9);
        columnNum = a.getInteger(R.styleable.ImagePickerRecyclerView_ivp_columnNum, 4);
        add = a.getDrawable(R.styleable.ImagePickerRecyclerView_ivp_addImage);
        del = a.getDrawable(R.styleable.ImagePickerRecyclerView_ivp_deleteImage);
        showDelete = a.getBoolean(R.styleable.ImagePickerRecyclerView_ivp_showDelete, false);
        if (add == null) {
            add = ContextCompat.getDrawable(context, R.drawable.ivp_addimage);
        }
        if (del == null) {
            del = ContextCompat.getDrawable(context, R.drawable.ivp_delete);
        }

        a.recycle();

        init(context);
    }

    private void init(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        int width = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            wm.getDefaultDisplay().getSize(size);
            width = size.x;
        } else {
            width = wm.getDefaultDisplay().getWidth();
        }
        mGridWidth = width / columnNum;
        Log.d("mGridWidth=", mGridWidth + "");
        setLayoutManager(new StaggeredGridLayoutManager(columnNum, StaggeredGridLayoutManager.VERTICAL));
        adapter = new ImagePickedAdapter(context);
        setAdapter(adapter);
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setData(List<String> data) {
        adapter.setData(data);
    }

    /**
     * 是否显示删除按钮
     *
     * @param showDelete
     */
    public void setShowDelete(boolean showDelete) {
        this.showDelete = showDelete;
        this.adapter.notifyDataSetChanged();
    }

    /***
     * 设置列
     * @param columnNum
     */
    public void setColumnNum(int columnNum) {
        this.columnNum = columnNum;
    }

    /**
     * 设置最大数量
     *
     * @param maxNum
     */
    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    /**
     * 设置添加的 图片
     *
     * @param addImageRes
     */
    public void setAddImage(int addImageRes) {
        this.add = getResources().getDrawable(addImageRes);
    }

    /**
     * 设置删除 的图片
     *
     * @param delImageRes
     */
    public void setDeleteImage(int delImageRes) {
        this.del = getResources().getDrawable(delImageRes);
    }


    public ImagePickedAdapter getAdapter() {
        return adapter;
    }


    public class ImagePickedAdapter extends RecyclerView.Adapter<ImagePickedAdapter.ImgViewHolder> {

        Context context;
        List<String> mData = new ArrayList<>();

        public ImagePickedAdapter(Context context) {
            this.context = context;
        }

        public void setData(List<String> data) {
            if (data == null) data = new ArrayList<>();
            mData = data;
            notifyDataSetChanged();
        }

        public List<String> getData() {
            return mData;
        }

        public void remove(int index) {
            mData.remove(index);
            notifyDataSetChanged();
        }

        public void remove(String data) {
            mData.remove(data);
            notifyDataSetChanged();
        }


        @Override
        public int getItemCount() {
            return Math.min(mData.size() + 1, maxNum);
        }

        @Override
        public int getItemViewType(int position) {
            if ((position) == mData.size() && mData.size() < maxNum) {
                return 0;
            } else {
                return R.layout.ivp_list_item_image;
            }
        }

        @Override
        public ImgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ImgViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ivp_list_item_image, parent, false));
        }

        @Override
        public void onBindViewHolder(ImgViewHolder holder, final int position) {
            if (getItemViewType(position) == 0) {//// 最后一项显示一个＋按钮
                holder.ivThumb.setImageDrawable(add);
            } else {
                ImagePicker.getInstance().getImageEngine().loadThumbnail(context, holder.ivThumb, mData.get(position), mGridWidth);
            }
            holder.ivSelect.setVisibility(showDelete ? VISIBLE : GONE);
            if (showDelete && getItemViewType(position) != 0) {
                holder.ivSelect.setVisibility(VISIBLE);
                holder.ivSelect.setImageDrawable(del);
            } else holder.ivSelect.setVisibility(GONE);

            //点击事件 交给外界处理
            if (onImagePickEventListener != null) {
                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onImagePickEventListener.onItemClick(position, getItemViewType(position) == 0);
                    }
                });
                holder.ivSelect.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onImagePickEventListener.onItemDelete(position);
                    }
                });
            }


        }


        public class ImgViewHolder extends RecyclerView.ViewHolder {
            ImageView ivThumb;
            ImageView ivSelect;

            public ImgViewHolder(View itemView) {
                super(itemView);
                ivThumb = itemView.findViewById(R.id.ivThumb);
                ivSelect = itemView.findViewById(R.id.ivSelect);
            }
        }


    }


    protected OnImagePickEventListener onImagePickEventListener;

    public void setOnImagePickEventListener(OnImagePickEventListener onImagePickEventListener) {
        this.onImagePickEventListener = onImagePickEventListener;
    }

    public interface OnImagePickEventListener {
        void onItemClick(int position, boolean isAddImage);

        void onItemDelete(int position);
    }

}
