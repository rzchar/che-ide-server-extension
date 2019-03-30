package edu.tongji.sse.notifiercenter.ide.manager;

import edu.tongji.sse.notifiercenter.ide.interfaces.IntelligentConfigPresenter;
import edu.tongji.sse.notifiercenter.ide.interfaces.IntelligentResultPresenter;
import java.util.Map;
import java.util.Set;

public interface IntelligentPluginManager {

  Set<String> getRegisteredPlugins();

  void registerPlugin(String name, IntelligentResultPresenter intelligentResultPresenter);

  void registerPlugin(
      String name,
      IntelligentResultPresenter intelligentResultPresenter,
      IntelligentConfigPresenter configPresenter);

  boolean isPluginEnabled(String name);

  Map<String, Boolean> getPluginAvailabilities();

  IntelligentConfigPresenter getCongfigPresenter(String name);

  void savePluginAvailability(Map<String, Boolean> availabilityMap);
}