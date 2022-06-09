package com.datnt.textart.fragment.emoji;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.datnt.textart.R;
import com.datnt.textart.adapter.EmojiAdapter;
import com.datnt.textart.callback.ICallBackItem;
import com.datnt.textart.data.DataEmoji;
import com.datnt.textart.model.EmojiModel;
import com.datnt.textart.utils.Utils;

import java.util.ArrayList;

public class EmojiBearFragment extends Fragment {

    private String nameEmoji;
    private RecyclerView rcvEmoji;

    private ICallBackItem callBack;

    public static EmojiBearFragment newInstance(String nameEmoji) {
        EmojiBearFragment fragment = new EmojiBearFragment();
        Bundle args = new Bundle();
        args.putString("bear", nameEmoji);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            nameEmoji = getArguments().getString("bear");
        }
    }

    private void setUpData(String nameEmoji) {
        EmojiAdapter emojiAdapter = new EmojiAdapter(requireContext(), (o, pos) -> {
            callBack.callBackItem(o, pos);
            Utils.showToast(requireContext(), getString(R.string.success));
        });

        emojiAdapter.setData(DataEmoji.getTitleEmoji(requireContext(), nameEmoji));
        GridLayoutManager manager = new GridLayoutManager(requireContext(), 3);
        rcvEmoji.setLayoutManager(manager);
        rcvEmoji.setAdapter(emojiAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emoji_bear, container, false);
        init(view);
        setUpData(nameEmoji);
        return view;
    }

    private void init(View view) {
        rcvEmoji = view.findViewById(R.id.rcvEmoji);
    }

    public void getEmoji(ICallBackItem callBack){
        this.callBack = callBack;
    }
}