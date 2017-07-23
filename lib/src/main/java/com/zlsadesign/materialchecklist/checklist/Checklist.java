package com.zlsadesign.materialchecklist.checklist;

import java.util.ArrayList;
import java.util.List;

public class Checklist {

  private List<Item> items = new ArrayList<>();

  private int active_item_id = -1;

  public void add(Item item) {
    this.items.add(item);

    item.setChecklist(this);
  }

  public List<Item> getItems() {
    return this.items;
  }

  public Item getItem(int index) {
    return this.items.get(index);
  }

  public int getItemIndex(Item item) {
    return this.items.indexOf(item);
  }

  void setActive(Item item) {
    this.active_item_id = this.getItemIndex(item);

    item.setActive();
  }

  void setInactive(Item item) {
    this.active_item_id = -1;

    item.setInactive();
  }

  public Item getActiveItem() {
    if(this.active_item_id < 0) {
      return null;
    }

    return this.items.get(this.active_item_id);
  }

  public Item getFinalItem() {
    return this.items.get(this.items.size() - 1);
  }

}
