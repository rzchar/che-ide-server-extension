package edu.tongji.sse.intelligentmanagementcenter.ide.inject;

import com.google.gwt.inject.client.AbstractGinModule;
import edu.tongji.sse.intelligentmanagementcenter.ide.manager.IntelligentPluginManager;
import edu.tongji.sse.intelligentmanagementcenter.ide.manager.IntelligentPluginManagerImpl;
import edu.tongji.sse.intelligentmanagementcenter.ide.view.configview.NfConfigView;
import edu.tongji.sse.intelligentmanagementcenter.ide.view.configview.NfConfigViewImpl;
import edu.tongji.sse.intelligentmanagementcenter.ide.view.infoview.NfCenterInfoView;
import edu.tongji.sse.intelligentmanagementcenter.ide.view.infoview.NfCenterInfoViewImpl;
import org.eclipse.che.ide.api.extension.ExtensionGinModule;

/** @author QYD */
@ExtensionGinModule
public class IntelligentManagementCenterGinModule extends AbstractGinModule {
  @Override
  protected void configure() {
    bind(NfCenterInfoView.class).to(NfCenterInfoViewImpl.class);
    bind(NfConfigView.class).to(NfConfigViewImpl.class);
    bind(IntelligentPluginManager.class).to(IntelligentPluginManagerImpl.class);
  }
}
