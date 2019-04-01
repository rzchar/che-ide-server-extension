package edu.tongji.sse.intelligentmanagementcenter.ide.view.configview;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.tongji.sse.intelligentmanagementcenter.ide.IMResources;
import org.eclipse.che.ide.api.parts.base.BasePresenter;
import org.vectomatic.dom.svg.ui.SVGResource;

@Singleton
public class NfConfigPresenter extends BasePresenter implements NfConfigView.ActionDelegate {

  NfConfigView view;
  edu.tongji.sse.intelligentmanagementcenter.ide.IMResources IMResources;

  @Inject
  public NfConfigPresenter(NfConfigView view, IMResources IMResources) {
    this.view = view;
    view.setDelegate(this);
    this.IMResources = IMResources;
  }

  public void showConfigDialog() {
    this.view.showDialog();
  }

  @Override
  public String getTitle() {
    return "Config IntelliDE Plugins";
  }

  @Override
  public IsWidget getView() {
    return this.view;
  }

  @Override
  public String getTitleToolTip() {
    return "Config IntelliDE ToolTip";
  }

  @Override
  public void go(AcceptsOneWidget container) {
    container.setWidget(view);
  }

  @Override
  public SVGResource getTitleImage() {
    return IMResources.configPartIcon();
  }
}
