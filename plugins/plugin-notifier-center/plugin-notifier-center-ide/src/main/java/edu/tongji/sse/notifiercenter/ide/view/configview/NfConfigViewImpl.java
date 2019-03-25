package edu.tongji.sse.notifiercenter.ide.view.configview;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.inject.Inject;
import org.eclipse.che.ide.ui.window.Window;

public class NfConfigViewImpl extends Window implements NfConfigView {

  private static final NfConfigViewImplUiBinder UI_BINDER =
      GWT.create(NfConfigViewImplUiBinder.class);

  private final DockLayoutPanel rootElement;

  private ActionDelegate delegate;

  @Inject
  public NfConfigViewImpl() {
    rootElement = UI_BINDER.createAndBindUi(this);
    // setContentWidget(rootElement);
  }

  @Override
  public void setDelegate(ActionDelegate delegate) {
    this.delegate = delegate;
  }

  @Override
  public void close() {
    this.hide();
  }

  @Override
  public void showDialog() {
    this.show();
  }

  interface NfConfigViewImplUiBinder extends UiBinder<DockLayoutPanel, NfConfigViewImpl> {}
}
