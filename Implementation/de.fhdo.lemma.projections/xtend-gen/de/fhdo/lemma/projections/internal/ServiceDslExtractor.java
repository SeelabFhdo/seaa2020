package de.fhdo.lemma.projections.internal;

import de.fhdo.lemma.data.ComplexType;
import de.fhdo.lemma.data.PrimitiveType;
import de.fhdo.lemma.data.Type;
import de.fhdo.lemma.projections.internal.ExtractorGeneratorContext;
import de.fhdo.lemma.service.Import;
import de.fhdo.lemma.service.ImportType;
import de.fhdo.lemma.service.ImportedType;
import de.fhdo.lemma.service.Interface;
import de.fhdo.lemma.service.Microservice;
import de.fhdo.lemma.service.MicroserviceType;
import de.fhdo.lemma.service.Operation;
import de.fhdo.lemma.service.Parameter;
import de.fhdo.lemma.service.ServiceModel;
import de.fhdo.lemma.service.Visibility;
import de.fhdo.lemma.technology.CommunicationType;
import de.fhdo.lemma.technology.ExchangePattern;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.AbstractGenerator;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class ServiceDslExtractor extends AbstractGenerator {
  private static final String ID_PATTERN = "(\\^?)([a-zA-Z_])\\w*";
  
  private static final String QUALIFIED_NAME_PATTERN = new Function0<String>() {
    @Override
    public String apply() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(ServiceDslExtractor.ID_PATTERN);
      _builder.append("(\\.");
      _builder.append(ServiceDslExtractor.ID_PATTERN);
      _builder.append(")*");
      return _builder.toString();
    }
  }.apply();
  
  private static final String QUALIFIED_NAME_WITH_AT_LEAST_ONE_LEVEL_PATTERN = new Function0<String>() {
    @Override
    public String apply() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(ServiceDslExtractor.ID_PATTERN);
      _builder.append("\\.");
      String _plus = (_builder.toString() + 
        ServiceDslExtractor.QUALIFIED_NAME_PATTERN);
      return _plus;
    }
  }.apply();
  
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
    final String targetFileName = ((ExtractorGeneratorContext) context).getTargetFileName();
    EObject _get = input.getContents().get(0);
    final ServiceModel serviceModel = ((ServiceModel) _get);
    fsa.generateFile(targetFileName, this.generate(serviceModel));
  }
  
  private String generate(final ServiceModel serviceModel) {
    final ArrayList<String> imports = CollectionLiterals.<String>newArrayList();
    final Consumer<Import> _function = (Import it) -> {
      imports.add(this.generate(it));
    };
    serviceModel.getImports().forEach(_function);
    String _xifexpression = null;
    boolean _isEmpty = imports.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      String _join = String.join("\n", imports);
      _xifexpression = (_join + "\n\n");
    } else {
      _xifexpression = "";
    }
    final String importStatements = _xifexpression;
    final Function1<Microservice, String> _function_1 = (Microservice it) -> {
      return this.generate(it);
    };
    final String microservices = String.join("\n\n", ListExtensions.<Microservice, String>map(serviceModel.getMicroservices(), _function_1));
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(importStatements);
    _builder.append(microservices);
    return _builder.toString();
  }
  
  private String generate(final Import import_) {
    String _switchResult = null;
    ImportType _importType = import_.getImportType();
    if (_importType != null) {
      switch (_importType) {
        case DATATYPES:
          _switchResult = "datatypes";
          break;
        case MICROSERVICES:
          _switchResult = "microservices";
          break;
        case TECHNOLOGY:
          _switchResult = "technology";
          break;
        default:
          _switchResult = null;
          break;
      }
    } else {
      _switchResult = null;
    }
    final String importTypeKeyword = _switchResult;
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import ");
    _builder.append(importTypeKeyword);
    _builder.append(" from \"");
    String _importURI = import_.getImportURI();
    _builder.append(_importURI);
    _builder.append("\" as ");
    String _name = import_.getName();
    _builder.append(_name);
    return _builder.toString();
  }
  
  private String generate(final Microservice service) {
    StringConcatenation _builder = new StringConcatenation();
    String _generate = this.generate(service.getVisibility());
    _builder.append(_generate);
    _builder.append(" ");
    String _generate_1 = this.generate(service.getType());
    _builder.append(_generate_1);
    final String preamble = _builder.toString();
    final Function1<Interface, Boolean> _function = (Interface it) -> {
      boolean _isEmpty = it.getOperations().isEmpty();
      return Boolean.valueOf((!_isEmpty));
    };
    final boolean hasInterfaceOperations = IterableExtensions.<Interface>exists(service.getInterfaces(), _function);
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append(preamble);
    _builder_1.append(" microservice ");
    String _lemmaName = this.lemmaName(service);
    _builder_1.append(_lemmaName);
    _builder_1.append(" {");
    _builder_1.newLineIfNotEmpty();
    {
      if (hasInterfaceOperations) {
        {
          EList<Interface> _interfaces = service.getInterfaces();
          for(final Interface iface : _interfaces) {
            _builder_1.append("\t");
            String _generate_2 = this.generate(iface);
            _builder_1.append(_generate_2, "\t");
            _builder_1.newLineIfNotEmpty();
          }
        }
      } else {
        _builder_1.append("\t");
        _builder_1.append("[DEFINE_OPERATIONS]");
        _builder_1.newLine();
      }
    }
    _builder_1.append("}");
    return _builder_1.toString();
  }
  
  private String lemmaName(final Microservice service) {
    String _xifexpression = null;
    boolean _matches = service.getName().matches(ServiceDslExtractor.QUALIFIED_NAME_WITH_AT_LEAST_ONE_LEVEL_PATTERN);
    if (_matches) {
      _xifexpression = service.getName();
    } else {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("ADD_QUALIFYING_PART.");
      String _name = service.getName();
      _builder.append(_name);
      _xifexpression = _builder.toString();
    }
    return _xifexpression;
  }
  
  private String generate(final Visibility visibility) {
    String _switchResult = null;
    if (visibility != null) {
      switch (visibility) {
        case INTERNAL:
          _switchResult = "internal";
          break;
        case PUBLIC:
          _switchResult = "public";
          break;
        default:
          _switchResult = "architecture";
          break;
      }
    } else {
      _switchResult = "architecture";
    }
    return _switchResult;
  }
  
  private String generate(final MicroserviceType type) {
    String _switchResult = null;
    if (type != null) {
      switch (type) {
        case FUNCTIONAL:
          _switchResult = "functional";
          break;
        case INFRASTRUCTURE:
          _switchResult = "infrastructure";
          break;
        case UTILITY:
          _switchResult = "utility";
          break;
        default:
          _switchResult = null;
          break;
      }
    } else {
      _switchResult = null;
    }
    return _switchResult;
  }
  
  private String generate(final Interface iface) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("interface ");
    String _name = iface.getName();
    _builder.append(_name);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    {
      EList<Operation> _operations = iface.getOperations();
      for(final Operation o : _operations) {
        _builder.append("\t");
        String _generate = this.generate(o);
        _builder.append(_generate, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    return _builder.toString();
  }
  
  private String generate(final Operation operation) {
    final Function1<Parameter, String> _function = (Parameter it) -> {
      return this.generate(it);
    };
    final String parameters = String.join(", ", ListExtensions.<Parameter, String>map(operation.getParameters(), _function));
    StringConcatenation _builder = new StringConcatenation();
    String _name = operation.getName();
    _builder.append(_name);
    _builder.append("(");
    _builder.append(parameters);
    _builder.append(");");
    return _builder.toString();
  }
  
  private String generate(final Parameter parameter) {
    final ArrayList<String> preamble = CollectionLiterals.<String>newArrayList();
    preamble.add(this.generate(parameter.getCommunicationType()));
    ExchangePattern _exchangePattern = parameter.getExchangePattern();
    boolean _tripleNotEquals = (_exchangePattern != ExchangePattern.IN);
    if (_tripleNotEquals) {
      preamble.add(this.generate(parameter.getExchangePattern()));
    }
    StringConcatenation _builder = new StringConcatenation();
    String _join = String.join(" ", preamble);
    _builder.append(_join);
    _builder.append(" ");
    String _name = parameter.getName();
    _builder.append(_name);
    _builder.append(" : ");
    String _generateType = this.generateType(parameter);
    _builder.append(_generateType);
    return _builder.toString();
  }
  
  private String generate(final CommunicationType type) {
    String _switchResult = null;
    if (type != null) {
      switch (type) {
        case ASYNCHRONOUS:
          _switchResult = "async";
          break;
        case SYNCHRONOUS:
          _switchResult = "sync";
          break;
        default:
          _switchResult = null;
          break;
      }
    } else {
      _switchResult = null;
    }
    return _switchResult;
  }
  
  private String generate(final ExchangePattern pattern) {
    String _switchResult = null;
    if (pattern != null) {
      switch (pattern) {
        case OUT:
          _switchResult = "out";
          break;
        case INOUT:
          _switchResult = "inout";
          break;
        default:
          _switchResult = "in";
          break;
      }
    } else {
      _switchResult = "in";
    }
    return _switchResult;
  }
  
  private String generateType(final Parameter parameter) {
    String _xifexpression = null;
    PrimitiveType _primitiveType = parameter.getPrimitiveType();
    boolean _tripleNotEquals = (_primitiveType != null);
    if (_tripleNotEquals) {
      PrimitiveType _primitiveType_1 = parameter.getPrimitiveType();
      _xifexpression = this.generate(((PrimitiveType) _primitiveType_1));
    } else {
      _xifexpression = this.generate(parameter.getImportedType());
    }
    return _xifexpression;
  }
  
  private String generate(final PrimitiveType type) {
    return type.getTypeName();
  }
  
  private String generate(final ImportedType importedType) {
    ImportType _importType = importedType.getImport().getImportType();
    boolean _tripleNotEquals = (_importType != ImportType.DATATYPES);
    if (_tripleNotEquals) {
      return null;
    }
    Type _type = importedType.getType();
    final String importedTypeName = ((ComplexType) _type).buildQualifiedName(".");
    StringConcatenation _builder = new StringConcatenation();
    String _name = importedType.getImport().getName();
    _builder.append(_name);
    _builder.append("::");
    _builder.append(importedTypeName);
    return _builder.toString();
  }
}
