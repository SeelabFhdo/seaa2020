package de.fhdo.lemma.ddmm2lemma

import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import static extension de.fhdo.lemma.ddmm2lemma.Utils.*

class XmiTransformationHandler extends AbstractHandler {
	override execute(ExecutionEvent event) throws ExecutionException {
		val selectedFile = getSelectedFile(event)
		selectedFile.serialize('''«selectedFile.name».xmi''')
		return null
	}
}