package edu.tongji.sse.notifiercenter.ide;

import com.google.inject.Inject;
import edu.tongji.sse.notifiercenter.ide.action.ConfigNotifiersAction;
import org.eclipse.che.ide.api.action.ActionManager;
import org.eclipse.che.ide.api.action.DefaultActionGroup;
import org.eclipse.che.ide.api.action.IdeActions;
import org.eclipse.che.ide.api.constraints.Constraints;
import org.eclipse.che.ide.api.extension.Extension;

/**
 * Server service extension that registers action .
 *
 * @author Qin Yidan
 */
@Extension(title = "Notifier Center Extension", version = "0.0.1")
public class NotifierCenterExtension {

  @Inject
  public NotifierCenterExtension(
      ActionManager actionManager, ConfigNotifiersAction configNotifiersAction) {

    actionManager.registerAction("configNotifierAction", configNotifiersAction);

    DefaultActionGroup mouseRightClickGroup =
        (DefaultActionGroup) actionManager.getAction(IdeActions.GROUP_WORKSPACE);

    mouseRightClickGroup.add(configNotifiersAction, Constraints.LAST);
  }
}
