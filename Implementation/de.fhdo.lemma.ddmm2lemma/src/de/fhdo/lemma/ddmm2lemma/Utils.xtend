package de.fhdo.lemma.ddmm2lemma

import org.eclipse.core.resources.IFile
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.ui.handlers.HandlerUtil
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.resource.IResourceServiceProvider
import org.eclipse.xtext.ui.resource.IResourceSetProvider
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.core.resources.IProject

class Utils {
	static def getSelectedFile(ExecutionEvent event) {
		val window = HandlerUtil.getActiveWorkbenchWindowChecked(event)
		val selection = window.selectionService.selection as IStructuredSelection
		val selectedElement = selection.firstElement

		return selectedElement as IFile
	}
	
	static def serialize(IFile file, String filePath) {
		file.xtextResource.serialize(file.project, filePath)		
	}
	
	static def serialize(Resource resource, IProject project, String filePath) {
		val file = project.getFile(filePath)
		val uri = URI.createURI(file.fullPath.toString)
		val xmiResource = if (resource instanceof XtextResource) {
				val newXmiResource = resource.resourceSet.createResource(uri)
				if (!resource.contents.empty)
					newXmiResource.contents.add(resource.contents.get(0))

				newXmiResource
			} else {
				resource.URI = uri
				resource
			}

        xmiResource.save(emptyMap())
	}
	
	static def getXtextResource(IFile file) {
		val uri = URI.createPlatformResourceURI(file.fullPath.toString, true)				
		val resource = file.xtextResourceSet.getResource(uri, true)
		resource.load(null)
		return resource
	}
	
	static def getXtextResourceSet(IFile file) {
		val uri = URI.createPlatformResourceURI(file.fullPath.toString, true)
		return IResourceServiceProvider.Registry.INSTANCE
			.getResourceServiceProvider(uri)
			.get(typeof(IResourceSetProvider))
			.get(file.project)
	}
	
	static def removeSuffix(String s, String suffix) {
		if (!s.endsWith(suffix))
			return s
			
		return s.substring(0, s.length - suffix.length)
	}
	
	static def removePrefix(String s, String prefix) {
		if (!s.startsWith(prefix))
			return s
			
		return s.substring(prefix.length)
	}
}