package com.datnt.textart.fragment.emoji;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.textart.R;
import com.datnt.textart.activity.edit.EditActivity;
import com.datnt.textart.adapter.EmojiAdapter;
import com.datnt.textart.data.DataEmoji;

public class EmojiFaceFragment extends Fragment {

    private String nameEmoji;
    private EmojiAdapter emojiAdapter;
    private RecyclerView rcvEmoji;

    public static EmojiFaceFragment newInstance(String nameEmoji) {
        EmojiFaceFragment fragment = new EmojiFaceFragment();
        Bundle args = new Bundle();
        args.putString("face", nameEmoji);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            nameEmoji = getArguments().getString("face");
        }
    }

    private void setUpData(String nameEmoji) {
        emojiAdapter = new EmojiAdapter(requireContext(), (o, pos) -> {

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
}