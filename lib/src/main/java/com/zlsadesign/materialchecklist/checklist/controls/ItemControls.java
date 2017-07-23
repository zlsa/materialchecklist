package com.zlsadesign.materialchecklist.checklist.controls;

import com.zlsadesign.materialchecklist.checklist.Item;

public abstract class ItemControls {

  protected Item item = null;

  ItemControls() {

  }

  public void setItem(Item item) {
    this.item = item;
  }

  // Note that these functions are only called if the `Item` hasn't already decided whether or not
  // to allow the action. For example, if `Item::skippable` is false, `canGoForwards()` here won't
  // be called (since the `Item` itself isn't capable of going forwards, so it's pointless to check
  // if the `ItemControls` can go forwards since it's not happening anyway.)

  public boolean canGoForwards() {
    return true;
  }

  public boolean canGoBack() {
    return true;
  }

  // Children of `ItemControls` are encouraged to override this function.
  public boolean canActivate() {
    return true;
  }

  // This one too.
  public boolean canDeactivate() {
    return true;
  }

}
