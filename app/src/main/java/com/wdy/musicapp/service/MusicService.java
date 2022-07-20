package com.wdy.musicapp.service;

import static android.media.AudioManager.AUDIOFOCUS_GAIN;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.wdy.musicapp.dao.Util;
import com.wdy.musicapp.model.SongBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MusicService extends MediaBrowserServiceCompat {
    /**
     * 输出日志.
     */
    public static final String MEDIA_ID_ROOT = "__ROOT__";
    /**
     * 输出日志.
     */
    private static final String TAG = "MusicService";
    /**
     * 播放器对象.
     */
    private static MediaPlayer mMediaPlayer;

    /**
     * 获取initSongList表单信息.
     */
    private ArrayList<SongBean> mSongBeanList = new ArrayList<>();
    /**
     * 音頻焦点.
     */
    private AudioManager mAudioManager;
    /**
     * MediaSessionCompat对象.
     */
    private MediaSessionCompat mSession;
    /**
     * flag = true 列表倒序播放.
     * flag = false 列表正序播放
     */
    private boolean flag = false;
    /**
     * 歌曲详情元数据.
     */
    private ArrayList<MediaMetadataCompat> metaDataList;
    /**
     * 焦点监听.
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener;
    /**
     * 播放位置.
     */
    private int mPosition = 0;
    /**
     * 音乐的各种播放状态.
     */
    private PlaybackStateCompat mPlaybackState;

    /**
     * 初始化.
     */
    @Override
    public  void onCreate() {
        Log.e(TAG, "onCreate: 1");
        super.onCreate();
        //音频处理
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        //监听焦点
        onAudioFocusChange();

            mPlaybackState = new PlaybackStateCompat.Builder()
                    .setState(PlaybackStateCompat.STATE_NONE, 0, 1.0f)
                    .build();

            //完成MediaSession的构建
            mSession = new MediaSessionCompat(this, "MusicService");

            //设置标志位，方便session接收控制器指令
            mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                    | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
            //播放状态
            mSession.setPlaybackState(mPlaybackState);
            //设置SessionCallback回调，当客户端使用控制器发送指令时，
            //就会触发这些回调方法，从而达到控制播放器的控制。
            mSession.setCallback(mSessionCallback);

            //此会话是否处于活动状态
            mSession.setActive(true);


            //保存Session的配对令牌
            //设置token后会触发MediaBrowserCompat.ConnectionCallback的回调方法
            //表示MediaBrowser与MediaBrowserService连接成功
            setSessionToken(mSession.getSessionToken());



            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //监听MediaPlayer.prepare（），音乐准备好在播放
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            //监听播放结束
            mMediaPlayer.setOnCompletionListener(mCompletionListener);

        }


    /**
     * 音频焦点.①
     */
    private void onAudioFocusChange() {
        Log.e(TAG, "onAudioFocusChange:音频焦点 2");
        mOnAudioFocusChangeListener =
                focusChange -> {
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_LOSS:
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            mSessionCallback.onPause();
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN:
                            mSessionCallback.onPlay();
                            break;
                        case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                            Toast.makeText(getApplicationContext(),
                                    "获取音乐焦点失败onAudioFocusChange",
                                    Toast.LENGTH_SHORT).show();
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            //降低音量
                        default:
                            break;
                    }
                };

    }

    /**
     * @return 焦点监听.
     */
    private int appFocus() {
        Log.e(TAG, "appFocus: 3返回焦点" + "列表二");
        int focus;
        focus = mAudioManager.requestAudioFocus(
                mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        return focus ;
    }
    /**
     * 响应控制器指令的回调歌曲顺序.
     */
    private final MediaSessionCompat.Callback mSessionCallback =
            new MediaSessionCompat.Callback() {

                @Override
                public void onSetRepeatMode(final int repeatMode) {
                    Log.e(TAG, "onSetRepeatMode: 响应控制器指令的回调歌曲顺序." );
                    if (repeatMode == 1 && !flag) {
                        //倒序排列
                        Collections.reverse(mSongBeanList);
                        Collections.reverse(metaDataList);
                        flag = true;
                        mPosition = mSongBeanList.size() - 1 - mPosition;
                    } else if (repeatMode == 0 && flag) {
                        Collections.reverse(mSongBeanList);
                        Collections.reverse(metaDataList);
                        flag = false;
                        mPosition = mSongBeanList.size() - 1 - mPosition;
                    }

                }

                /**
                 * 响应MediaController.getTransportControls().play
                 */
                @Override
                public void onPlay() {
                    Log.e(TAG, "onPlay: 播放" );
                    if (appFocus() == AudioManager.AUDIOFOCUS_GAIN) {
                        mMediaPlayer.start();
                        mPlaybackState = new PlaybackStateCompat.Builder()
                                .setState(PlaybackStateCompat.STATE_PLAYING,
                                        0, 1.0f)
                                .build();
                        mSession.setPlaybackState(mPlaybackState);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "请求焦点失败,无法播放", Toast.LENGTH_SHORT).show();
                    }
                }

                /**
                 * 响应MediaController.getTransportControls().onPause
                 */
                @Override
                public void onPause() {
                    Log.e(TAG, "onPause: 暂停" );

                    mMediaPlayer.pause();
                    mPlaybackState = new PlaybackStateCompat.Builder()
                            .setState(PlaybackStateCompat.STATE_PAUSED, 0, 1.0f)
                            .build();

                    mSession.setPlaybackState(mPlaybackState);
                }

                /**
                 * 上一首音乐.
                 */
                @Override
                public void onSkipToPrevious() {
                    Log.e(TAG, "onSkipToPrevious: 上一首");
                    if (appFocus() == AudioManager.AUDIOFOCUS_GAIN) {
                        mMediaPlayer.reset();
                        mPosition--;
                        if (mPosition >= metaDataList.size() || mPosition <= -1) {
                            mPosition = metaDataList.size() - 1;
                        }
                        try {
                            //根据歌曲位置获取id再转换成uri(需要操作的数据)
                            mMediaPlayer.setDataSource(MusicService.this,
                                    Util.rawToUri(mSongBeanList.get(mPosition).getId()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mPlaybackState = new PlaybackStateCompat.Builder()
                                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1.0f)
                                .build();
                        mSession.setPlaybackState(mPlaybackState);
                        mSession.setMetadata(metaDataList.get(mPosition));
                        try {
                            //阻塞播放，直至准备好
                            mMediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mMediaPlayer.start();
//                        if (mPosition >= metaDataList.size() || mPosition <= -1) {
//                            mPosition = metaDataList.size() - 1;
//                        }
                    }else{
                        Toast.makeText(getApplicationContext(),
                                "请求焦点失败,无法上一首", Toast.LENGTH_SHORT).show();

                    }
                }

                /**
                 * 下一首音乐.
                 */
                @Override
                public void onSkipToNext() {
                    Log.e(TAG, "onSkipToNext: 下一首");
                    if (appFocus() == AudioManager.AUDIOFOCUS_GAIN) {
                        mMediaPlayer.reset();
                        mPosition++;
                        if (mPosition > metaDataList.size() - 1 || mPosition <= 0) {
                            mPosition = 0;
                        }

                        try {
                            //根据歌曲位置获取id再转换成uri(需要操作的数据)
                            mMediaPlayer.setDataSource(MusicService.this,
                                    Util.rawToUri(mSongBeanList.get(mPosition).getId()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mPlaybackState = new PlaybackStateCompat.Builder()
                                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1.0f)
                                .build();
                        mSession.setPlaybackState(mPlaybackState);
                        mSession.setMetadata(metaDataList.get(mPosition));

                        try {
                            mMediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mMediaPlayer.start();
//                        if (mPosition >= metaDataList.size() || mPosition <= -1) {
//                            mPosition = 0;
//                        }

                    }else{
                        Toast.makeText(getApplicationContext(),
                                "请求焦点失败,无法下一首", Toast.LENGTH_SHORT).show();
                    }
                }

                /**
                 * 响应MediaController.getTransportControls().playFromMediaId.
                 * 点击歌曲回调信息和播放
                 */
                @Override
                public void onPlayFromMediaId(final String mediaId,
                                              final Bundle extras) {
                    Log.e(TAG, "onPlayFromMediaId: 列表1");
                    if (appFocus() == AUDIOFOCUS_GAIN) {
                        mMediaPlayer.reset();
                        try {
                            mMediaPlayer.setDataSource(MusicService.this,
                                    Util.rawToUri(mediaId));
                            for (int i = 0; i < mSongBeanList.size(); i++) {
                                if (mSongBeanList.get(i).getId()
                                        .equals(mediaId)) {
                                    mPosition = i;
                                }
                            }
                            mMediaPlayer.prepare();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mMediaPlayer.start();
                        mPlaybackState = new PlaybackStateCompat.Builder()
                                .setState(PlaybackStateCompat.STATE_PLAYING,
                                        0, 1.0f)
                                .build();
                        mSession.setPlaybackState(mPlaybackState);
                        //歌曲元数据
                        for (MediaMetadataCompat metadata : metaDataList) {
                            if (metadata.getString(MediaMetadataCompat
                                    .METADATA_KEY_MEDIA_ID)
                                    .equals(mediaId)) {
                                //歌曲地址
                                mSession.setMetadata(metadata);
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "请求焦点失败,无法播放", Toast.LENGTH_SHORT).show();
                    }
                }
            };
    /**
     * 监听MediaPlayer.prepare().
     */

    private final MediaPlayer.OnPreparedListener mPreparedListener =
            new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {
                    Log.e(TAG, " MediaPlayer.OnPreparedListener onPrepared: 监听MediaPlayer.prepare()8" + " 13" + "列表3");
                    mMediaPlayer.start();
                    mPlaybackState = new PlaybackStateCompat.Builder()
                            .setState(PlaybackStateCompat.STATE_PLAYING,
                                    0, 1.0f)
                            .build();
                    mSession.setPlaybackState(mPlaybackState);
                }

            };
    /**
     * 监听播放结束的事件.
     */
    private final MediaPlayer.OnCompletionListener mCompletionListener =
            new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(final MediaPlayer mediaPlayer) {
                    Log.e(TAG, "MediaPlayer.OnCompletionListener onCompletion: 监听播放结束的事件10");
                    mPlaybackState = new PlaybackStateCompat.Builder()
                            .setState(PlaybackStateCompat.STATE_NONE, 0, 1.0f)
                            .build();
                    mSession.setPlaybackState(mPlaybackState);
                    mMediaPlayer.reset();
                    //播放结束自动下一首
                    mSessionCallback.onSkipToNext();
                }

            };

    /**
     * 控制连接请求.通过返回值决定是否允许客户端联机服务.
     */
    @Override
    public final BrowserRoot onGetRoot(final @NonNull String clientPackageName,
                                       final int clientUid,
                                       final @Nullable Bundle rootHints) {
        Log.e(TAG, "onGetRoot: 控制连接请求.通过返回值决定是否允许客户端联机服务");
        return new BrowserRoot(MEDIA_ID_ROOT, null);
    }

    /**
     * 执行异步获取数据，最后将数据发送至媒体浏览器的会掉接口.
     */
    @Override
    public final void onLoadChildren(final @NonNull String parentId,
                                     final @NonNull Result<List<
                                             MediaBrowserCompat.MediaItem>>
                                             result) {
        Log.e(TAG, "onLoadChildren: 执行异步获取数据，最后将数据发送至媒体浏览器的会掉接口6");

        // 将信息从当前线程中移除，允许后续调用sendResult方法
        result.detach();
        // 我们模拟获取数据的过程，真实情况应该是异步从网络或本地读取数据
        if (metaDataList == null) {
            try {
                mSongBeanList = Util.initSongList();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (SongBean songBean : mSongBeanList) {
                try {
                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(MusicService.this,
                            Util.rawToUri(songBean.getId()));
                    mMediaPlayer.prepare();
                    //歌曲时间
                    songBean.setLength(mMediaPlayer.getDuration());
                    mMediaPlayer.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                metaDataList = Util.metadataBuilder(mSongBeanList);

                mMediaPlayer.setDataSource(MusicService.this,
                        Util.rawToUri(mSongBeanList.get(mPosition).getId()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //重置播放位置
            if (mPosition >= mSongBeanList.size() || mPosition <= -1) {
                mPosition = 0;
            }
            //连接状态
            mPlaybackState = new PlaybackStateCompat.Builder()
                    .setState(PlaybackStateCompat.STATE_CONNECTING, 0, 1.0f)
                    .build();
            try {
                mMediaPlayer.prepare();
                // 向Browser发送 播放数据
                mSession.setMetadata(metaDataList.get(mPosition));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("list", mSongBeanList);
            mSession.sendSessionEvent("list", bundle);
        }
    }



    /**
     * 服务结束音乐结束.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mSession != null) {
            mSession.release();
            mSession = null;
        }
    }
}



