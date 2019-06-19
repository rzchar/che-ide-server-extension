package edu.tongji.sse.qyd.recommendersample.ide.view.configwindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import edu.tongji.sse.qyd.recommendersample.ide.view.outputview1.SampleAction1Presenter;
import org.eclipse.che.ide.ui.window.Window;

public class ConfigWindowViewImpl extends Window implements ConfigWindowView {

  ActionDelegate actionDelegate;

  private static final ConfigWindowViewUiBinder UI_BINDER =
      GWT.create(ConfigWindowViewUiBinder.class);

  SampleAction1Presenter sampleAction1Presenter;

  private final DockLayoutPanel rootElement;

  @UiField HTMLPanel configPanel;

  @Override
  public void setDelegate(ActionDelegate delegate) {
    this.actionDelegate = delegate;
  }

  @Inject
  public ConfigWindowViewImpl(SampleAction1Presenter sampleAction1Presenter1) {
    rootElement = UI_BINDER.createAndBindUi(this);
    addFooterButton("Save", "intelligent-config-save", clickEvent -> this.cancelChange());
    addFooterButton("Cancel", "intelligent-config-cancel", clickEvent -> this.cancelChange());
    setWidget(rootElement);
    this.sampleAction1Presenter = sampleAction1Presenter1;
  }

  public void cancelChange() {
    this.hide();
  }

  @Override
  public void onShow() {
    this.sampleAction1Presenter.appendTextLine("config window did try to show");
  }

  interface ConfigWindowViewUiBinder extends UiBinder<DockLayoutPanel, ConfigWindowViewImpl> {}
}
