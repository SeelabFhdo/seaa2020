package de.fhdo.lemma.ddmm2lemma;

import de.fhdo.lemma.data.DataModel;
import de.fhdo.lemma.ddmm2lemma.Utils;
import de.fhdo.lemma.projections.DataDslProjections;
import de.fhdo.lemma.projections.Projections;
import de.fhdo.lemma.projections.ServiceDslProjections;
import de.fhdo.lemma.service.ServiceModel;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class LemmaExtractor extends AbstractHandler {
  @Override
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    final IFile inputFile = Utils.getSelectedFile(event);
    final EList<EObject> contents = this.getResource(inputFile).getContents();
    EObject _xifexpression = null;
    boolean _isEmpty = contents.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      _xifexpression = contents.get(0);
    } else {
      return null;
    }
    final EObject modelRoot = _xifexpression;
    this.extract(modelRoot, inputFile);
    return null;
  }
  
  protected void _extract(final EObject eObject, final IProject project) {
    throw new UnsupportedOperationException();
  }
  
  protected void _extract(final DataModel domainModel, final IFile file) {
    DataDslProjections _dataDslProjections = new DataDslProjections();
    this.<DataModel>doExtract(_dataDslProjections, domainModel, file);
  }
  
  protected void _extract(final ServiceModel serviceModel, final IFile file) {
    ServiceDslProjections _serviceDslProjections = new ServiceDslProjections();
    this.<ServiceModel>doExtract(_serviceDslProjections, serviceModel, file);
  }
  
  public <M extends EObject> void doExtract(final Projections<M> projections, final M model, final IFile file) {
    final String targetFileName = Utils.removeSuffix(file.getName(), ".xmi");
    final List<IFile> generatedFiles = projections.extract(model, file.getProject(), "", targetFileName);
    final Consumer<IFile> _function = (IFile it) -> {
      try {
        boolean _isDerived = it.isDerived();
        NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
        it.setDerived(_isDerived, _nullProgressMonitor);
      } catch (Throwable _e) {
        throw Exceptions.sneakyThrow(_e);
      }
    };
    generatedFiles.forEach(_function);
  }
  
  private Resource getResource(final IFile file) {
    try {
      final URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
      final Resource resource = this.buildResourceSet().getResource(uri, true);
      resource.load(null);
      return resource;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private ResourceSetImpl buildResourceSet() {
    final ResourceSetImpl rs = new ResourceSetImpl();
    final Map<String, Object> extensionToFactoryMap = rs.getResourceFactoryRegistry().getExtensionToFactoryMap();
    XMIResourceFactoryImpl _xMIResourceFactoryImpl = new XMIResourceFactoryImpl();
    extensionToFactoryMap.put("xmi", _xMIResourceFactoryImpl);
    return rs;
  }
  
  public void extract(final EObject domainModel, final IResource file) {
    if (domainModel instanceof DataModel
         && file instanceof IFile) {
      _extract((DataModel)domainModel, (IFile)file);
      return;
    } else if (domainModel instanceof ServiceModel
         && file instanceof IFile) {
      _extract((ServiceModel)domainModel, (IFile)file);
      return;
    } else if (domainModel != null
         && file instanceof IProject) {
      _extract(domainModel, (IProject)file);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(domainModel, file).toString());
    }
  }
}
