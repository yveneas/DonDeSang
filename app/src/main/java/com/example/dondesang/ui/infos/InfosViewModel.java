package com.example.dondesang.ui.infos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InfosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public InfosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Infos fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}