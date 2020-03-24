package de.fhdo.lemma.ddmm2lemma

import org.eclipse.jface.preference.FieldEditorPreferencePage
import org.eclipse.jface.preference.StringFieldEditor
import org.eclipse.ui.IWorkbench
import org.eclipse.ui.IWorkbenchPreferencePage
import org.eclipse.ui.preferences.ScopedPreferenceStore
import org.eclipse.core.runtime.preferences.InstanceScope

class ConfigurationDialog extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public static val String QUALIFIER = "de.fhdo.lemma.ddmm2lemma"
	public static val String DOMAIN_REFINEMENT_MODULE_PREF = "domainRefinementModule"
	public static val String SERVICE_TRANSFORMATION_MODULE = "serviceTransformationModule"
	public static val String EXTRACTION_TARGET_FOLDER = "serviceTransformationModule"
		
	override protected createFieldEditors() {
		addField(new StringFieldEditor(DOMAIN_REFINEMENT_MODULE_PREF, "Path to ATL module for " +
			"service-oriented domain refinement:", fieldEditorParent))
		addField(new StringFieldEditor(SERVICE_TRANSFORMATION_MODULE, "Path to ATL module for " +
			"service transformation:", fieldEditorParent))
		addField(new StringFieldEditor(EXTRACTION_TARGET_FOLDER, "Target folder for extracted " +
			"LEMMA models:", fieldEditorParent))
	}
	
	override init(IWorkbench workbench) {
		preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, QUALIFIER)
		description = "Configuration for the DDMM2LEMMA plugin"
	}
}