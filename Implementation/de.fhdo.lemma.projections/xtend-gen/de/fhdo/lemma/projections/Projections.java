package de.fhdo.lemma.projections;

import com.google.inject.Inject;
import com.google.inject.Provider;
import de.fhdo.lemma.projections.internal.ExtractorGeneratorContext;
import de.fhdo.lemma.projections.internal.PluginActivator;
import de.fhdo.lemma.projections.internal.TrackingFileSystemAccess;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.generator.IGenerator2;

@SuppressWarnings("all")
public abstract class Projections<M extends EObject> {
  @Inject
  private Provider<TrackingFileSystemAccess> fileAccessProvider;
  
  public abstract IGenerator2 extractor();
  
  public List<IFile> extract(final M model, final IProject project, final String targetFolder, final String targetFileName) {
    PluginActivator.getInstance().injectMembers(this);
    TrackingFileSystemAccess fsa = this.newFileSystemAccessTo(project, targetFolder);
    ExtractorGeneratorContext context = new ExtractorGeneratorContext(targetFileName);
    this.extractor().doGenerate(model.eResource(), fsa, context);
    return fsa.getGeneratedFiles();
  }
  
  private TrackingFileSystemAccess newFileSystemAccessTo(final IProject project, final String targetFolder) {
    final TrackingFileSystemAccess fsa = this.fileAccessProvider.get();
    fsa.setProject(project);
    fsa.setOutputPath(targetFolder);
    NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
    fsa.setMonitor(_nullProgressMonitor);
    return fsa;
  }
}
