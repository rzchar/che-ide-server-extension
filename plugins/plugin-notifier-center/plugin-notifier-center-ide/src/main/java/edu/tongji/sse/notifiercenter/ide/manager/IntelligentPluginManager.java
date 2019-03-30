package edu.tongji.sse.notifiercenter.ide.manager;

import edu.tongji.sse.notifiercenter.ide.action.BaseIntelligentAssistantAction;
import java.util.Map;
import java.util.Set;
import org.eclipse.che.ide.ui.window.Window;

public interface IntelligentPluginManager {

  Set<String> getRegisteredPlugins();

  void registerPlugin(String name, BaseIntelligentAssistantAction baseIntelligentAssistantAction);

  boolean isPluginEnabled(String name);

  Map<String, Boolean> getPluginAvailabilities();

  Window getCongfigPresenter(String name);

  void savePluginAvailability(Map<String, Boolean> availabilityMap);

  void logOnPresenter(String str);
}
