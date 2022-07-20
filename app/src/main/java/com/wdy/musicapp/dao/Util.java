package com.wdy.musicapp.dao;

import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;

import com.wdy.musicapp.R;
import com.wdy.musicapp.model.SongBean;

import java.io.IOException;
import java.util.ArrayList;

public final class Util {


    private static final String TAG = "Util";

    /**
     * @param id 歌曲id
     * @return 返回歌曲id
     */
    public static Uri rawToUri(final String id) {
        String uriStr = "android.resource://com.wdy.musicapp/" + id;
        return Uri.parse(uriStr);
    }

    /**
     * @return 歌曲信息.
     * @throws IOException 异常处理.
     */
    public static ArrayList<SongBean> initSongList() throws IOException {

        ArrayList<SongBean> songList = new ArrayList<SongBean>() {
        };
        SongBean songBean = new SongBean(String.valueOf(R.raw.test),
                "测试", "歌手", "专辑");
        SongBean songBean1 = new SongBean(String.valueOf(R.raw.music1),
                "刚昂好", "薛之谦", "刚好");
        SongBean songBean2 = new SongBean(String.valueOf(R.raw.music2),
                "认真的雪", "薛之谦", "雪");
        SongBean songBean3 = new SongBean(String.valueOf(R.raw.music3),
                "听见你", "薛之谦", "听见你");
        SongBean songBean4 = new SongBean(String.valueOf(R.raw.music4),
                "还有什么", "薛之谦", "还有什么");
        SongBean songBean5 = new SongBean(String.valueOf(R.raw.music5),
                "麻雀", "李荣浩", "麻雀");
        SongBean songBean6 = new SongBean(String.valueOf(R.raw.music6),
                "平常", "李荣浩", "李荣浩");
        SongBean songBean7 = new SongBean(String.valueOf(R.raw.music7),
                "轻音乐", "无名", "轻音乐");
        SongBean songBean8 = new SongBean(String.valueOf(R.raw.music8),
                "成都", "赵雷", "成都");
        SongBean songBean9 = new SongBean(String.valueOf(R.raw.music9),
                "斑马斑马", "网红", "斑马斑马");
        SongBean songBean10 = new SongBean(String.valueOf(R.raw.music10),
                "斑马复制版", "网红", "斑马复制版");
        SongBean songBean11 = new SongBean(String.valueOf(R.raw.music11),
                "初期", "薛之谦", "初期");
        SongBean songBean12 = new SongBean(String.valueOf(R.raw.music12),
                "歌曲1", "歌手1", "歌曲1");
        SongBean songBean13 = new SongBean(String.valueOf(R.raw.music13),
                "歌曲2", "歌手2", "歌曲2");
        SongBean songBean14 = new SongBean(String.valueOf(R.raw.music14),
                "歌曲3", "歌手3", "歌曲3");
        SongBean songBean15 = new SongBean(String.valueOf(R.raw.music15),
                "歌曲4", "歌手4", "歌曲4");
        SongBean songBean16 = new SongBean(String.valueOf(R.raw.music16),
                "歌曲5", "歌手5", "歌曲5");
        SongBean songBean17 = new SongBean(String.valueOf(R.raw.music17),
                "歌曲6", "歌手6", "歌曲6");
        SongBean songBean18 = new SongBean(String.valueOf(R.raw.music18),
                "歌曲7", "歌手7", "歌曲7");
        SongBean songBean19 = new SongBean(String.valueOf(R.raw.music19),
                "歌曲8", "歌手8", "歌曲8");
        SongBean songBean20 = new SongBean(String.valueOf(R.raw.music20),
                "歌曲9", "歌手9", "歌曲9");
        songList.add(songBean);
        songList.add(songBean1);
        songList.add(songBean2);
        songList.add(songBean3);
        songList.add(songBean4);
        songList.add(songBean5);
        songList.add(songBean6);
        songList.add(songBean7);
        songList.add(songBean8);
        songList.add(songBean9);
        songList.add(songBean10);
        songList.add(songBean11);
        songList.add(songBean12);
        songList.add(songBean13);
        songList.add(songBean14);
        songList.add(songBean15);
        songList.add(songBean16);
        songList.add(songBean17);
        songList.add(songBean18);
        songList.add(songBean19);
        songList.add(songBean20);
        return songList;
    }

    /**
     * @param songBeanList 歌曲列表.
     * @return 返回歌曲信息
     * @throws IOException 抛出异常.
     */
    public static ArrayList<MediaMetadataCompat> metadataBuilder(
            final ArrayList<SongBean> songBeanList) throws IOException {
        ArrayList<MediaMetadataCompat> metadataList = new ArrayList<>();
        for (SongBean song : songBeanList) {
            metadataList.add(new MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, song.getId())
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE,
                            song.getName())
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST,
                            song.getArtist())
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM,
                            song.getAlbum())
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION,
                            song.getLength())
                    .build());
        }
        Log.e(TAG, "metadataBuilder: "+metadataList);
        return metadataList;
    }

}
