package com.wdy.musicapp.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wdy.musicapp.R;
import com.wdy.musicapp.adapter.MusicAdapter;
import com.wdy.musicapp.adapter.OnSwitchClickListener;
import com.wdy.musicapp.databinding.PlayFragmentBinding;
import com.wdy.musicapp.viewModel.MyViewModel;

import java.util.Objects;

public class SongFragment extends Fragment {
    private static final String TAG = "SongFragment";
    /**
     * 创建viewModel对象.
     */
    private MyViewModel viewModel;
    /**
     * 监听点击事件.
     */
    private OnSwitchClickListener onSwitchClickListener;
    /**
     * 接口.
     */
    private MusicAdapter adapter;


    @Override
    public  void onAttach(@NonNull final Context context) {
        Log.e(TAG, "onAttach: ");
        onSwitchClickListener = (OnSwitchClickListener) context;
        super.onAttach(context);
    }

    @NonNull
    @Override
    public final View onCreateView(@NonNull final LayoutInflater inflater,
                                   @Nullable final ViewGroup container,
                                   @Nullable final Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: " );
        PlayFragmentBinding mBinding = DataBindingUtil.inflate(inflater,
                R.layout.play_fragment, container, false);

        viewModel = new ViewModelProvider(Objects.requireNonNull(
                getActivity())).get(MyViewModel.class);
        mBinding.sort.setChecked(viewModel.isOrder());

        adapter = new MusicAdapter();
        //正序倒序
        mBinding.sort.setOnClickListener(v -> {
            onSwitchClickListener.onClick();
            viewModel.order();
            viewModel.setPos(viewModel.getSongBeanList()
                    .size() - 1 - viewModel.getPos());
            adapter.notifyDataSetChanged();
        });

        return mBinding.getRoot();
    }

    @Override
    public final void onViewCreated(@NonNull final View view,
                                    @Nullable final Bundle savedInstanceState) {
        Log.e(TAG, "onViewCreated: " );
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        //歌曲列表显示
        adapter.setSongBeanList(viewModel.getSongBeanList());
        adapter.setListener(songBean -> {
            //点击回调歌曲信息播放点击音乐
            viewModel.setSongBean(songBean);
            //返回activity
            Objects.requireNonNull(getActivity()).onBackPressed();
        });
        recyclerView.setAdapter(adapter);
        super.onViewCreated(view, savedInstanceState);
    }
}
