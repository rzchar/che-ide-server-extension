package edu.tongji.sse.notifiercenter.ide.config;

import com.google.gwt.event.shared.GwtEvent;

public class FetchContextConfigBean {

  private String inputStreamTrigger;

  private int contextLines;

  private GwtEvent listenedEvent;

  public FetchContextConfigBean(
      String inputStreamTrigger, int contextLines, GwtEvent listenedEvent) {
    this.inputStreamTrigger = inputStreamTrigger;
    this.contextLines = contextLines;
    this.listenedEvent = listenedEvent;
  }

  public String getInputStreamTrigger() {
    return inputStreamTrigger;
  }

  public void setInputStreamTrigger(String inputStreamTrigger) {
    this.inputStreamTrigger = inputStreamTrigger;
  }

  public int getContextLines() {
    return contextLines;
  }

  public void setContextLines(int contextLines) {
    this.contextLines = contextLines;
  }

  public GwtEvent getListenedEvent() {
    return listenedEvent;
  }

  public void setListenedEvent(GwtEvent listenedEvent) {
    this.listenedEvent = listenedEvent;
  }
}
