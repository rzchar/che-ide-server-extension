package edu.tongji.sse.notifiercenter.ide.action;

import org.eclipse.che.ide.api.action.BaseAction;
import org.eclipse.che.ide.api.parts.PartPresenter;
import org.eclipse.che.ide.api.parts.PartStackType;
import org.eclipse.che.ide.ui.window.Window;
import org.vectomatic.dom.svg.ui.SVGResource;

public abstract class BaseIntelligentAssistantAction extends BaseAction {

  public BaseIntelligentAssistantAction() {}

  public BaseIntelligentAssistantAction(String text) {
    super(text);
  }

  public BaseIntelligentAssistantAction(String text, String description) {
    super(text, description);
  }

  public BaseIntelligentAssistantAction(String text, String description, SVGResource svgResource) {
    super(text, description, svgResource);
  }

  public BaseIntelligentAssistantAction(String text, String description, String htmlResource) {
    super(text, description, htmlResource);
  }

  public BaseIntelligentAssistantAction(
      String text, String description, SVGResource svgResource, String htmlResource) {
    super(text, description, svgResource, htmlResource);
  }

  public abstract boolean isEnable();

  public abstract void setEnable(boolean enable);

  public Window getConfigWindow() {
    return null;
  }

  public abstract PartPresenter getResultPresenter();

  public PartStackType getResultPresenterPartStack() {
    return PartStackType.INFORMATION;
  }
}
