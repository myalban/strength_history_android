package strength.history.ui.history;

import java.util.Collection;
import java.util.Comparator;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import strength.history.R;
import strength.history.data.DataListener;
import strength.history.data.DataProvider;
import strength.history.data.SortedList;
import strength.history.data.provider.WeightProvider;
import strength.history.data.provider.WorkoutDataProvider;
import strength.history.data.provider.WorkoutProvider;
import strength.history.data.structure.Weight;
import strength.history.data.structure.Workout;
import strength.history.data.structure.WorkoutData;
import strength.history.ui.custom.CustomTitleActivity;

public class HistoryActivity extends CustomTitleActivity implements
		WeightProvider.Events, WorkoutDataProvider.Events,
		WorkoutProvider.Events {
	private HistoryAdapter historyAdapter;
	private SortedList<Workout> workouts = new SortedList<Workout>(
			new Comparator<Workout>() {
				@Override
				public int compare(Workout lhs, Workout rhs) {
					return lhs.compareTo(rhs);
				}
			}, true);
	private SortedList<HistoryEvent> historyEvents = new SortedList<HistoryEvent>(
			new Comparator<HistoryEvent>() {
				@Override
				public int compare(HistoryEvent lhs, HistoryEvent rhs) {
					return Long.valueOf(rhs.getTime()).compareTo(lhs.getTime());
				}
			});
	private DataProvider dataProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.history);
		setCustomBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ListView listViewHistory = (ListView) findViewById(R.id.listViewHistory);
		listViewHistory.setEmptyView(findViewById(R.id.textViewEmptyList));
		historyAdapter = new HistoryAdapter(this, historyEvents, workouts);
		listViewHistory.setAdapter(historyAdapter);
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_history;
	}

	@Override
	protected void onResume() {
		super.onResume();
		historyEvents.clear();
		workouts.clear();
		Context c = getApplicationContext();
		dataProvider = DataListener.add(this);
		dataProvider.queryWorkout(c);
		dataProvider.queryWorkoutData(c);
		dataProvider.queryWeight(c);
		// TODO Fix progress bar
	}

	@Override
	protected void onPause() {
		super.onPause();
		DataListener.remove(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// TODO Is it bad to stop?
		Context c = getApplicationContext();
		dataProvider.stop((Workout) null, c);
		dataProvider.stop((WorkoutData) null, c);
		dataProvider.stop((Weight) null, c);
	}

	@Override
	public void deleteCallback(Workout e, boolean ok) {
		if (ok) {
			workouts.remove(e);
		}
	}

	@Override
	public void insertCallback(Workout e, boolean ok) {
		if (ok) {
			workouts.add(e);
		}
	}

	@Override
	public void updateCallback(Workout old, Workout e, boolean ok) {
		if (ok) {
			workouts.remove(old);
			workouts.add(e);
		}
	}

	@Override
	public void workoutQueryCallback(Collection<Workout> e, boolean done) {
		workouts.addAll(e);
		historyAdapter.notifyDataSetChanged();
	}

	@Override
	public void deleteCallback(WorkoutData e, boolean ok) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertCallback(WorkoutData e, boolean ok) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCallback(WorkoutData old, WorkoutData e, boolean ok) {
		// TODO Auto-generated method stub

	}

	@Override
	public void workoutDataQueryCallback(Collection<WorkoutData> e, boolean done) {
		// TODO Auto-generated method stub
		for (WorkoutData d : e) {
			historyEvents.add(new HistoryEvent(d));
		}
		historyAdapter.notifyDataSetChanged();
	}

	@Override
	public void deleteCallback(Weight e, boolean ok) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertCallback(Weight e, boolean ok) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCallback(Weight old, Weight e, boolean ok) {
		// TODO Auto-generated method stub

	}

	@Override
	public void weightQueryCallback(Collection<Weight> e, boolean done) {
		// TODO Auto-generated method stub
		for (Weight w : e) {
			historyEvents.add(new HistoryEvent(w));
		}
		historyAdapter.notifyDataSetChanged();
	}
}
