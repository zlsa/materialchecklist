package com.zlsadesign.materialchecklist.checklist.controls;

import com.zlsadesign.materialchecklist.R;

public class ItemControlsNext extends ItemControlsButtons {

  public ItemControlsNext() {
    this(false);
  }

  public ItemControlsNext(boolean skip_button) {

    if(skip_button) {
      this.addButton(ItemControlsButtons.BUTTON_SKIP);
    }

    this.addButton(ItemControlsButtons.BUTTON_NEXT, true);
  }

  public ItemControlsNext(int button) {
    if(button >= 0) {
      this.addButton(button);
    }

    this.addButton(ItemControlsButtons.BUTTON_NEXT, true);
  }

}
