package edu.tongji.sse.notifiercenter.ide.action;

import com.google.inject.Inject;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.action.BaseAction;
import org.eclipse.che.ide.api.notification.NotificationManager;

public class ConfigNotifiersAction extends BaseAction {
  private final NotificationManager notificationManager;
  //  private final MyServiceClient serviceClient;
  //  private final CodeRecommendResultPresenter codeRecommendResultPresenter;
  /**
   * Constructor.
   *
   * @param notificationManager the notification manager
   */
  @Inject
  public ConfigNotifiersAction(final NotificationManager notificationManager
      // final MyServiceClient serviceClient,
      // final CodeRecommendResultPresenter codeRecommendResultPresenter,
      ) {
    super("Config IntelliDE", "Enable or disable intelligent support");
    this.notificationManager = notificationManager;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    notificationManager.notify("config intellide", "the root action succeed");
  }
}
