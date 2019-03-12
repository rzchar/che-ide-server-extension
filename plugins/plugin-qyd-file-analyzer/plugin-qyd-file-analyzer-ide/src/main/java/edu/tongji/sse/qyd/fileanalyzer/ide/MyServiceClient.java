/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package edu.tongji.sse.qyd.fileanalyzer.ide;

import javax.inject.Inject;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.rest.AsyncRequestFactory;
import org.eclipse.che.ide.rest.StringUnmarshaller;
import org.eclipse.che.ide.ui.loaders.request.LoaderFactory;

/**
 * Client for consuming the sample server service.
 *
 * @author Edgar Mueller
 */
public class MyServiceClient {

  private AppContext appContext;
  private AsyncRequestFactory asyncRequestFactory;
  private LoaderFactory loaderFactory;

  /**
   * Constructor.
   *
   * @param appContext the {@link AppContext}
   * @param asyncRequestFactory the {@link AsyncRequestFactory} that is used to create requests
   * @param loaderFactory the {@link LoaderFactory} for displaying a message while waiting for a
   *     response
   */
  @Inject
  public MyServiceClient(
      final AppContext appContext,
      final AsyncRequestFactory asyncRequestFactory,
      final LoaderFactory loaderFactory) {

    this.appContext = appContext;
    this.asyncRequestFactory = asyncRequestFactory;
    this.loaderFactory = loaderFactory;
  }

  /**
   * Invoke the sample server service.
   *
   * @param name a parameter
   * @return a Promise containing the server response
   */
  public Promise<String> getHello(String name) {
    return asyncRequestFactory
        .createGetRequest(appContext.getWsAgentServerApiEndpoint() + "/hello/" + name)
        .loader(loaderFactory.newLoader("Waiting for hello..."))
        .send(new StringUnmarshaller());
  }
}
