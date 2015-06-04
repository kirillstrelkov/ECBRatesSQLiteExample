package ee.kirill.ecbratessqliteexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {

    public static final String TAG = "DbHandler";
    public static final String TABLE = "currencies";
    private final HandleXML handleXML;

    public DbHandler(Context context) {
        super(context, "ecb_rates", null, 1);
        handleXML = new HandleXML(context, "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "onCreate");
        db.execSQL("create table " + TABLE + " ("
                + "id integer primary key autoincrement,"
                + Currency.CURRENCY + " text,"
                + Currency.RATE + " text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(TAG, "onUpgrade");
    }

    public void updateDb() {
        Log.v(TAG, "updateDb");
        List<Currency> currencies = handleXML.getCurrenciesFromInternet();
        updateCurrencies(currencies);
    }

    public List<Currency> getCurrencies() {
        Log.v(TAG, "getCurrencies");
        List<Currency> currencies = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE, null, null, null, null, null, null);
        cursor.moveToFirst();

        do {
            String currency = cursor.getString(1);
            String rate = cursor.getString(2);

            Log.v(TAG, "getCurrency: " + currency + " " + rate);

            currencies.add(new Currency(currency, rate));
        } while (cursor.moveToNext());
        cursor.close();

        return currencies;
    }

    private void updateCurrencies(List<Currency> currencies) {
        SQLiteDatabase database = this.getWritableDatabase();
        for (Currency currency : currencies) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(Currency.CURRENCY, currency.getCurrency());
            contentValues.put(Currency.RATE, currency.getRate());

            Log.v(TAG, String.format("inserting... %s %s", currency.getCurrency(), currency.getRate()));

            database.insert(TABLE, null, contentValues);
        }
    }
}
