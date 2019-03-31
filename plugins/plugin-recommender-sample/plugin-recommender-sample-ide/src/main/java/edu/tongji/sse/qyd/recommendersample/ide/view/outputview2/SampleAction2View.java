package edu.tongji.sse.qyd.recommendersample.ide.view.outputview2;

import com.google.inject.ImplementedBy;
import java.util.List;
import org.eclipse.che.ide.api.editor.text.TextPosition;
import org.eclipse.che.ide.api.editor.texteditor.TextEditor;
import org.eclipse.che.ide.api.mvp.View;
import org.eclipse.che.ide.api.parts.base.BaseActionDelegate;

@ImplementedBy(SampleAction2ViewImpl.class)
public interface SampleAction2View extends View<SampleAction2View.ActionDelegate> {

  void setVisible(boolean visible);

  void showResult(TextEditor textEditor, TextPosition position, List<String> candidates);

  interface ActionDelegate extends BaseActionDelegate {

    void showResult(TextEditor textEditor, TextPosition position, List<String> candidates);
  }
}
