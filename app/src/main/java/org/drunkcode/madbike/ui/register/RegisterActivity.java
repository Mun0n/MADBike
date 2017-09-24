package org.drunkcode.madbike.ui.register;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.aeat.valida.Validador;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseActivity;
import org.drunkcode.madbike.common.dialogs.options.DialogListener;
import org.drunkcode.madbike.common.dialogs.options.DialogManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.InjectView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements DialogListener {

    @InjectView(R.id.dateButton)
    public Button dateButton;
    @InjectView(R.id.documenttextInputLayout)
    TextInputLayout documentInputLayout;
    @InjectView(R.id.documentEditText)
    public EditText documentEditText;
    @InjectView(R.id.nameEditText)
    public EditText nameEditText;
    @InjectView(R.id.firstSurnameEditText)
    public EditText firstSurnameEditText;
    @InjectView(R.id.secondSurnameEditText)
    public EditText secondSurnameEditText;
    @InjectView(R.id.emailEditText)
    public EditText emailEditText;
    @InjectView(R.id.telephoneEditText)
    public EditText telephoneEditText;
    @InjectView(R.id.postalEditText)
    public EditText postalEditText;
    @InjectView(R.id.nameInputLayout)
    public TextInputLayout nameInputLayout;
    @InjectView(R.id.firstSurnameInputLayout)
    public TextInputLayout firstSurnameInputLayout;
    @InjectView(R.id.secondSurnameInputLayout)
    public TextInputLayout secondSurnameInputLayout;
    @InjectView(R.id.emailInputLayout)
    public TextInputLayout emailInputLayout;
    @InjectView(R.id.telephoneInputLayout)
    public TextInputLayout telephoneInputLayout;
    @InjectView(R.id.postalInputLayout)
    public TextInputLayout postalInputLayout;

    private SimpleDateFormat dateFormatter;

    private DatePickerDialog fromDatePickerDialog;
    private static final long FOURTEEN_YEARS = (long) 1000 * 60 * 60 * 24 * 365 * 14;
    private DialogManager dialogManager;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_register;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.register_toolbar);
    }

    @Override
    protected boolean getActivityHomeAsUpEnabled() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogManager = new DialogManager(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_register_confirm, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_done:
                if (checkDataForm()) {
                    Toast.makeText(this, getString(R.string.register_not_working), Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkDataForm() {
        if (documentEditText.getText().toString().trim().isEmpty()) {
            setErrorStyle(documentEditText, documentInputLayout, R.string.error_no_document);
            return false;
        } else {
            documentInputLayout.setErrorEnabled(false);
        }
        if (nameEditText.getText().toString().trim().isEmpty()) {
            setErrorStyle(nameEditText, nameInputLayout, R.string.error_no_name);
            return false;
        } else {
            nameInputLayout.setErrorEnabled(false);
        }
        if (firstSurnameEditText.getText().toString().trim().isEmpty()) {
            setErrorStyle(firstSurnameEditText, firstSurnameInputLayout, R.string.error_no_first_name);
            return false;
        } else {
            firstSurnameInputLayout.setErrorEnabled(false);
        }
        if (secondSurnameEditText.getText().toString().trim().isEmpty()) {
            setErrorStyle(secondSurnameEditText, secondSurnameInputLayout, R.string.error_no_second_name);
            return false;
        } else {
            secondSurnameInputLayout.setErrorEnabled(false);
        }
        if (telephoneEditText.getText().toString().trim().isEmpty()) {
            setErrorStyle(telephoneEditText, telephoneInputLayout, R.string.error_no_telephone);
            return false;
        } else {
            telephoneInputLayout.setErrorEnabled(false);
        }
        if (emailEditText.getText().toString().trim().isEmpty()) {
            setErrorStyle(emailEditText, emailInputLayout, R.string.error_no_email);
            return false;
        } else {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                emailInputLayout.setErrorEnabled(false);
            } else {
                setErrorStyle(emailEditText, emailInputLayout, R.string.error_no_email);
                return false;
            }
        }
        if (postalEditText.getText().length() != 5) {
            setErrorStyle(postalEditText, postalInputLayout, R.string.error_no_postal_code);
            return false;
        } else {
            postalInputLayout.setErrorEnabled(false);
        }
        if (dateButton.getText().toString().equalsIgnoreCase(getString(R.string.hint_date))) {
            dateButton.setError(getString(R.string.error_no_date));
        }

        return true;
    }

    private void setErrorStyle(EditText editText, TextInputLayout textInputLayout, int idMessage) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(getString(idMessage));
        requestFocus(editText);
    }

    @OnClick(R.id.dateButton)
    public void onDatePressed() {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Calendar currentTime = Calendar.getInstance();
                if ((currentTime.getTimeInMillis() - FOURTEEN_YEARS) >= c.getTimeInMillis()) {
                    dateButton.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                } else {
                    dialogManager.showPositiveDialog(R.string.age_title, R.string.age_message, android.R.string.ok);
                }
            }

        }
                , newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }

    @Override
    public void onPositivePressed() {
        dialogManager.hideDialog();
    }

    @Override
    public void onNegativePressed() {

    }

    @Override
    public void onNeutralPressed() {

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
