package de.fhdo.lemma.projections.internal

import org.eclipse.ui.plugin.AbstractUIPlugin
import org.apache.log4j.Logger
import com.google.inject.Injector
import org.osgi.framework.BundleContext
import de.fhdo.lemma.data.datadsl.ui.internal.DatadslActivator
import java.util.Map
import de.fhdo.lemma.projections.DataDslProjections
import de.fhdo.lemma.projections.ServiceDslProjections
import de.fhdo.lemma.servicedsl.ui.internal.ServicedslActivator
import de.fhdo.lemma.projections.Projections

class PluginActivator extends AbstractUIPlugin {
	static final Logger LOGGER = Logger.getLogger(PluginActivator)

	static PluginActivator INSTANCE

	Map<String, Injector> injectors = newHashMap

	override start(BundleContext context) throws Exception {
		super.start(context)
		INSTANCE = this
	}

	override stop(BundleContext context) throws Exception {
		super.stop(context)
		INSTANCE = null
	}

	static def getInstance() {
		return INSTANCE
	}

	def getInjector(String language) {
		var injector = injectors.get(language)
		if (injector !== null)
			return injector

		try {
			val languageInjector = switch(language) {
				case DatadslActivator.DE_FHDO_LEMMA_DATA_DATADSL:
					DatadslActivator.instance.getInjector(language)
				case ServicedslActivator.DE_FHDO_LEMMA_SERVICEDSL:
					ServicedslActivator.instance.getInjector(language)
				default: null
			}
			injector = languageInjector.createChildInjector(new RuntimeModule)
			injectors.put(language, injector)
			return injector
		} catch (Exception ex) {
			LOGGER.error('''Failed to create injector for «language»''')
			LOGGER.error(ex.message, ex)
			throw new RuntimeException('''Failed to create injector for «language»''', ex)
		}
	}

	def injectMembers(String language, Object target) {
		getInjector(language).injectMembers(target)
	}
	
	def dispatch injectMembers(Projections target) {
		throw new UnsupportedOperationException
	}
	
	def dispatch injectMembers(DataDslProjections target) {
		getInjector(DatadslActivator.DE_FHDO_LEMMA_DATA_DATADSL).injectMembers(target)
	}
	
	def dispatch injectMembers(ServiceDslProjections target) {
		getInjector(ServicedslActivator.DE_FHDO_LEMMA_SERVICEDSL).injectMembers(target)
	}
}