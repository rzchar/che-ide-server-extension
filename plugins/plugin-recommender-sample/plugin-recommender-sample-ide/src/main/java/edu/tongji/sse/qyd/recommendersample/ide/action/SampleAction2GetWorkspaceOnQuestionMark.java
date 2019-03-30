package edu.tongji.sse.qyd.recommendersample.ide.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import edu.tongji.sse.notifiercenter.ide.action.BaseIntelligentAssistantAction;
import edu.tongji.sse.qyd.recommendersample.ide.view.outputview2.SampleAction2Presenter;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.editor.document.DocumentEventBus;
import org.eclipse.che.ide.api.editor.events.TextChangeEvent;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.parts.PartPresenter;
import org.eclipse.che.ide.api.parts.base.BasePresenter;
import org.eclipse.che.ide.api.resources.ResourceChangedEvent;
import org.eclipse.che.ide.console.OutputConsoleView;
import org.eclipse.che.ide.rest.AsyncRequestFactory;
import org.eclipse.che.ide.rest.StringMapUnmarshaller;

@Singleton
public class SampleAction2GetWorkspaceOnQuestionMark extends BaseIntelligentAssistantAction {

  private final AppContext appContext;
  private final StringMapUnmarshaller unmarshaller;
  private final AsyncRequestFactory asyncRequestFactory;
  private final NotificationManager notificationManager;
  private final OutputConsoleView consoleView;
  private final SampleAction2Presenter sampleAction2Presenter;
  private final EditorAgent editorAgent;

  private final DocumentEventBus documentEventBus;
  private final EventBus eventBus;

  /**
   * Constructor
   *
   * @param appContext the IDE application context
   * @param asyncRequestFactory asynchronous request factory for creating the server request
   * @param notificationManager the notification manager used to display the lines of code per file
   */
  @Inject
  public SampleAction2GetWorkspaceOnQuestionMark(
      AppContext appContext,
      AsyncRequestFactory asyncRequestFactory,
      NotificationManager notificationManager,
      OutputConsoleView consoleView,
      EditorAgent editorAgent,
      DocumentEventBus documentEventBus,
      EventBus eventBus,
      SampleAction2Presenter sampleAction2Presenter) {

    super("Analyzer From Current File", "try to call the wsagent to analyze");

    this.appContext = appContext;
    this.asyncRequestFactory = asyncRequestFactory;
    this.notificationManager = notificationManager;
    this.unmarshaller = new StringMapUnmarshaller();
    this.consoleView = consoleView;
    this.sampleAction2Presenter = sampleAction2Presenter;
    this.editorAgent = editorAgent;

    this.documentEventBus = documentEventBus;
    this.eventBus = eventBus;
  }

  private void addHandlerToEditor() {

    this.eventBus.addHandler(
        TextChangeEvent.TYPE,
        event ->
            this.sampleAction2Presenter.appendTextLine(
                "[event bus - TextChangeEvent]" + event.getChange().getNewText()));
    this.eventBus.addHandler(
        ResourceChangedEvent.getType(),
        event ->
            this.sampleAction2Presenter.appendTextLine(
                "[event bus - ResourceChangeEvent]" + event.getDelta().getResource().isFile()));
    this.documentEventBus.addHandler(
        TextChangeEvent.TYPE,
        event ->
            this.sampleAction2Presenter.appendTextLine(
                "[document event bus - TextChangeEvent]" + event.getChange().getNewText()));
    this.documentEventBus.addHandler(
        ResourceChangedEvent.getType(),
        event ->
            this.sampleAction2Presenter.appendTextLine(
                "[document event bus - ResourceChangeEvent]"
                    + event.getDelta().getResource().isFile()));
  }

  public BasePresenter getBasePresenter() {
    return this.sampleAction2Presenter;
  }

  @Override
  public void actionPerformed(ActionEvent e) {}

  @Override
  public boolean isEnable() {
    return false;
  }

  @Override
  public void setEnable(boolean enable) {}

  @Override
  public PartPresenter getResultPresenter() {
    return this.sampleAction2Presenter;
  }
}
