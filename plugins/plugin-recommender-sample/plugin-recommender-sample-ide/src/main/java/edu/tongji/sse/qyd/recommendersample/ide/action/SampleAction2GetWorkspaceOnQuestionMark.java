package edu.tongji.sse.qyd.recommendersample.ide.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.tongji.sse.qyd.recommendersample.ide.view.outputview1.SampleAction1Presenter;
import java.util.Map;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.action.BaseAction;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification;
import org.eclipse.che.ide.console.OutputConsoleView;
import org.eclipse.che.ide.rest.AsyncRequestCallback;
import org.eclipse.che.ide.rest.AsyncRequestFactory;
import org.eclipse.che.ide.rest.StringMapUnmarshaller;

@Singleton
public class SampleAction2GetWorkspaceOnQuestionMark extends BaseAction {

  private final AppContext appContext;
  private final StringMapUnmarshaller unmarshaller;
  private final AsyncRequestFactory asyncRequestFactory;
  private final NotificationManager notificationManager;
  private final OutputConsoleView consoleView;
  private final SampleAction1Presenter presenter;

  /**
   * Constructor
   *
   * @param appContext the IDE application context
   * @param asyncRequestFactory asynchronous request factory for creating the server request
   * @param notificationManager the notification manager used to display the lines of code per file
   */
  @Inject
  public SampleAction2GetWorkspaceOnQuestionMark(
      AppContext appContext,
      AsyncRequestFactory asyncRequestFactory,
      NotificationManager notificationManager,
      OutputConsoleView consoleView,
      SampleAction1Presenter presenter) {

    super("Analyzer From Current File", "try to call the wsagent to analyze");

    this.appContext = appContext;
    this.asyncRequestFactory = asyncRequestFactory;
    this.notificationManager = notificationManager;
    this.unmarshaller = new StringMapUnmarshaller();
    this.consoleView = consoleView;
    this.presenter = presenter;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String url =
        appContext.getWsAgentServerApiEndpoint()
            + "/getFile/"
            + appContext.getWorkspaceId()
            + "/"
            + appContext.getRootProject().getLocation();
    asyncRequestFactory
        .createGetRequest(url, false)
        .send(
            new AsyncRequestCallback<Map<String, String>>(unmarshaller) {
              @Override
              protected void onSuccess(Map<String, String> result) {
                notificationManager.notify(
                    "SampleAction2GetWorkspaceOnQuestionMark Success",
                    StatusNotification.Status.SUCCESS,
                    StatusNotification.DisplayMode.FLOAT_MODE);
                for (String key : result.keySet()) {
                  String newline = key + " : " + result.get(key);
                  consoleView.print(newline, false);
                }
              }

              @Override
              protected void onFailure(Throwable exception) {
                presenter.appendTextLine(exception.getMessage());
                StringBuilder sbForStaceTrace = new StringBuilder();
                for (StackTraceElement element : exception.getStackTrace()) {
                  sbForStaceTrace.append(element.toString());
                  sbForStaceTrace.append(";;");
                }
                presenter.appendTextLine(sbForStaceTrace.toString());
                notificationManager.notify(
                    "SampleAction2GetWorkspaceOnQuestionMark Fail",
                    StatusNotification.Status.FAIL,
                    StatusNotification.DisplayMode.FLOAT_MODE);
              }
            });
  }
}
