package de.fhdo.lemma.projections.internal

import org.eclipse.xtext.generator.GeneratorContext
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtend.lib.annotations.Accessors

@FinalFieldsConstructor
class ExtractorGeneratorContext extends GeneratorContext {
	@Accessors(PUBLIC_GETTER)
	val String targetFileName	
}