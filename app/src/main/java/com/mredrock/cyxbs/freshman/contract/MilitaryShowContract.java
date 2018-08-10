package com.mredrock.cyxbs.freshman.contract;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.mredrock.cyxbs.freshman.bean.MilitaryShow;

public class MilitaryShowContract  {

    public interface IMilitaryShowModel extends BaseContract.ISomethingModel{
        void setItem(MilitaryShow bean, LoadCallBack callBack);
        void error(String str,LoadCallBack callBack);
    }

    public interface IMilitaryShowView extends BaseContract.ISomethingView{
        RecyclerView getPhotoRV();
        RecyclerView getVideoRV();
    }


}
