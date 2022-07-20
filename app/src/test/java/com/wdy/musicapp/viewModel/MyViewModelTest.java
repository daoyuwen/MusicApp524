package com.wdy.musicapp.viewModel;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import com.wdy.musicapp.R;
import com.wdy.musicapp.model.SongBean;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.ArrayList;

public class MyViewModelTest {
    SongBean mSongBean = new SongBean(String.valueOf(R.raw.test),
            "测试", "歌手", "专辑");
    private MyViewModel mMyViewModel;
    private final ArrayList<SongBean> mSongBeanList = new ArrayList<>();


    @Before
    public void setUp() {
        mMyViewModel = new MyViewModel();
        mMyViewModel.setSongBean(mSongBean);
        mMyViewModel.setPos(0);
        mMyViewModel.setFlag(true);
        mMyViewModel.order();

        mSongBeanList.add(mSongBean);
        mMyViewModel.setSongBeanList(mSongBeanList);

        System.out.println(mSongBeanList);
        System.out.println(mMyViewModel);
    }

    @After
    public void tearDown(){
    }

    @Test
    @PrepareForTest({MyViewModel.class})
    public void test() {
        assertEquals(mMyViewModel.getPos(), 0);
        assertFalse(mMyViewModel.isFlag());
        assertTrue(mMyViewModel.isOrder());
        assertEquals(mMyViewModel.getSongBean(), mSongBean);
        assertEquals(mMyViewModel.getSongBeanList(), mSongBeanList);


    }

}