package edu.tongji.sse.notifiercenter.ide.inject;

import com.google.gwt.inject.client.AbstractGinModule;
import edu.tongji.sse.notifiercenter.ide.controller.IntelligentPluginManager;
import edu.tongji.sse.notifiercenter.ide.controller.IntelligentPluginManagerImpl;
import edu.tongji.sse.notifiercenter.ide.view.configview.NfConfigView;
import edu.tongji.sse.notifiercenter.ide.view.configview.NfConfigViewImpl;
import edu.tongji.sse.notifiercenter.ide.view.infoview.NfCenterInfoView;
import edu.tongji.sse.notifiercenter.ide.view.infoview.NfCenterInfoViewImpl;
import org.eclipse.che.ide.api.extension.ExtensionGinModule;

/** @author QYD */
@ExtensionGinModule
public class NotifierCenterGinModule extends AbstractGinModule {
  @Override
  protected void configure() {
    bind(NfCenterInfoView.class).to(NfCenterInfoViewImpl.class);
    bind(NfConfigView.class).to(NfConfigViewImpl.class);
    bind(IntelligentPluginManager.class).to(IntelligentPluginManagerImpl.class);
  }
}
