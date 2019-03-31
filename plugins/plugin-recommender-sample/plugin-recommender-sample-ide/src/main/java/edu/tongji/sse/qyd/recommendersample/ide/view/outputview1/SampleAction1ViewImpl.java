package edu.tongji.sse.qyd.recommendersample.ide.view.outputview1;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import org.eclipse.che.ide.api.parts.base.BaseView;

public class SampleAction1ViewImpl extends BaseView<SampleAction1View.ActionDelegate>
    implements SampleAction1View {

  private static final CodeRecommendResultViewImplUiBinder UI_BINDER =
      GWT.create(CodeRecommendResultViewImplUiBinder.class);

  private final DockLayoutPanel rootElement;

  @UiField FlowPanel resultTextLines;

  @UiField ScrollPanel scrollPanel;

  @Inject
  public SampleAction1ViewImpl() {
    rootElement = UI_BINDER.createAndBindUi(this);
    setContentWidget(rootElement);
  }

  @Override
  public void appendTextLine(String text) {
    HTML html = new HTML();
    html.setHTML("<div>" + text + "</div>");
    resultTextLines.add(html);
    while (resultTextLines.getWidgetCount() > 1000) {
      resultTextLines.remove(0);
    }
    scrollPanel.scrollToBottom();
  }

  interface CodeRecommendResultViewImplUiBinder
      extends UiBinder<DockLayoutPanel, SampleAction1ViewImpl> {}
}
