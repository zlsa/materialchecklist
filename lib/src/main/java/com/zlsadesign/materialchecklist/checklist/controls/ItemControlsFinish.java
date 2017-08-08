package com.zlsadesign.materialchecklist.checklist.controls;

import com.zlsadesign.materialchecklist.R;

public class ItemControlsFinish extends ItemControlsButtons {

  public ItemControlsFinish() {
  }

  public void build() {
    Button button = new Button(R.string.action_finish, ItemControlsButtons.FLAG_FINISH);
    button.action = true;

    this.addButton(button);
  }

}
