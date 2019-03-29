package edu.tongji.sse.notifiiercenter.server.inject;

import com.google.inject.AbstractModule;
import edu.tongji.sse.notifiiercenter.server.IntelligentAvailabilitiesService;
import org.eclipse.che.inject.DynaModule;

@DynaModule
public class IntelligentPreferenceGuiceModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(IntelligentAvailabilitiesService.class);
  }
}
