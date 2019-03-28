package edu.tongji.sse.qyd.recommendersample.ide.view.outputview1;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.PreElement;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.inject.Inject;
import org.eclipse.che.ide.api.parts.base.BaseView;

public class SampleAction1ViewImpl extends BaseView<SampleAction1View.ActionDelegate>
    implements SampleAction1View {

  private static final CodeRecommendResultViewImplUiBinder UI_BINDER =
      GWT.create(CodeRecommendResultViewImplUiBinder.class);

  private final DockLayoutPanel rootElement;

  @UiField ScrollPanel resultTextLines;

  @Inject
  public SampleAction1ViewImpl() {
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
    DivElement div = DOM.createElement("div").cast();
    div.setInnerText(SafeHtmlUtils.htmlEscape(text));
    resultTextLines.getElement().appendChild(div);
    this.resultTextLines.scrollToBottom();
  }

  interface CodeRecommendResultViewImplUiBinder
      extends UiBinder<DockLayoutPanel, SampleAction1ViewImpl> {}
}
