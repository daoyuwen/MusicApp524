package com.wdy.musicapp.service;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.ParentRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MusicService.class})
public class MusicServiceTest {

    @Test
    public void onCreate() {
    }

    @Test
    @PrepareForTest(MusicService.class)
    public void onGetRoot() {
    PowerMockito.mockStatic(MusicService.class);
//    PowerMockito.when(MusicService.)



    }


    @Test
    public void onLoadChildren() {
    }

    @Test
    public void onDestroy() {
       MusicService musicService= PowerMockito.mock(MusicService.class);
    PowerMockito.doNothing().when(musicService).onDestroy();
    }

    @Test

    public void TestAppFocus() throws Exception {

    }


}

