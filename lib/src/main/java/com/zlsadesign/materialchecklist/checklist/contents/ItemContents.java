package com.zlsadesign.materialchecklist.checklist.contents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zlsadesign.materialchecklist.R;
import com.zlsadesign.materialchecklist.checklist.Item;

public abstract class ItemContents {

  private Item item = null;
  private View view = null;

  ItemContents() {

  }

  public void setItem(Item item) {
    this.item = item;
  }

  public void setView(View view) {
    this.view = view;
  }

  public ItemContents.View getView() {
    return new View(this);
  }

  public static class View {

    protected Context context;

    protected ItemContents contents;
    protected android.view.View view;

    public View(ItemContents contents) {
      this.contents = contents;

      contents.setView(this);
    }

    public android.view.View getView(Context context, ViewGroup root) {
      this.context = context;

      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      return this.createView(inflater, root);
    }

    public android.view.View createView(LayoutInflater inflater, ViewGroup root) {
      this.view = inflater.inflate(R.layout.item_contents_generic, root, false);

      this.update();

      return this.view;
    }

    public void update() {

    }

  }

}
