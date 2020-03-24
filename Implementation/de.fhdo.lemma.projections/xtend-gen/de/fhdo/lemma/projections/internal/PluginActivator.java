package de.fhdo.lemma.projections.internal;

import com.google.inject.Injector;
import de.fhdo.lemma.data.datadsl.ui.internal.DatadslActivator;
import de.fhdo.lemma.projections.DataDslProjections;
import de.fhdo.lemma.projections.Projections;
import de.fhdo.lemma.projections.ServiceDslProjections;
import de.fhdo.lemma.projections.internal.RuntimeModule;
import de.fhdo.lemma.servicedsl.ui.internal.ServicedslActivator;
import java.util.Arrays;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.osgi.framework.BundleContext;

@SuppressWarnings("all")
public class PluginActivator extends AbstractUIPlugin {
  private static final Logger LOGGER = Logger.getLogger(PluginActivator.class);
  
  private static PluginActivator INSTANCE;
  
  private Map<String, Injector> injectors = CollectionLiterals.<String, Injector>newHashMap();
  
  @Override
  public void start(final BundleContext context) throws Exception {
    super.start(context);
    PluginActivator.INSTANCE = this;
  }
  
  @Override
  public void stop(final BundleContext context) throws Exception {
    super.stop(context);
    PluginActivator.INSTANCE = null;
  }
  
  public static PluginActivator getInstance() {
    return PluginActivator.INSTANCE;
  }
  
  public Injector getInjector(final String language) {
    Injector injector = this.injectors.get(language);
    if ((injector != null)) {
      return injector;
    }
    try {
      Injector _switchResult = null;
      if (language != null) {
        switch (language) {
          case DatadslActivator.DE_FHDO_LEMMA_DATA_DATADSL:
            _switchResult = DatadslActivator.getInstance().getInjector(language);
            break;
          case ServicedslActivator.DE_FHDO_LEMMA_SERVICEDSL:
            _switchResult = ServicedslActivator.getInstance().getInjector(language);
            break;
          default:
            _switchResult = null;
            break;
        }
      } else {
        _switchResult = null;
      }
      final Injector languageInjector = _switchResult;
      RuntimeModule _runtimeModule = new RuntimeModule();
      injector = languageInjector.createChildInjector(_runtimeModule);
      this.injectors.put(language, injector);
      return injector;
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception ex = (Exception)_t;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("Failed to create injector for ");
        _builder.append(language);
        PluginActivator.LOGGER.error(_builder);
        PluginActivator.LOGGER.error(ex.getMessage(), ex);
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("Failed to create injector for ");
        _builder_1.append(language);
        throw new RuntimeException(_builder_1.toString(), ex);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  public void injectMembers(final String language, final Object target) {
    this.getInjector(language).injectMembers(target);
  }
  
  protected void _injectMembers(final Projections target) {
    throw new UnsupportedOperationException();
  }
  
  protected void _injectMembers(final DataDslProjections target) {
    this.getInjector(DatadslActivator.DE_FHDO_LEMMA_DATA_DATADSL).injectMembers(target);
  }
  
  protected void _injectMembers(final ServiceDslProjections target) {
    this.getInjector(ServicedslActivator.DE_FHDO_LEMMA_SERVICEDSL).injectMembers(target);
  }
  
  public void injectMembers(final Projections target) {
    if (target instanceof DataDslProjections) {
      _injectMembers((DataDslProjections)target);
      return;
    } else if (target instanceof ServiceDslProjections) {
      _injectMembers((ServiceDslProjections)target);
      return;
    } else if (target != null) {
      _injectMembers(target);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(target).toString());
    }
  }
}
