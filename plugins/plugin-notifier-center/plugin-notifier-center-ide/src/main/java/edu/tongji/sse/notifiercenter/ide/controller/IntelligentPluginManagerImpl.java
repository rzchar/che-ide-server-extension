package edu.tongji.sse.notifiercenter.ide.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.tongji.sse.notifiercenter.ide.interfaces.IntelligentConfigPresenter;
import edu.tongji.sse.notifiercenter.ide.interfaces.IntelligentResultPresenter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class IntelligentPluginManagerImpl implements IntelligentPluginManager {

  private Map<String, IntelligentResultPresenter> intelligentResultPresenterMap;

  private Map<String, IntelligentConfigPresenter> intelligentConfigPresenterMap;

  private Map<String, Boolean> pluginAvailability;

  @Inject
  public IntelligentPluginManagerImpl() {
    intelligentResultPresenterMap = new HashMap<>();
    intelligentConfigPresenterMap = new HashMap<>();
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

  private void loadPluginSwitch() {
    this.pluginAvailability = new HashMap<>();
  }
}
