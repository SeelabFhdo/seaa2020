package de.fhdo.lemma.projections.internal

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import org.eclipse.xtext.generator.AbstractGenerator
import de.fhdo.lemma.data.DataModel
import de.fhdo.lemma.data.Context
import java.util.List
import de.fhdo.lemma.data.ComplexType
import de.fhdo.lemma.data.DataStructure
import org.eclipse.emf.common.util.Enumerator
import de.fhdo.lemma.data.DataStructureFeature
import de.fhdo.lemma.data.DataFieldFeature
import de.fhdo.lemma.data.DataField
import de.fhdo.lemma.data.PrimitiveType
import de.fhdo.lemma.data.DataOperation
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.emf.ecore.EObject
import de.fhdo.lemma.data.Type
import de.fhdo.lemma.data.DataOperationParameter
import de.fhdo.lemma.data.DataOperationFeature
import de.fhdo.lemma.data.ListType

class DataDslExtractor extends AbstractGenerator {
	static val FEATURE_TO_TEXT_MAPPINGS = #{
		DataStructureFeature.AGGREGATE -> "aggregate",
		DataStructureFeature.APPLICATION_SERVICE -> "applicationService",
		DataStructureFeature.DOMAIN_EVENT -> "domainEvent",		
		DataStructureFeature.DOMAIN_SERVICE -> "domainService",		
		DataStructureFeature.ENTITY -> "entity",
		DataStructureFeature.FACTORY -> "factory",
		DataStructureFeature.INFRASTRUCTURE_SERVICE -> "infrastructureService",
		DataStructureFeature.REPOSITORY -> "repository",
		DataStructureFeature.SERVICE -> "service",
		DataStructureFeature.SPECIFICATION -> "specification",
		DataStructureFeature.VALUE_OBJECT -> "valueObject",
		
		DataFieldFeature.IDENTIFIER -> "identifier",
		DataFieldFeature.NEVER_EMPTY -> "neverEmpty",
		DataFieldFeature.PART -> "part",
		
		DataOperationFeature.CLOSURE -> "closure",
		DataOperationFeature.IDENTIFIER -> "identifier",
		DataOperationFeature.SIDE_EFFECT_FREE -> "sideEffectFree",
		DataOperationFeature.VALIDATOR -> "validator"
	}
	
	override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {
		val targetFileName = (context as ExtractorGeneratorContext).targetFileName
		val domainModel = input.contents.get(0) as DataModel		
		fsa.generateFile(targetFileName, domainModel.contexts.generateContexts)
	}
	
	private def generateContexts(List<Context> contexts) {
		return '''
			«FOR c : contexts SEPARATOR '\n'»
				context «c.name» {
				«FOR t : c.complexTypes SEPARATOR '\n'»					
					«'\t'»«t.generateComplexTypeDefinition»
				«ENDFOR»
				}
			«ENDFOR»
		'''
	}
	
	private def dispatch generateComplexTypeDefinition(ListType list) {
		return '''
			list «list.name» {
				«IF !list.dataFields.empty»
					«FOR f : list.dataFields SEPARATOR ','»
					«'\t'»«f.generateDataField»
					«ENDFOR»
				«ELSE»
					«'\t'»«list.primitiveType.generateTypeReference»
				«ENDIF»
			«'\t'»}'''
	}

	private def dispatch generateComplexTypeDefinition(DataStructure structure) {
		val preamble = new StringBuilder
		preamble.append(structure.features.generateFeatures)
		preamble.append(" ")
		preamble.append(structure.generateSuperReference)
		var preambleString = preamble.toString
		if (!preambleString.empty)
			preambleString = ''' «preambleString.trim» '''
		
		if (structure.dataFields.empty && structure.operations.empty)
			return '''structure «structure.name»«preambleString»{}'''

		val lastFieldIndex = structure.dataFields.size - 1
		var currentFieldIndex = 0
		return '''
			structure «structure.name»«preambleString»{
				«FOR f : structure.dataFields»
					«IF lastFieldIndex == currentFieldIndex++»
						«'\t'»«f.generateDataField»«IF !structure.operations.empty»,«ENDIF»
					«ELSE»
						«'\t'»«f.generateDataField»,
					«ENDIF»
				«ENDFOR»
				«IF !structure.dataFields.empty && !structure.operations.empty»«"\n"»«ENDIF»
				«FOR o : structure.operations SEPARATOR ','»
					«'\t'»«o.generateDataOperation»
				«ENDFOR»
			«'\t'»}'''
	}
		
	private def <E extends Enumerator> generateFeatures(List<E> features) {
		val supportedFeatures = FEATURE_TO_TEXT_MAPPINGS.filter[f, t | features.contains(f)]
		if (supportedFeatures.empty)
			return ""

		return '''<«FOR f : supportedFeatures.values SEPARATOR ', '»«f»«ENDFOR»>'''
	}
	
	private def generateSuperReference(DataStructure structure) {
		return if (structure.^super !== null)
				'''extends «structure.^super.name»'''
			else
				""
	}
	
	private def generateDataField(DataField dataField) {
		val effectiveType = dataField.effectiveType
		val generatedType = if (effectiveType === null)
				"unspecified"
			else if (dataField.hasTypeFromOtherContext(effectiveType))
				'''«dataField.complexType.context».«effectiveType.generateTypeReference»'''
			else
				effectiveType.generateTypeReference
		
		val preamble = new StringBuffer
		if (dataField.hidden)
			preamble.append('hide ')
		if (dataField.immutable)
			preamble.append('immutable ')
		var preambleString = preamble.toString
		
		val features = dataField.features.generateFeatures
		return '''«preambleString»«generatedType» «dataField.name»''' + 
			'''«IF !features.empty» «features»«ENDIF»'''
	}
	
	private def hasTypeFromOtherContext(EObject container, Type type) {
		if (!(type instanceof ComplexType))
			return false
		
		val typeContext = EcoreUtil2.getContainerOfType(type, Context)
		val parentContext = EcoreUtil2.getContainerOfType(type, Context)
		return typeContext !== parentContext
	}	
	
	private def dispatch generateTypeReference(PrimitiveType primitiveType) {
		return primitiveType.typeName
	}

	private def dispatch generateTypeReference(ComplexType complexType) {
		return complexType.name
	}
	
	private def generateDataOperation(DataOperation dataOperation) {
		val effectiveReturnType = dataOperation.primitiveOrComplexReturnType
		val generatedType = if (effectiveReturnType === null)
				"procedure"
			else if (dataOperation.hasTypeFromOtherContext(effectiveReturnType))
				'''function «dataOperation.complexReturnType.context».''' + 
				effectiveReturnType.generateTypeReference
			else
				'''function «effectiveReturnType.generateTypeReference»'''
			
		val generatedParameters = dataOperation.parameters.map[generateDataOperationParameter]
		val generatedFeatures = dataOperation.features.generateFeatures
		return '''«generatedType» «dataOperation.name»''' +
			'''(«generatedParameters.join(", ")»)''' +
			'''«IF !generatedFeatures.empty»«generatedFeatures»«ENDIF»'''	
	}
	
	private def generateDataOperationParameter(DataOperationParameter parameter) {
		val effectiveType = parameter.effectiveType
		val generatedType = if (effectiveType === null)
				"unspecified"
			else if (parameter.hasTypeFromOtherContext(effectiveType))
				'''«parameter.complexType.context».«effectiveType.generateTypeReference»'''
			else
				effectiveType.generateTypeReference
				
		return '''«generatedType» «parameter.name»'''
	}
}