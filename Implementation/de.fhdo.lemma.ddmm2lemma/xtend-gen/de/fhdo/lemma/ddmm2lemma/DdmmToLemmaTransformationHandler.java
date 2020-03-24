package de.fhdo.lemma.ddmm2lemma;

import com.google.common.base.Objects;
import de.fhdo.lemma.data.ComplexType;
import de.fhdo.lemma.data.DataModel;
import de.fhdo.lemma.data.DataPackage;
import de.fhdo.lemma.data.Type;
import de.fhdo.lemma.ddmm2lemma.ConfigurationDialog;
import de.fhdo.lemma.ddmm2lemma.Utils;
import de.fhdo.lemma.projections.DataDslProjections;
import de.fhdo.lemma.projections.ServiceDslProjections;
import de.fhdo.lemma.service.Import;
import de.fhdo.lemma.service.ImportType;
import de.fhdo.lemma.service.ImportedType;
import de.fhdo.lemma.service.Parameter;
import de.fhdo.lemma.service.ServiceFactory;
import de.fhdo.lemma.service.ServiceModel;
import de.fhdo.lemma.service.ServicePackage;
import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.m2m.atl.emftvm.EmftvmFactory;
import org.eclipse.m2m.atl.emftvm.ExecEnv;
import org.eclipse.m2m.atl.emftvm.Metamodel;
import org.eclipse.m2m.atl.emftvm.impl.resource.EMFTVMResourceFactoryImpl;
import org.eclipse.m2m.atl.emftvm.util.DefaultModuleResolver;
import org.eclipse.m2m.atl.emftvm.util.TimingData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class DdmmToLemmaTransformationHandler extends AbstractHandler {
  @FinalFieldsConstructor
  public static class AtlParam {
    private final String metamodelName;
    
    private final Resource metamodelResource;
    
    private final String modelName;
    
    private final Resource modelResource;
    
    public AtlParam(final String metamodelName, final Resource metamodelResource, final String modelName, final Resource modelResource) {
      super();
      this.metamodelName = metamodelName;
      this.metamodelResource = metamodelResource;
      this.modelName = modelName;
      this.modelResource = modelResource;
    }
  }
  
  public static class Counter {
    private int currentValue = 0;
    
    public int increment() {
      this.currentValue++;
      return this.currentValue;
    }
  }
  
  private static final String TRANSFORMATION_MODULE_BASE_PATH = ("platform:/plugin/de.fhdo.lemma.ddmm2lemma" + 
    "/de/fhdo/lemma/ddmm2lemma/transformations/");
  
  private static final String DEFAULT_DOMAIN_REFINEMENT_MODULE = (DdmmToLemmaTransformationHandler.TRANSFORMATION_MODULE_BASE_PATH + 
    "/domain_services_refinement");
  
  private static final String DEFAULT_SERVICE_TRANSFORMATION_MODULE = (DdmmToLemmaTransformationHandler.TRANSFORMATION_MODULE_BASE_PATH + "/services");
  
  private static final String DEFAULT_TARGET_FOLDER = "extracted models";
  
  private String domainRefinementModule;
  
  private String serviceTransformationModule;
  
  private String targetFolder;
  
  @Override
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    this.domainRefinementModule = this.getPreference(
      ConfigurationDialog.DOMAIN_REFINEMENT_MODULE_PREF, 
      DdmmToLemmaTransformationHandler.DEFAULT_DOMAIN_REFINEMENT_MODULE);
    this.serviceTransformationModule = this.getPreference(
      ConfigurationDialog.SERVICE_TRANSFORMATION_MODULE, 
      DdmmToLemmaTransformationHandler.DEFAULT_SERVICE_TRANSFORMATION_MODULE);
    this.targetFolder = this.getPreference(
      ConfigurationDialog.EXTRACTION_TARGET_FOLDER, 
      DdmmToLemmaTransformationHandler.DEFAULT_TARGET_FOLDER);
    final IFile inputFile = Utils.getSelectedFile(event);
    final HashMap<String, Resource> contextResources = this.boundedContextResources(inputFile);
    final BiConsumer<String, Resource> _function = (String context, Resource umlResource) -> {
      final Resource domainModelResource = this.domainTransformations(context, umlResource, inputFile.getProject());
      this.serviceTransformations(context, domainModelResource, umlResource, inputFile.getProject());
    };
    contextResources.forEach(_function);
    final Display display = Display.getCurrent();
    display.asyncExec(
      new Runnable() {
        @Override
        public void run() {
          final IPath targetFolderProjectPath = inputFile.getProject().getFile(DdmmToLemmaTransformationHandler.this.targetFolder).getFullPath();
          Shell _activeShell = display.getActiveShell();
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("folder \"");
          _builder.append(targetFolderProjectPath);
          _builder.append("\".");
          String _plus = ("Editable models were extracted successfully to target " + _builder);
          MessageDialog.openInformation(_activeShell, ("Editable Models\' " + 
            "Extraction"), _plus);
        }
      });
    return null;
  }
  
  private String getPreference(final String name, final String default_) {
    return Platform.getPreferencesService().getString(ConfigurationDialog.QUALIFIER, name, default_, null);
  }
  
  private Resource domainTransformations(final String context, final Resource umlResource, final IProject project) {
    DdmmToLemmaTransformationHandler.AtlParam _atlParam = this.atlParam("UML2", UMLPackage.eINSTANCE.eResource(), "IN", umlResource);
    DdmmToLemmaTransformationHandler.AtlParam _atlParam_1 = this.atlParam("Domain", DataPackage.eINSTANCE.eResource(), "INOUT", null);
    Resource resultModel = this.atlTransformation(
      DdmmToLemmaTransformationHandler.TRANSFORMATION_MODULE_BASE_PATH, 
      "domain", 
      Collections.<DdmmToLemmaTransformationHandler.AtlParam>unmodifiableList(CollectionLiterals.<DdmmToLemmaTransformationHandler.AtlParam>newArrayList(_atlParam)), 
      null, 
      Collections.<DdmmToLemmaTransformationHandler.AtlParam>unmodifiableList(CollectionLiterals.<DdmmToLemmaTransformationHandler.AtlParam>newArrayList(_atlParam_1))).get("INOUT");
    DdmmToLemmaTransformationHandler.AtlParam _atlParam_2 = this.atlParam("UML2", UMLPackage.eINSTANCE.eResource(), "DDMM", umlResource);
    DdmmToLemmaTransformationHandler.AtlParam _atlParam_3 = this.atlParam("Domain", DataPackage.eINSTANCE.eResource(), "IN", resultModel);
    resultModel = this.atlTransformation(
      this.path(this.domainRefinementModule), 
      this.filenameWithoutExt(this.domainRefinementModule), 
      Collections.<DdmmToLemmaTransformationHandler.AtlParam>unmodifiableList(CollectionLiterals.<DdmmToLemmaTransformationHandler.AtlParam>newArrayList(_atlParam_2)), 
      null, 
      Collections.<DdmmToLemmaTransformationHandler.AtlParam>unmodifiableList(CollectionLiterals.<DdmmToLemmaTransformationHandler.AtlParam>newArrayList(_atlParam_3))).get("IN");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.targetFolder);
    _builder.append(File.separator);
    StringConcatenation _builder_1 = new StringConcatenation();
    {
      boolean _isEmpty = context.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder_1.append(context);
        _builder_1.append(File.separator);
      }
    }
    final String baseFolder = (_builder.toString() + _builder_1);
    StringConcatenation _builder_2 = new StringConcatenation();
    _builder_2.append("domain");
    {
      boolean _isEmpty_1 = context.isEmpty();
      boolean _not_1 = (!_isEmpty_1);
      if (_not_1) {
        _builder_2.append("_");
        _builder_2.append(context);
      }
    }
    _builder_2.append(".data");
    final String baseFilename = _builder_2.toString();
    StringConcatenation _builder_3 = new StringConcatenation();
    _builder_3.append("extracted_");
    _builder_3.append(baseFilename);
    final String extractedBaseFilename = _builder_3.toString();
    StringConcatenation _builder_4 = new StringConcatenation();
    _builder_4.append(baseFolder);
    _builder_4.append(extractedBaseFilename);
    _builder_4.append(".xmi");
    Utils.serialize(resultModel, project, _builder_4.toString());
    EObject _get = resultModel.getContents().get(0);
    final DataModel domainModel = ((DataModel) _get);
    final IFile generatedFile = new DataDslProjections().extract(domainModel, project, baseFolder, extractedBaseFilename).get(0);
    StringConcatenation _builder_5 = new StringConcatenation();
    IPath _path = this.path(generatedFile);
    _builder_5.append(_path);
    _builder_5.append(File.separator);
    _builder_5.append(baseFilename);
    final String editableFilePath = _builder_5.toString();
    IContainer _parent = project.getParent();
    Path _path_1 = new Path(editableFilePath);
    boolean _exists = _parent.exists(_path_1);
    boolean _not_2 = (!_exists);
    if (_not_2) {
      this.copyTo(generatedFile, editableFilePath, false);
    }
    return resultModel;
  }
  
  private void serviceTransformations(final String context, final Resource domainModelResource, final Resource umlResource, final IProject project) {
    try {
      DdmmToLemmaTransformationHandler.AtlParam _atlParam = this.atlParam("UML2", UMLPackage.eINSTANCE.eResource(), "IN", umlResource);
      DdmmToLemmaTransformationHandler.AtlParam _atlParam_1 = this.atlParam("Domain", DataPackage.eINSTANCE.eResource(), "CONTEXT", domainModelResource);
      DdmmToLemmaTransformationHandler.AtlParam _atlParam_2 = this.atlParam("Service", ServicePackage.eINSTANCE.eResource(), "INOUT", null);
      Resource resultModel = this.atlTransformation(
        this.path(this.serviceTransformationModule), 
        this.filenameWithoutExt(this.serviceTransformationModule), 
        Collections.<DdmmToLemmaTransformationHandler.AtlParam>unmodifiableList(CollectionLiterals.<DdmmToLemmaTransformationHandler.AtlParam>newArrayList(_atlParam, _atlParam_1)), 
        null, 
        Collections.<DdmmToLemmaTransformationHandler.AtlParam>unmodifiableList(CollectionLiterals.<DdmmToLemmaTransformationHandler.AtlParam>newArrayList(_atlParam_2))).get("INOUT");
      EObject _get = domainModelResource.getContents().get(0);
      final DataModel domainModel = ((DataModel) _get);
      EObject _get_1 = resultModel.getContents().get(0);
      final ServiceModel serviceModel = ((ServiceModel) _get_1);
      boolean _isEmpty = context.isEmpty();
      if (_isEmpty) {
        this.adaptToGenericModel(serviceModel, domainModel);
      }
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(this.targetFolder);
      _builder.append(File.separator);
      StringConcatenation _builder_1 = new StringConcatenation();
      {
        boolean _isEmpty_1 = context.isEmpty();
        boolean _not = (!_isEmpty_1);
        if (_not) {
          _builder_1.append(context);
          _builder_1.append(File.separator);
        }
      }
      final String baseFolder = (_builder.toString() + _builder_1);
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append("service");
      {
        boolean _isEmpty_2 = context.isEmpty();
        boolean _not_1 = (!_isEmpty_2);
        if (_not_1) {
          _builder_2.append("_");
          _builder_2.append(context);
        }
      }
      _builder_2.append(".services");
      final String baseFilename = _builder_2.toString();
      StringConcatenation _builder_3 = new StringConcatenation();
      _builder_3.append("extracted_");
      _builder_3.append(baseFilename);
      final String extractedBaseFilename = _builder_3.toString();
      final Consumer<Import> _function = (Import it) -> {
        ImportType _importType = it.getImportType();
        boolean _equals = Objects.equal(_importType, ImportType.DATATYPES);
        if (_equals) {
          StringConcatenation _builder_4 = new StringConcatenation();
          _builder_4.append("extracted_");
          String _importURI = it.getImportURI();
          _builder_4.append(_importURI);
          it.setImportURI(_builder_4.toString());
        }
      };
      serviceModel.getImports().forEach(_function);
      Resource _eResource = serviceModel.eResource();
      StringConcatenation _builder_4 = new StringConcatenation();
      _builder_4.append(baseFolder);
      _builder_4.append(extractedBaseFilename);
      _builder_4.append(".xmi");
      Utils.serialize(_eResource, project, _builder_4.toString());
      final ServiceDslProjections projections = new ServiceDslProjections();
      final IFile extractedFile = projections.extract(serviceModel, project, baseFolder, extractedBaseFilename).get(0);
      final Consumer<Import> _function_1 = (Import it) -> {
        ImportType _importType = it.getImportType();
        boolean _equals = Objects.equal(_importType, ImportType.DATATYPES);
        if (_equals) {
          it.setImportURI(Utils.removePrefix(it.getImportURI(), "extracted_"));
        }
      };
      serviceModel.getImports().forEach(_function_1);
      StringConcatenation _builder_5 = new StringConcatenation();
      IPath _path = this.path(extractedFile);
      _builder_5.append(_path);
      _builder_5.append(File.separator);
      _builder_5.append(baseFilename);
      final String editableFilePath = _builder_5.toString();
      IContainer _parent = project.getParent();
      Path _path_1 = new Path(editableFilePath);
      boolean _exists = _parent.exists(_path_1);
      boolean _not_2 = (!_exists);
      if (_not_2) {
        final IFile editableFile = projections.extract(serviceModel, project, baseFolder, baseFilename).get(0);
        NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
        editableFile.setDerived(false, _nullProgressMonitor);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private boolean adaptToGenericModel(final ServiceModel serviceModel, final DataModel genericDomainModel) {
    boolean _xblockexpression = false;
    {
      final Import genericDomainImport = ServiceFactory.eINSTANCE.createImport();
      genericDomainImport.setName("domain");
      genericDomainImport.setImportURI("domain.data");
      genericDomainImport.setImportType(ImportType.DATATYPES);
      final Function1<ComplexType, String> _function = (ComplexType it) -> {
        return it.buildQualifiedName(".");
      };
      final Function1<ComplexType, ComplexType> _function_1 = (ComplexType it) -> {
        return it;
      };
      final Map<String, ComplexType> genericTypes = IterableExtensions.<ComplexType, String, ComplexType>toMap(EcoreUtil2.<ComplexType>getAllContentsOfType(genericDomainModel, ComplexType.class), _function, _function_1);
      final Function1<Parameter, Boolean> _function_2 = (Parameter it) -> {
        return Boolean.valueOf(((it.getImportedType() != null) && (it.getImportedType().getType() instanceof ComplexType)));
      };
      final Function1<Parameter, Parameter> _function_3 = (Parameter it) -> {
        return it;
      };
      final Function1<Parameter, String> _function_4 = (Parameter it) -> {
        Type _type = it.getImportedType().getType();
        return ((ComplexType) _type).buildQualifiedName(".");
      };
      final Map<Parameter, String> importedTypes = IterableExtensions.<Parameter, Parameter, String>toMap(IterableExtensions.<Parameter>filter(EcoreUtil2.<Parameter>getAllContentsOfType(serviceModel, Parameter.class), _function_2), _function_3, _function_4);
      final BiConsumer<Parameter, String> _function_5 = (Parameter parameter, String typeName) -> {
        parameter.setImportedType(ServiceFactory.eINSTANCE.createImportedType());
        ImportedType _importedType = parameter.getImportedType();
        _importedType.setImport(genericDomainImport);
        ImportedType _importedType_1 = parameter.getImportedType();
        _importedType_1.setType(genericTypes.get(typeName));
      };
      importedTypes.forEach(_function_5);
      serviceModel.getImports().removeAll(serviceModel.getImports());
      _xblockexpression = serviceModel.getImports().add(genericDomainImport);
    }
    return _xblockexpression;
  }
  
  private Resource getUmlResource(final IFile file) {
    try {
      final URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
      final Resource resource = this.buildUml2ResourceSet().getResource(uri, true);
      resource.load(null);
      return resource;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private ResourceSetImpl buildUml2ResourceSet() {
    final ResourceSetImpl rs = new ResourceSetImpl();
    final Map<String, Object> extensionToFactoryMap = rs.getResourceFactoryRegistry().getExtensionToFactoryMap();
    extensionToFactoryMap.put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
    return rs;
  }
  
  private HashMap<String, Resource> boundedContextResources(final IFile file) {
    final Function1<org.eclipse.uml2.uml.Package, String> _function = (org.eclipse.uml2.uml.Package it) -> {
      return it.getName();
    };
    final Iterable<String> boundedContextNames = IterableExtensions.<org.eclipse.uml2.uml.Package, String>map(this.boundedContexts(this.getUmlResource(file)), _function);
    final HashMap<String, Resource> contextResources = CollectionLiterals.<String, Resource>newHashMap();
    final Consumer<String> _function_1 = (String currentContext) -> {
      final Resource resource = this.getUmlResource(file);
      final Function1<org.eclipse.uml2.uml.Package, Boolean> _function_2 = (org.eclipse.uml2.uml.Package it) -> {
        String _name = it.getName();
        return Boolean.valueOf((!Objects.equal(_name, currentContext)));
      };
      final Consumer<org.eclipse.uml2.uml.Package> _function_3 = (org.eclipse.uml2.uml.Package it) -> {
        EcoreUtil2.remove(it);
      };
      IterableExtensions.<org.eclipse.uml2.uml.Package>filter(this.boundedContexts(resource), _function_2).forEach(_function_3);
      contextResources.put(currentContext, resource);
    };
    boundedContextNames.forEach(_function_1);
    contextResources.put("", this.getUmlResource(file));
    return contextResources;
  }
  
  private Iterable<org.eclipse.uml2.uml.Package> boundedContexts(final Resource umlResource) {
    EObject _get = umlResource.getContents().get(0);
    final Model model = ((Model) _get);
    final Function1<org.eclipse.uml2.uml.Package, Boolean> _function = (org.eclipse.uml2.uml.Package it) -> {
      final Function1<Stereotype, Boolean> _function_1 = (Stereotype it_1) -> {
        String _qualifiedName = it_1.getQualifiedName();
        return Boolean.valueOf(Objects.equal(_qualifiedName, "RootElement::BoundedContext"));
      };
      return Boolean.valueOf(IterableExtensions.<Stereotype>exists(it.getAppliedStereotypes(), _function_1));
    };
    return IterableExtensions.<org.eclipse.uml2.uml.Package>filter(EcoreUtil2.<org.eclipse.uml2.uml.Package>getAllContentsOfType(model, org.eclipse.uml2.uml.Package.class), _function);
  }
  
  private HashMap<String, Resource> atlTransformation(final String moduleBasePath, final String moduleName, final List<DdmmToLemmaTransformationHandler.AtlParam> inModels, final List<DdmmToLemmaTransformationHandler.AtlParam> outModels, final List<DdmmToLemmaTransformationHandler.AtlParam> inoutModels) {
    final ExecEnv env = EmftvmFactory.eINSTANCE.createExecEnv();
    final HashMap<String, Resource> metamodels = CollectionLiterals.<String, Resource>newHashMap();
    Map<? extends String, ? extends Resource> _elvis = null;
    Map<String, Resource> _map = null;
    if (inModels!=null) {
      final Function1<DdmmToLemmaTransformationHandler.AtlParam, String> _function = (DdmmToLemmaTransformationHandler.AtlParam it) -> {
        return it.metamodelName;
      };
      final Function1<DdmmToLemmaTransformationHandler.AtlParam, Resource> _function_1 = (DdmmToLemmaTransformationHandler.AtlParam it) -> {
        return it.metamodelResource;
      };
      _map=IterableExtensions.<DdmmToLemmaTransformationHandler.AtlParam, String, Resource>toMap(inModels, _function, _function_1);
    }
    if (_map != null) {
      _elvis = _map;
    } else {
      Map<String, Resource> _emptyMap = CollectionLiterals.<String, Resource>emptyMap();
      _elvis = _emptyMap;
    }
    metamodels.putAll(_elvis);
    Map<? extends String, ? extends Resource> _elvis_1 = null;
    Map<String, Resource> _map_1 = null;
    if (outModels!=null) {
      final Function1<DdmmToLemmaTransformationHandler.AtlParam, String> _function_2 = (DdmmToLemmaTransformationHandler.AtlParam it) -> {
        return it.metamodelName;
      };
      final Function1<DdmmToLemmaTransformationHandler.AtlParam, Resource> _function_3 = (DdmmToLemmaTransformationHandler.AtlParam it) -> {
        return it.metamodelResource;
      };
      _map_1=IterableExtensions.<DdmmToLemmaTransformationHandler.AtlParam, String, Resource>toMap(outModels, _function_2, _function_3);
    }
    if (_map_1 != null) {
      _elvis_1 = _map_1;
    } else {
      Map<String, Resource> _emptyMap_1 = CollectionLiterals.<String, Resource>emptyMap();
      _elvis_1 = _emptyMap_1;
    }
    metamodels.putAll(_elvis_1);
    Map<? extends String, ? extends Resource> _elvis_2 = null;
    Map<String, Resource> _map_2 = null;
    if (inoutModels!=null) {
      final Function1<DdmmToLemmaTransformationHandler.AtlParam, String> _function_4 = (DdmmToLemmaTransformationHandler.AtlParam it) -> {
        return it.metamodelName;
      };
      final Function1<DdmmToLemmaTransformationHandler.AtlParam, Resource> _function_5 = (DdmmToLemmaTransformationHandler.AtlParam it) -> {
        return it.metamodelResource;
      };
      _map_2=IterableExtensions.<DdmmToLemmaTransformationHandler.AtlParam, String, Resource>toMap(inoutModels, _function_4, _function_5);
    }
    if (_map_2 != null) {
      _elvis_2 = _map_2;
    } else {
      Map<String, Resource> _emptyMap_2 = CollectionLiterals.<String, Resource>emptyMap();
      _elvis_2 = _emptyMap_2;
    }
    metamodels.putAll(_elvis_2);
    final BiConsumer<String, Resource> _function_6 = (String name, Resource resource) -> {
      final Metamodel metamodel = EmftvmFactory.eINSTANCE.createMetamodel();
      metamodel.setResource(resource);
      env.registerMetaModel(name, metamodel);
    };
    metamodels.forEach(_function_6);
    final DdmmToLemmaTransformationHandler.Counter inmemIndexCounter = new DdmmToLemmaTransformationHandler.Counter();
    final HashMap<String, Resource> models = CollectionLiterals.<String, Resource>newHashMap();
    Map<? extends String, ? extends Resource> _elvis_3 = null;
    Map<String, Resource> _map_3 = null;
    if (inModels!=null) {
      final Function1<DdmmToLemmaTransformationHandler.AtlParam, String> _function_7 = (DdmmToLemmaTransformationHandler.AtlParam it) -> {
        return it.modelName;
      };
      final Function1<DdmmToLemmaTransformationHandler.AtlParam, Resource> _function_8 = (DdmmToLemmaTransformationHandler.AtlParam it) -> {
        Resource _xblockexpression = null;
        {
          final org.eclipse.m2m.atl.emftvm.Model model = EmftvmFactory.eINSTANCE.createModel();
          Resource _elvis_4 = null;
          if (it.modelResource != null) {
            _elvis_4 = it.modelResource;
          } else {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("_inmem");
            int _increment = inmemIndexCounter.increment();
            _builder.append(_increment);
            _builder.append(".xmi");
            Resource _createResource = this.buildTransformationResourceSet().createResource(
              URI.createFileURI(_builder.toString()));
            _elvis_4 = _createResource;
          }
          final Resource resource = _elvis_4;
          model.setResource(resource);
          env.registerInputModel(it.modelName, model);
          _xblockexpression = resource;
        }
        return _xblockexpression;
      };
      _map_3=IterableExtensions.<DdmmToLemmaTransformationHandler.AtlParam, String, Resource>toMap(inModels, _function_7, _function_8);
    }
    if (_map_3 != null) {
      _elvis_3 = _map_3;
    } else {
      Map<String, Resource> _emptyMap_3 = CollectionLiterals.<String, Resource>emptyMap();
      _elvis_3 = _emptyMap_3;
    }
    models.putAll(_elvis_3);
    Map<? extends String, ? extends Resource> _elvis_4 = null;
    Map<String, Resource> _map_4 = null;
    if (outModels!=null) {
      final Function1<DdmmToLemmaTransformationHandler.AtlParam, String> _function_9 = (DdmmToLemmaTransformationHandler.AtlParam it) -> {
        return it.modelName;
      };
      final Function1<DdmmToLemmaTransformationHandler.AtlParam, Resource> _function_10 = (DdmmToLemmaTransformationHandler.AtlParam it) -> {
        Resource _xblockexpression = null;
        {
          final org.eclipse.m2m.atl.emftvm.Model model = EmftvmFactory.eINSTANCE.createModel();
          Resource _elvis_5 = null;
          if (it.modelResource != null) {
            _elvis_5 = it.modelResource;
          } else {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("_inmem");
            int _increment = inmemIndexCounter.increment();
            _builder.append(_increment);
            _builder.append(".xmi");
            Resource _createResource = this.buildTransformationResourceSet().createResource(
              URI.createFileURI(_builder.toString()));
            _elvis_5 = _createResource;
          }
          final Resource resource = _elvis_5;
          model.setResource(resource);
          env.registerOutputModel(it.modelName, model);
          _xblockexpression = resource;
        }
        return _xblockexpression;
      };
      _map_4=IterableExtensions.<DdmmToLemmaTransformationHandler.AtlParam, String, Resource>toMap(outModels, _function_9, _function_10);
    }
    if (_map_4 != null) {
      _elvis_4 = _map_4;
    } else {
      Map<String, Resource> _emptyMap_4 = CollectionLiterals.<String, Resource>emptyMap();
      _elvis_4 = _emptyMap_4;
    }
    models.putAll(_elvis_4);
    Map<? extends String, ? extends Resource> _elvis_5 = null;
    Map<String, Resource> _map_5 = null;
    if (inoutModels!=null) {
      final Function1<DdmmToLemmaTransformationHandler.AtlParam, String> _function_11 = (DdmmToLemmaTransformationHandler.AtlParam it) -> {
        return it.modelName;
      };
      final Function1<DdmmToLemmaTransformationHandler.AtlParam, Resource> _function_12 = (DdmmToLemmaTransformationHandler.AtlParam it) -> {
        Resource _xblockexpression = null;
        {
          final org.eclipse.m2m.atl.emftvm.Model model = EmftvmFactory.eINSTANCE.createModel();
          Resource _elvis_6 = null;
          if (it.modelResource != null) {
            _elvis_6 = it.modelResource;
          } else {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("_inmem");
            int _increment = inmemIndexCounter.increment();
            _builder.append(_increment);
            _builder.append(".xmi");
            Resource _createResource = this.buildTransformationResourceSet().createResource(
              URI.createFileURI(_builder.toString()));
            _elvis_6 = _createResource;
          }
          final Resource resource = _elvis_6;
          model.setResource(resource);
          env.registerInOutModel(it.modelName, model);
          _xblockexpression = resource;
        }
        return _xblockexpression;
      };
      _map_5=IterableExtensions.<DdmmToLemmaTransformationHandler.AtlParam, String, Resource>toMap(inoutModels, _function_11, _function_12);
    }
    if (_map_5 != null) {
      _elvis_5 = _map_5;
    } else {
      Map<String, Resource> _emptyMap_5 = CollectionLiterals.<String, Resource>emptyMap();
      _elvis_5 = _emptyMap_5;
    }
    models.putAll(_elvis_5);
    ResourceSetImpl _buildTransformationResourceSet = this.buildTransformationResourceSet();
    final DefaultModuleResolver mr = new DefaultModuleResolver(moduleBasePath, _buildTransformationResourceSet);
    final TimingData td = new TimingData();
    try {
      env.loadModule(mr, moduleName);
      td.finishLoading();
      env.run(td);
      td.finish();
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception ex = (Exception)_t;
        ex.printStackTrace();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return models;
  }
  
  private DdmmToLemmaTransformationHandler.AtlParam atlParam(final String metamodelName, final Resource metamodelResource, final String modelName, final Resource modelResource) {
    return new DdmmToLemmaTransformationHandler.AtlParam(metamodelName, metamodelResource, modelName, modelResource);
  }
  
  private ResourceSetImpl buildTransformationResourceSet() {
    final ResourceSetImpl rs = new ResourceSetImpl();
    final Map<String, Object> extensionToFactoryMap = rs.getResourceFactoryRegistry().getExtensionToFactoryMap();
    XMIResourceFactoryImpl _xMIResourceFactoryImpl = new XMIResourceFactoryImpl();
    extensionToFactoryMap.put("xmi", _xMIResourceFactoryImpl);
    EMFTVMResourceFactoryImpl _eMFTVMResourceFactoryImpl = new EMFTVMResourceFactoryImpl();
    extensionToFactoryMap.put("emftvm", _eMFTVMResourceFactoryImpl);
    EcoreResourceFactoryImpl _ecoreResourceFactoryImpl = new EcoreResourceFactoryImpl();
    extensionToFactoryMap.put("ecore", _ecoreResourceFactoryImpl);
    return rs;
  }
  
  private void copyTo(final IFile source, final String targetFileName, final boolean isDerived) {
    try {
      final IPath targetPathWithoutProject = this.removePrefix(new Path(targetFileName), source.getProject().getName());
      String targetFileNameWithoutProject = targetPathWithoutProject.toString();
      final IFile target = source.getProject().getFile(targetFileNameWithoutProject);
      boolean _exists = target.exists();
      boolean _not = (!_exists);
      if (_not) {
        NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
        target.create(null, IResource.NONE, _nullProgressMonitor);
      }
      InputStream _contents = source.getContents();
      NullProgressMonitor _nullProgressMonitor_1 = new NullProgressMonitor();
      target.setContents(_contents, true, false, _nullProgressMonitor_1);
      NullProgressMonitor _nullProgressMonitor_2 = new NullProgressMonitor();
      target.setDerived(isDerived, _nullProgressMonitor_2);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private IPath path(final IFile file) {
    return file.getFullPath().removeLastSegments(1);
  }
  
  private IPath removePrefix(final Path p, final String prefix) {
    IPath _xifexpression = null;
    if ((p.isEmpty() || (!Objects.equal(p.segment(0), prefix)))) {
      _xifexpression = p;
    } else {
      _xifexpression = p.removeFirstSegments(1);
    }
    return _xifexpression;
  }
  
  private String path(final String fullPath) {
    return FilenameUtils.getPath(fullPath);
  }
  
  private String filenameWithoutExt(final String fullPath) {
    return FilenameUtils.getBaseName(fullPath);
  }
}
