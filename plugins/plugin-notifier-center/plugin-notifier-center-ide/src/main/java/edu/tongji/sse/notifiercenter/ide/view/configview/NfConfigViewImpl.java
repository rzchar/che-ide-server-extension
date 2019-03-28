package edu.tongji.sse.notifiercenter.ide.view.configview;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import edu.tongji.sse.notifiercenter.ide.controller.IntelligentPluginManager;
import edu.tongji.sse.notifiercenter.ide.interfaces.IntelligentConfigPresenter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification;
import org.eclipse.che.ide.ui.window.Window;

public class NfConfigViewImpl extends Window implements NfConfigView {

  private static final NfConfigViewImplUiBinder UI_BINDER =
      GWT.create(NfConfigViewImplUiBinder.class);

  private final DockLayoutPanel rootElement;

  private ActionDelegate delegate;

  private IntelligentPluginManager intelligentPluginManager;

  private List<CheckBox> checkBoxes;

  private NotificationManager notificationManager;

  @UiField ScrollPanel configPanel;

  @Inject
  public NfConfigViewImpl(
      IntelligentPluginManager intelligentPluginManager, NotificationManager notificationManager) {
    rootElement = UI_BINDER.createAndBindUi(this);
    this.intelligentPluginManager = intelligentPluginManager;
    this.notificationManager = notificationManager;
    setWidget(rootElement);
    addFooterButton("Save", "intelligent-config-save", clickEvent -> this.saveChange());
    addFooterButton("Cancel", "intelligent-config-cancel", clickEvent -> this.cancelChange());
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
    configPanel.getElement().removeAllChildren();
    for (String pluginName : intelligentPluginManager.getRegisteredPlugins()) {
      FlowPanel flowPanel = new FlowPanel();
      CheckBox checkBox = new CheckBox();
      checkBox.setText(pluginName);
      checkBox.setValue(intelligentPluginManager.isPluginEnabled(pluginName));
      flowPanel.getElement().appendChild(checkBox.getElement());
      checkBoxes.add(checkBox);
      IntelligentConfigPresenter icp = intelligentPluginManager.getCongfigPresenter(pluginName);
      if (icp != null) {
        Button button = new Button();
        button.addClickHandler(clickEvent -> icp.showConfigWindow());
        flowPanel.getElement().appendChild(button.getElement());
      }
      configPanel.getElement().appendChild(flowPanel.getElement());
    }
    this.show();
  }

  @Override
  public void saveChange() {
    Map<String, String> availability = new HashMap();
    for (CheckBox checkBox : checkBoxes) {
      availability.put(checkBox.getText(), checkBox.getValue().toString());
    }
    this.intelligentPluginManager.savePluginAvailability(availability);
    this.notificationManager.notify(
        "Save succeed",
        "Save intelligent plugins config succeed",
        StatusNotification.Status.SUCCESS,
        StatusNotification.DisplayMode.FLOAT_MODE);
  }

  @Override
  public void cancelChange() {
    this.hide();
  }

  interface NfConfigViewImplUiBinder extends UiBinder<DockLayoutPanel, NfConfigViewImpl> {}
}
