package ro.marianperca.receiptmanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

import ro.marianperca.receiptmanager.database.ReceiptsDatabase;
import ro.marianperca.receiptmanager.database.model.Receipt;

public class MainActivity extends AppCompatActivity {

    ReceiptListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        mListAdapter = new ReceiptListAdapter();
        mListAdapter.setListener(new ReceiptListAdapter.ListActionListener() {
            @Override
            public void deleteReceipt(Receipt r) {
                (new DeleteReceiptTask(MainActivity.this, new DeleteReceiptTask.AsyncDeleteResponse() {
                    @Override
                    public void deleteProcessFinish(Boolean response) {
                        Snackbar.make(findViewById(android.R.id.content), "Receipt deleted", Snackbar.LENGTH_SHORT).show();
                    }
                })).execute(r);
            }
        });

        recyclerView.setAdapter(mListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.add_receipt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddReceiptActivity.class);
                startActivityForResult(i, 1);
            }
        });

        // get all receipts
        (new FetchReceiptsTask(this, new FetchReceiptsTask.AsyncFetchResponse() {
            @Override
            public void fetchProcessFinish(List<Receipt> receipts) {
                mListAdapter.swap(receipts);
            }
        })).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode > 0) {
            (new FetchReceiptsTask(this, new FetchReceiptsTask.AsyncFetchResponse() {
                @Override
                public void fetchProcessFinish(List<Receipt> receipts) {
                    mListAdapter.swap(receipts);
                }
            })).execute();
        }
    }

    private static class FetchReceiptsTask extends AsyncTask<Void, Void, List<Receipt>> {

        private WeakReference<MainActivity> activityReference;
        private AsyncFetchResponse listener = null;

        FetchReceiptsTask(MainActivity activityReference, AsyncFetchResponse listener) {
            this.activityReference = new WeakReference<>(activityReference);
            this.listener = listener;
        }

        @Override
        protected List<Receipt> doInBackground(Void... voids) {
            return ReceiptsDatabase.getInstance(activityReference.get()).receiptDao().getAll();
        }

        @Override
        protected void onPostExecute(List<Receipt> receipts) {
            listener.fetchProcessFinish(receipts);
        }

        public interface AsyncFetchResponse {
            void fetchProcessFinish(List<Receipt> receipts);
        }
    }

    private static class DeleteReceiptTask extends AsyncTask<Receipt, Void, Boolean> {
        private WeakReference<MainActivity> activityReference;
        private AsyncDeleteResponse listener = null;

        DeleteReceiptTask(MainActivity activityReference, AsyncDeleteResponse listener) {
            this.activityReference = new WeakReference<>(activityReference);
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Receipt... receipts) {
            for (Receipt receipt : receipts) {
                ReceiptsDatabase.getInstance(activityReference.get()).receiptDao().delete(receipt);
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean response) {
            listener.deleteProcessFinish(true);
        }

        public interface AsyncDeleteResponse {
            void deleteProcessFinish(Boolean response);
        }
    }
}
