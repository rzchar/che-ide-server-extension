package edu.tongji.sse.qyd.recommendersample.ide.outputView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.PreElement;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;
import org.eclipse.che.ide.api.parts.base.BaseView;

public class CodeRecommendResultViewImpl extends BaseView<CodeRecommendResultView.ActionDelegate>
    implements CodeRecommendResultView {

  private static final CodeRecommendResultViewImplUiBinder UI_BINDER =
      GWT.create(CodeRecommendResultViewImplUiBinder.class);

  private final DockLayoutPanel rootElement;

  @UiField FlowPanel resultTextLines;

  @Inject
  public CodeRecommendResultViewImpl() {
    rootElement = UI_BINDER.createAndBindUi(this);
    setContentWidget(rootElement);
  }

  @Override
  public String getText() {
    return "";
  }

  @Override
  public void setText(String text) {
    PreElement pre = DOM.createElement("pre").cast();
    pre.setInnerText(SafeHtmlUtils.htmlEscape(text));
    resultTextLines.getElement().removeAllChildren();
    resultTextLines.getElement().appendChild(pre);
  }

  @Override
  public void appendTextLine(String text) {
    if (resultTextLines.getElement().getChildCount() >= 1000) {
      Node firstChild = resultTextLines.getElement().getFirstChild();
      resultTextLines.getElement().removeChild(firstChild);
    }
    ParagraphElement paragraph = DOM.createElement("p").cast();
    // PreElement pre = DOM.createElement("pre").cast();
    paragraph.setInnerText(SafeHtmlUtils.htmlEscape(text));
    resultTextLines.getElement().appendChild(paragraph);
  }

  interface CodeRecommendResultViewImplUiBinder
      extends UiBinder<DockLayoutPanel, CodeRecommendResultViewImpl> {}
}
