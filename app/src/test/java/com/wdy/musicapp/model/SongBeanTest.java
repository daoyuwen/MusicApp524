package com.wdy.musicapp.model;



import com.wdy.musicapp.R;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class SongBeanTest extends TestCase {
    private SongBean song1;
    private SongBean song2;
    private SongBean song3;


    @Test
    @PrepareForTest(SongBean.class)
    public void test() throws Exception {
        assertEquals(song2.time(),"0 :0");
        assertEquals(song3.getId(), String.valueOf(R.raw.music3));
        assertEquals(song3.getName(), "歌名");
        assertEquals(song3.getArtist(), "歌手");
        assertEquals(song3.getAlbum(), "专辑");
        assertEquals(song3.getLength(), 2);


        assertEquals(song1.toString(), "SongBean{"
                + "id='" + R.raw.music1 + '\''
                + ", name='" + "1" + '\''
                + ", artist='" + "2"
                + '\'' + ", album='" + "3"
                + '\'' + ", length=" + 0L
                +'}');

    }



    @Before
    public void setUp() throws Exception {
        song1 = new SongBean(String.valueOf(R.raw.music1), "1", "2", "3", 0);
        song2 = new SongBean(String.valueOf(R.raw.music2), "1", "2", "3");
        song2.setLength(1);
        song3 = new SongBean();
        song3.setId(String.valueOf(R.raw.music3));
        song3.setName("歌名");
        song3.setArtist("歌手");
        song3.setAlbum("专辑");
        song3.setLength(2);

    }




}