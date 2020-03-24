package de.fhdo.lemma.ddmm2lemma;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

@SuppressWarnings("all")
public class ConfigurationDialog extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
  public static final String QUALIFIER = "de.fhdo.lemma.ddmm2lemma";
  
  public static final String DOMAIN_REFINEMENT_MODULE_PREF = "domainRefinementModule";
  
  public static final String SERVICE_TRANSFORMATION_MODULE = "serviceTransformationModule";
  
  public static final String EXTRACTION_TARGET_FOLDER = "serviceTransformationModule";
  
  @Override
  protected void createFieldEditors() {
    Composite _fieldEditorParent = this.getFieldEditorParent();
    StringFieldEditor _stringFieldEditor = new StringFieldEditor(ConfigurationDialog.DOMAIN_REFINEMENT_MODULE_PREF, ("Path to ATL module for " + 
      "service-oriented domain refinement:"), _fieldEditorParent);
    this.addField(_stringFieldEditor);
    Composite _fieldEditorParent_1 = this.getFieldEditorParent();
    StringFieldEditor _stringFieldEditor_1 = new StringFieldEditor(ConfigurationDialog.SERVICE_TRANSFORMATION_MODULE, ("Path to ATL module for " + 
      "service transformation:"), _fieldEditorParent_1);
    this.addField(_stringFieldEditor_1);
    Composite _fieldEditorParent_2 = this.getFieldEditorParent();
    StringFieldEditor _stringFieldEditor_2 = new StringFieldEditor(ConfigurationDialog.EXTRACTION_TARGET_FOLDER, ("Target folder for extracted " + 
      "LEMMA models:"), _fieldEditorParent_2);
    this.addField(_stringFieldEditor_2);
  }
  
  @Override
  public void init(final IWorkbench workbench) {
    ScopedPreferenceStore _scopedPreferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, ConfigurationDialog.QUALIFIER);
    this.setPreferenceStore(_scopedPreferenceStore);
    this.setDescription("Configuration for the DDMM2LEMMA plugin");
  }
}
