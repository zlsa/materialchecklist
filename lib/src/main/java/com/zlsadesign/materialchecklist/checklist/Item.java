package com.zlsadesign.materialchecklist.checklist;

import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zlsadesign.materialchecklist.R;
import com.zlsadesign.materialchecklist.checklist.contents.ItemContents;
import com.zlsadesign.materialchecklist.checklist.contents.ItemContentsTextDescription;
import com.zlsadesign.materialchecklist.checklist.controls.ItemControls;
import com.zlsadesign.materialchecklist.checklist.controls.ItemControlsNext;

import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Item {

  // Pending: user action has not yet occurred.
  public final int STATUS_PENDING = 1 << 0;
  // Completed: generic "complete" status.
  public final int STATUS_COMPLETED = 1 << 1;
  // Item was skipped.
  public final int STATUS_SKIPPED = 1 << 4;
  // Permission(s) granted.
  public final int STATUS_PERMISSION_GRANTED = 1 << 10;
  // Permission(s) denied.
  public final int STATUS_PERMISSION_DENIED = 1 << 11;

  // The `0` is present so I can properly align the _real_ statuses.
  public final int STATUS_DONE = 0
      | STATUS_COMPLETED
      | STATUS_PERMISSION_GRANTED
      | STATUS_PERMISSION_DENIED
      | STATUS_SKIPPED;

  // # Variables

  // ## User-facing stuff

  private String title = null;
  private ItemContents contents = new ItemContentsTextDescription("");
  private ItemControls controls = new ItemControlsNext();

  // ## Flow control

  // Can the user skip past this item?
  private boolean skippable = false;
  // Can the user go back to this item after it's been activated?
  private boolean reversible = true;

  // ## Internal state tracking

  private int status = STATUS_PENDING;

  // How many times has this item been activated?
  private int activation_times = 0;
  // Is this item currently active?
  private boolean active = false;

  private Checklist checklist = null;
  private View view = null;

  // # Initialization

  Item() {
  }

  public void setChecklist(Checklist checklist) {
    this.checklist = checklist;
  }

  // # Getters and setters

  // ## Title

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;

    this.updateView();
    // TODO: add hooks to notify relevant views of title change
  }

  // ## Content

  public ItemContents getContents() {
    return this.contents;
  }

  public void setContents(ItemContents contents) {
    this.contents = contents;

    this.updateView();
    // TODO: add hooks to notify relevant views of content changes
  }

  // ## Controls

  public ItemControls getControls() {
    return this.controls;
  }

  public void setControls(ItemControls controls) {
    this.controls = controls;

    this.updateView();
    // TODO: add hooks to notify relevant views of control changes
  }

  // ## Status

  public int getStatus() {
    return this.status;
  }

  public void setStatus(int status) {
    this.status = status;
    // TODO: notify basically everybody that our status has changed.
  }

  // ## Skippable

  public boolean isSkippable() {
    return this.skippable;
  }

  public void setSkippable(boolean skippable) {
    this.skippable = skippable;
  }

  // ## Reversible

  public boolean isReversible() {
    return this.reversible;
  }

  public void setReversible(boolean reversible) {
    this.reversible = reversible;
  }

  // ## Movement

  private int getIndex() {
    return this.checklist.getItemIndex(this);
  }

  private String getIndexString() {
    return String.valueOf(this.getIndex() + 1);
  }

  private boolean isFinalItem() {
    return this.checklist.getFinalItem() == this;
  }

  public boolean canGoForwards() {

    // We're not done yet.
    if((this.getStatus() & STATUS_DONE) >= 0) {

      if(!this.isSkippable()) {
        return false;
      }
    }

    return this.controls.canGoForwards();
  }

  public boolean canGoBack() {
    return this.controls.canGoBack();
  }

  // ## Activation

  // Returns `true` if `activate()` can be successfully called, `false` otherwise.
  public boolean canActivate() {
    // If we're active, return `false`.
    if(this.isActive()) {
      return false;
    }

    if(!this.isReversible() && this.activation_times >= 1 && !this.isActive()) {
      return false;
    }

    return this.controls.canActivate();
  }

  // Returns `true` if `deactivate()` can be successfully called, `false` otherwise.
  public boolean canDeactivate() {
    // If we're inactive, return `false`.
    if(!this.isActive()) {
      return false;
    }

    if(this.controls.canDeactivate()) {
      return true;
    }

    // If we're unskippable, return `false`.
    if(!this.isSkippable()) {
      return false;
    }

    return true;
  }

  public boolean isActive() {
    return this.active;
  }

  // Returns `true` if we are now activated, `false` otherwise. Returns `true` if already active.
  public boolean activate() {
    if(this.isActive()) {
      return true;
    }

    if(!this.canActivate()) {
      return false;
    }

    Item checklist_active_item = this.checklist.getActiveItem();
    if(checklist_active_item != null) {
      if(!checklist_active_item.deactivate()) {
        // Can't activate; the current item won't deactivate.

        return false;
      }
    }

    this.checklist.setActive(this);

    // TODO: inform our contents and controls that we're now active.

    return true;
  }

  // Called by the parent `Checklist`. This is a dumb function, and no checking is done here. This
  // function must activate the item no matter how stupid.

  public void setActive() {
    this.active = true;
    this.activation_times += 1;

    this.updateView();
  }

  // See above.

  public void setInactive() {
    this.active = false;

    this.updateView();
  }

  // Returns `true` if we are now deactivated, `false` otherwise. Returns `true` if we're already
  // inactive. If `false` is returned, we are guaranteed to be active.

  public boolean deactivate() {
    if(!this.isActive()) {
      return true;
    }

    if(!this.canDeactivate()) {
      // TODO: inform our contents and controls that a deactivation attempt was made
      return false;
    }

    this.checklist.setInactive(this);

    // TODO: inform our contents and controls that we're now inactive.

    return true;
  }

  // # View

  // ## Item view management

  private void setView(View view) {
    this.view = view;
  }

  private void updateView() {

    if(this.view != null) {
      this.view.update();
    }

  }
  // ## View class

  public static class View implements android.view.View.OnClickListener {

    private Context context;

    private Item item;
    private android.view.View view;

    public View(Item item) {
      this.item = item;

      item.setView(this);
    }

    public android.view.View getView(Context context, ViewGroup root) {
      this.context = context;

      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      this.view = inflater.inflate(R.layout.item, root, false);

      this.update();

      return this.view;
    }

    public void update() {

      // # Click events
      this.view.setOnClickListener(this);

      // # Setting values

      // ## Title
      TextView title_view = ButterKnife.findById(this.view, R.id.item_title);
      title_view.setText(this.item.getTitle());

      // ## Number
      TextView number_view = ButterKnife.findById(this.view, R.id.item_number);
      number_view.setText(this.item.getIndexString());

      // Handle the final item (should be a checkbox)

      int number_visibility = VISIBLE;
      int final_number_visibility = GONE;

      if(this.item.isFinalItem()) {
        number_visibility = GONE;
        final_number_visibility = VISIBLE;
      }

      ButterKnife.findById(this.view, R.id.item_line).setVisibility(number_visibility);
      ButterKnife.findById(this.view, R.id.item_number).setVisibility(number_visibility);
      ButterKnife.findById(this.view, R.id.item_line_final).setVisibility(final_number_visibility);
      ButterKnife.findById(this.view, R.id.item_number_final).setVisibility(final_number_visibility);

      Resources res = this.context.getResources();

      LinearLayout.LayoutParams params = null;

      if(this.item.isActive()) {
        this.view.setBackgroundColor(res.getColor(R.color.item_active_background));
        this.view.setElevation(res.getDimension(R.dimen.item_active_elevation));

        title_view.setTextColor(res.getColor(R.color.item_title_active));

        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
      } else {
        this.view.setBackgroundColor(res.getColor(android.R.color.transparent));
        this.view.setElevation(0);

        title_view.setTextColor(res.getColor(R.color.item_title_inactive));

        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) res.getDimension(R.dimen.item_height));
      }

      this.view.setLayoutParams(params);

    }

    private void toggleActive() {

      if(this.item.isActive()) {
        this.item.deactivate();
      } else {
        this.item.activate();
      }

    }

    @Override
    public void onClick(android.view.View view) {

      if(view == this.view) {
        this.toggleActive();
      }

    }
  }

  // # Builder

  public static class Builder {

    private String title = null;
    private ItemContents contents = null;
    private ItemControls controls = null;

    public Builder() {
      this.controls = new ItemControlsNext();
    }

    public Builder withTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder withContents(String description) {
      this.contents = new ItemContentsTextDescription(description);
      return this;
    }

    public Item build() {
      Item item = new Item();

      this.contents.setItem(item);
      this.controls.setItem(item);

      item.setTitle(this.title);

      return item;
    }

  }

}
