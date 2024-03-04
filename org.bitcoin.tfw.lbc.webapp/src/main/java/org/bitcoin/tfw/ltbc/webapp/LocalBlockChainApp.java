package org.bitcoin.tfw.ltbc.webapp;

import javax.inject.Inject;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;

public class LocalBlockChainApp extends ResourceConfig {
	@Inject
	public LocalBlockChainApp(ServiceLocator serviceLocator) throws Exception {
		packages("org.bitcoin.tfw.lbc.webapp");
	}
}