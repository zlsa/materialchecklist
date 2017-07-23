package com.zlsadesign.materialchecklist.views;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.zlsadesign.materialchecklist.R;
import com.zlsadesign.materialchecklist.checklist.Checklist;

public abstract class ChecklistActivity extends Activity {

  protected Checklist checklist = new Checklist();

  @Override
  public void onCreate(Bundle state) {
    super.onCreate(state);

    this.createChecklist();

    setTheme(R.style.MaterialChecklistTheme);

    setContentView(R.layout.activity_checklist);

    this.createFragment();
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
}
