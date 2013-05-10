package strength.history.data.service.local;

import java.util.ArrayList;

import strength.history.data.db.WeightDBHelper;
import strength.history.data.service.LocalServiceBase;
import strength.history.data.structure.Weight;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * Local weight service
 */
public class LocalWeightService extends
		LocalServiceBase<Weight, WeightDBHelper> {
	/**
	 * Name of the weight data passed with the intent
	 */
	public static final String WEIGHT = "WEIGHT";

	/**
	 * Constructor
	 */
	public LocalWeightService() {
		super("LocalWeightService");
	}

	@Override
	protected WeightDBHelper getDB() {
		return WeightDBHelper.getInstance(getApplicationContext());
	}

	@Override
	protected int getArg1() {
		return Service.WEIGHT.ordinal();
	}

	@Override
	protected void delete(Weight e, Messenger messenger) {
		if (e != null) {
			WeightDBHelper db = WeightDBHelper
					.getInstance(getApplicationContext());
			Message msg = Message.obtain();
			msg.arg1 = getArg1();
			msg.arg2 = Request.DELETE.ordinal();

			boolean deleted = db.delete(e);
			for (int i = 0; i < DB_TRIES && !deleted; i++) {
				deleted = db.delete(e);
			}
			msg.what = deleted ? 1 : 0;
			msg.obj = e;

			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e("LocalWeightService", "Failed to send message");
			}
		}
	}

	@Override
	protected void insert(Weight e, Messenger messenger) {
		if (e != null) {
			WeightDBHelper db = WeightDBHelper
					.getInstance(getApplicationContext());
			Message msg = Message.obtain();
			msg.arg1 = getArg1();
			msg.arg2 = Request.INSERT.ordinal();

			boolean inserted = db.insert(e);
			for (int i = 0; i < DB_TRIES && !inserted; i++) {
				inserted = db.insert(e);
			}
			msg.what = inserted ? 1 : 0;
			msg.obj = e;

			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e("LocalWeightService", "Failed to send message");
			}
		}
	}

	@Override
	protected void query(Messenger messenger) {
		// Divide into smaller queries
		for (int offset = 0; !query_interrupt; offset += QUERY_LIMIT) {
			WeightDBHelper db = WeightDBHelper
					.getInstance(getApplicationContext());
			Message msg = Message.obtain();
			msg.arg1 = getArg1();
			msg.arg2 = Request.QUERY.ordinal();

			ArrayList<Weight> res = db.query(offset, QUERY_LIMIT);
			msg.what = 1;
			msg.obj = res;

			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e("LocalWeightService", "Failed to send message");
			}

			if (res.size() < QUERY_LIMIT) {
				break;
			}
		}
	}

	@Override
	protected void update(Weight e, Messenger messenger) {
		if (e != null) {
			WeightDBHelper db = WeightDBHelper
					.getInstance(getApplicationContext());
			Message msg = Message.obtain();
			msg.arg1 = getArg1();
			msg.arg2 = Request.UPDATE.ordinal();

			boolean updated = db.update(e);
			for (int i = 0; i < DB_TRIES && !updated; i++) {
				updated = db.update(e);
			}
			msg.what = updated ? 1 : 0;
			msg.obj = e;

			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e("LocalWeightService", "Failed to send message");
			}
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("LocalWeightService", "onHandleIntent");

		Request request = (Request) intent.getSerializableExtra(REQUEST);
		Messenger messenger = intent.getParcelableExtra(MESSENGER);
		if (request != null && messenger != null) {
			switch (request) {
			case DELETE:
				delete((Weight) intent.getParcelableExtra(WEIGHT), messenger);
				break;
			case INSERT:
				insert((Weight) intent.getParcelableExtra(WEIGHT), messenger);
				break;
			case QUERY:
				query(messenger);
				break;
			case UPDATE:
				update((Weight) intent.getParcelableExtra(WEIGHT), messenger);
				break;
			}
		}
	}
}
