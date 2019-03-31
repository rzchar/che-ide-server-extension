package edu.tongji.sse.qyd.recommendersample.ide.view.outputview2;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import edu.tongji.sse.qyd.recommendersample.ide.view.outputview1.SampleAction1Presenter;
import java.util.List;
import org.eclipse.che.ide.api.editor.text.TextPosition;
import org.eclipse.che.ide.api.editor.texteditor.TextEditor;
import org.eclipse.che.ide.api.parts.base.BaseView;

public class SampleAction2ViewImpl extends BaseView<SampleAction2View.ActionDelegate>
    implements SampleAction2View {

  private static final CodeRecommendResultViewImplUiBinder UI_BINDER =
      GWT.create(CodeRecommendResultViewImplUiBinder.class);

  private final DockLayoutPanel rootElement;

  private final SampleAction1Presenter sampleAction1Presenter;

  @UiField VerticalPanel resultList;

  @UiField ScrollPanel resultScrollPanel;

  @Inject
  public SampleAction2ViewImpl(SampleAction1Presenter sampleAction1Presenter) {
    this.sampleAction1Presenter = sampleAction1Presenter;
    rootElement = UI_BINDER.createAndBindUi(this);
    setContentWidget(rootElement);
  }

  @Override
  public void showResult(TextEditor textEditor, TextPosition position, List<String> candidates) {
    resultList.clear();
    for (String candidate : candidates) {
      logInPresenter1(candidate);
      HorizontalPanel candidatePanel = new HorizontalPanel();
      HTML candidateText = new HTML();
      SafeHtmlBuilder safeHtmlBuilder = new SafeHtmlBuilder();
      safeHtmlBuilder.appendEscaped(candidate);
      candidateText.setHTML(safeHtmlBuilder.toSafeHtml());
      logInPresenter1("html succeed" + candidateText.getHTML());

      Button addToCodeButton = new Button();
      addToCodeButton.setText("addToCode");
      addToCodeButton.addClickHandler(
          clickEvent -> {
            textEditor
                .getDocument()
                .replace(
                    position.getLine(),
                    position.getCharacter(),
                    position.getLine(),
                    position.getCharacter(),
                    candidate);
            TextPosition newPosition =
                new TextPosition(position.getLine(), position.getCharacter() + candidate.length());
            textEditor.setCursorPosition(newPosition);
          });
      logInPresenter1("button succeed" + addToCodeButton.getHTML());
      candidatePanel.add(addToCodeButton);
      candidatePanel.add(candidateText);
      resultList.add(candidatePanel);
      logInPresenter1("num" + resultList.getWidgetCount());
    }
    this.setVisible(true);
    resultScrollPanel.scrollToTop();
  }

  private void logInPresenter1(String s) {
    sampleAction1Presenter.appendTextLine("[sample 2 presenter]" + s);
  }

  interface CodeRecommendResultViewImplUiBinder
      extends UiBinder<DockLayoutPanel, SampleAction2ViewImpl> {}
}
