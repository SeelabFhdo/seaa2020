package de.fhdo.lemma.projections.internal

import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import de.fhdo.lemma.service.ServiceModel
import de.fhdo.lemma.service.Import
import de.fhdo.lemma.service.Microservice
import de.fhdo.lemma.service.Visibility
import de.fhdo.lemma.service.MicroserviceType
import de.fhdo.lemma.service.Interface
import de.fhdo.lemma.service.Operation
import de.fhdo.lemma.service.Parameter
import de.fhdo.lemma.technology.CommunicationType
import de.fhdo.lemma.technology.ExchangePattern
import de.fhdo.lemma.data.PrimitiveType
import de.fhdo.lemma.data.ComplexType
import de.fhdo.lemma.service.ImportedType
import de.fhdo.lemma.service.ImportType

class ServiceDslExtractor extends AbstractGenerator {
	static val ID_PATTERN = "(\\^?)([a-zA-Z_])\\w*"
	static val QUALIFIED_NAME_PATTERN = '''«ID_PATTERN»(\.«ID_PATTERN»)*'''
	static val QUALIFIED_NAME_WITH_AT_LEAST_ONE_LEVEL_PATTERN = '''«ID_PATTERN»\.''' +
		QUALIFIED_NAME_PATTERN
		
	override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {
		val targetFileName = (context as ExtractorGeneratorContext).targetFileName
		val serviceModel = input.contents.get(0) as ServiceModel		
		fsa.generateFile(targetFileName, serviceModel.generate)
	}
	
	private def generate(ServiceModel serviceModel) {
		val imports = <String>newArrayList
		serviceModel.imports.forEach[imports.add(it.generate)]		
		val importStatements = if (!imports.empty)
				String.join("\n", imports) + "\n\n"
			else
				""
		
		val microservices = String.join("\n\n", serviceModel.microservices.map[generate])
		
		return '''«importStatements»«microservices»'''
	}
	
	private def generate(Import ^import) {
		val importTypeKeyword = switch(^import.importType) {
			case DATATYPES: "datatypes"
			case MICROSERVICES: "microservices"
			case TECHNOLOGY: "technology"
			default: null
		}
		return '''import «importTypeKeyword» from "«^import.importURI»" as «^import.name»'''
	}
	
	private def generate(Microservice service) {
		val preamble = '''«service.visibility.generate» «service.type.generate»'''
		val hasInterfaceOperations = service.interfaces.exists[!operations.empty]
		return '''
		«preamble» microservice «service.lemmaName» {
			«IF hasInterfaceOperations»
				«FOR iface : service.interfaces»
					«iface.generate»
				«ENDFOR»
			«ELSE»
				[DEFINE_OPERATIONS]
			«ENDIF»
		}'''
	}
	
	private def lemmaName(Microservice service) {
		return if (service.name.matches(QUALIFIED_NAME_WITH_AT_LEAST_ONE_LEVEL_PATTERN))
				service.name
			else
				'''ADD_QUALIFYING_PART.«service.name»'''
	}
	
	private def generate(Visibility visibility) {
		return switch(visibility) {
			case INTERNAL: 'internal'			
			case PUBLIC: 'public'
			default: 'architecture'
		}
	}
	
	private def generate(MicroserviceType type) {
		return switch(type) {
			case FUNCTIONAL: 'functional'
			case INFRASTRUCTURE: 'infrastructure'
			case UTILITY: 'utility'
			default: null
		}
	}
	
	private def generate(Interface iface) {
		return '''
		interface «iface.name» {
			«FOR o: iface.operations»
				«o.generate»
			«ENDFOR»
		}'''
	}
	
	private def generate(Operation operation) {
		val parameters = String.join(", ", operation.parameters.map[generate])
		return '''«operation.name»(«parameters»);'''
	}
	
	private def generate(Parameter parameter) {
		val preamble = <String>newArrayList
		preamble.add(parameter.communicationType.generate)
		
		if (parameter.exchangePattern !== ExchangePattern.IN)
			preamble.add(parameter.exchangePattern.generate)
		
		return '''«String.join(" ", preamble)» «parameter.name» : «parameter.generateType»'''
	}
	
	private def generate(CommunicationType type) {
		return switch(type) {
			case ASYNCHRONOUS: "async"
			case SYNCHRONOUS: "sync"
			default: null
		}
	}
	
	private def generate(ExchangePattern pattern) {
		return switch(pattern) {
			case OUT: "out"
			case INOUT: "inout"
			default: "in"
		}
	}
	
	private def generateType(Parameter parameter) {
		return if (parameter.primitiveType !== null)
				(parameter.primitiveType as PrimitiveType).generate 
			else
				parameter.importedType.generate
	}
	
	private def generate(PrimitiveType type) {
		return type.typeName
	}
	
	private def generate(ImportedType importedType) {
		if (importedType.import.importType !== ImportType.DATATYPES)
			return null
		
		val importedTypeName = (importedType.type as ComplexType).buildQualifiedName(".")
		return '''«importedType.import.name»::«importedTypeName»'''
	}
}