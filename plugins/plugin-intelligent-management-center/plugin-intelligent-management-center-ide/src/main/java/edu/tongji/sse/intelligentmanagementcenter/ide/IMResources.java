package edu.tongji.sse.intelligentmanagementcenter.ide;

import com.google.gwt.resources.client.ClientBundle;
import org.vectomatic.dom.svg.ui.SVGResource;

public interface IMResources extends ClientBundle {
  @Source("nfcenter-info-icon.svg")
  SVGResource eventsPartIcon();

  @Source("nfconfig-icon.svg")
  SVGResource configPartIcon();
}
