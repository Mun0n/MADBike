package org.drunkcode.madbike.ui.home.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseFragment;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

import butterknife.InjectView;
import butterknife.OnClick;

public class ContactFragment extends BaseFragment {

    @InjectView(R.id.attentionTextView)
    TextView attentionTextView;
    @InjectView(R.id.suggestionsTextView)
    TextView suggestionsTextView;

    public static ContactFragment newInstance(Bundle arguments) {
        ContactFragment f = new ContactFragment();
        if (arguments != null) {
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_contact;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnalyticsManager.getInstance().trackContentView("Contact");
        underlineTextView(attentionTextView);
        underlineTextView(suggestionsTextView);
    }

    private void underlineTextView(TextView textView) {
        SpannableString content = new SpannableString(textView.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
    }

    @OnClick(R.id.madridPhoneTextView)
    public void onMadridPhonePressed() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getString(R.string.number_phone_mad)));
        startActivity(intent);
    }

    @OnClick(R.id.outPhoneTextView)
    public void onOutPhonePressed() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getString(R.string.number_phone_out_mad)));
        startActivity(intent);
    }

    @OnClick(R.id.attentionTextView)
    public void onAttentionPressed() {
        openWeb("http://www.madrid.es/portales/munimadrid/es/Inicio/Ayuntamiento/Linea-Madrid/Oficinas-de-Atencion-al-Ciudadano?vgnextfmt=default&vgnextchannel=5b99cde2e09a4310VgnVCM1000000b205a0aRCRD");
    }

    @OnClick(R.id.suggestionsTextView)
    public void onSuggestionPressed() {
        openWeb("http://www.madrid.es/portales/munimadrid/es/Inicio/Ayuntamiento/Linea-Madrid/Oficinas-de-Atencion-al-Ciudadano?vgnextfmt=default&vgnextchannel=5b99cde2e09a4310VgnVCM1000000b205a0aRCRD");
    }

    private void openWeb(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
