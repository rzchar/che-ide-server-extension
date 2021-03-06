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
import edu.tongji.sse.intelligentmanagementcenter.ide.action.AbstractIntelligentPluginAction;
import edu.tongji.sse.qyd.recommendersample.ide.view.outputview1.SampleAction1Presenter;
import java.util.Date;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.editor.EditorPartPresenter;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.parts.PartPresenter;
import org.eclipse.che.ide.api.parts.WorkspaceAgent;
import org.eclipse.che.ide.api.parts.base.BasePresenter;
import org.eclipse.che.ide.rest.AsyncRequestFactory;
import org.eclipse.che.ide.rest.StringMapUnmarshaller;

public class SampleAction1GetFileLinesOnRightClick extends AbstractIntelligentPluginAction {

  private final NotificationManager notificationManager;
  private final StringMapUnmarshaller unmarshaller;
  private final AsyncRequestFactory asyncRequestFactory;
  private final WorkspaceAgent workspaceAgent;
  private final SampleAction1Presenter sampleAction1Presenter;
  private final EditorAgent editorAgent;
  private final AppContext appContext;

  @Inject
  public SampleAction1GetFileLinesOnRightClick(
      final NotificationManager notificationManager,
      final WorkspaceAgent workspaceAgent,
      final SampleAction1Presenter sampleAction1Presenter,
      final EditorAgent editorAgent,
      final AppContext appContext,
      final AsyncRequestFactory asyncRequestFactory) {
    super("Sample Action Get File Lines", "Get File Lines");
    this.notificationManager = notificationManager;
    this.workspaceAgent = workspaceAgent;
    this.sampleAction1Presenter = sampleAction1Presenter;
    this.editorAgent = editorAgent;
    this.appContext = appContext;
    this.unmarshaller = new StringMapUnmarshaller();
    this.asyncRequestFactory = asyncRequestFactory;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // This calls the service in the workspace.
    // This method is in our org.eclipse.che.plugin.serverservice.ide.MyServiceClient class
    // This is a Promise, so the .then() method is invoked after the response is made
    workspaceAgent.setActivePart(sampleAction1Presenter);
    this.sampleAction1Presenter.clear();
    EditorPartPresenter editor = editorAgent.getActiveEditor();
    String fileName = editor.getEditorInput().getFile().getName();
    //    String url =
    //        appContext.getWsAgentServerApiEndpoint()
    //            + "/getFileLines/"
    //            + appContext.getWorkspaceId()
    //            + appContext.getRootProject().getLocation();
    String url =
        appContext.getWsAgentServerApiEndpoint()
            + "/getFileLines/"
            + appContext.getWorkspaceId()
            + "/ast"
            + appContext.getRootProject().getLocation();

    this.logInConsole("triggered by right click");
    this.logInConsole("currentFileName=" + fileName);
    this.logInConsole("workspace=" + appContext.getWorkspaceId());
    this.logInConsole("rootProject=" + appContext.getRootProject().getLocation());
    this.logInConsole("url=" + url);
    long requestStartTime = (new Date()).getTime();
    asyncRequestFactory
        .createGetRequest(url)
        .send(this.unmarshaller)
        .then(
            fileLinesMap -> {
              long requestEndTime = (new Date()).getTime();
              long duration = requestEndTime - requestStartTime;
              this.logInConsole("[sample 1 server succeed] cost " + duration + "ms.");

              for (String respondFileName : fileLinesMap.keySet()) {
                this.logInConsole(
                    "   " + respondFileName + " : " + fileLinesMap.get(respondFileName));
              }
            })
        .catchError(
            error -> {
              this.logInConsole("[sample 1 server error]" + error.getMessage());
            });
  }

  private void logInConsole(String s) {
    this.sampleAction1Presenter.appendTextLine("[sample1]" + s);
  }

  public BasePresenter getBasePresenter() {
    return this.sampleAction1Presenter;
  }

  @Override
  public boolean isEnable() {
    return false;
  }

  @Override
  public void setEnable(boolean enable) {}

  @Override
  public PartPresenter getResultPresenter() {
    return this.sampleAction1Presenter;
  }
}
