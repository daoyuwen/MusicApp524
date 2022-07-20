package com.wdy.musicapp.viewModel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.wdy.musicapp.model.SongBean;

import java.util.ArrayList;
import java.util.Collections;

public class MyViewModel extends ViewModel {
    private static final String TAG = "MyViewModel";
    /**
     * 是否允许后台播放.
     */
    private boolean flag = true;
    /**
     * 记录当前播放位置.
     */
    private int pos = 0;
    /**
     * 当前播放歌曲.
     */
    private SongBean songBean = new SongBean();
    /**
     * 歌曲列表.
     */
    private ArrayList<SongBean> songBeanList = new ArrayList<>();
    /**
     * 歌曲顺序.
     */
    private boolean order;

    /**
     * @return 后台播放.
     */
    public boolean isFlag() {
        Log.e(TAG, "isFlag: " );
        return !flag;
    }

    /**
     * @param mFlag 后台播放.
     */
    public void setFlag(final boolean mFlag) {
        Log.e(TAG, "setFlag: " );
        this.flag = mFlag;
    }

    /**
     * @return 歌曲位置.
     */
    public int getPos() {
        return pos;
    }

    /**
     * @param nPos 歌曲位置.
     */
    public void setPos(final int nPos) {
        this.pos = nPos;
    }

    /**
     * 歌曲顺序.
     */
    public void order() {
        order = !order;
        Collections.reverse(songBeanList);
    }

    /**
     * @return 歌曲顺序.
     */
    public boolean isOrder() {
        return order;
    }

    /**
     * @return 获取歌曲.
     */
    public final SongBean getSongBean() {
        return songBean;
    }

    /**
     * @param mSongBean 歌曲信息.
     */
    public void setSongBean(final SongBean mSongBean) {
        this.songBean = mSongBean;
//        resetPos();
    }

    /**
     * 放回播放列表.
     *
     * @return 播放列表
     */
    public ArrayList<SongBean> getSongBeanList() {
        return songBeanList;
    }

    /**
     * 初始化播放列表.
     *
     * @param mSongBeanList 播放列表
     */
    public void setSongBeanList(final ArrayList<SongBean> mSongBeanList) {
        pos = 0;
        this.songBeanList = mSongBeanList;
        songBean = songBeanList.get(pos);
    }

//    /**
//     * 重新设定位置.
//     */
//    public void resetPos() {
//        pos = songBeanList.indexOf(songBean);
//    }
//

}
