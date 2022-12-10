package com.example.dondesang.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dondesang.R;
import com.example.dondesang.databinding.FragmentNewsBinding;

import java.util.Calendar;
import java.util.Date;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final EditText editText = binding.userNews;
        final Button sendButton = binding.sendButton;
        LinearLayout newsLayout = binding.newsZone;
        ScrollView scrollView = binding.scrollView;

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextIsEmpty(editText)){
                    String newsContent = editText.getText().toString();
                    View newPost = getLayoutInflater().inflate(R.layout.news_card, null);
                    newsLayout.addView(newPost);
                    TextView textView = (TextView) newPost.findViewById(R.id.messageView);
                    textView.setText(newsContent);
                    TextView dateView = (TextView) newPost.findViewById(R.id.dateView);
                    dateView.setText(getCurrentDate());
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    editText.getText().clear();
                    editText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private boolean editTextIsEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    private String getCurrentDate() {
        Date currentTimeDate = Calendar.getInstance().getTime();
        return currentTimeDate.toString();
    }
}