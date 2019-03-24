package edu.tongji.sse.qyd.recommendersample.ide.inject;

import com.google.gwt.inject.client.AbstractGinModule;
import edu.tongji.sse.qyd.recommendersample.ide.outputView.CodeRecommendResultView;
import edu.tongji.sse.qyd.recommendersample.ide.outputView.CodeRecommendResultViewImpl;
import org.eclipse.che.ide.api.extension.ExtensionGinModule;

/** @author QYD */
@ExtensionGinModule
public class FileAnalyzerGinModule extends AbstractGinModule {
  @Override
  protected void configure() {
    bind(CodeRecommendResultView.class).to(CodeRecommendResultViewImpl.class);
  }
}
