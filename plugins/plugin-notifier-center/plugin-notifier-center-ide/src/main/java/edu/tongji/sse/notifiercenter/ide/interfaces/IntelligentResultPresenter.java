package edu.tongji.sse.notifiercenter.ide.interfaces;

import edu.tongji.sse.notifiercenter.ide.config.EditorContextBean;
import edu.tongji.sse.notifiercenter.ide.config.FetchContextConfigBean;

public interface IntelligentResultPresenter {
  FetchContextConfigBean getFetchContextConfigBean();

  boolean isTimeToShowPart(EditorContextBean editorContextBean);

  void showResult();
}
