package edu.tongji.sse.intelligentmanagementcenter.ide.view.infoview;

import com.google.inject.ImplementedBy;
import org.eclipse.che.ide.api.mvp.View;
import org.eclipse.che.ide.api.parts.base.BaseActionDelegate;

@ImplementedBy(NfCenterInfoViewImpl.class)
public interface NfCenterInfoView extends View<NfCenterInfoView.ActionDelegate> {

  interface ActionDelegate extends BaseActionDelegate {
    void appendLine(String text);
  }

  void appendLine(String text);
}
