package edu.tongji.sse.notifiercenter.ide.view.infoview;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.tongji.sse.notifiercenter.ide.NfResources;
import org.eclipse.che.ide.api.parts.base.BasePresenter;
import org.vectomatic.dom.svg.ui.SVGResource;

@Singleton
public class NfCenterInfoPresenter extends BasePresenter
    implements NfCenterInfoView.ActionDelegate {
  NfCenterInfoView view;
  NfResources nfResources;

  @Inject
  public NfCenterInfoPresenter(NfCenterInfoView view, NfResources nfResources) {
    this.view = view;
    this.view.setDelegate(this);
    this.nfResources = nfResources;
  }

  @Override
  public String getTitle() {
    return "Notifier Center Title";
  }

  @Override
  public IsWidget getView() {
    return this.view;
  }

  @Override
  public String getTitleToolTip() {
    return "Notifier Center Tooltip";
  }

  @Override
  public void go(AcceptsOneWidget container) {
    container.setWidget(view);
  }

  @Override
  public SVGResource getTitleImage() {
    return nfResources.eventsPartIcon();
  }

  @Override
  public void appendLine(String text) {
    this.view.appendLine(text);
  }
}
