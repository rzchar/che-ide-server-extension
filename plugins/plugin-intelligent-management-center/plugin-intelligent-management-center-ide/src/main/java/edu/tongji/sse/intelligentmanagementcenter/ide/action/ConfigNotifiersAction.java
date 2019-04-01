package edu.tongji.sse.intelligentmanagementcenter.ide.action;

import com.google.inject.Inject;
import edu.tongji.sse.intelligentmanagementcenter.ide.view.configview.NfConfigPresenter;
import edu.tongji.sse.intelligentmanagementcenter.ide.view.infoview.NfCenterInfoPresenter;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.action.BaseAction;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.parts.PartStackType;
import org.eclipse.che.ide.api.parts.WorkspaceAgent;

public class ConfigNotifiersAction extends BaseAction {

  private final NotificationManager notificationManager;
  private final NfConfigPresenter nfConfigPresenter;
  private final NfCenterInfoPresenter nfCenterInfoPresenter;
  private final WorkspaceAgent workspaceAgent;
  //  private final MyServiceClient serviceClient;
  //  private final CodeRecommendResultPresenter codeRecommendResultPresenter;
  /**
   * Constructor.
   *
   * @param notificationManager the notification manager
   */
  @Inject
  public ConfigNotifiersAction(
      final NotificationManager notificationManager,
      final WorkspaceAgent workspaceAgent,
      final NfCenterInfoPresenter nfCenterInfoPresenter,
      final NfConfigPresenter nfConfigPresenter
      // final MyServiceClient serviceClient,
      // final CodeRecommendResultPresenter codeRecommendResultPresenter,
      ) {
    super("Config IntelliDE", "Enable or disable intelligent support");
    this.notificationManager = notificationManager;
    this.nfConfigPresenter = nfConfigPresenter;
    this.nfCenterInfoPresenter = nfCenterInfoPresenter;
    this.workspaceAgent = workspaceAgent;
    workspaceAgent.openPart(nfCenterInfoPresenter, PartStackType.INFORMATION);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // workspaceAgent.openPart(nfCenterInfoPresenter, PartStackType.);
    nfConfigPresenter.showConfigDialog();
  }
}
