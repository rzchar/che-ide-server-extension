package edu.tongji.sse.qyd.recommendersample.ide.outputView;

import com.google.inject.ImplementedBy;
import org.eclipse.che.ide.api.mvp.View;
import org.eclipse.che.ide.api.parts.base.BaseActionDelegate;

@ImplementedBy(CodeRecommendResultViewImpl.class)
public interface CodeRecommendResultView extends View<CodeRecommendResultView.ActionDelegate> {

  void setVisible(boolean visible);

  String getText();

  void setText(String text);

  void appendTextLine(String text);

  interface ActionDelegate extends BaseActionDelegate {

    String getText();

    void setText(String text);

    void appendTextLine(String text);
  }
}
