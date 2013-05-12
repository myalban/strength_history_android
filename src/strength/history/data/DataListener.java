package strength.history.data;

import android.app.Activity;
import android.os.Bundle;

/**
 * Base Activity that acts as a listener
 */
public abstract class DataListener extends Activity implements
		DataProvider.Events {
	/**
	 * Data provider object
	 */
	protected static final DataProvider data = new DataProvider();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		data.setListener(this);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		data.setListener(null);
		super.onDestroy();
	}
}