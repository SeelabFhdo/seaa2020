package de.fhdo.lemma.projections.internal;

import com.google.inject.Binder;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.name.Names;
import de.fhdo.lemma.projections.internal.DataDslExtractor;
import de.fhdo.lemma.projections.internal.ServiceDslExtractor;
import org.eclipse.xtext.generator.IGenerator2;
import org.eclipse.xtext.service.AbstractGenericModule;

@SuppressWarnings("all")
public class RuntimeModule extends AbstractGenericModule {
  public ScopedBindingBuilder configureDataDslExtractor(final Binder binder) {
    return binder.<IGenerator2>bind(IGenerator2.class).annotatedWith(Names.named("DataDslExtractor")).to(DataDslExtractor.class);
  }
  
  public ScopedBindingBuilder configureServiceDslExtractor(final Binder binder) {
    return binder.<IGenerator2>bind(IGenerator2.class).annotatedWith(Names.named("ServiceDslExtractor")).to(ServiceDslExtractor.class);
  }
}
