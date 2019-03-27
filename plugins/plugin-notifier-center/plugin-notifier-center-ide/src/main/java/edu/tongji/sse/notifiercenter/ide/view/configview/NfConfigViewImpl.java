package edu.tongji.sse.notifiercenter.ide.view.configview;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import edu.tongji.sse.notifiercenter.ide.controller.IntelligentPluginManager;
import edu.tongji.sse.notifiercenter.ide.interfaces.IntelligentConfigPresenter;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.che.ide.ui.window.Window;

public class NfConfigViewImpl extends Window implements NfConfigView {

  private static final NfConfigViewImplUiBinder UI_BINDER =
      GWT.create(NfConfigViewImplUiBinder.class);

  private final DockLayoutPanel rootElement;

  private ActionDelegate delegate;

  private IntelligentPluginManager intelligentPluginManager;

  private List<CheckBox> checkBoxes;

  @UiField ScrollPanel configPanel;

  @Inject
  public NfConfigViewImpl(IntelligentPluginManager intelligentPluginManager) {
    rootElement = UI_BINDER.createAndBindUi(this);
    this.intelligentPluginManager = intelligentPluginManager;
    setWidget(rootElement);
    addFooterButton("Save", "intelligent-config-save", clickEvent -> saveChange());
    addFooterButton("Cancel", "intelligent-config-cancel", clickEvent -> cancelChange());
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
    checkBoxes = new ArrayList<>();
    configPanel.getElement().setInnerHTML("");
    for (String pluginName : intelligentPluginManager.getRegisteredPlugins()) {
      FlowPanel flowPanel = new FlowPanel();
      CheckBox checkBox = new CheckBox();
      checkBox.setText(pluginName);
      checkBox.setValue(intelligentPluginManager.isPluginEnabled(pluginName));
      flowPanel.add(checkBox);
      checkBoxes.add(checkBox);
      IntelligentConfigPresenter icp = intelligentPluginManager.getCongfigPresenter(pluginName);
      if (icp != null) {
        Button button = new Button();
        button.addClickHandler(clickEvent -> icp.showConfigWindow());
        flowPanel.add(button);
      }
      configPanel.add(flowPanel);
    }
    this.show();
  }

  @Override
  public void saveChange() {}

  @Override
  public void cancelChange() {}

  interface NfConfigViewImplUiBinder extends UiBinder<DockLayoutPanel, NfConfigViewImpl> {}
}
