package de.fhdo.lemma.projections.internal

import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess2
import org.eclipse.core.resources.IFile
import java.io.InputStream
import org.eclipse.xtext.generator.OutputConfiguration
import java.util.Map

class TrackingFileSystemAccess extends EclipseResourceFileSystemAccess2 {
	Map<String, IFile> generatedFiles = newHashMap
	
	override generateFile(IFile file, InputStream content, IFile traceFile,
		CharSequence traceContent, OutputConfiguration outputConfig) {
		generatedFiles.put(file.fullPath.toString(), file)
		super.generateFile(file, content, traceFile, traceContent, outputConfig)
	}
	
	def getGeneratedFiles() {
		return generatedFiles.values().toList
	}
}