package com.zlsadesign.materialchecklist.checklist.contents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zlsadesign.materialchecklist.R;
import com.zlsadesign.materialchecklist.checklist.Item;

public abstract class ItemContents {

  private Item item = null;

  ItemContents() {

  }

  public void setItem(Item item) {
    this.item = item;
  }

  public View getView(Context context, ViewGroup root) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    return inflater.inflate(R.layout.item_contents_generic, root);
  }

}
