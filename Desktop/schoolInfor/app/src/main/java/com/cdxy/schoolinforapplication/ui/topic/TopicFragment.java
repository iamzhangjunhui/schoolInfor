package com.cdxy.schoolinforapplication.ui.topic;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopicFragment extends BaseFragment {


    public TopicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topic, container, false);
    }

    @Override
    public void init() {

    }

}
