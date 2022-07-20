package com.wdy.musicapp.model;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.wdy.musicapp.BR;

import java.io.Serializable;

public class SongBean  extends BaseObservable implements Serializable {
    /**
     * 序号.
     */
    private String id;
    /**
     * 歌曲名.
     */
    private String name;
    /**
     * 歌手名.
     */
    private String artist;
    /**
     * 专辑.
     */
    private String album;
    /**
     * 时长.
     */
    private long length = 0;
    /**
     * 时间处理.
     */
    static final int LENGTH = 1000;
    /**
     * 时间.
     */
    static final int TIME = 60;

    /**
     * 无参构造器.
     */
    public SongBean() {
    }
    /**
     * 列表.
     * @param mId 序号
     * @param mName 歌名
     * @param mArtist 歌手
     * @param mAlbum 时间
     */
    public SongBean(final String mId, final String mName,
                    final String mArtist, final String mAlbum) {
        this.id = mId;
        this.name = mName;
        this.artist = mArtist;
        this.album = mAlbum;
        notifyChange();
    }

    /**
     * 首页.
     * @param mId 序号
     * @param mName 歌名
     * @param mArtist 歌手
     * @param mAlbum 专辑
     * @param mLength 时间
     */
    public SongBean(final String mId, final String mName, final String mArtist,
                    final String mAlbum, final long mLength) {
        this.id = mId;
        this.name = mName;
        this.artist = mArtist;
        this.album = mAlbum;
        this.length = mLength;
    }

    /**
     * @return 获取id.
     */
    @Bindable
    public String getId() {
        return id;
    }

    /**
     * @param mId 编号.
     */
    public void setId(final String mId) {
        this.id = mId;
        notifyPropertyChanged(BR.id);
    }
    /**
     * @return 获取name.
     */
    @Bindable
    public String getName() {
        return name;
    }

    /**
     * @param mName 歌曲名.
     */
    public void setName(final String mName) {
        this.name = mName;
        notifyPropertyChanged(BR.name);
    }
    /**
     * @return 获取artist.
     */
    @Bindable
    public String getArtist() {
        return artist;
    }

    /**
     * @param mArtist 歌手名.
     */
    public void setArtist(final String mArtist) {
        this.artist = mArtist;
        notifyPropertyChanged(BR.artist);
    }
    /**
     * @return 获取Album.
     */
    @Bindable
    public String getAlbum() {
        return album;
    }

    /**
     * @param mAlbum 专辑名.
     */
    public void setAlbum(final String mAlbum) {
        this.album = mAlbum;
        notifyPropertyChanged(BR.album);
    }



    @NonNull
    @Override
    public final String toString() {
        return "SongBean{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", artist='" + artist
                + '\'' + ", album='" + album
                + '\'' + ", length=" + length
                + '}';
    }

    /**
     * @return 歌曲长度.
     */
    @Bindable
    public final long getLength() {
        return length;
    }

    /**
     * @param mLength 歌曲长度.
     */
    public final void setLength(final long mLength) {
        this.length = mLength;
        notifyPropertyChanged(BR.length);
    }

    /**
     * @return 转换为时长.
     */
    public final String time() {
        int a = (int) length / LENGTH;
    return a / TIME + " :" + a % TIME;
    }
}
