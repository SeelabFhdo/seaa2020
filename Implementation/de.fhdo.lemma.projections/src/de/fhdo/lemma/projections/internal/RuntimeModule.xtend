package de.fhdo.lemma.projections.internal

import org.eclipse.xtext.service.AbstractGenericModule
import com.google.inject.Binder
import org.eclipse.xtext.generator.IGenerator2
import com.google.inject.name.Names

class RuntimeModule extends AbstractGenericModule {
	def configureDataDslExtractor(Binder binder) {
		binder.bind(IGenerator2)
			.annotatedWith(Names.named("DataDslExtractor"))
			.to(DataDslExtractor)
	}
	
	def configureServiceDslExtractor(Binder binder) {
		binder.bind(IGenerator2)
			.annotatedWith(Names.named("ServiceDslExtractor"))
			.to(ServiceDslExtractor)
	}
}