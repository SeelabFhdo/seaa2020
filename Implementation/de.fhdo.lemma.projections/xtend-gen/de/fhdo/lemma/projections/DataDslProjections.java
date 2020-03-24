package de.fhdo.lemma.projections;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.fhdo.lemma.data.DataModel;
import de.fhdo.lemma.projections.Projections;
import org.eclipse.xtext.generator.IGenerator2;

@SuppressWarnings("all")
public class DataDslProjections extends Projections<DataModel> {
  @Inject
  @Named("DataDslExtractor")
  private IGenerator2 extractor;
  
  @Override
  public IGenerator2 extractor() {
    return this.extractor;
  }
}
