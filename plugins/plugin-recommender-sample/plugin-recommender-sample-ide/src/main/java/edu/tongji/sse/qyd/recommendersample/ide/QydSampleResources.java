package edu.tongji.sse.qyd.recommendersample.ide;

import com.google.gwt.resources.client.ClientBundle;
import org.vectomatic.dom.svg.ui.SVGResource;

public interface QydSampleResources extends ClientBundle {
  @Source("sample1-icon.svg")
  SVGResource getSample1Icon();

  @Source("sample2-icon.svg")
  SVGResource getSample2Icon();
}
