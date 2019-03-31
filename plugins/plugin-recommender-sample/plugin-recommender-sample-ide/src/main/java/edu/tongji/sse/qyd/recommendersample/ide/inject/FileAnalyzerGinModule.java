package edu.tongji.sse.qyd.recommendersample.ide.inject;

import com.google.gwt.inject.client.AbstractGinModule;
import edu.tongji.sse.qyd.recommendersample.ide.view.outputview1.SampleAction1View;
import edu.tongji.sse.qyd.recommendersample.ide.view.outputview1.SampleAction1ViewImpl;
import edu.tongji.sse.qyd.recommendersample.ide.view.outputview2.SampleAction2View;
import edu.tongji.sse.qyd.recommendersample.ide.view.outputview2.SampleAction2ViewImpl;
import org.eclipse.che.ide.api.extension.ExtensionGinModule;

/** @author QYD */
@ExtensionGinModule
public class FileAnalyzerGinModule extends AbstractGinModule {
  @Override
  protected void configure() {
    bind(SampleAction2View.class).to(SampleAction2ViewImpl.class);
    bind(SampleAction1View.class).to(SampleAction1ViewImpl.class);
  }
}
