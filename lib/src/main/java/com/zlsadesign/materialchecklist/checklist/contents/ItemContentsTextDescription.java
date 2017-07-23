package com.zlsadesign.materialchecklist.checklist.contents;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zlsadesign.materialchecklist.R;
import com.zlsadesign.materialchecklist.checklist.Item;

import butterknife.ButterKnife;

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

  public ItemContents.View getView() {
    return new View(this);
  }

  public static class View extends ItemContents.View {

    private TextView description;

    public View(ItemContents contents) {
      super(contents);
    }

    public android.view.View createView(LayoutInflater inflater, ViewGroup root) {
      this.view = inflater.inflate(R.layout.item_contents_text_description, root, false);

      this.description = ButterKnife.findById(this.view, R.id.description);

      this.update();

      return this.view;
    }

    public void update() {
      String description = ((ItemContentsTextDescription) this.contents).getDescription();

      this.description.setText(description);
    }

  }

}
