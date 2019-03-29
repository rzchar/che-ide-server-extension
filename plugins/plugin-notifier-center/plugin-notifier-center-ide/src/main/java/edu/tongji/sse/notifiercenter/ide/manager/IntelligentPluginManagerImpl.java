package edu.tongji.sse.notifiercenter.ide.manager;

import static org.eclipse.che.ide.MimeType.APPLICATION_JSON;
import static org.eclipse.che.ide.rest.HTTPHeader.CONTENT_TYPE;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import edu.tongji.sse.notifiercenter.ide.interfaces.IntelligentConfigPresenter;
import edu.tongji.sse.notifiercenter.ide.interfaces.IntelligentResultPresenter;
import edu.tongji.sse.notifiercenter.ide.view.infoview.NfCenterInfoPresenter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.editor.events.FileEvent;
import org.eclipse.che.ide.api.editor.events.TextChangeEvent;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification;
import org.eclipse.che.ide.api.resources.ResourceChangedEvent;
import org.eclipse.che.ide.json.JsonHelper;
import org.eclipse.che.ide.rest.AsyncRequestFactory;
import org.eclipse.che.ide.rest.StringMapUnmarshaller;

@Singleton
public class IntelligentPluginManagerImpl implements IntelligentPluginManager {

  private Map<String, IntelligentResultPresenter> intelligentResultPresenterMap;

  private Map<String, IntelligentConfigPresenter> intelligentConfigPresenterMap;

  private Map<String, Boolean> pluginAvailabilities;

  private AppContext appContext;

  private AsyncRequestFactory asyncRequestFactory;

  private NfCenterInfoPresenter nfCenterInfoPresenter;

  private EventBus eventBus;

  private EditorAgent editorAgent;

  private NotificationManager notificationManager;

  private static String AVAILABILITY_PATH = "intelliPluginAva/";

  @Inject
  public IntelligentPluginManagerImpl(
      AppContext appContext,
      AsyncRequestFactory asyncRequestFactory,
      NfCenterInfoPresenter nfCenterInfoPresenter,
      EventBus eventBus,
      EditorAgent editorAgent,
      NotificationManager notificationManager) {
    intelligentResultPresenterMap = new HashMap<>();
    intelligentConfigPresenterMap = new HashMap<>();
    this.appContext = appContext;
    this.asyncRequestFactory = asyncRequestFactory;
    this.nfCenterInfoPresenter = nfCenterInfoPresenter;
    this.eventBus = eventBus;
    this.editorAgent = editorAgent;

    this.notificationManager = notificationManager;
    this.loadPluginAvailability();
    this.addEventHandler();
  }

  @Override
  public void registerPlugin(String name, IntelligentResultPresenter intelligentResultPresenter) {
    this.registerPlugin(name, intelligentResultPresenter, null);
  }

  @Override
  public void registerPlugin(
      String name,
      IntelligentResultPresenter intelligentResultPresenter,
      IntelligentConfigPresenter intelligentConfigPresenter) {
    this.intelligentResultPresenterMap.put(name, intelligentResultPresenter);
    if (!pluginAvailabilities.containsKey(name)) {
      pluginAvailabilities.put(name, Boolean.FALSE);
    }
    if (intelligentConfigPresenter != null) {
      intelligentConfigPresenterMap.put(name, intelligentConfigPresenter);
    }
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
    this.loadPluginAvailability();
    return this.pluginAvailabilities;
  }

  @Override
  public IntelligentConfigPresenter getCongfigPresenter(String name) {
    return this.intelligentConfigPresenterMap.get(name);
  }

  @Override
  public Set<String> getRegisteredPlugins() {
    return this.intelligentResultPresenterMap.keySet();
  }

  @Override
  public void savePluginAvailability(Map<String, Boolean> availabilityMap) {
    logOnPresenter("plugin config clicked");
    logOnPresenter(availabilityMap.toString());
    String url = appContext.getWsAgentServerApiEndpoint() + "/" + AVAILABILITY_PATH;
    this.pluginAvailabilities.putAll(availabilityMap);
    Map<String, String> availabilitySS = new HashMap<>();
    for (String pluginName : this.pluginAvailabilities.keySet()) {
      availabilitySS.put(pluginName, this.pluginAvailabilities.get(pluginName).toString());
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

  private boolean saveProcessSucceed;

  private void loadPluginAvailability() {
    this.pluginAvailabilities = new HashMap<>();

    String url = appContext.getWsAgentServerApiEndpoint() + "/" + AVAILABILITY_PATH;

    logOnPresenter("url=" + url);

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
                this.pluginAvailabilities.put(str, b);
              }
            })
        .catchError(
            error -> {
              logOnPresenter("[get error]" + error.getMessage());
            });
  }

  private void addEventHandler() {
    // this.eventBus.addHandler(TextChangeEvent.TYPE, event -> onTextChange(event));
    // this.eventBus.addHandler(FileEvent.TYPE, event -> onFileEvent(event));
    // this.eventBus.addHandler(ResourceChangedEvent.getType(), event -> onResourceChange(event));
  }

  public void onTextChange(TextChangeEvent event) {
    logOnPresenter("[text change-textChangeEvent]");
    if (event.getChange().getFrom() != null) {
      logOnPresenter("from " + event.getChange().getFrom().toString());
    }
    if (event.getChange().getTo() != null) {
      logOnPresenter("to   " + event.getChange().getTo().toString());
    }
    if (event.getChange().getNewText() != null) {
      logOnPresenter(event.getChange().getNewText());
    }
  }

  public void onFileEvent(FileEvent event) {
    logOnPresenter("[text change-fileEvent]");
    logOnPresenter(event.getOperationType().toString());
    event
        .getEditorTab()
        .getRelativeEditorPart()
        .getEditorInput()
        .getFile()
        .getContent()
        .then(this::logOnPresenter);
  }

  public void onResourceChange(ResourceChangedEvent event) {
    logOnPresenter("[text change-resourceChangeEvent]");
    logOnPresenter(event.toString());
    logOnPresenter(event.getDelta().toString());
  }

  public void logOnPresenter(String str) {
    this.nfCenterInfoPresenter.appendLine(str);
  }
}
