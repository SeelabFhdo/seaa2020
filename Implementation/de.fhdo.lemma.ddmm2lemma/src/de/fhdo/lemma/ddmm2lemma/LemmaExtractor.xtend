package de.fhdo.lemma.ddmm2lemma

import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.emf.common.util.URI
import org.eclipse.core.resources.IFile
import de.fhdo.lemma.projections.DataDslProjections
import de.fhdo.lemma.data.DataModel
import static extension de.fhdo.lemma.ddmm2lemma.Utils.*
import org.eclipse.emf.ecore.EObject
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.NullProgressMonitor
import de.fhdo.lemma.service.ServiceModel
import de.fhdo.lemma.projections.ServiceDslProjections
import de.fhdo.lemma.projections.Projections

class LemmaExtractor extends AbstractHandler {	
	override execute(ExecutionEvent event) throws ExecutionException {
		val inputFile = event.selectedFile
		val contents = inputFile.resource.contents
		val modelRoot = if (!contents.empty) contents.get(0) else return null
		
		modelRoot.extract(inputFile)
		return null
	}	
	
	def dispatch void extract(EObject eObject, IProject project) {
		throw new UnsupportedOperationException
	}
	
	def dispatch void extract(DataModel domainModel, IFile file) {
		doExtract(new DataDslProjections(), domainModel, file)	
	}
	
	def dispatch void extract(ServiceModel serviceModel, IFile file) {
		doExtract(new ServiceDslProjections(), serviceModel, file)		 
	}
	
	def <M extends EObject> doExtract(Projections<M> projections, M model, IFile file) {
		val targetFileName = file.name.removeSuffix(".xmi")
		val generatedFiles = projections.extract(model, file.project, "", targetFileName)
		generatedFiles.forEach[setDerived(isDerived, new NullProgressMonitor)]
	}
	
	private def getResource(IFile file) {
		val uri = URI.createPlatformResourceURI(file.fullPath.toString, true)
		val resource = buildResourceSet().getResource(uri, true)
		resource.load(null)
		return resource
	}	
	
	private def buildResourceSet() {
        val rs = new ResourceSetImpl
        val extensionToFactoryMap = rs.getResourceFactoryRegistry().getExtensionToFactoryMap()
        extensionToFactoryMap.put("xmi", new XMIResourceFactoryImpl())
        return rs
    }
}