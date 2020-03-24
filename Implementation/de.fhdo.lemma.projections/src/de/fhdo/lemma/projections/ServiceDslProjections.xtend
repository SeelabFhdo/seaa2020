package de.fhdo.lemma.projections

import de.fhdo.lemma.service.ServiceModel
import com.google.inject.Inject
import com.google.inject.name.Named
import org.eclipse.xtext.generator.IGenerator2

class ServiceDslProjections extends Projections<ServiceModel> {
	@Inject
	@Named("ServiceDslExtractor")
	IGenerator2 extractor	
	
	override extractor() {
		return extractor
	}	
}