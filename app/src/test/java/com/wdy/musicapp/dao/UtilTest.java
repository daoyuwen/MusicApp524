package com.wdy.musicapp.dao;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;

import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;

import com.wdy.musicapp.model.SongBean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.ArrayList;

@RunWith(PowerMockRunner.class)
public class UtilTest {


    ArrayList<MediaMetadataCompat> metadataList = new ArrayList<>();

    ArrayList<SongBean> songBeanList = Util.initSongList();
    SongBean song = new SongBean();

    public UtilTest() throws IOException {
    }


    @Before
    public void setUp() throws Exception {
        metadataList.add(new MediaMetadataCompat.Builder().putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, song.getId())
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

    @Test
    @PrepareForTest(Util.class)
    public void rawToUri() throws Exception {

//        PowerMockito.mockStatic(Util.class);
//        PowerMockito.spy(Util.class);
//
//        PowerMockito.when(Util.rawToUri("test")).thenCallRealMethod();
//        assertEquals(Uri.parse("android.resource://com.wdy.musicapp/test"),
//                Util.rawToUri("test"));
//
//        System.out.println(Uri.parse("test" + "" + Util.rawToUri("test")));

//spy  URi类
Uri uri =mock(Uri.class);
spy(Uri.class);

spy(Util.class);
        PowerMockito.mockStatic(Util.class);
//        PowerMockito.doCallRealMethod().when(mainActivity).onPause();
//真实方法
        String uriStr = "android.resource://com.wdy.musicapp/test";
      PowerMockito.when(Util.rawToUri("test")).thenCallRealMethod();

        Uri.parse(uriStr);
//Mockito.verify(uri,Mockito.times(1)).parse(uriStr);
//Missing method call for verify(mock) here:
//-> at com.wdy.musicapp.dao.UtilTest.rawToUri(UtilTest.java:78)
//
//Example of correct verification:
//    verify(mock).doSomething()
//
//Also, this error might show up because you verify either of: final/private/equals()/hashCode() methods.
//Those methods *cannot* be stubbed/verified.
//Mocking methods declared on non-public parent classes is not supported.


    }

    @Test
    @PrepareForTest(Util.class)
    public void initSongList() throws Exception {
        PowerMockito.mockStatic(Util.class);
        spy(Util.class);
        PowerMockito.when(Util.initSongList()).thenCallRealMethod();


        ArrayList<SongBean> songList = Util.initSongList();


        System.out.println(13 + "" + Util.initSongList());
        System.out.println(1212 + "" + songList);
//        Assert.assertEquals(songList,Util.initSongList());


    }

    @Test
    @PrepareForTest(Util.class)
    public void metadataBuilder() throws Exception {
//        PowerMockito.mockStatic(Util.class);
//        PowerMockito.spy(Util.class);
//        Util util = PowerMockito.mock(Util.class);
//        PowerMockito.when(Util.metadataBuilder(songBeanList)).thenCallRealMethod();
//        System.out.println(111 + "" + metadataList);
//        System.out.println(222 + "" + Util.metadataBuilder(songBeanList));




    }
}