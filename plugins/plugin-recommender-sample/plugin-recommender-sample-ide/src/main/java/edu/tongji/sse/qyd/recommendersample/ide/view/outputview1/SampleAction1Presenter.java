package edu.tongji.sse.qyd.recommendersample.ide.view.outputview1;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.tongji.sse.qyd.recommendersample.ide.QydSampleResources;
import org.eclipse.che.ide.api.parts.base.BasePresenter;
import org.vectomatic.dom.svg.ui.SVGResource;

@Singleton
public class SampleAction1Presenter extends BasePresenter
    implements SampleAction1View.ActionDelegate {

  private SampleAction1View view;

  private QydSampleResources resources;

  @Inject
  public SampleAction1Presenter(SampleAction1View view, QydSampleResources resources) {
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
  public void appendTextLine(String text) {
    view.appendTextLine(text);
  }

  @Override
  public SVGResource getTitleImage() {
    return resources.getSample1Icon();
  }

  @Override
  public void clear() {
    view.clear();
  }
}
