package com.testing.hoan.at_mobile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Hoan on 12/2/2016.
 */
public class HotNewsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=(View) inflater.inflate(R.layout.hot_news,null);
        TextView tv=(TextView) root.findViewById(R.id.new_title);
        return root;
    }
}
