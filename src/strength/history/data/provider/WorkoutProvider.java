package strength.history.data.provider;

import java.util.Collection;
import java.util.LinkedHashSet;

import android.content.Context;

import strength.history.data.DataListener;
import strength.history.data.service.local.LocalWorkoutService;
import strength.history.data.structure.Workout;

public class WorkoutProvider extends Provider<Workout> {
	public interface Events {
		public void deleteCallback(Workout e, boolean ok);

		public void insertCallback(Workout e, boolean ok);

		public void workoutQueryCallback(Collection<Workout> e, boolean done);

		public void updateCallback(Workout e, boolean ok);
	}

	public interface Provides {
		public void delete(Workout e, Context context);

		public void insert(Workout e, Context context);

		public void query(Workout e, Context context);

		public void stop(Workout e, Context context);

		public void update(Workout e, Context context);
	}

	private LinkedHashSet<Events> listeners = new LinkedHashSet<Events>();

	@Override
	public void tryAddListener(DataListener dataListener) {
		if (dataListener instanceof Events) {
			Events e = (Events) dataListener;
			e.workoutQueryCallback(data, false); // Initial values
			listeners.add(e);
		}
	}

	@Override
	public void tryRemoveListener(DataListener dataListener) {
		if (dataListener instanceof Events) {
			listeners.remove((Events) dataListener);
		}
	}

	@Override
	protected Class<?> getLocalServiceClass() {
		return LocalWorkoutService.class;
	}

	@Override
	protected String getDataFieldName() {
		return LocalWorkoutService.WORKOUT;
	}

	@Override
	protected void deleteCallback(Workout e, boolean ok) {
		for (Events t : listeners) {
			t.deleteCallback(e, ok);
		}
	}

	@Override
	protected void insertCallback(Workout e, boolean ok) {
		for (Events t : listeners) {
			t.insertCallback(e, ok);
		}
	}

	@Override
	protected void queryCallback(Collection<Workout> e, boolean done) {
		for (Events t : listeners) {
			t.workoutQueryCallback(e, done);
		}
	}

	@Override
	protected void updateCallback(Workout e, boolean ok) {
		for (Events t : listeners) {
			t.updateCallback(e, ok);
		}
	}
}
