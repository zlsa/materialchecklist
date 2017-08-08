package com.zlsadesign.materialchecklist.views;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.zlsadesign.materialchecklist.R;
import com.zlsadesign.materialchecklist.checklist.Checklist;

public abstract class ChecklistActivity extends Activity {

  protected Checklist checklist = new Checklist();

  @Override
  public void onCreate(Bundle state) {
    super.onCreate(state);

    this.createChecklist();

    if(state != null && state.containsKey("checklist_state")) {
      this.checklist.applyState(state.getBundle("checklist_state"));
    }

    setTheme(R.style.MaterialChecklistTheme);

    setContentView(R.layout.activity_checklist);

    this.createFragment();
  }

  @Override
  public void onSaveInstanceState(Bundle out) {
    super.onSaveInstanceState(out);

    out.putBundle("checklist_state", this.checklist.getState());
  }

  protected abstract void createChecklist();

  private void createFragment() {
    FragmentManager manager = getFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();

    ChecklistFragment fragment = new ChecklistFragment();

    fragment.setChecklist(this.checklist);

    transaction.replace(R.id.checklist_fragment, fragment);
    transaction.commit();
  }

  public void onClose() {
    this.finish();
  }

  @Override
  public void onRequestPermissionsResult(int code, @NonNull String[] permissions, @NonNull int[] results) {
    this.checklist.onRequestPermissionsResult(code, permissions, results);
  }
}
