package edu.tongji.sse.notifiercenter.ide.view.configview;

import com.google.inject.ImplementedBy;
import org.eclipse.che.ide.api.mvp.View;

@ImplementedBy(NfConfigViewImpl.class)
public interface NfConfigView extends View<NfConfigView.ActionDelegate> {
  interface ActionDelegate {}

  void saveChange();

  void cancelChange();

  void close();

  void showDialog();
}
