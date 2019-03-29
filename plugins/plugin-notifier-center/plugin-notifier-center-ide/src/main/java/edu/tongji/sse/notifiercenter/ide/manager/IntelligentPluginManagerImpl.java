package edu.tongji.sse.notifiercenter.ide.manager;

import static org.eclipse.che.ide.MimeType.APPLICATION_JSON;
import static org.eclipse.che.ide.rest.HTTPHeader.CONTENT_TYPE;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.tongji.sse.notifiercenter.ide.interfaces.IntelligentConfigPresenter;
import edu.tongji.sse.notifiercenter.ide.interfaces.IntelligentResultPresenter;
import edu.tongji.sse.notifiercenter.ide.view.infoview.NfCenterInfoPresenter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.json.JsonHelper;
import org.eclipse.che.ide.rest.AsyncRequestFactory;
import org.eclipse.che.ide.rest.AsyncRequestLoader;
import org.eclipse.che.ide.rest.StringMapUnmarshaller;
import org.eclipse.che.ide.ui.loaders.request.LoaderFactory;

@Singleton
public class IntelligentPluginManagerImpl implements IntelligentPluginManager {

  private Map<String, IntelligentResultPresenter> intelligentResultPresenterMap;

  private Map<String, IntelligentConfigPresenter> intelligentConfigPresenterMap;

  private Map<String, Boolean> pluginAvailability;

  private AppContext appContext;

  private AsyncRequestFactory asyncRequestFactory;

  private AsyncRequestLoader asyncRequestLoader;

  private NfCenterInfoPresenter nfCenterInfoPresenter;

  private static String PREFERENCE_PATH = "intelligentPluginPreference/";

  private static String WORKSPACE = "workspace/";

  @Inject
  public IntelligentPluginManagerImpl(
      AppContext appContext,
      AsyncRequestFactory asyncRequestFactory,
      NfCenterInfoPresenter nfCenterInfoPresenter,
      LoaderFactory loaderFactory) {
    intelligentResultPresenterMap = new HashMap<>();
    intelligentConfigPresenterMap = new HashMap<>();
    this.appContext = appContext;
    this.asyncRequestFactory = asyncRequestFactory;
    this.nfCenterInfoPresenter = nfCenterInfoPresenter;
    this.asyncRequestLoader = loaderFactory.newLoader();
    this.loadPluginSwitch();
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
    if (!pluginAvailability.containsKey(name)) {
      pluginAvailability.put(name, Boolean.FALSE);
    }
    if (intelligentConfigPresenter != null) {
      intelligentConfigPresenterMap.put(name, intelligentConfigPresenter);
    }
  }

  @Override
  public boolean isPluginEnabled(String name) {
    if (this.pluginAvailability.containsKey(name)) {
      return this.pluginAvailability.get(name).booleanValue();
    }
    return false;
  }

  @Override
  public Map<String, Boolean> getPluginAvailability() {
    return this.pluginAvailability;
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
  public boolean savePluginAvailability(Map<String, String> availabilityMap) {
    this.nfCenterInfoPresenter.appendLine("plugin config clicked");
    String url = appContext.getWsAgentServerApiEndpoint() + "/" + PREFERENCE_PATH;
    String data = JsonHelper.toJson(availabilityMap);

    this.asyncRequestFactory
        .createPostRequest(url, null)
        .data(data)
        .header(CONTENT_TYPE, APPLICATION_JSON)
        .send()
        .then(
            succeed -> nfCenterInfoPresenter.appendLine("[post return from backend]"),
            reject -> {
              nfCenterInfoPresenter.appendLine("[post rejected by backend]" + reject.getMessage());
            })
        .catchError(
            error -> {
              nfCenterInfoPresenter.appendLine("[post error]" + error.getMessage());
            });
    return false;
  }

  private void loadPluginSwitch() {
    this.pluginAvailability = new HashMap<>();

    String url = appContext.getWsAgentServerApiEndpoint() + "/" + PREFERENCE_PATH;

    nfCenterInfoPresenter.appendLine("url=" + url);

    this.asyncRequestFactory
        .createGetRequest(url)
        .header(CONTENT_TYPE, APPLICATION_JSON)
        .send(new StringMapUnmarshaller())
        .then(
            preference -> {
              nfCenterInfoPresenter.appendLine(preference.toString());
              for (String str : preference.keySet()) {
                boolean b = preference.get(str).equalsIgnoreCase("true");
                nfCenterInfoPresenter.appendLine("[get return from backend]" + str + " : " + b);
                this.pluginAvailability.put(str, b);
              }
            })
        .catchError(
            error -> {
              nfCenterInfoPresenter.appendLine("[get error]" + error.getMessage());
            });
  }



  private void addEventListener(){


  }
}
