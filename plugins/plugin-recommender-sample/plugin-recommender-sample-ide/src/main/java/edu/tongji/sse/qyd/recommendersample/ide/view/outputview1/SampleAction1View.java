package edu.tongji.sse.qyd.recommendersample.ide.view.outputview1;

import com.google.inject.ImplementedBy;
import org.eclipse.che.ide.api.mvp.View;
import org.eclipse.che.ide.api.parts.base.BaseActionDelegate;

@ImplementedBy(SampleAction1ViewImpl.class)
public interface SampleAction1View extends View<SampleAction1View.ActionDelegate> {

  void setVisible(boolean visible);

  void appendTextLine(String text);

  interface ActionDelegate extends BaseActionDelegate {

    void appendTextLine(String text);
  }
}
