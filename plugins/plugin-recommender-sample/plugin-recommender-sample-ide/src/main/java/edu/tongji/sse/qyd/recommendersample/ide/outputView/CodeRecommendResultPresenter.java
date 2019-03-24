package edu.tongji.sse.qyd.recommendersample.ide.outputView;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import javax.inject.Singleton;
import org.eclipse.che.ide.api.parts.base.BasePresenter;

@Singleton
public class CodeRecommendResultPresenter extends BasePresenter
    implements CodeRecommendResultView.ActionDelegate {

  private CodeRecommendResultView view;

  @Inject
  public CodeRecommendResultPresenter(CodeRecommendResultView view) {
    this.view = view;
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
}
