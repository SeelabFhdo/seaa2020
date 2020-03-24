package de.fhdo.lemma.ddmm2lemma

import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.m2m.atl.emftvm.impl.resource.EMFTVMResourceFactoryImpl
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl
import org.eclipse.m2m.atl.emftvm.EmftvmFactory
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.common.util.URI
import org.eclipse.m2m.atl.emftvm.util.DefaultModuleResolver
import org.eclipse.m2m.atl.emftvm.util.TimingData
import org.eclipse.core.resources.IFile
import de.fhdo.lemma.data.DataPackage
import de.fhdo.lemma.projections.DataDslProjections
import de.fhdo.lemma.data.DataModel
import static extension de.fhdo.lemma.ddmm2lemma.Utils.*
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.core.runtime.Path
import java.io.File
import org.eclipse.core.resources.IResource
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.uml2.uml.resource.UMLResource
import org.eclipse.uml2.uml.Model
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.Platform
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import java.util.List
import org.apache.commons.io.FilenameUtils
import de.fhdo.lemma.service.ServicePackage
import de.fhdo.lemma.service.ServiceModel
import de.fhdo.lemma.projections.ServiceDslProjections
import de.fhdo.lemma.service.ImportType
import de.fhdo.lemma.service.ServiceFactory
import de.fhdo.lemma.service.Parameter
import de.fhdo.lemma.data.ComplexType
import org.eclipse.swt.widgets.Display
import org.eclipse.jface.dialogs.MessageDialog

class DdmmToLemmaTransformationHandler extends AbstractHandler {
	static val TRANSFORMATION_MODULE_BASE_PATH = "platform:/plugin/de.fhdo.lemma.ddmm2lemma" +
		"/de/fhdo/lemma/ddmm2lemma/transformations/"
	static val DEFAULT_DOMAIN_REFINEMENT_MODULE = TRANSFORMATION_MODULE_BASE_PATH +
		"/domain_services_refinement"
	static val DEFAULT_SERVICE_TRANSFORMATION_MODULE = TRANSFORMATION_MODULE_BASE_PATH + "/services"
	static val DEFAULT_TARGET_FOLDER = "extracted models"
	
	var String domainRefinementModule 
	var String serviceTransformationModule
	var String targetFolder
	
	override execute(ExecutionEvent event) throws ExecutionException {
		domainRefinementModule = getPreference(
			ConfigurationDialog.DOMAIN_REFINEMENT_MODULE_PREF,
			DEFAULT_DOMAIN_REFINEMENT_MODULE
		)
		serviceTransformationModule = getPreference(
			ConfigurationDialog.SERVICE_TRANSFORMATION_MODULE,
			DEFAULT_SERVICE_TRANSFORMATION_MODULE
		)
		targetFolder = getPreference(
			ConfigurationDialog.EXTRACTION_TARGET_FOLDER,
			DEFAULT_TARGET_FOLDER
		)

		val inputFile = event.selectedFile				
		val contextResources = inputFile.boundedContextResources		
		contextResources.forEach[context, umlResource |
			val domainModelResource = domainTransformations(context, umlResource, inputFile.project)
			serviceTransformations(context, domainModelResource, umlResource, inputFile.project)
		]
		
		val display = Display.current
		display.asyncExec(
			new Runnable {				
				override run() {
					val targetFolderProjectPath = inputFile.project.getFile(targetFolder).fullPath
					MessageDialog.openInformation(display.activeShell, "Editable Models' " +
						"Extraction", "Editable models were extracted successfully to target " +
						'''folder "«targetFolderProjectPath»".''')
				}				
			}
		)		
		
		return null
	}
	
	private def getPreference(String name, String ^default) {
		Platform.preferencesService.getString(ConfigurationDialog.QUALIFIER, name, ^default, null)
	}
	
	private def domainTransformations(String context, Resource umlResource, IProject project) {
		// Transform DDMM-conform UML resource to LEMMA domain data model 
		var resultModel = atlTransformation(
			TRANSFORMATION_MODULE_BASE_PATH,
			"domain",
			#[atlParam("UML2", UMLPackage.eINSTANCE.eResource, "IN", umlResource)],
			null,
			#[atlParam("Domain", DataPackage.eINSTANCE.eResource, "INOUT", null)]
		).get("INOUT")
		
		// Service-oriented refinement of domain data model
		resultModel = atlTransformation(
			domainRefinementModule.path,
			domainRefinementModule.filenameWithoutExt,
			#[atlParam("UML2", UMLPackage.eINSTANCE.eResource, "DDMM", umlResource)],
			null,
			#[atlParam("Domain", DataPackage.eINSTANCE.eResource, "IN", resultModel)]
		).get("IN")
		
		// Serialize result model to XMI
		val baseFolder = '''«targetFolder»«File.separator»''' + 
			'''«IF !context.empty»«context»«File.separator»«ENDIF»'''
		val baseFilename = '''domain«IF !context.empty»_«context»«ENDIF».data'''
		val extractedBaseFilename = '''extracted_«baseFilename»'''
		resultModel.serialize(project, '''«baseFolder»«extractedBaseFilename».xmi''')
		
		// Extract result model to derived textual modeling file
		val domainModel = resultModel.contents.get(0) as DataModel
		val generatedFile = new DataDslProjections().extract(domainModel, project, baseFolder,
			extractedBaseFilename).get(0)
		
		// Copy derived textual modeling file to editable, i.e., non-derived, modeling file
		val editableFilePath = '''«generatedFile.path»«File.separator»«baseFilename»'''
		if (!project.parent.exists(new Path(editableFilePath)))
			generatedFile.copyTo(editableFilePath, false) 
			
		return resultModel
	}
	
	private def serviceTransformations(String context, Resource domainModelResource,
		Resource umlResource, IProject project) {
		// Transform DDMM-conform UML resource to LEMMA service model 
		var resultModel = atlTransformation(
			serviceTransformationModule.path,
			serviceTransformationModule.filenameWithoutExt,
			#[
				atlParam("UML2", UMLPackage.eINSTANCE.eResource, "IN", umlResource),
				atlParam("Domain", DataPackage.eINSTANCE.eResource, "CONTEXT", domainModelResource)
			],
			null,
			#[atlParam("Service", ServicePackage.eINSTANCE.eResource, "INOUT", null)]
		).get("INOUT")
		
		val domainModel = domainModelResource.contents.get(0) as DataModel
		val serviceModel = resultModel.contents.get(0) as ServiceModel
		if (context.empty)
			serviceModel.adaptToGenericModel(domainModel)
		
		// Serialize result model to XMI
		val baseFolder = '''«targetFolder»«File.separator»''' +
			'''«IF !context.empty»«context»«File.separator»«ENDIF»'''
		val baseFilename = '''service«IF !context.empty»_«context»«ENDIF».services'''
		val extractedBaseFilename = '''extracted_«baseFilename»'''
		serviceModel.imports.forEach[
			if (importType == ImportType.DATATYPES)
				importURI = '''extracted_«importURI»'''
		]
		serviceModel.eResource.serialize(project, '''«baseFolder»«extractedBaseFilename».xmi''')
		
		// Extract result model to derived textual modeling file
		val projections = new ServiceDslProjections
		val extractedFile = projections.extract(serviceModel, project, baseFolder,
			extractedBaseFilename).get(0)
		
		// Copy derived textual modeling file to editable, i.e., non-derived, modeling file		
		serviceModel.imports.forEach[
			if (importType == ImportType.DATATYPES)
				importURI = importURI.removePrefix("extracted_")
		]
		val editableFilePath = '''«extractedFile.path»«File.separator»«baseFilename»'''
		if (!project.parent.exists(new Path(editableFilePath))) {
			val editableFile = projections.extract(serviceModel, project, baseFolder,
				baseFilename).get(0)
			editableFile.setDerived(false, new NullProgressMonitor)
		}
	}
	
	private def adaptToGenericModel(ServiceModel serviceModel, DataModel genericDomainModel) {
		val genericDomainImport = ServiceFactory.eINSTANCE.createImport
		genericDomainImport.name = "domain"
		genericDomainImport.importURI = "domain.data"
		genericDomainImport.importType = ImportType.DATATYPES
		
		val genericTypes = EcoreUtil2.getAllContentsOfType(genericDomainModel, ComplexType).toMap(
			[it.buildQualifiedName(".")],
			[it]
		)
		
		val importedTypes = EcoreUtil2.getAllContentsOfType(serviceModel, Parameter)
			.filter[importedType !== null && importedType.type instanceof ComplexType]		
			.toMap(
				[it],
				[(importedType.type as ComplexType).buildQualifiedName(".")]
			)
		
		importedTypes.forEach[parameter, typeName |
			parameter.importedType = ServiceFactory.eINSTANCE.createImportedType
			parameter.importedType.import = genericDomainImport
			parameter.importedType.type = genericTypes.get(typeName)
		]
		
		//serviceModel.imports.forEach[it.eCrossReferences.remo]
		serviceModel.imports.removeAll(serviceModel.imports)
		serviceModel.imports.add(genericDomainImport)
	}
	
	private def getUmlResource(IFile file) {
		val uri = URI.createPlatformResourceURI(file.fullPath.toString, true)
		val resource = buildUml2ResourceSet.getResource(uri, true)
		resource.load(null)
		return resource
	}
	
	private def buildUml2ResourceSet() {
		val rs = new ResourceSetImpl
		
		val extensionToFactoryMap = rs.getResourceFactoryRegistry().getExtensionToFactoryMap()
        extensionToFactoryMap.put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE)
        
        return rs
	}
	
	private def boundedContextResources(IFile file) {
		val boundedContextNames = file.umlResource.boundedContexts.map[name]
		
		val contextResources = <String, Resource>newHashMap
		boundedContextNames.forEach[currentContext |
			val resource = file.umlResource
			resource.boundedContexts.filter[name != currentContext].forEach[EcoreUtil2.remove(it)]
			contextResources.put(currentContext, resource)
		]
		contextResources.put("", file.umlResource)
		
		return contextResources
	}
	
	private def boundedContexts(Resource umlResource) {
		val model = umlResource.contents.get(0) as Model
		return EcoreUtil2.getAllContentsOfType(model, org.eclipse.uml2.uml.Package)
			.filter[appliedStereotypes.exists[qualifiedName == 'RootElement::BoundedContext']]
	}
	
	private def atlTransformation(String moduleBasePath, String moduleName, List<AtlParam> inModels,
		List<AtlParam> outModels, List<AtlParam> inoutModels) {
		val env = EmftvmFactory.eINSTANCE.createExecEnv()
		
		val metamodels = <String, Resource>newHashMap
		metamodels.putAll(inModels?.toMap([metamodelName], [metamodelResource]) ?: emptyMap)
		metamodels.putAll(outModels?.toMap([metamodelName], [metamodelResource]) ?: emptyMap)
		metamodels.putAll(inoutModels?.toMap([metamodelName], [metamodelResource]) ?: emptyMap)
		metamodels.forEach[name, resource |
			val metamodel = EmftvmFactory.eINSTANCE.createMetamodel()
			metamodel.resource = resource
			env.registerMetaModel(name, metamodel)
		]
		
		val inmemIndexCounter = new Counter
		val models = <String, Resource>newHashMap
		models.putAll(inModels?.toMap(
			[modelName],
			[
				val model = EmftvmFactory.eINSTANCE.createModel()
				val resource = modelResource ?: buildTransformationResourceSet.createResource(
					URI.createFileURI('''_inmem«inmemIndexCounter.increment».xmi''')
				)
				model.resource = resource
				env.registerInputModel(modelName, model)
				resource
			]
		) ?: emptyMap)
		
		models.putAll(outModels?.toMap(
			[modelName],
			[
				val model = EmftvmFactory.eINSTANCE.createModel()
				val resource = modelResource ?: buildTransformationResourceSet.createResource(
					URI.createFileURI('''_inmem«inmemIndexCounter.increment».xmi''')
				)
				model.resource = resource
				env.registerOutputModel(modelName, model)
				resource
			]
		) ?: emptyMap)
		
		models.putAll(inoutModels?.toMap(
			[modelName],
			[
				val model = EmftvmFactory.eINSTANCE.createModel()
				val resource = modelResource ?: buildTransformationResourceSet.createResource(
					URI.createFileURI('''_inmem«inmemIndexCounter.increment».xmi''')
				)
				model.resource = resource
				env.registerInOutModel(modelName, model)
				resource
			]
		) ?: emptyMap)
		
		val mr = new DefaultModuleResolver(moduleBasePath, buildTransformationResourceSet)
		val td = new TimingData()		
		try {
			env.loadModule(mr, moduleName)
			td.finishLoading()
			env.run(td)
			td.finish()		
		} catch (Exception ex) {
			ex.printStackTrace
		}
		
		return models
	}
	
	private def atlParam(String metamodelName, Resource metamodelResource, String modelName,
		Resource modelResource) {
		return new AtlParam(metamodelName, metamodelResource, modelName, modelResource)
	}
	
	@FinalFieldsConstructor
	static class AtlParam {
		val String metamodelName
		val Resource metamodelResource
		val String modelName
		val Resource modelResource		
	}
	
	static class Counter {
		var currentValue = 0
		def increment() {
			currentValue++
			return currentValue
		}
	}
	
	private def buildTransformationResourceSet() {
        val rs = new ResourceSetImpl

        val extensionToFactoryMap = rs.getResourceFactoryRegistry().getExtensionToFactoryMap()
        extensionToFactoryMap.put("xmi", new XMIResourceFactoryImpl())
        extensionToFactoryMap.put("emftvm", new EMFTVMResourceFactoryImpl)
        extensionToFactoryMap.put("ecore", new EcoreResourceFactoryImpl)

        return rs
    }
	
	private def copyTo(IFile source, String targetFileName, boolean isDerived) {
		val targetPathWithoutProject = new Path(targetFileName).removePrefix(source.project.name)
		var targetFileNameWithoutProject = targetPathWithoutProject.toString 
		val target = source.project.getFile(targetFileNameWithoutProject)
		if (!target.exists)
			target.create(null, IResource.NONE, new NullProgressMonitor)
		target.setContents(source.contents, true, false, new NullProgressMonitor)
		target.setDerived(isDerived, new NullProgressMonitor)
	}
	
	private def path(IFile file) {
		return file.fullPath.removeLastSegments(1)
	}
	
	private def removePrefix(Path p, String prefix) {
		return if (p.empty || p.segment(0) != prefix)
				p
			else
				p.removeFirstSegments(1)
	}
	
	private def path(String fullPath) {
		return FilenameUtils.getPath(fullPath)
	}
	
	private def filenameWithoutExt(String fullPath) {
		return FilenameUtils.getBaseName(fullPath)
	}
}