package edu.tongji.sse.qyd.recommendersample.ide.view.outputview2;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.tongji.sse.qyd.recommendersample.ide.QydSampleResources;
import edu.tongji.sse.qyd.recommendersample.ide.view.outputview1.SampleAction1View;
import java.util.List;
import org.eclipse.che.ide.api.editor.text.TextPosition;
import org.eclipse.che.ide.api.editor.texteditor.TextEditor;
import org.eclipse.che.ide.api.parts.base.BasePresenter;
import org.vectomatic.dom.svg.ui.SVGResource;

@Singleton
public class SampleAction2Presenter extends BasePresenter
    implements SampleAction2View.ActionDelegate {

  private SampleAction1View view1;

  private SampleAction2View view2;

  private QydSampleResources resources;

  @Inject
  public SampleAction2Presenter(
      SampleAction1View view1, SampleAction2View view2, QydSampleResources resources) {
    this.view1 = view1;
    this.view2 = view2;
    this.resources = resources;
    view2.setDelegate(this);
  }

  @Override
  public String getTitle() {
    return "analyze file title";
  }

  @Override
  public IsWidget getView() {
    return view2;
  }

  @Override
  public String getTitleToolTip() {
    return "analyze file tool tip";
  }

  @Override
  public void go(AcceptsOneWidget container) {
    container.setWidget(view2);
  }

  public void appendTextLine(String text) {
    view1.appendTextLine(text);
  }

  public void showView() {
    view2.setVisible(true);
  }

  @Override
  public SVGResource getTitleImage() {
    return resources.getSample2Icon();
  }

  @Override
  public void showResult(TextEditor textEditor, TextPosition position, List<String> candidates) {
    this.view2.showResult(textEditor, position, candidates);
  }
}
