package de.fhdo.lemma.projections;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.fhdo.lemma.projections.Projections;
import de.fhdo.lemma.service.ServiceModel;
import org.eclipse.xtext.generator.IGenerator2;

@SuppressWarnings("all")
public class ServiceDslProjections extends Projections<ServiceModel> {
  @Inject
  @Named("ServiceDslExtractor")
  private IGenerator2 extractor;
  
  @Override
  public IGenerator2 extractor() {
    return this.extractor;
  }
}
