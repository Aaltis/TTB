package fi.breakwaterworks.model.factory;

import fi.breakwaterworks.exception.FactoryValidationException;
import fi.breakwaterworks.model.SetRepsWeight;
import fi.breakwaterworks.model.request.SetRepsWeightJson;

public class SetRepsWeightFactory {
	public static SetRepsWeight createInstance(SetRepsWeightJson srwJson) throws Exception {

		if (srwJson.getReps() == 0) {
			throw new FactoryValidationException("reps are mandatory for setrepsweight. Check capitalisation.");
		}
		if (srwJson.getSet() == 0) {
			throw new FactoryValidationException("sets are mandatory for setrepsweight. Check capitalisation.");
		}
		if (srwJson.getWeight() == 0) {
			throw new FactoryValidationException("weight are mandatory for setrepsweight. Check capitalisation.");
		}
		if (srwJson.getWeightUnit()==null) {
			throw new FactoryValidationException("weightUnit are mandatory for setrepsweight. Check capitalisation.");
		}		
		if (srwJson.getSetType()==null) {
			throw new FactoryValidationException("setType are mandatory for setrepsweight. Check capitalisation.");
		}
		
		return new SetRepsWeight(srwJson);
	}
}
