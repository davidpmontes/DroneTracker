package com.example.dronetracker2.ui.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DetailsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DetailsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is details fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}