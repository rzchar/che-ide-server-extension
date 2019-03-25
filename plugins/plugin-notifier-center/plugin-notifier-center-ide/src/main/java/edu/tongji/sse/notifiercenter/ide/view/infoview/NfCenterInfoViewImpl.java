package edu.tongji.sse.notifiercenter.ide.view.infoview;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;
import org.eclipse.che.ide.api.parts.base.BaseView;

public class NfCenterInfoViewImpl extends BaseView<NfCenterInfoView.ActionDelegate>
    implements NfCenterInfoView {
  private static final NfCenterInfoViewImplUiBinder UI_BINDER =
      GWT.create(NfCenterInfoViewImplUiBinder.class);

  private final DockLayoutPanel rootElement;

  @UiField FlowPanel nfCenterTextLines;

  @Inject
  public NfCenterInfoViewImpl() {
    rootElement = UI_BINDER.createAndBindUi(this);
    setContentWidget(rootElement);
  }

  interface NfCenterInfoViewImplUiBinder extends UiBinder<DockLayoutPanel, NfCenterInfoViewImpl> {}
}
