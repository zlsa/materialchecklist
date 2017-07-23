package com.zlsadesign.materialchecklist.checklist.controls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zlsadesign.materialchecklist.R;
import com.zlsadesign.materialchecklist.checklist.Item;

public abstract class ItemControls {

  protected Item item = null;

  protected View view = null;

  ItemControls() {

  }

  public void setItem(Item item) {
    this.item = item;
  }

  // Note that these functions are only called if the `Item` hasn't already decided whether or not
  // to allow the action. For example, if `Item::skippable` is false, `canGoForwards()` here won't
  // be called (since the `Item` itself isn't capable of going forwards, so it's pointless to check
  // if the `ItemControls` can go forwards since it's not happening anyway.)

  // Children of `ItemControls` are encouraged to override this function.
  public boolean canActivate() {
    return true;
  }

  // This one too.
  public boolean canDeactivate() {
    return true;
  }

  public void setView(View view) {
    this.view = view;
  }

  public ItemControls.View getView() {
    return new View(this);
  }

  public static class View {

    protected Context context;

    protected ItemControls controls;
    protected android.view.View view;

    public View(ItemControls controls) {
      this.controls = controls;

      controls.setView(this);
    }

    public android.view.View getView(Context context, ViewGroup root) {
      this.context = context;

      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      return this.createView(inflater, root);
    }

    public android.view.View createView(LayoutInflater inflater, ViewGroup root) {
      this.view = inflater.inflate(R.layout.item_controls_generic, root, false);

      this.update();

      return this.view;
    }

    public void update() {

    }

  }

}
