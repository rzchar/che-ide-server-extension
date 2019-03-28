package edu.tongji.sse.qyd.recommendersample.ide.view.outputview2;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.tongji.sse.notifiercenter.ide.config.EditorContextBean;
import edu.tongji.sse.notifiercenter.ide.config.FetchContextConfigBean;
import edu.tongji.sse.notifiercenter.ide.interfaces.IntelligentResultPresenter;
import edu.tongji.sse.qyd.recommendersample.ide.QydSampleResources;
import edu.tongji.sse.qyd.recommendersample.ide.view.outputview1.SampleAction1View;
import org.eclipse.che.ide.api.parts.base.BasePresenter;
import org.vectomatic.dom.svg.ui.SVGResource;

@Singleton
public class SampleAction2Presenter extends BasePresenter
    implements SampleAction1View.ActionDelegate, IntelligentResultPresenter {

  private SampleAction1View view;

  private QydSampleResources resources;

  @Inject
  public SampleAction2Presenter(SampleAction1View view, QydSampleResources resources) {
    this.view = view;
    this.resources = resources;
    view.setDelegate(this);
  }

  @Override
  public String getTitle() {
    return "analyze file title";
  }

  @Override
  public IsWidget getView() {
    return view;
  }

  @Override
  public String getTitleToolTip() {
    return "analyze file tool tip";
  }

  @Override
  public void go(AcceptsOneWidget container) {
    container.setWidget(view);
  }

  @Override
  public String getText() {
    return view.getText();
  }

  @Override
  public void setText(String text) {
    view.setText(text);
  }

  @Override
  public void appendTextLine(String text) {
    view.appendTextLine(text);
  }

  public void showView() {
    view.setVisible(true);
  }

  @Override
  public FetchContextConfigBean getFetchContextConfigBean() {
    return null;
  }

  @Override
  public boolean isTimeToShowPart(EditorContextBean editorContextBean) {
    return true;
  }

  @Override
  public void showResult() {
    view.setVisible(true);
  }

  @Override
  public SVGResource getTitleImage() {
    return resources.getSample2Icon();
  }
}
