package fi.breakwaterworks.model.factory;

import fi.breakwaterworks.model.SetRepsWeight;
import fi.breakwaterworks.model.request.SetRepsWeightJson;

public class SetRepsWeightJsonFactory {
	public static SetRepsWeightJson createInstance(SetRepsWeight srw) {
		return new SetRepsWeightJson(srw);
	}
}