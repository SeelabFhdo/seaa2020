package de.fhdo.lemma.projections.internal;

import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.generator.GeneratorContext;
import org.eclipse.xtext.xbase.lib.Pure;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class ExtractorGeneratorContext extends GeneratorContext {
  @Accessors(AccessorType.PUBLIC_GETTER)
  private final String targetFileName;
  
  public ExtractorGeneratorContext(final String targetFileName) {
    super();
    this.targetFileName = targetFileName;
  }
  
  @Pure
  public String getTargetFileName() {
    return this.targetFileName;
  }
}
