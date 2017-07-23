package com.zlsadesign.materialchecklist.checklist.controls;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zlsadesign.materialchecklist.R;
import com.zlsadesign.materialchecklist.checklist.Item;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class ItemControlsButtons extends ItemControls {

  public static final int FLAG_STATUS_SKIPPED = 1 << 0;
  public static final int FLAG_STATUS_COMPLETED = 1 << 1;
  public static final int FLAG_STATUS_PERMISSION_DENIED = 1 << 2;
  public static final int FLAG_STATUS_PERMISSION_GRANTED = 1 << 3;

  public static final int FLAG_GO_BACK = 1 << 10;
  public static final int FLAG_GO_FORWARDS = 1 << 11;
  public static final int FLAG_CLOSE_CHECKLIST = 1 << 12;

  public static final int FLAG_SKIP = FLAG_STATUS_SKIPPED | FLAG_GO_FORWARDS;
  public static final int FLAG_NEXT = FLAG_STATUS_COMPLETED | FLAG_GO_FORWARDS;
  public static final int FLAG_BACK = FLAG_GO_BACK;

  public static final int FLAG_FINISH = FLAG_STATUS_COMPLETED | FLAG_CLOSE_CHECKLIST;

  public static final int BUTTON_YES = 10;
  public static final int BUTTON_NO = 11;

  public static final int BUTTON_AGREE = 20;
  public static final int BUTTON_DISAGREE = 21;

  public static final int BUTTON_NEXT = 100;
  public static final int BUTTON_BACK = 110;
  public static final int BUTTON_SKIP = 120;

  public static final int BUTTON_FINISH = 200;


  private List<Button> buttons = new ArrayList<>();

  public ItemControlsButtons() {

  }

  public ItemControlsButtons addButton(Button button) {
    this.buttons.add(button);

    return this;
  }

  public ItemControlsButtons addButton(int button, boolean action) {
    int label = R.string.action_default;
    int flags = 0;

    switch(button) {
      case BUTTON_YES:
        label = R.string.action_yes;
        flags = FLAG_NEXT;
        break;
      case BUTTON_NO:
        label = R.string.action_no;
        flags = FLAG_BACK;
        break;

      case BUTTON_AGREE:
        label = R.string.action_agree;
        flags = FLAG_NEXT;
        break;
      case BUTTON_DISAGREE:
        label = R.string.action_disagree;
        flags = FLAG_CLOSE_CHECKLIST;
        break;

      case BUTTON_NEXT:
        label = R.string.action_next;
        flags = FLAG_NEXT;
        break;
      case BUTTON_BACK:
        label = R.string.action_back;
        flags = FLAG_BACK;
        break;
      case BUTTON_SKIP:
        label = R.string.action_skip;
        flags = FLAG_SKIP;
        break;

      case BUTTON_FINISH:
        label = R.string.action_finish;
        flags = FLAG_FINISH;
        break;
    }

    this.buttons.add(new Button(label, flags, action));

    return this;
  }

  public ItemControlsButtons addButton(int button) {
    return this.addButton(button, false);
  }

  public boolean canGoForwards() {
    return true;
  }

  public boolean canGoBack() {
    return true;
  }

  public boolean canActivate() {
    return true;
  }

  public boolean canDeactivate() {
    return true;
  }

  public ItemControls.View getView() {
    return new View(this);
  }

  public static class View extends ItemControls.View {

    public View(ItemControls controls) {
      super(controls);
    }

    public android.view.View createView(LayoutInflater inflater, ViewGroup root) {
      this.view = inflater.inflate(R.layout.item_controls_buttons, root, false);

      ViewGroup button_row = ButterKnife.findById(this.view, R.id.button_row);

      for(Button button : ((ItemControlsButtons) this.controls).buttons) {
        button_row.addView(button.createView((ItemControlsButtons) this.controls, this.context, inflater, button_row));
      }

      this.update();

      return this.view;
    }

    public void update() {

    }

  }

  private void buttonClicked(Button button) {

    Log.d("ItemControlsButtons", "Flags: " + button.flags);

    if((button.flags & FLAG_STATUS_SKIPPED) != 0) {
      this.item.addStatus(Item.STATUS_SKIPPED);
    } else if((button.flags & FLAG_STATUS_COMPLETED) != 0) {
      this.item.addStatus(Item.STATUS_COMPLETED);
    } else if((button.flags & FLAG_STATUS_PERMISSION_GRANTED) != 0) {
      this.item.addStatus(Item.STATUS_PERMISSION_GRANTED);
    } else if((button.flags & FLAG_STATUS_PERMISSION_DENIED) != 0) {
      this.item.addStatus(Item.STATUS_PERMISSION_DENIED);
    }

    if((button.flags & FLAG_GO_FORWARDS) != 0) {
      this.item.next();
    } else if((button.flags & FLAG_GO_BACK) != 0) {
      this.item.prev();
    } else if((button.flags & FLAG_CLOSE_CHECKLIST) != 0) {
      this.item.getChecklist().close();
    }

  }

  // # The button itself
  public static class Button implements android.view.View.OnClickListener {

    private int id;
    private int flags;

    private String label;
    private int res_label = -1;

    public boolean action = false;
    public boolean disabled = false;

    private android.view.View view = null;
    private ItemControlsButtons controls = null;

    Button(int id, String label, int flags) {
      this.label = label;
      this.id = id;
      this.flags = flags;
    }

    Button(int id, int label, int flags) {
      this.res_label = label;
      this.id = id;
      this.flags = flags;
    }

    Button(int label, int flags) {
      this(label, flags, false);
    }

    Button(int label, int flags, boolean action) {
      this.res_label = label;
      this.flags = flags;
      this.action = action;
    }

    public android.view.View createView(ItemControlsButtons controls, Context context, LayoutInflater inflater, ViewGroup root) {
      this.controls = controls;

      this.view = inflater.inflate(R.layout.item_controls_buttons_button, root, false);

      android.widget.Button button = ButterKnife.findById(this.view, R.id.button);

      if(this.res_label >= 0) {
        button.setText(this.res_label);
      } else {
        button.setText(this.label);
      }

      if(this.action) {
        button.setTextColor(context.getResources().getColor(R.color.item_success));
      }

      button.setOnClickListener(this);

      return this.view;
    }

    @Override
    public void onClick(android.view.View view) {
      this.controls.buttonClicked(this);
    }

  }

}
