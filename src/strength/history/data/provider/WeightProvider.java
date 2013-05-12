package strength.history.data.provider;

import java.util.ArrayList;

import strength.history.data.service.local.LocalWeightService;
import strength.history.data.structure.Weight;

/**
 * Provides data mappings for the weight service
 * 
 * @param <T>
 */
public class WeightProvider<T extends WeightProvider.Events> extends
		Provider<Weight> {
	public interface Events {
		public void deleteCallback(Weight e, boolean ok);

		public void insertCallback(Weight e, boolean ok);

		public void weightQueryCallback(ArrayList<Weight> e, boolean ok);

		public void updateCallback(Weight e, boolean ok);
	}

	public interface Provides {
		public void delete(Weight e);

		public void insert(Weight e);

		public void query(Weight e);

		public void update(Weight e);
	}

	private T t;

	public WeightProvider(T t) {
		this.t = t;
	}

	@Override
	protected Class<?> getLocalServiceClass() {
		return LocalWeightService.class;
	}

	@Override
	protected String getDataFieldName() {
		return LocalWeightService.WEIGHT;
	}

	@Override
	protected void deleteCallback(Weight e, boolean ok) {
		t.deleteCallback(e, ok);
	}

	@Override
	protected void insertCallback(Weight e, boolean ok) {
		t.insertCallback(e, ok);
	}

	@Override
	protected void queryCallback(ArrayList<Weight> e, boolean ok) {
		t.weightQueryCallback(e, ok);
	}

	@Override
	protected void updateCallback(Weight e, boolean ok) {
		t.updateCallback(e, ok);
	}
}