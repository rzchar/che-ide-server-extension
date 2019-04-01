package edu.tongji.sse.intelligentmanagementcenter.server.inject;

import com.google.inject.AbstractModule;
import edu.tongji.sse.intelligentmanagementcenter.server.IntelligentAvailabilitiesService;
import org.eclipse.che.inject.DynaModule;

@DynaModule
public class IntelligentPreferenceGuiceModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(IntelligentAvailabilitiesService.class);
  }
}
