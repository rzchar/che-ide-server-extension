package edu.tongji.sse.notifiercenter.ide.view.configview;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.tongji.sse.notifiercenter.ide.NfResources;
import org.eclipse.che.ide.api.parts.base.BasePresenter;
import org.vectomatic.dom.svg.ui.SVGResource;

@Singleton
public class NfConfigPresenter extends BasePresenter implements NfConfigView.ActionDelegate {

  NfConfigView view;
  NfResources nfResources;

  @Inject
  public NfConfigPresenter(NfConfigView view, NfResources nfResources) {
    this.view = view;
    view.setDelegate(this);
    this.nfResources = nfResources;
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
    return nfResources.configPartIcon();
  }
}
