package com.zlsadesign.materialchecklist.checklist;

import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zlsadesign.materialchecklist.R;
import com.zlsadesign.materialchecklist.checklist.contents.ItemContents;
import com.zlsadesign.materialchecklist.checklist.contents.ItemContentsTextDescription;
import com.zlsadesign.materialchecklist.checklist.controls.ItemControls;
import com.zlsadesign.materialchecklist.checklist.controls.ItemControlsFinish;
import com.zlsadesign.materialchecklist.checklist.controls.ItemControlsNext;
import com.zlsadesign.materialchecklist.views.animation.ResizeAnimation;

import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.UNSPECIFIED;
import static android.view.View.VISIBLE;

public class Item {

  // Pending: user action has not yet occurred.
  public static final int STATUS_PENDING = 1 << 0;
  // Completed: generic "complete" status.
  public static final int STATUS_COMPLETED = 1 << 1;
  // Item was skipped.
  public static final int STATUS_SKIPPED = 1 << 4;
  // Permission(s) granted.
  public static final int STATUS_PERMISSION_GRANTED = 1 << 10;
  // Permission(s) denied.
  public static final int STATUS_PERMISSION_DENIED = 1 << 11;

  // An item which is totally finished and no longer needs any user input.
  // The `0` is present so I can properly align the _real_ statuses.
  public static final int STATUS_DONE = 0
      | STATUS_COMPLETED
      | STATUS_PERMISSION_GRANTED
      | STATUS_PERMISSION_DENIED
      | STATUS_SKIPPED;

  // An item which has been completed successfully.
  public static final int STATUS_DONE_SUCCESS = 0
      | STATUS_COMPLETED
      | STATUS_PERMISSION_GRANTED;

  // An item which has not been completed but is done nonetheless.
  public static final int STATUS_DONE_FAILED = 0
      | STATUS_SKIPPED
      | STATUS_PERMISSION_DENIED;

  // # Variables

  // ## User-facing stuff

  private String title = null;
  private String subtitle = null;
  private ItemContents contents = new ItemContentsTextDescription("");
  private ItemControls controls = new ItemControlsNext();

  private boolean subtitle_visible_when_active = false;

  // ## Flow control

  // Can the user skip past this item?
  private boolean skippable = false;
  // Can the user go back to this item after it's been completed successfully?
  private boolean reversible = false;

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

  public Checklist getChecklist() {
    return this.checklist;
  }

  // # State management. Yay Android lifecycles.

  public Bundle getState() {
    Bundle bundle = new Bundle();

    bundle.putInt("status", this.status);
    bundle.putInt("activation_times", this.activation_times);

    bundle.putString("title", this.title);
    bundle.putString("subtitle", this.subtitle);

    //bundle.putBundle("contents", this.contents.getState());

    return bundle;
  }

  public void applyState(Bundle bundle) {
    this.status = bundle.getInt("status");
    this.activation_times = bundle.getInt("activation_times");

    this.title = bundle.getString("title");
    this.subtitle = bundle.getString("subtitle");

    this.deactivate();
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

  public String getSubtitle() {
    return this.subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;

    this.updateView();
    // TODO: add hooks to notify relevant views of title change
  }

  // ## Content

  public ItemContents getContents() {
    return this.contents;
  }

  public void setContents(ItemContents contents) {
    this.contents = contents;
    this.contents.setItem(this);


    this.updateView();
    // TODO: add hooks to notify relevant views of content changes
  }

  // ## Controls

  public ItemControls getControls() {
    return this.controls;
  }

  public void setControls(ItemControls controls) {
    this.controls = controls;
    this.controls.setItem(this);

    this.updateView();
    // TODO: add hooks to notify relevant views of control changes
  }

  // ## Status

  public int getStatus() {
    return this.status;
  }

  private void _setStatus(int status) {
    this.status = status;

    this.updateView();
    // TODO: notify basically everybody that our status has changed.
  }

  public void addStatus(int status) {
    this._setStatus(this.status | status);
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

  int getIndex() {
    return this.checklist.getItemIndex(this);
  }

  private String getIndexString() {
    return String.valueOf(this.getIndex() + 1);
  }

  private boolean isFinalItem() {
    return this.checklist.getFinalItem() == this;
  }

  public boolean canGoNext() {
    if((this.status & STATUS_DONE_SUCCESS) != 0) {
      return true;
    }

    if(this.skippable) {
      return true;
    }

    return false;
  }

  public boolean canGoPrev() {

    if(!this.isReversible() && ((this.status & STATUS_DONE_SUCCESS) != 0)) {
      return false;
    }

    return true;
  }

  public boolean next() {
    return this.checklist.next();
  }

  public boolean prev() {
    return this.checklist.prev();
  }

  // ## Activation

  // Returns `true` if `activate()` can be successfully called, `false` otherwise.
  public boolean canActivate() {
    // If we're active, return `false`.
    if(this.isActive()) {
      return false;
    }

    //if(this.activation_times >= 1 && !this.isActive()) {
    if(this.activation_times >= 1 && !this.isReversible()) {
      if((this.status & STATUS_DONE_SUCCESS) != 0) {
        return false;
      }
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

    if(this.controls.getClass() == ItemControlsFinish.class) {
      this.addStatus(STATUS_COMPLETED);
    }

    this.checklist._setActive(this);

    // TODO: inform our contents and controls that we're now active.

    return true;
  }

  // Called by the parent `Checklist`. This is a dumb function, and no checking is done here. This
  // function must activate the item no matter how stupid.

  public void _setActive() {
    this.active = true;
    this.activation_times += 1;

    if(this.view != null) {
      this.view.setActive(true);
    }

    this.updateView();
  }

  // See above.

  public void _setInactive() {
    this.active = false;

    if(this.view != null) {
      this.view.setActive(false);
    }

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

    this.checklist._setInactive(this);

    // TODO: inform our contents and controls that we're now inactive.

    return true;
  }

  // # View

  // ## Item view management

  private void setView(View view) {
    this.view = view;
  }

  public View getView() {
    return this.view;
  }

  void updateView() {

    if(this.view != null) {
      this.view.update();
    }

  }

  // ## View class

  public static class View implements android.view.View.OnClickListener {

    private Context context;

    private Item item;

    Transition transition_manager;
    Scene scene_normal;
    Scene scene_expanded;

    private android.view.View view;
    private android.view.View contents_root;
    private android.view.View contents_controls_view;

    private TextView title_view;
    private TextView subtitle_view;
    private TextView number_view;

    public View(Item item) {
      this.item = item;

      item.setView(this);
    }

    public static void expand(final android.view.View v, boolean suppress_animation) {
      int availableWidth = ((android.view.View) v.getParent()).getWidth();
      Log.d("View", "availableWidth is " + availableWidth);

      int widthSpec = android.view.View.MeasureSpec.makeMeasureSpec(availableWidth, android.view.View.MeasureSpec.AT_MOST);
      int heightSpec = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);

      v.measure(widthSpec, heightSpec);
      final int targetHeight = v.getMeasuredHeight();

      // Older versions of android (pre API 21) cancel animations for views with a height of 0.
      v.getLayoutParams().height = 1;
      v.setVisibility(android.view.View.VISIBLE);

      Animation a = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
          v.getLayoutParams().height = interpolatedTime == 1
              ? ViewGroup.LayoutParams.WRAP_CONTENT
              : (int)(targetHeight * interpolatedTime);
          v.requestLayout();
        }

        @Override
        public boolean willChangeBounds() {
          return true;
        }
      };

      a.setInterpolator(new DecelerateInterpolator());

      a.setDuration(200);

      if(suppress_animation) {
        a.setDuration(1);
      }

      v.startAnimation(a);
    }

    public static void collapse(final android.view.View v, boolean suppress_animation) {
      final int initialHeight = v.getMeasuredHeight();

      Animation a = new Animation() {

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
          if(interpolatedTime == 1){
            v.setVisibility(android.view.View.GONE);
          }else{
            v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
            v.requestLayout();
          }
        }

        @Override
        public boolean willChangeBounds() {
          return true;
        }
      };

      a.setInterpolator(new DecelerateInterpolator());

      a.setDuration(200);

      if(suppress_animation) {
        a.setDuration(1);
      }

      v.startAnimation(a);
    }

    public void setActive(boolean active, boolean suppress_animation) {
      if(active) {
        expand(this.contents_controls_view, suppress_animation);
      } else {
        collapse(this.contents_controls_view, suppress_animation);
      }

      if(this.item.isActive()) {
        this.item.checklist.updateScroll();
      }

    }

    public void setActive(boolean active) {
      this.setActive(active, false);
    }

    public android.view.View createView(Context context, ViewGroup root) {
      this.context = context;

      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      this.view = inflater.inflate(R.layout.item, root, false);

      // Add the contents view.
      ((ViewGroup) ButterKnife.findById(this.view, R.id.item_contents))
          .addView(this.item.contents.getView().getView(context, (ViewGroup) this.view));

      // And the controls view.
      ((ViewGroup) ButterKnife.findById(this.view, R.id.item_controls))
          .addView(this.item.controls.getView().getView(context, (ViewGroup) this.view));

      this.contents_root = ButterKnife.findById(this.view, R.id.item_contents_root);
      this.contents_controls_view = ButterKnife.findById(this.view, R.id.item_contents_controls);

      this.title_view = ButterKnife.findById(this.view, R.id.item_title);
      this.subtitle_view = ButterKnife.findById(this.view, R.id.item_subtitle);
      this.number_view = ButterKnife.findById(this.view, R.id.item_number);

      this.update();

      // # Click events
      this.view.setOnClickListener(this);

      if(item.isActive()) {
        setActive(true, true);
      }

      return this.view;
    }

    public android.view.View getRoot() {
      return this.view;
    }

    private void setTitle(String title) {
      this.title_view.setText(title);
    }

    private void setSubtitle(String subtitle) {
      this.subtitle_view.setText(subtitle);
    }

    private void setNumber(String number) {
      this.number_view.setText(number);
    }

    public void update() {

      // # Setting values

      // ## Title
      this.setTitle(this.item.getTitle());

      // ## Subitle
      this.setSubtitle(this.item.getSubtitle());

      // ## Number
      this.setNumber(this.item.getIndexString());

      // Handle the final item (should be a checkbox)

      if(this.item.isFinalItem()) {
        ButterKnife.findById(this.view, R.id.item_number).setVisibility(GONE);
        ButterKnife.findById(this.view, R.id.item_line_below).setVisibility(GONE);
        ButterKnife.findById(this.view, R.id.item_number_final).setVisibility(VISIBLE);
      } else {
        ButterKnife.findById(this.view, R.id.item_number).setVisibility(VISIBLE);
        ButterKnife.findById(this.view, R.id.item_line_below).setVisibility(VISIBLE);
        ButterKnife.findById(this.view, R.id.item_number_final).setVisibility(GONE);
      }

      Resources res = this.context.getResources();

      ButterKnife.findById(this.view, R.id.item_number_scrim).setVisibility(GONE);
      ButterKnife.findById(this.view, R.id.item_line_above).setVisibility(VISIBLE);

      final TypedValue value = new TypedValue();
      this.context.getTheme().resolveAttribute(android.R.attr.colorPrimary, value, true);

      int primary_color = value.data;

      // If this should be green
      //if((this.item.status & STATUS_DONE) != 0) {
      if(this.item.getIndex() <= this.item.checklist.getActiveItemIndex()) {
        ButterKnife.findById(this.view, R.id.item_line_above).setBackgroundColor(primary_color);
        ButterKnife.findById(this.view, R.id.item_line_below).setBackgroundColor(primary_color);
        ButterKnife.findById(this.view, R.id.item_number).setBackgroundResource(R.drawable.item_number_background_success);
        ButterKnife.findById(this.view, R.id.item_number_final).setBackgroundResource(R.drawable.item_number_background_success);
      } else {
        ButterKnife.findById(this.view, R.id.item_line_above).setBackgroundResource(R.color.item_pending);
        ButterKnife.findById(this.view, R.id.item_line_below).setBackgroundResource(R.color.item_pending);
        ButterKnife.findById(this.view, R.id.item_number).setBackgroundResource(R.drawable.item_number_background);
        ButterKnife.findById(this.view, R.id.item_number_final).setBackgroundResource(R.drawable.item_number_background);

        // If the user is on an earlier item but we've already been completed successfully.
        if((this.item.status & STATUS_SKIPPED) != 0) {
          ButterKnife.findById(this.view, R.id.item_number).setBackgroundResource(R.drawable.item_number_background_success_redo);
          ButterKnife.findById(this.view, R.id.item_number_final).setBackgroundResource(R.drawable.item_number_background_success_redo);
          ButterKnife.findById(this.view, R.id.item_number_scrim).setVisibility(VISIBLE);
          ButterKnife.findById(this.view, R.id.item_number_scrim).setBackgroundResource(R.drawable.item_number_scrim_background_pending);
          ButterKnife.findById(this.view, R.id.item_line_above).setVisibility(GONE);
        }

      }

      if(this.item.getIndex() == this.item.checklist.getActiveItemIndex()) {
        ButterKnife.findById(this.view, R.id.item_line_below).setBackgroundResource(R.color.item_pending);
        ButterKnife.findById(this.view, R.id.item_number_scrim).setBackgroundResource(R.drawable.item_number_scrim_background);
        ButterKnife.findById(this.view, R.id.item_number_scrim).setVisibility(VISIBLE);
      }

      if(this.item.isFinalItem() && !this.item.isActive()) {
        ButterKnife.findById(this.view, R.id.item_line_above).setVisibility(VISIBLE);
        ButterKnife.findById(this.view, R.id.item_number_scrim).setVisibility(GONE);
        ButterKnife.findById(this.view, R.id.item_number).setBackgroundResource(R.drawable.item_number_background);
        ButterKnife.findById(this.view, R.id.item_number_final).setBackgroundResource(R.drawable.item_number_background);
      }

      if(this.item.isActive()) {
        this.view.setBackgroundColor(res.getColor(R.color.item_active_background));

        if(true) {
          this.view.setBackgroundResource(R.drawable.item_background_card);
        }

        this.view.setElevation(res.getDimension(R.dimen.item_active_elevation));

        this.title_view.setTextColor(res.getColor(R.color.item_title_active));

        if(this.item.subtitle_visible_when_active) {
          this.subtitle_view.setVisibility(VISIBLE);
        } else {
          this.subtitle_view.setVisibility(INVISIBLE);
        }

      } else {
        this.view.setBackgroundColor(res.getColor(android.R.color.transparent));
        this.view.setElevation(0);

        this.title_view.setTextColor(res.getColor(R.color.item_title_inactive));

        this.subtitle_view.setVisibility(VISIBLE);
      }

    }

    @Override
    public void onClick(android.view.View view) {

      if(view == this.view) {
        this.item.checklist.goTo(this.item);
      }

    }
  }

  // # Builder

  public static class Builder {

    private boolean skippable = false;
    private boolean reversible = false;

    private String title = null;
    private String subtitle = null;
    private ItemContents contents = null;
    private ItemControls controls = null;

    private boolean subtitle_visible_when_active = false;

    public Builder() {

    }

    public Builder withTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder withSubtitle(String subtitle) {
      this.subtitle = subtitle;
      return this;
    }

    public Builder withSubtitleVisibleWhenActive(boolean subtitle_visible_when_active) {
      this.subtitle_visible_when_active = subtitle_visible_when_active;
      return this;
    }

    public Builder withSkippable(boolean skippable) {
      this.skippable = skippable;
      return this;
    }

    public Builder withReversible(boolean reversible) {
      this.reversible = reversible;
      return this;
    }

    public Builder withContents(String description) {
      this.contents = new ItemContentsTextDescription(description);
      return this;
    }

    public Builder withControls(ItemControls controls) {
      this.controls = controls;
      return this;
    }

    public Item build() {
      Item item = new Item();

      item.setSkippable(this.skippable);
      item.setReversible(this.reversible);

      item.setTitle(this.title);
      item.setSubtitle(this.subtitle);

      item.subtitle_visible_when_active = this.subtitle_visible_when_active;

      if(this.contents == null) {
        this.contents = new ItemContentsTextDescription("");
      }

      item.setContents(this.contents);

      if(this.controls == null) {
        this.controls = new ItemControlsNext(this.skippable);
      }

      item.setControls(this.controls);

      return item;
    }

  }

}
