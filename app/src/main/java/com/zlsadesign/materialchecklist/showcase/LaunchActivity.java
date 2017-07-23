package com.zlsadesign.materialchecklist.showcase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LaunchActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_launch);
  }

  private void launchActivity(Class<? extends Activity> activity) {
    Intent intent = new Intent(this, activity);
    startActivity(intent);
  }

  public void launchActivity(View view) {

    switch(view.getId()) {
      case R.id.launch_basic_checklist:
        this.launchActivity(BasicChecklistActivity.class);
        break;
    }

  }

}
