package com.zlsadesign.materialchecklist.checklist;

import android.util.Log;

import com.zlsadesign.materialchecklist.checklist.controls.ItemControlsFinish;

import java.util.ArrayList;
import java.util.List;

public class Checklist {

  private List<Item> items = new ArrayList<>();

  private int active_item_id = -1;
  private Listener listener;

  public void add(Item item) {
    this.items.add(item);

    item.setChecklist(this);

    if(this.active_item_id == -1) {
      item.activate();
    }

  }

  // Applies some special magic to the final item
  public void addFinal(Item item) {
    this.add(item);

    item.setReversible(true);
    item.setSkippable(true);
    item.setControls(new ItemControlsFinish());
  }

  public List<Item> getItems() {
    return this.items;
  }

  public Item getItem(int index) {
    if(index < 0 || index >= this.items.size()) {
      return null;
    }

    return this.items.get(index);
  }

  public int getItemIndex(Item item) {
    return this.items.indexOf(item);
  }

  void _setActive(Item item) {
    this.active_item_id = this.getItemIndex(item);

    item._setActive();
  }

  void _setInactive(Item item) {
    this.active_item_id = -1;

    item._setInactive();
  }

  public int getActiveItemIndex() {
    return this.active_item_id;
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

  public boolean go(int step) {
    return this.goTo(this.active_item_id + step);
  }

  public boolean goTo(Item item) {
    return this.goTo(this.getItemIndex(item));
  }

  public boolean goTo(int item_id) {
    Log.d("Checklist", "Going to item " + item_id);

    if(item_id >= this.items.size() || item_id < 0) {
      return false;
    }

    Item current_item = this.getActiveItem();
    Item next_item = this.getLimitItem(item_id);

    if(next_item == null) {
      return false;
    }

    if(current_item == next_item) {
      return false;
    }

    if(!current_item.canDeactivate() || !next_item.canActivate()) {
      return false;
    }

    boolean status = current_item.deactivate() && next_item.activate();

    for(Item item : this.items) {
      item.updateView();
    }

    return status;
  }

  // Returns the furthest possible item that can be reached in the direction of `target_item_id`.
  private Item getLimitItem(int target_item_id) {

    int step = 0;

    if(target_item_id == this.active_item_id) {
      return this.getActiveItem();
    } else if(target_item_id > this.active_item_id) {
      step = 1;
      // Yes, I know I can just set `step` up there to `-1` by default. But I feel that this is
      // clearer. So tell the efficient programmer in your head to stop talking.
    } else if(target_item_id < this.active_item_id) {
      step = -1;
    }

    Item item;
    // Not actually the next item; it's only the next item on the way to the target item.
    Item next_item;

    for(int i = this.active_item_id; i != target_item_id; i += step) {
      next_item = this.getItem(i + step);
      item = this.getItem(i);

      // Don't go out-of-bounds.
      if(i == this.items.size() || i == -1) {
        return null;
      }

      if(next_item != null && !next_item.canActivate()) {
        Log.d("Checklist", "Can't activate " + next_item.getTitle());

        return null;
      }

      if((step == 1 && !item.canGoNext()) || (step == -1 && !item.canGoPrev())) {
        Log.d("Checklist", "Can't go back to " + next_item.getTitle());

        return null;
      }

    }

    for(int i = this.active_item_id; i != target_item_id; i += step) {
      item = this.getItem(i);
      item.addStatus(Item.STATUS_SKIPPED);
    }

    return this.getItem(target_item_id);

  }

  public boolean next() {
    return this.go(1);
  }

  public boolean prev() {
    return this.go(-1);
  }

  public void close() {
    if(this.listener != null) {
      this.listener.onClose();
    }
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  public interface Listener {
    void onClose();
  }

}
