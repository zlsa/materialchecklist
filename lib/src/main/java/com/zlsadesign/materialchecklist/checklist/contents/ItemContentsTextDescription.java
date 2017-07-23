package com.zlsadesign.materialchecklist.checklist.contents;

import com.zlsadesign.materialchecklist.checklist.Item;

public class ItemContentsTextDescription extends ItemContents {

  private String description = null;

  public ItemContentsTextDescription(String description) {
    super();

    this.setDescription(description);
  }

  // # Description

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
