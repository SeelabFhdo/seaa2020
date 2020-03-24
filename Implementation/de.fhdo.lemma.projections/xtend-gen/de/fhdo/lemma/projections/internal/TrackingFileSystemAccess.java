package de.fhdo.lemma.projections.internal;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess2;
import org.eclipse.xtext.generator.OutputConfiguration;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class TrackingFileSystemAccess extends EclipseResourceFileSystemAccess2 {
  private Map<String, IFile> generatedFiles = CollectionLiterals.<String, IFile>newHashMap();
  
  @Override
  public void generateFile(final IFile file, final InputStream content, final IFile traceFile, final CharSequence traceContent, final OutputConfiguration outputConfig) {
    this.generatedFiles.put(file.getFullPath().toString(), file);
    super.generateFile(file, content, traceFile, traceContent, outputConfig);
  }
  
  public List<IFile> getGeneratedFiles() {
    return IterableExtensions.<IFile>toList(this.generatedFiles.values());
  }
}
