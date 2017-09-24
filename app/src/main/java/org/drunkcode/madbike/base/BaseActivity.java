package org.drunkcode.madbike.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.drunkcode.madbike.R;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.inject(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getActivityTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(getActivityHomeAsUpEnabled());
        }
    }

    protected abstract int getLayoutResource();

    protected abstract String getActivityTitle();

    protected abstract boolean getActivityHomeAsUpEnabled();

}
