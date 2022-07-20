package com.wdy.musicapp.view;

import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_NONE;
import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_ONE;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.wdy.musicapp.R;
import com.wdy.musicapp.adapter.OnSwitchClickListener;
import com.wdy.musicapp.databinding.ActivityMainBinding;
import com.wdy.musicapp.model.SongBean;
import com.wdy.musicapp.service.MusicService;
import com.wdy.musicapp.viewModel.MyViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements OnSwitchClickListener {
    private static final String TAG = "MainActivity";
    /**
     * 创建viewModel对象.
     */
    private MyViewModel myViewModel;
    /**
     * dataBinding.
     */
    private ActivityMainBinding mBinding;
    /**
     * 媒体控制器控制播放过程中的回调接口，可以用来根据播放状态更新UI.
     */
    private final MediaControllerCompat.Callback mControllerCallback =
            new MediaControllerCompat.Callback() {

                /**
                 * 上一首下一首相关
                 * @param event  .
                 * @param extras  .
                 */
                @Override
                public void onSessionEvent(final String event,
                                           final Bundle extras) {
                    Log.e(TAG, "onSessionEvent: 9");
                    //检查歌曲列表初始化
                    if (event.equals("list")) {
                        myViewModel.setSongBeanList((ArrayList<SongBean>)
                                extras.getSerializable("list"));
                    }
                }

                /***
                 * @param state  音乐播放状态改变的回调.
                 */
                @Override
                public void onPlaybackStateChanged(
                        final PlaybackStateCompat state) {
                    Log.e(TAG, "onPlaybackStateChanged: 10");
                    updatePlayState(state);
                }

                /**
                 * @param metadata  播放音乐改变的回调.
                 */
                @Override
                public void onMetadataChanged(
                        final MediaMetadataCompat metadata) {
                    Log.e(TAG, "onMetadataChanged: 11");
                    updatePlayMetadata(metadata);
                }
            };
    /**
     * 获取FragmentManager对象.
     */
    private FragmentManager mFragmentManager;
    /**
     * 创建FragmentTransaction对象.
     */
    private FragmentTransaction mFragmentTransaction;
    /**
     * 媒体浏览器.创建MediaBrowser，并执行服务端service和订阅数据.①.
     */
    private MediaBrowserCompat mBrowser;
    /**
     * 媒体控制器.
     */
    private MediaControllerCompat mController;
    /**
     * 连接状态的回调接口，连接成功时会调用onConnected()方法.
     */
    private final MediaBrowserCompat.ConnectionCallback
            browserConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    Log.e(TAG, "onConnected: 6");
                    if (mBrowser.isConnected()) {
                        //mediaId即为MediaBrowserService.onGetRoot的返回值
                        String mediaId = mBrowser.getRoot();
                        //Browser通过订阅的方式向Service请求数据，发起订阅请求需要两个参数，其一为mediaId
                        //取消mediaId的订阅者
                        mBrowser.unsubscribe(mediaId);
                        //之前说到订阅的方法还需要一个参数，即设置订阅回调SubscriptionCallback
                        //当Service获取数据后会将数据发送回来，此时会触发SubscriptionCallback.onChildrenLoaded回调

                        //歌曲信息回调
                        mBrowser.subscribe(mediaId,
                                new MediaBrowserCompat.SubscriptionCallback() {
                                    @Override
                                    public void onChildrenLoaded(
                                            @NonNull final String parentId,
                                            @NonNull final List
                                                    <MediaBrowserCompat
                                                            .MediaItem> children) {
                                        Log.e(TAG, "onChildrenLoaded: 7");

                                    }
                                });


                        //设置订阅回调SubscriptionCallback
                        try {
                            mController = new MediaControllerCompat(
                                    MainActivity.this,
                                    mBrowser.getSessionToken());

                            mController.registerCallback(mControllerCallback);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onConnectionFailed() {
                    Log.e(TAG, "onConnectionFailed: 8");
                    super.onConnectionFailed();
                }
            };
    /**
     * 歌曲列表碎片.
     */
    private final FragmentManager.FragmentLifecycleCallbacks
            mFragmentLifecycleCallbacks
            = new FragmentManager.FragmentLifecycleCallbacks() {
        //监听碎片生命周期
        @Override
        public void onFragmentViewDestroyed(@NonNull final FragmentManager fm,
                                            @NonNull final Fragment f) {
            Log.e(TAG, "onFragmentViewDestroyed: 12");
            mBinding.setMyViewModel(myViewModel);
            mController.getTransportControls()
                    .playFromMediaId(myViewModel.getSongBean()
                            .getId(), new Bundle());

        }

    };

    /**
     * 启动.
     */
    @Override
    protected void onStart() {
//        Log.e(TAG, "onStart: 2" );

        //Browser发送连接请求
        mBrowser.connect();
        super.onStart();


    }

    /**
     * @param savedInstanceState 初始化界面.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
//        Log.e(TAG, "onCreate: 1");


        // 避免ui 解绑后，后台播放音乐停止
        startService(new Intent(this, MusicService.class));
        //dataBinding将点击事件类绑定到xml文件中
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        //点击事件
        dataBinding();

        // 绑定服务端
        mBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MusicService.class),
                // 设置连接回调
                browserConnectionCallback,
                null);
        super.onCreate(savedInstanceState);

    }

    /**
     * 暂停.
     */
    @Override
    protected void onPause() {
        Log.e(TAG, "onPause: 3");
        if (myViewModel.isFlag()) {
            mController.getTransportControls().pause();
        }
        super.onPause();
    }

    /**
     * 返回activity时调用.
     */
    @Override
    protected void onResume() {
        Log.e(TAG, "onResume: 4");
        mBinding.setMyViewModel(myViewModel);
        if (myViewModel.isFlag()) {
            mController.getTransportControls().play();
        }
        super.onResume();
    }

    /**
     * 回调信息和歌曲.
     */
    @Override
    protected void onStop() {
        Log.e(TAG, "onStop: 5");
        //与媒体浏览服务断开连接。在此之后，将不再收到回调。
        mBrowser.disconnect();
        super.onStop();
    }

    /**
     * @param state 播放状态.
     */
    private void updatePlayState(final PlaybackStateCompat state) {
        if (state == null) {
            return;
        }

        switch (state.getState()) {
            case PlaybackStateCompat.STATE_NONE://无任何状态
            case PlaybackStateCompat.STATE_PAUSED:
                mBinding.play.setBackgroundResource(
                        R.drawable.playmusic_24);
                break;
            case PlaybackStateCompat.STATE_PLAYING:
                mBinding.play.setBackgroundResource(
                        R.drawable.stopmusic_24);
                break;
            case PlaybackStateCompat.STATE_BUFFERING:
            case PlaybackStateCompat.STATE_CONNECTING:
            case PlaybackStateCompat.STATE_ERROR:
            case PlaybackStateCompat.STATE_FAST_FORWARDING:
            case PlaybackStateCompat.STATE_REWINDING:
            case PlaybackStateCompat.STATE_SKIPPING_TO_NEXT:
            case PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS:
            case PlaybackStateCompat.STATE_SKIPPING_TO_QUEUE_ITEM:
            case PlaybackStateCompat.STATE_STOPPED:
            default:
                break;
        }
    }

    /**
     * @param metadata 播放源数据更新.
     */
    private void updatePlayMetadata(final MediaMetadataCompat metadata) {
        if (metadata == null) {
            return;
        }
        //获取信息显示页面上
        myViewModel.setSongBean(
                new SongBean(
                        (metadata.getString(MediaMetadataCompat
                                .METADATA_KEY_MEDIA_ID)),
                        (metadata.getString(MediaMetadataCompat
                                .METADATA_KEY_TITLE)),
                        (metadata.getString(MediaMetadataCompat
                                .METADATA_KEY_ARTIST)),
                        (metadata.getString(MediaMetadataCompat
                                .METADATA_KEY_ALBUM)),
                        (metadata.getLong(MediaMetadataCompat
                                .METADATA_KEY_DURATION))));
        //显示首页播放信息
        mBinding.setSongBean(myViewModel.getSongBean());
    }

    /**
     * dataBinding按钮点击事件.
     */
    private void dataBinding() {
        Log.e(TAG, "dataBinding: 13");
//点击切换到播放列表页面
        mBinding.list.setOnClickListener(v -> {
            SongFragment fragment = new SongFragment();
            //获取FragmentManager
            mFragmentManager = getSupportFragmentManager();
            mFragmentManager.registerFragmentLifecycleCallbacks(
                    mFragmentLifecycleCallbacks, true);
            //开启一个事务
            mFragmentTransaction = mFragmentManager.beginTransaction();
            //向容器添加或替换一个碎片，一般使用replace
            mFragmentTransaction.replace(R.id.main_frame, fragment);
            mFragmentTransaction.addToBackStack(null);
            //提交事务
            mFragmentTransaction.commit();

        });
//点击播放暂停
        mBinding.play.setOnClickListener(v -> {
            switch (mController.getPlaybackState().getState()) {
                case PlaybackStateCompat.STATE_PLAYING:
                    mController.getTransportControls().pause();
                    break;
                case PlaybackStateCompat.STATE_PAUSED:
                case PlaybackStateCompat.STATE_NONE:
                    mController.getTransportControls().play();
                    break;
                case PlaybackStateCompat.STATE_BUFFERING:
                case PlaybackStateCompat.STATE_CONNECTING:
                case PlaybackStateCompat.STATE_ERROR:
                case PlaybackStateCompat.STATE_FAST_FORWARDING:
                case PlaybackStateCompat.STATE_REWINDING:
                case PlaybackStateCompat.STATE_SKIPPING_TO_NEXT:
                case PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS:
                case PlaybackStateCompat.STATE_SKIPPING_TO_QUEUE_ITEM:
                case PlaybackStateCompat.STATE_STOPPED:
                default:
                    mController.getTransportControls().playFromSearch("", null);
                    break;

            }
        });
        //切换上一首音乐
        mBinding.btnFirstMusic.setOnClickListener(v ->
                mController.getTransportControls().skipToPrevious()
        );

        //切换下一首音乐
        mBinding.btnLastMusic.setOnClickListener(v ->
                mController.getTransportControls().skipToNext());


        //后台开关
        mBinding.serviceMusic.setOnCheckedChangeListener((CompoundButton
                                                                  buttonView,
                                                          boolean isChecked) ->
                myViewModel.setFlag(isChecked));
    }


    /**
     * 顺序倒叙.
     */
    @Override
    public void onClick() {
        Log.e(TAG, "onClick: 14");
        if (myViewModel.isOrder()) {
            mController.getTransportControls().setRepeatMode(REPEAT_MODE_NONE);
        } else if (!myViewModel.isOrder()) {
            mController.getTransportControls().setRepeatMode(REPEAT_MODE_ONE);
        }
    }

    /**
     * 活动结束.
     */
    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy: 15");
        super.onDestroy();

    }


}
