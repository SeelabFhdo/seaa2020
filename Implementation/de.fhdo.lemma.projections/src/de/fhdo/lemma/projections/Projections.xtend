package de.fhdo.lemma.projections

import org.eclipse.emf.ecore.EObject
import com.google.inject.Inject
import com.google.inject.Provider
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.NullProgressMonitor
import de.fhdo.lemma.projections.internal.PluginActivator
import org.eclipse.xtext.generator.IGenerator2
import de.fhdo.lemma.projections.internal.TrackingFileSystemAccess
import de.fhdo.lemma.projections.internal.ExtractorGeneratorContext

abstract class Projections<M extends EObject> {
	@Inject
	Provider<TrackingFileSystemAccess> fileAccessProvider
	
	abstract def IGenerator2 extractor()
	
	def extract(M model, IProject project, String targetFolder, String targetFileName) {
		PluginActivator.instance.injectMembers(this)
		
		var fsa = project.newFileSystemAccessTo(targetFolder)
		var context = new ExtractorGeneratorContext(targetFileName)
		extractor().doGenerate(model.eResource, fsa, context)		
		return fsa.generatedFiles
	}
	
	private def newFileSystemAccessTo(IProject project, String targetFolder) {
		val fsa = fileAccessProvider.get()

		fsa.setProject(project)
	    fsa.setOutputPath(targetFolder)
		fsa.setMonitor(new NullProgressMonitor)

		return fsa
	}
}