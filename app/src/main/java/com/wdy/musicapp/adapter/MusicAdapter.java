package com.wdy.musicapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.wdy.musicapp.BR;
import com.wdy.musicapp.R;
import com.wdy.musicapp.model.SongBean;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView
        .Adapter<MusicAdapter.ViewHolder> {
    private static final String TAG = "MusicAdapter";
    /**
     * 歌曲列表.
     */
    private List<SongBean> songBeanList = new ArrayList<>();

    /**
     * 点击监听.
     */
    private OnItemClickListener listener;

    /**
     * @param mListener 监听歌曲.
     */
    public void setListener(final OnItemClickListener mListener) {
        Log.e(TAG, "setListener: " );
        this.listener = mListener;
    }

    /**
     * @param mSongBeanList 歌曲列表.
     */
    public void setSongBeanList(final List<SongBean> mSongBeanList) {
        Log.e(TAG, "setSongBeanList: " );
        this.songBeanList = mSongBeanList;
    }

    @NonNull
    @Override
    public final ViewHolder onCreateViewHolder(
            @NonNull final ViewGroup parent,
                                                      final int viewType) {
        Log.e(TAG, "onCreateViewHolder: " );
        ViewDataBinding viewDataBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.footview, parent, false);
        return new ViewHolder(viewDataBinding);
    }

    @Override
    public final void onBindViewHolder(
            @NonNull final ViewHolder holder,
                                 final int position) {
        Log.e(TAG, "onBindViewHolder: " );
        SongBean songBean = songBeanList.get(position);
        holder.getViewDataBinding().setVariable(BR.SongBean, songBean);
        holder.getViewDataBinding().executePendingBindings();
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.itemClickListener(songBean);
            }
        });
    }

    @Override
    public final int getItemCount() {
        Log.e(TAG, "getItemCount: " );
        return songBeanList.size();
    }

    /**
     * 列表音乐.
     */
    public interface OnItemClickListener {
        /**
         * @param songBean 音乐列表.
         */
        void itemClickListener(SongBean songBean);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * 视图绑定.
         */
        private final ViewDataBinding viewDataBinding;

        /**
         * @param binding 显示信息
         */
        public ViewHolder(@NonNull final ViewDataBinding binding) {
            super(binding.getRoot());
            this.viewDataBinding = binding;
            Log.e(TAG, "ViewHolder: " );
        }

        /**
         * @return 获取数据.
         */
        public final ViewDataBinding getViewDataBinding() {
            Log.e(TAG, "getViewDataBinding: " );
            return viewDataBinding;
        }


    }
}


