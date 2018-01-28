package com.djavid.bitcoinrate.core;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseDialogFragment extends DialogFragment {

    private Unbinder unbinder;

    public abstract int getLayoutId();

    public abstract View setupView(View view);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    public void showError(int errorId) {
        //todo getContext() or App.getContext() test this
        Toast.makeText(getContext(), getString(errorId), Toast.LENGTH_SHORT).show();
    }

}
