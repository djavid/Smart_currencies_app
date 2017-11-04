package com.djavid.bitcoinrate.core;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.*;
import android.view.View;


import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;

/**
 * Created by djavid on 03.11.17.
 */


public abstract class BaseDialogFragment extends DialogFragment {

    protected Realm realm;
    private Unbinder unbinder;

    public abstract int getLayoutId();

    public abstract View setupView(View view);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);

        return setupView(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
