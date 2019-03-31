package edu.tongji.sse.notifiercenter.ide.manager;

import static org.eclipse.che.ide.MimeType.APPLICATION_JSON;
import static org.eclipse.che.ide.rest.HTTPHeader.CONTENT_TYPE;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.tongji.sse.notifiercenter.ide.action.BaseIntelligentAssistantAction;
import edu.tongji.sse.notifiercenter.ide.view.infoview.NfCenterInfoPresenter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification;
import org.eclipse.che.ide.api.parts.WorkspaceAgent;
import org.eclipse.che.ide.json.JsonHelper;
import org.eclipse.che.ide.rest.AsyncRequestFactory;
import org.eclipse.che.ide.rest.StringMapUnmarshaller;
import org.eclipse.che.ide.ui.window.Window;

@Singleton
public class IntelligentPluginManagerImpl implements IntelligentPluginManager {

  private Map<String, BaseIntelligentAssistantAction> intelligentAssistantActionMap;

  private Map<String, Window> intelligentConfigWindowMap;

  private Map<String, Boolean> pluginAvailabilities;

  private AppContext appContext;

  private AsyncRequestFactory asyncRequestFactory;

  private NfCenterInfoPresenter nfCenterInfoPresenter;

  private WorkspaceAgent workspaceAgent;

  private NotificationManager notificationManager;

  private static String AVAILABILITY_PATH = "intelliPluginAva/";

  @Inject
  public IntelligentPluginManagerImpl(
      AppContext appContext,
      AsyncRequestFactory asyncRequestFactory,
      NfCenterInfoPresenter nfCenterInfoPresenter,
      WorkspaceAgent workspaceAgent,
      NotificationManager notificationManager) {
    intelligentAssistantActionMap = new HashMap<>();
    intelligentConfigWindowMap = new HashMap<>();
    pluginAvailabilities = new HashMap<>();

    this.appContext = appContext;
    this.asyncRequestFactory = asyncRequestFactory;
    this.nfCenterInfoPresenter = nfCenterInfoPresenter;
    this.workspaceAgent = workspaceAgent;
    this.notificationManager = notificationManager;

    this.loadPluginAvailability();
    logOnPresenter("notifier center init succeed");
  }

  @Override
  public void registerPlugin(
      String name, BaseIntelligentAssistantAction baseIntelligentAssistantAction) {
    this.intelligentAssistantActionMap.put(name, baseIntelligentAssistantAction);
    if (this.isPluginEnabled(name)) {
      workspaceAgent.openPart(
          baseIntelligentAssistantAction.getResultPresenter(),
          baseIntelligentAssistantAction.getResultPresenterPartStack());
    } else {
      pluginAvailabilities.put(name, Boolean.FALSE);
    }
    if (baseIntelligentAssistantAction.getConfigWindow() != null) {
      intelligentConfigWindowMap.put(name, baseIntelligentAssistantAction.getConfigWindow());
    }
    this.logOnPresenter("[plugin register]" + name + "|show=" + this.isPluginEnabled(name));
  }

  @Override
  public boolean isPluginEnabled(String name) {
    if (this.pluginAvailabilities.containsKey(name)) {
      return this.pluginAvailabilities.get(name).booleanValue();
    }
    return false;
  }

  @Override
  public Map<String, Boolean> getPluginAvailabilities() {
    return this.pluginAvailabilities;
  }

  @Override
  public Window getCongfigPresenter(String name) {
    return this.intelligentConfigWindowMap.get(name);
  }

  @Override
  public Set<String> getRegisteredPlugins() {
    return this.intelligentAssistantActionMap.keySet();
  }

  @Override
  public void savePluginAvailability(Map<String, Boolean> availabilityMap) {
    logOnPresenter("plugin config clicked");
    logOnPresenter(availabilityMap.toString());
    String url = appContext.getWsAgentServerApiEndpoint() + "/" + AVAILABILITY_PATH;
    Map<String, String> availabilitySS = new HashMap<>();

    for (String pluginName : this.pluginAvailabilities.keySet()) {
      availabilitySS.put(pluginName, this.pluginAvailabilities.get(pluginName).toString());
    }

    for (String pluginName : availabilityMap.keySet()) {
      this.setPluginEnable(pluginName, availabilityMap.get(pluginName));
      availabilitySS.put(pluginName, availabilityMap.get(pluginName).toString());
    }
    String data = JsonHelper.toJson(availabilitySS);

    this.asyncRequestFactory
        .createPostRequest(url, null)
        .data(data)
        .header(CONTENT_TYPE, APPLICATION_JSON)
        .send()
        .then(
            succeed -> {
              logOnPresenter("[post return from backend succeed]");
              notificationManager.notify(
                  "Save Succeed",
                  "intelligent config save succeed",
                  StatusNotification.Status.SUCCESS,
                  StatusNotification.DisplayMode.FLOAT_MODE);
            },
            reject -> {
              logOnPresenter("[post rejected by backend]" + reject.getMessage());
              notificationManager.notify(
                  "Save Fail",
                  "intelligent config save fail",
                  StatusNotification.Status.FAIL,
                  StatusNotification.DisplayMode.FLOAT_MODE);
            })
        .catchError(
            error -> {
              logOnPresenter("[post error]" + error.getMessage());
            });
  }

  private void loadPluginAvailability() {
    this.pluginAvailabilities = new HashMap<>();
    String url = appContext.getWsAgentServerApiEndpoint() + "/" + AVAILABILITY_PATH;

    logOnPresenter("[try get]url=" + url);

    this.asyncRequestFactory
        .createGetRequest(url)
        .header(CONTENT_TYPE, APPLICATION_JSON)
        .send(new StringMapUnmarshaller())
        .then(
            availability -> {
              logOnPresenter(availability.toString());
              for (String str : availability.keySet()) {
                boolean b = availability.get(str).equalsIgnoreCase("true");
                logOnPresenter("[get return from backend]" + str + " : " + b);
                setPluginEnable(str, b);
              }
            })
        .catchError(
            error -> {
              logOnPresenter("[get error]" + error.getMessage());
            });
  }

  private void setPluginEnable(String pluginName, boolean enable) {
    boolean previous = this.isPluginEnabled(pluginName);
    if (this.intelligentAssistantActionMap.containsKey(pluginName) && enable != previous) {
      BaseIntelligentAssistantAction biaa = this.intelligentAssistantActionMap.get(pluginName);
      if (biaa != null) {
        if (enable) {
          workspaceAgent.openPart(biaa.getResultPresenter(), biaa.getResultPresenterPartStack());
        } else {
          workspaceAgent.removePart(biaa.getResultPresenter());
        }
      }
    }
    this.pluginAvailabilities.put(pluginName, enable);
  }

  @Override
  public void logOnPresenter(String str) {
    this.nfCenterInfoPresenter.appendLine(str);
  }

  //  public void refreshPlugins(){
  //    for (String pluginName        : this.intelligentAssistantActionMap.keySet()){
  //      BaseIntelligentAssistantAction baseIntelligentAssistantAction =
  //          this.intelligentAssistantActionMap.get(pluginName);
  //
  //    }
  //  }
}
