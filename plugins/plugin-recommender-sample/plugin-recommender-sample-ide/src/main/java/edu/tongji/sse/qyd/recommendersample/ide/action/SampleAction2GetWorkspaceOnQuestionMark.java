package edu.tongji.sse.qyd.recommendersample.ide.action;

import static org.eclipse.che.ide.MimeType.TEXT_PLAIN;
import static org.eclipse.che.ide.rest.HTTPHeader.CONTENT_TYPE;

import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import edu.tongji.sse.intelligentmanagementcenter.ide.action.AbstractIntelligentPluginAction;
import edu.tongji.sse.qyd.recommendersample.ide.view.configwindow.ConfigWindowViewImpl;
import edu.tongji.sse.qyd.recommendersample.ide.view.outputview1.SampleAction1Presenter;
import edu.tongji.sse.qyd.recommendersample.ide.view.outputview2.SampleAction2Presenter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.che.ide.actions.EditFileAction;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.editor.EditorOpenedEvent;
import org.eclipse.che.ide.api.editor.EditorPartPresenter;
import org.eclipse.che.ide.api.editor.document.Document;
import org.eclipse.che.ide.api.editor.document.DocumentEventBus;
import org.eclipse.che.ide.api.editor.events.DocumentChangedEvent;
import org.eclipse.che.ide.api.editor.text.TextPosition;
import org.eclipse.che.ide.api.editor.texteditor.TextEditor;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.parts.PartPresenter;
import org.eclipse.che.ide.console.OutputConsoleView;
import org.eclipse.che.ide.rest.AsyncRequestFactory;
import org.eclipse.che.ide.rest.StringMapUnmarshaller;
import org.eclipse.che.ide.ui.window.Window;

@Singleton
public class SampleAction2GetWorkspaceOnQuestionMark extends AbstractIntelligentPluginAction {

  private final AppContext appContext;
  private final StringMapUnmarshaller unmarshaller;
  private final AsyncRequestFactory asyncRequestFactory;
  private final NotificationManager notificationManager;
  private final OutputConsoleView consoleView;
  private final SampleAction1Presenter sampleAction1Presenter;
  private final SampleAction2Presenter sampleAction2Presenter;
  private final EventBus eventBus;
  private final EditorAgent editorAgent;
  private final ConfigWindowViewImpl configWindowViewImpl;
  // private final Map<String,List<DocumentChangedEvent>> inputCaches;
  private final Map<String, String> inputCaches;
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
      EventBus eventBus,
      EditorAgent editorAgent,
      SampleAction1Presenter sampleAction1Presenter,
      SampleAction2Presenter sampleAction2Presenter,
      ConfigWindowViewImpl configWindowViewImpl) {

    super("Analyzer From Current File", "try to call the wsagent to analyze");

    this.appContext = appContext;
    this.asyncRequestFactory = asyncRequestFactory;
    this.notificationManager = notificationManager;
    this.unmarshaller = new StringMapUnmarshaller();
    this.consoleView = consoleView;
    this.sampleAction1Presenter = sampleAction1Presenter;
    this.sampleAction2Presenter = sampleAction2Presenter;
    this.eventBus = eventBus;
    this.editorAgent = editorAgent;
    this.addHandlerToEditor();
    this.tryGet();
    this.logInConsole("sample init complete");
    this.configWindowViewImpl = configWindowViewImpl;
    this.inputCaches = new HashMap<>();
  }

  private void addHandlerToEditor() {
    this.logInConsole("add handler start");
    EditFileAction editFileAction;

    this.eventBus.addHandler(
        EditorOpenedEvent.TYPE,
        event -> {
          this.logInConsole("[event bus - EditorOpenedEvent]" + event.getFile());
          EditorPartPresenter editor = event.getEditor();
          if (editor instanceof TextEditor) {
            TextEditor textEditor = (TextEditor) editor;
            DocumentEventBus documentEventBus =
                textEditor.getDocument().getDocumentHandle().getDocEventBus();
            documentEventBus.addHandler(
                DocumentChangedEvent.TYPE,
                documentEvent -> {
                  String fileName =
                      documentEvent.getDocument().getDocument().getFile().getContentUrl();
                  if (!inputCaches.containsKey(fileName)) {
                    inputCaches.put(fileName, "");
                  }
                  inputCaches.put(fileName, inputCaches.get(fileName) + documentEvent.getText());

                  String currentCashe = inputCaches.get(fileName);

                  this.sampleAction1Presenter.appendTextLine(
                      "[text change event: cache]" + currentCashe);
                  if (currentCashe.endsWith("???")) {

                    getRecommendation(textEditor, textEditor.getCursorPosition());
                    this.inputCaches.put(fileName, "");
                  }
                  //                          + documentEvent.getText()
                  //                          + " : "
                  //                          + documentEvent.getRemoveCharCount()
                  //                          + " : "
                  //                          + documentEvent.getAssociatedType()
                  //                          + " : "
                  //                          + documentEvent.getOffset());
                });
          }
        });
    //    this.eventBus.addHandler(
    //        EditorDirtyStateChangedEvent.TYPE,
    //        event -> {
    //          TextEditor textEditor = (TextEditor) event.getEditor();
    //          TextPosition tp = textEditor.getCursorPosition();
    //          // String line = textEditor.getDocument().getgetLineAtOffset(tp.getLine());
    //          this.logInConsole(
    //              "[event bus - EditorDirtyStateChangedEvent]"
    //                  + tp.toString()
    //                  + "::"
    //                  + event.getEditor().isDirty());
    //          if (youSaidJOJOJustNowDidn_tYou(textEditor, tp) && event.getEditor().isDirty()) {
    //            getRecommendation(textEditor, tp);
    //          }
    //        });

  }

  private void logInConsole(String s) {
    this.sampleAction1Presenter.appendTextLine("[sample2]" + s);
  }

  private boolean youSaidJOJOJustNowDidn_tYou(TextEditor textEditor, TextPosition position) {
    Document document = textEditor.getDocument();
    String currentLine = document.getLineContent(position.getLine());
    if (currentLine.length() < 4 || position.getCharacter() < 4) {
      return false;
    }
    return currentLine
        .substring(position.getCharacter() - 4, position.getCharacter())
        .equalsIgnoreCase("jojo");
  }

  private void getRecommendation(TextEditor textEditor, TextPosition position) {
    this.logInConsole("some one said jojo");
    String url = appContext.getWsAgentServerApiEndpoint() + "/sample2Recommendation/";
    JSONObject jo = new JSONObject();
    jo.put("pre_test", JSONBoolean.getInstance(true));

    url += URL.encode(jo.toString());

    asyncRequestFactory
        .createGetRequest(url)
        .header(CONTENT_TYPE, TEXT_PLAIN)
        .send(new StringMapUnmarshaller())
        .then(
            respondMap -> {
              logInConsole("[sample jojo respond]" + respondMap);
              List<String> respondValues = new ArrayList<>();
              for (String key : respondMap.keySet()) {
                respondValues.add(key + respondMap.get(key));
              }
              sampleAction2Presenter.showResult(textEditor, position, respondValues);
            })
        .catchError(
            error -> {
              logInConsole("[jojo get error]" + error.getMessage());
            });
  }

  private void tryGet() {
    String url = appContext.getWsAgentServerApiEndpoint() + "/sample2Recommendation/";
    JSONObject jo = new JSONObject();
    jo.put("pre_test", JSONBoolean.getInstance(true));

    url += URL.encode(jo.toString());

    this.logInConsole("get test url: " + url);
    this.logInConsole(jo.toString());

    asyncRequestFactory
        .createGetRequest(url)
        .header(CONTENT_TYPE, TEXT_PLAIN)
        .send(new StringMapUnmarshaller())
        .then(
            respondMap -> {
              logInConsole("[sample test respond]" + respondMap);
            })
        .catchError(
            error -> {
              logInConsole("[try get error]" + error.getMessage());
            });
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

  @Override
  public Window getConfigWindow() {
    return this.configWindowViewImpl;
  }
}
