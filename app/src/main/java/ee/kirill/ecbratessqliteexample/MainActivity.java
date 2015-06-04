package ee.kirill.ecbratessqliteexample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = "ECBRatesActivity";

    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DbHandler(this);

        List<Currency> currencies = dbHandler.getCurrencies();
        if (currencies.size() > 0) {
            updateListView(currencies);
        }
    }

    private void updateListView(List<Currency> currencies) {
        String[] from = new String[]{Currency.CURRENCY, Currency.RATE};
        int[] to = {R.id.textViewCurrency, R.id.textViewRate};

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new SimpleAdapter(this, currencies, R.layout.currreny_list_item, from, to));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.update_xml) {
            ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "Updating DB", "Please wait while updating DB...");
            dbHandler.updateDb();
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "DB was updated", Toast.LENGTH_LONG);

            updateListView(dbHandler.getCurrencies());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
