package edu.tongji.sse.notifiercenter.ide.view.configview;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import edu.tongji.sse.notifiercenter.ide.interfaces.IntelligentConfigPresenter;
import edu.tongji.sse.notifiercenter.ide.manager.IntelligentPluginManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.che.ide.ui.window.Window;

public class NfConfigViewImpl extends Window implements NfConfigView {

  private static final NfConfigViewImplUiBinder UI_BINDER =
      GWT.create(NfConfigViewImplUiBinder.class);

  private final DockLayoutPanel rootElement;

  private ActionDelegate delegate;

  private IntelligentPluginManager intelligentPluginManager;

  private List<CheckBox> checkBoxList;

  @UiField ScrollPanel configPanel;

  @Inject
  public NfConfigViewImpl(IntelligentPluginManager intelligentPluginManager) {
    rootElement = UI_BINDER.createAndBindUi(this);
    this.intelligentPluginManager = intelligentPluginManager;
    addFooterButton("Save", "intelligent-config-save", clickEvent -> this.saveChange());
    addFooterButton("Cancel", "intelligent-config-cancel", clickEvent -> this.cancelChange());
    setWidget(rootElement);
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
    checkBoxList = new ArrayList<>();

    for (String pluginName : intelligentPluginManager.getRegisteredPlugins()) {
      FlowPanel flowPanel = new FlowPanel();
      CheckBox checkBox = new CheckBox();
      checkBox.setText(pluginName);
      checkBox.setValue(intelligentPluginManager.isPluginEnabled(pluginName));

      checkBoxList.add(checkBox);
      flowPanel.add(checkBox);

      // flowPanel.getElement().appendChild(checkBox.getElement());
      IntelligentConfigPresenter icp = intelligentPluginManager.getCongfigPresenter(pluginName);
      if (icp != null) {
        Button button = new Button();
        button.addClickHandler(clickEvent -> icp.showConfigWindow());
        flowPanel.getElement().appendChild(button.getElement());
      }
      configPanel.add(flowPanel);
      // configPanel.getElement().appendChild(flowPanel.getElement());
    }
    this.setWidget(rootElement);
    this.show();
  }

  @Override
  public void onHide() {
    this.configPanel.clear();
    this.setWidget(rootElement);
  }

  @Override
  public void saveChange() {
    Map<String, Boolean> availability = new HashMap<>();
    for (CheckBox checkBox : checkBoxList) {
      availability.put(checkBox.getText(), checkBox.getValue());
    }
    intelligentPluginManager.savePluginAvailability(availability);
  }

  @Override
  public void cancelChange() {
    this.hide();
  }

  interface NfConfigViewImplUiBinder extends UiBinder<DockLayoutPanel, NfConfigViewImpl> {}
}
