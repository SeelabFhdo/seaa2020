package de.fhdo.lemma.projections

import com.google.inject.Inject
import com.google.inject.name.Named
import org.eclipse.xtext.generator.IGenerator2
import de.fhdo.lemma.data.DataModel

class DataDslProjections extends Projections<DataModel> {
	@Inject
	@Named("DataDslExtractor")
	IGenerator2 extractor
	
	override extractor() {
		return extractor
	}	
}