package lm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import lm.context.ContextHelper;

/**
 * Created by limin on 2016-01-25.
 */
public abstract class DatabaseHelper extends ContextHelper {
	private final SQLiteOpenHelper mOpenHelper;

	private SQLiteDatabase mDatabase;

	public DatabaseHelper(Context context) {
		super(context);
		mOpenHelper = onCreateHelper(context);
	}

	protected abstract SQLiteOpenHelper onCreateHelper(Context context);

	public synchronized SQLiteDatabase openDatabase() {
		mDatabase = mOpenHelper.getWritableDatabase();
		mDatabase.beginTransaction();
		return mDatabase;
	}

	public synchronized void closeDatabase() {
		closeDatabase(true);
	}

	public synchronized void closeDatabase(boolean success) {
		if(success) {
			mDatabase.setTransactionSuccessful();
		}

		mDatabase.endTransaction();

		mDatabase.close();
	}
}
