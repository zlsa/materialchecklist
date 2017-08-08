package com.zlsadesign.materialchecklist.checklist.controls;

import com.zlsadesign.materialchecklist.R;

public class ItemControlsNext extends ItemControlsButtons {

  private int button = -1;

  public ItemControlsNext() {
    this(false);
  }

  public ItemControlsNext(boolean skip_button) {
    this.button = ItemControlsButtons.BUTTON_SKIP;
  }

  public ItemControlsNext(int button) {
    this.button = button;
  }

  public void build() {
    if(this.button >= 0) {
      this.addButton(button);
    }

    this.addButton(ItemControlsButtons.BUTTON_NEXT, true);
  }

}
