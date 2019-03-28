package edu.tongji.sse.notifiercenter.ide.controller;

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

  Map<String, Boolean> getPluginAvailability();

  IntelligentConfigPresenter getCongfigPresenter(String name);

  boolean savePluginAvailability(Map<String, String> availabilityMap);
}
