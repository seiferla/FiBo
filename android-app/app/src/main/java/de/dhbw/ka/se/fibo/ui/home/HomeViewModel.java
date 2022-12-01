package de.dhbw.ka.se.fibo.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.dhbw.ka.se.fibo.models.Cashflow;

public class HomeViewModel extends ViewModel {

    // TODO understand how to use HomeViewModel
    private LiveData<List<Cashflow>> mList;

    public LiveData<List<Cashflow>> getListView() {
        if (null == mList) {
            mList = new MutableLiveData<>();
        }
        return mList;
    }

    public void setmList(LiveData<List<Cashflow>> mList) {
        this.mList = mList;
    }
}