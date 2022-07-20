package com.wdy.musicapp.view;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

import android.content.ComponentName;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.wdy.musicapp.R;
import com.wdy.musicapp.databinding.ActivityMainBinding;
import com.wdy.musicapp.service.MusicService;
import com.wdy.musicapp.viewModel.MyViewModel;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MainActivity.class, MediaBrowserCompat.class})
public class MainActivityTest {



@InjectMocks
private MainActivity mainActivity;



    //   MediaBrowserCompat mBrowser;
    //无返回值
    @Test
    public void onStart() throws Exception {

        MainActivity mMain = spy(new MainActivity());
        MediaBrowserCompat mBrowser = PowerMockito.mock(MediaBrowserCompat.class);
        PowerMockito.doCallRealMethod().when(mMain).onStart();
        PowerMockito.doNothing().when(mBrowser).connect();
//        mMain.onStart();
        mBrowser.connect();
//        Mockito.verify(mMain, Mockito.times(1)).onStart();
        Mockito.verify(mBrowser, Mockito.times(1)).connect();


    }

//
//    @Test
//    public void onPause() throws Exception {
//
//
//        MainActivity mainActivity = spy(new MainActivity());
//        MyViewModel myViewModel = PowerMockito.mock(MyViewModel.class);
////        PowerMockito.when(myViewModel.isFlag()).thenReturn(true);
//        PowerMockito.doCallRealMethod().when(myViewModel).isFlag();
//        boolean total = myViewModel.isFlag();
//        System.out.println(11 + "" + total);
//        Assert.assertTrue(total);
//        PowerMockito.doCallRealMethod().when(mainActivity).onPause();
//        Mockito.verify(myViewModel, Mockito.times(1)).isFlag();
//
//
//    }



//    @Test
//    @PrepareForTest({MainActivity.class, MediaBrowserCompat.class})
//    public void onStop() throws Exception {
////        MainActivity mMain = spy(new MainActivity());
////        PowerMockito.doCallRealMethod().when(mMain).onStop();
////
////        MediaBrowserCompat mediaBrowserCompat = PowerMockito.mock(MediaBrowserCompat.class);
////        PowerMockito.doNothing().when(mediaBrowserCompat).disconnect();
////
////        PowerMockito.suppress(method(AppCompatActivity.class, "onStop"));
////
////        mediaBrowserCompat.disconnect();
////        mMain.onStop();
////        Mockito.verify(mediaBrowserCompat, Mockito.times(1)).disconnect();
////        Mockito.verify(mMain, Mockito.times(1)).onStop();
//
//
//    }

    @Test
    public void onClick() {

//        MainActivity mainActivity = spy(new MainActivity());
//        SongFragment songFragment = mock(SongFragment.class);
//
//        PowerMockito.doCallRealMethod().when(mainActivity).onClick();
//        MyViewModel myViewModel = mock(MyViewModel.class);
//        PowerMockito.doCallRealMethod().when(myViewModel).isOrder();
//
////        PowerMockito.when(myViewModel.isOrder()).thenReturn(true);
//
//        mainActivity.onClick();
//        Mockito.verify(mainActivity,Mockito.times(1)).onClick();
 mock(MyViewModel.class);
 when(MyViewModel.class);

    }

    @Test
    @PrepareForTest({MainActivity.class})
    public void onDestroy() {

        MainActivity mainActivity = spy(new MainActivity());
        PowerMockito.doCallRealMethod().when(mainActivity).onDestroy();
//        PowerMockito.suppress(Whitebox.getMethod(
//                MainActivity.class,"OnDestroy"));
        PowerMockito.suppress(method(AppCompatActivity.class, "onDestroy"));
        mainActivity.onDestroy();
        Mockito.verify(mainActivity, Mockito.times(3)).onDestroy();

    }


    @Test
    @PrepareForTest({MainActivity.class})
    public void onCreateTest() throws Exception {
        MainActivity mMain = spy(new MainActivity());
        Bundle bundle = new Bundle();
        PowerMockito.doCallRealMethod().when(mMain).onCreate(bundle);

    }
}


























