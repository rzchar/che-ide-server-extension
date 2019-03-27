/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package edu.tongji.sse.qyd.recommendersample.ide.action;

import com.google.inject.Inject;
import edu.tongji.sse.qyd.recommendersample.ide.MyServiceClient;
import edu.tongji.sse.qyd.recommendersample.ide.outputView.CodeRecommendResultPresenter;
import org.eclipse.che.api.promises.client.Operation;
import org.eclipse.che.api.promises.client.OperationException;
import org.eclipse.che.api.promises.client.PromiseError;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.action.BaseAction;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification;
import org.eclipse.che.ide.api.parts.PartStackType;
import org.eclipse.che.ide.api.parts.WorkspaceAgent;

/**
 * Actions that triggers the sample server service call.
 *
 * @author Edgar Mueller
 */
public class MyAction extends BaseAction {

  private final NotificationManager notificationManager;
  private final MyServiceClient serviceClient;
  private final WorkspaceAgent workspaceAgent;
  private final CodeRecommendResultPresenter codeRecommendResultPresenter;
  /**
   * Constructor.
   *
   * @param notificationManager the notification manager
   * @param serviceClient the client that is used to create requests
   */
  @Inject
  public MyAction(
      final NotificationManager notificationManager,
      final MyServiceClient serviceClient,
      final WorkspaceAgent workspaceAgent,
      final CodeRecommendResultPresenter codeRecommendResultPresenter) {
    super("My Action", "My Action Description");
    this.notificationManager = notificationManager;
    this.serviceClient = serviceClient;
    this.workspaceAgent = workspaceAgent;
    this.codeRecommendResultPresenter = codeRecommendResultPresenter;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // This calls the service in the workspace.
    // This method is in our org.eclipse.che.plugin.serverservice.ide.MyServiceClient class
    // This is a Promise, so the .then() method is invoked after the response is made
    workspaceAgent.openPart(codeRecommendResultPresenter, PartStackType.INFORMATION);
    workspaceAgent.setActivePart(codeRecommendResultPresenter);

    serviceClient
        .getHello("CheTheAllPowerful!")
        .then(
            new Operation<String>() {
              @Override
              public void apply(String response) throws OperationException {
                // This passes the response String to the notification manager.
                notificationManager.notify(
                    response,
                    StatusNotification.Status.SUCCESS,
                    StatusNotification.DisplayMode.FLOAT_MODE);
                codeRecommendResultPresenter.showView();
                codeRecommendResultPresenter.appendTextLine("succeed");
              }
            })
        .catchError(
            new Operation<PromiseError>() {
              @Override
              public void apply(PromiseError error) throws OperationException {
                notificationManager.notify(
                    "Fail",
                    StatusNotification.Status.FAIL,
                    StatusNotification.DisplayMode.FLOAT_MODE);
                codeRecommendResultPresenter.showView();
                codeRecommendResultPresenter.appendTextLine("fail");
              }
            });
  }
}