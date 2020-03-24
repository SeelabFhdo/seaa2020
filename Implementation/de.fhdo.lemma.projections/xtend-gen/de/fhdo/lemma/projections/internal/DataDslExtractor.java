package de.fhdo.lemma.projections.internal;

import de.fhdo.lemma.data.ComplexType;
import de.fhdo.lemma.data.Context;
import de.fhdo.lemma.data.DataField;
import de.fhdo.lemma.data.DataFieldFeature;
import de.fhdo.lemma.data.DataModel;
import de.fhdo.lemma.data.DataOperation;
import de.fhdo.lemma.data.DataOperationFeature;
import de.fhdo.lemma.data.DataOperationParameter;
import de.fhdo.lemma.data.DataStructure;
import de.fhdo.lemma.data.DataStructureFeature;
import de.fhdo.lemma.data.ListType;
import de.fhdo.lemma.data.PrimitiveType;
import de.fhdo.lemma.data.Type;
import de.fhdo.lemma.projections.internal.ExtractorGeneratorContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.generator.AbstractGenerator;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.MapExtensions;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class DataDslExtractor extends AbstractGenerator {
  private static final Map<Enum<?>, String> FEATURE_TO_TEXT_MAPPINGS = Collections.<Enum<?>, String>unmodifiableMap(CollectionLiterals.<Enum<?>, String>newHashMap(Pair.<DataStructureFeature, String>of(DataStructureFeature.AGGREGATE, "aggregate"), Pair.<DataStructureFeature, String>of(DataStructureFeature.APPLICATION_SERVICE, "applicationService"), Pair.<DataStructureFeature, String>of(DataStructureFeature.DOMAIN_EVENT, "domainEvent"), Pair.<DataStructureFeature, String>of(DataStructureFeature.DOMAIN_SERVICE, "domainService"), Pair.<DataStructureFeature, String>of(DataStructureFeature.ENTITY, "entity"), Pair.<DataStructureFeature, String>of(DataStructureFeature.FACTORY, "factory"), Pair.<DataStructureFeature, String>of(DataStructureFeature.INFRASTRUCTURE_SERVICE, "infrastructureService"), Pair.<DataStructureFeature, String>of(DataStructureFeature.REPOSITORY, "repository"), Pair.<DataStructureFeature, String>of(DataStructureFeature.SERVICE, "service"), Pair.<DataStructureFeature, String>of(DataStructureFeature.SPECIFICATION, "specification"), Pair.<DataStructureFeature, String>of(DataStructureFeature.VALUE_OBJECT, "valueObject"), Pair.<DataFieldFeature, String>of(DataFieldFeature.IDENTIFIER, "identifier"), Pair.<DataFieldFeature, String>of(DataFieldFeature.NEVER_EMPTY, "neverEmpty"), Pair.<DataFieldFeature, String>of(DataFieldFeature.PART, "part"), Pair.<DataOperationFeature, String>of(DataOperationFeature.CLOSURE, "closure"), Pair.<DataOperationFeature, String>of(DataOperationFeature.IDENTIFIER, "identifier"), Pair.<DataOperationFeature, String>of(DataOperationFeature.SIDE_EFFECT_FREE, "sideEffectFree"), Pair.<DataOperationFeature, String>of(DataOperationFeature.VALIDATOR, "validator")));
  
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
    final String targetFileName = ((ExtractorGeneratorContext) context).getTargetFileName();
    EObject _get = input.getContents().get(0);
    final DataModel domainModel = ((DataModel) _get);
    fsa.generateFile(targetFileName, this.generateContexts(domainModel.getContexts()));
  }
  
  private String generateContexts(final List<Context> contexts) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _hasElements = false;
      for(final Context c : contexts) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate("\n", "");
        }
        _builder.append("context ");
        String _name = c.getName();
        _builder.append(_name);
        _builder.append(" {");
        _builder.newLineIfNotEmpty();
        {
          EList<ComplexType> _complexTypes = c.getComplexTypes();
          boolean _hasElements_1 = false;
          for(final ComplexType t : _complexTypes) {
            if (!_hasElements_1) {
              _hasElements_1 = true;
            } else {
              _builder.appendImmediate("\n", "");
            }
            _builder.append("\t");
            String _generateComplexTypeDefinition = this.generateComplexTypeDefinition(t);
            _builder.append(_generateComplexTypeDefinition);
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("}");
        _builder.newLine();
      }
    }
    return _builder.toString();
  }
  
  private String _generateComplexTypeDefinition(final ListType list) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("list ");
    String _name = list.getName();
    _builder.append(_name);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    {
      boolean _isEmpty = list.getDataFields().isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        {
          EList<DataField> _dataFields = list.getDataFields();
          boolean _hasElements = false;
          for(final DataField f : _dataFields) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(",", "\t");
            }
            _builder.append("\t");
            _builder.append("\t", "\t");
            String _generateDataField = this.generateDataField(f);
            _builder.append(_generateDataField, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      } else {
        _builder.append("\t");
        _builder.append("\t", "\t");
        String _generateTypeReference = this.generateTypeReference(list.getPrimitiveType());
        _builder.append(_generateTypeReference, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("}");
    return _builder.toString();
  }
  
  private String _generateComplexTypeDefinition(final DataStructure structure) {
    final StringBuilder preamble = new StringBuilder();
    preamble.append(this.<DataStructureFeature>generateFeatures(structure.getFeatures()));
    preamble.append(" ");
    preamble.append(this.generateSuperReference(structure));
    String preambleString = preamble.toString();
    boolean _isEmpty = preambleString.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(" ");
      String _trim = preambleString.trim();
      _builder.append(_trim, " ");
      _builder.append(" ");
      preambleString = _builder.toString();
    }
    if ((structure.getDataFields().isEmpty() && structure.getOperations().isEmpty())) {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("structure ");
      String _name = structure.getName();
      _builder_1.append(_name);
      _builder_1.append(preambleString);
      _builder_1.append("{}");
      return _builder_1.toString();
    }
    int _size = structure.getDataFields().size();
    final int lastFieldIndex = (_size - 1);
    int currentFieldIndex = 0;
    StringConcatenation _builder_2 = new StringConcatenation();
    _builder_2.append("structure ");
    String _name_1 = structure.getName();
    _builder_2.append(_name_1);
    _builder_2.append(preambleString);
    _builder_2.append("{");
    _builder_2.newLineIfNotEmpty();
    {
      EList<DataField> _dataFields = structure.getDataFields();
      for(final DataField f : _dataFields) {
        {
          int _plusPlus = currentFieldIndex++;
          boolean _equals = (lastFieldIndex == _plusPlus);
          if (_equals) {
            _builder_2.append("\t");
            _builder_2.append("\t", "\t");
            String _generateDataField = this.generateDataField(f);
            _builder_2.append(_generateDataField, "\t");
            {
              boolean _isEmpty_1 = structure.getOperations().isEmpty();
              boolean _not_1 = (!_isEmpty_1);
              if (_not_1) {
                _builder_2.append(",");
              }
            }
            _builder_2.newLineIfNotEmpty();
          } else {
            _builder_2.append("\t");
            _builder_2.append("\t", "\t");
            String _generateDataField_1 = this.generateDataField(f);
            _builder_2.append(_generateDataField_1, "\t");
            _builder_2.append(",");
            _builder_2.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder_2.append("\t");
    {
      if (((!structure.getDataFields().isEmpty()) && (!structure.getOperations().isEmpty()))) {
        _builder_2.append("\n", "\t");
      }
    }
    _builder_2.newLineIfNotEmpty();
    {
      EList<DataOperation> _operations = structure.getOperations();
      boolean _hasElements = false;
      for(final DataOperation o : _operations) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder_2.appendImmediate(",", "\t");
        }
        _builder_2.append("\t");
        _builder_2.append("\t", "\t");
        String _generateDataOperation = this.generateDataOperation(o);
        _builder_2.append(_generateDataOperation, "\t");
        _builder_2.newLineIfNotEmpty();
      }
    }
    _builder_2.append("\t");
    _builder_2.append("}");
    return _builder_2.toString();
  }
  
  private <E extends Enumerator> String generateFeatures(final List<E> features) {
    final Function2<Enum<?>, String, Boolean> _function = (Enum<?> f, String t) -> {
      return Boolean.valueOf(features.contains(f));
    };
    final Map<Enum<?>, String> supportedFeatures = MapExtensions.<Enum<?>, String>filter(DataDslExtractor.FEATURE_TO_TEXT_MAPPINGS, _function);
    boolean _isEmpty = supportedFeatures.isEmpty();
    if (_isEmpty) {
      return "";
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<");
    {
      Collection<String> _values = supportedFeatures.values();
      boolean _hasElements = false;
      for(final String f : _values) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(", ", "");
        }
        _builder.append(f);
      }
    }
    _builder.append(">");
    return _builder.toString();
  }
  
  private String generateSuperReference(final DataStructure structure) {
    String _xifexpression = null;
    DataStructure _super = structure.getSuper();
    boolean _tripleNotEquals = (_super != null);
    if (_tripleNotEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("extends ");
      String _name = structure.getSuper().getName();
      _builder.append(_name);
      _xifexpression = _builder.toString();
    } else {
      _xifexpression = "";
    }
    return _xifexpression;
  }
  
  private String generateDataField(final DataField dataField) {
    final Type effectiveType = dataField.getEffectiveType();
    String _xifexpression = null;
    if ((effectiveType == null)) {
      _xifexpression = "unspecified";
    } else {
      String _xifexpression_1 = null;
      boolean _hasTypeFromOtherContext = this.hasTypeFromOtherContext(dataField, effectiveType);
      if (_hasTypeFromOtherContext) {
        StringConcatenation _builder = new StringConcatenation();
        Context _context = dataField.getComplexType().getContext();
        _builder.append(_context);
        _builder.append(".");
        String _generateTypeReference = this.generateTypeReference(effectiveType);
        _builder.append(_generateTypeReference);
        _xifexpression_1 = _builder.toString();
      } else {
        _xifexpression_1 = this.generateTypeReference(effectiveType);
      }
      _xifexpression = _xifexpression_1;
    }
    final String generatedType = _xifexpression;
    final StringBuffer preamble = new StringBuffer();
    boolean _isHidden = dataField.isHidden();
    if (_isHidden) {
      preamble.append("hide ");
    }
    boolean _isImmutable = dataField.isImmutable();
    if (_isImmutable) {
      preamble.append("immutable ");
    }
    String preambleString = preamble.toString();
    final String features = this.<DataFieldFeature>generateFeatures(dataField.getFeatures());
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append(preambleString);
    _builder_1.append(generatedType);
    _builder_1.append(" ");
    String _name = dataField.getName();
    _builder_1.append(_name);
    StringConcatenation _builder_2 = new StringConcatenation();
    {
      boolean _isEmpty = features.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder_2.append(" ");
        _builder_2.append(features);
      }
    }
    return (_builder_1.toString() + _builder_2);
  }
  
  private boolean hasTypeFromOtherContext(final EObject container, final Type type) {
    if ((!(type instanceof ComplexType))) {
      return false;
    }
    final Context typeContext = EcoreUtil2.<Context>getContainerOfType(type, Context.class);
    final Context parentContext = EcoreUtil2.<Context>getContainerOfType(type, Context.class);
    return (typeContext != parentContext);
  }
  
  private String _generateTypeReference(final PrimitiveType primitiveType) {
    return primitiveType.getTypeName();
  }
  
  private String _generateTypeReference(final ComplexType complexType) {
    return complexType.getName();
  }
  
  private String generateDataOperation(final DataOperation dataOperation) {
    final Type effectiveReturnType = dataOperation.getPrimitiveOrComplexReturnType();
    String _xifexpression = null;
    if ((effectiveReturnType == null)) {
      _xifexpression = "procedure";
    } else {
      String _xifexpression_1 = null;
      boolean _hasTypeFromOtherContext = this.hasTypeFromOtherContext(dataOperation, effectiveReturnType);
      if (_hasTypeFromOtherContext) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("function ");
        Context _context = dataOperation.getComplexReturnType().getContext();
        _builder.append(_context);
        _builder.append(".");
        String _generateTypeReference = this.generateTypeReference(effectiveReturnType);
        _xifexpression_1 = (_builder.toString() + _generateTypeReference);
      } else {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("function ");
        String _generateTypeReference_1 = this.generateTypeReference(effectiveReturnType);
        _builder_1.append(_generateTypeReference_1);
        _xifexpression_1 = _builder_1.toString();
      }
      _xifexpression = _xifexpression_1;
    }
    final String generatedType = _xifexpression;
    final Function1<DataOperationParameter, String> _function = (DataOperationParameter it) -> {
      return this.generateDataOperationParameter(it);
    };
    final List<String> generatedParameters = ListExtensions.<DataOperationParameter, String>map(dataOperation.getParameters(), _function);
    final String generatedFeatures = this.<DataOperationFeature>generateFeatures(dataOperation.getFeatures());
    StringConcatenation _builder_2 = new StringConcatenation();
    _builder_2.append(generatedType);
    _builder_2.append(" ");
    String _name = dataOperation.getName();
    _builder_2.append(_name);
    StringConcatenation _builder_3 = new StringConcatenation();
    _builder_3.append("(");
    String _join = IterableExtensions.join(generatedParameters, ", ");
    _builder_3.append(_join);
    _builder_3.append(")");
    String _plus = (_builder_2.toString() + _builder_3);
    StringConcatenation _builder_4 = new StringConcatenation();
    {
      boolean _isEmpty = generatedFeatures.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder_4.append(generatedFeatures);
      }
    }
    return (_plus + _builder_4);
  }
  
  private String generateDataOperationParameter(final DataOperationParameter parameter) {
    final Type effectiveType = parameter.getEffectiveType();
    String _xifexpression = null;
    if ((effectiveType == null)) {
      _xifexpression = "unspecified";
    } else {
      String _xifexpression_1 = null;
      boolean _hasTypeFromOtherContext = this.hasTypeFromOtherContext(parameter, effectiveType);
      if (_hasTypeFromOtherContext) {
        StringConcatenation _builder = new StringConcatenation();
        Context _context = parameter.getComplexType().getContext();
        _builder.append(_context);
        _builder.append(".");
        String _generateTypeReference = this.generateTypeReference(effectiveType);
        _builder.append(_generateTypeReference);
        _xifexpression_1 = _builder.toString();
      } else {
        _xifexpression_1 = this.generateTypeReference(effectiveType);
      }
      _xifexpression = _xifexpression_1;
    }
    final String generatedType = _xifexpression;
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append(generatedType);
    _builder_1.append(" ");
    String _name = parameter.getName();
    _builder_1.append(_name);
    return _builder_1.toString();
  }
  
  private String generateComplexTypeDefinition(final ComplexType structure) {
    if (structure instanceof DataStructure) {
      return _generateComplexTypeDefinition((DataStructure)structure);
    } else if (structure instanceof ListType) {
      return _generateComplexTypeDefinition((ListType)structure);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(structure).toString());
    }
  }
  
  private String generateTypeReference(final Type complexType) {
    if (complexType instanceof ComplexType) {
      return _generateTypeReference((ComplexType)complexType);
    } else if (complexType instanceof PrimitiveType) {
      return _generateTypeReference((PrimitiveType)complexType);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(complexType).toString());
    }
  }
}
