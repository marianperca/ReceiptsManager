package ro.marianperca.receiptmanager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ro.marianperca.receiptmanager.database.ReceiptsDatabase;
import ro.marianperca.receiptmanager.database.model.Receipt;

public class AddReceiptActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    protected static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
    private DatePickerDialog datePickerDialog;
    private EditText mValue;
    private EditText mDate;
    private EditText mStore;
    private Date mSelectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt);

        // create the dialog for date picker
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // get views references
        mValue = findViewById(R.id.value);
        mStore = findViewById(R.id.store);
        mDate = findViewById(R.id.date);

        // on date click, open the dialog for datepicker
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                datePickerDialog.show();
            }
        });

        findViewById(R.id.btn_add).setOnClickListener(this);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        mSelectedDate = calendar.getTime();
        mDate.setText(simpleDateFormat.format(mSelectedDate));
    }

    private void hideKeyboard() {
        // Check if no view has focus
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View view) {
        String value = mValue.getText().toString();
        String store = mStore.getText().toString();

        if (TextUtils.isEmpty(value) || mSelectedDate == null || TextUtils.isEmpty(store)) {
            Toast.makeText(this, "Please fill in all receipt details", Toast.LENGTH_LONG).show();
            return;
        }

        Receipt receipt = new Receipt(mSelectedDate, Double.valueOf(value), store);
        (new InsertTask(this, new InsertTask.AsyncResponse() {
            @Override
            public void processFinish(Boolean response) {
                AddReceiptActivity.this.setResult(1);
                AddReceiptActivity.this.finish();
            }
        })).execute(receipt);
    }

    private static class InsertTask extends AsyncTask<Receipt, Void, Boolean> {

        private WeakReference<AddReceiptActivity> activityReference;
        private AsyncResponse listener = null;

        InsertTask(AddReceiptActivity activityReference, AsyncResponse listener) {
            this.activityReference = new WeakReference<>(activityReference);
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Receipt... receipts) {
            for (Receipt receipt : receipts) {
                ReceiptsDatabase.getInstance(activityReference.get()).receiptDao().insert(receipt);
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean response) {
            listener.processFinish(true);
        }

        public interface AsyncResponse {
            void processFinish(Boolean response);
        }
    }
}
