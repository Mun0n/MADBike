package org.drunkcode.madbike.ui.home.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseFragment;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

import butterknife.InjectView;
import butterknife.OnClick;

public class AboutUsFragment extends BaseFragment {

    @InjectView(R.id.imageView)
    ImageView imageView;
    @InjectView(R.id.madbikeTextView)
    TextView madbikeTextView;

    public static AboutUsFragment newInstance(Bundle arguments) {
        AboutUsFragment f = new AboutUsFragment();
        if (arguments != null) {
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_about_us;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnalyticsManager.getInstance().trackContentView("AboutUs");
        SpannableString content = new SpannableString(madbikeTextView.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        madbikeTextView.setText(content);
    }

    @OnClick(R.id.madbikeTextView)
    public void onMadBikePressed(){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("twitter://user?screen_name=madbikeapp"));
            startActivity(intent);

        }catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/#!/madbikeapp")));
        }
    }
}
