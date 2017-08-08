package com.zlsadesign.materialchecklist.checklist.controls;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.zlsadesign.materialchecklist.R;
import com.zlsadesign.materialchecklist.checklist.Item;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class ItemControlsPermissions extends ItemControlsButtons {

  private static final int BUTTON_GRANT_PERMISSIONS = 0;
  private Button skip_button;
  private Button next_button;

  private int request_code;
  private Activity activity;

  List<String> permissions = new ArrayList<>();

  @RequiresApi(api = Build.VERSION_CODES.M)
  public ItemControlsPermissions(Activity activity, String[] permissions_array, int code) {
    super();

    this.activity = activity;
    this.request_code = code;

    for(String permission : permissions_array) {
      if(!this.hasPermission(permission)) {
        this.permissions.add(permission);
      }
    }

  }

  public void build() {

    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      this.addButton(ItemControlsButtons.BUTTON_NEXT);
      return;
    }

    if(this.permissions.size() == 0) {
      this.addButton(ItemControlsButtons.BUTTON_NEXT, true);
      return;
    }

    if(this.item.isSkippable()) {
      this.skip_button = new Button(BUTTON_SKIP, R.string.action_skip, 0);
      this.addButton(this.skip_button);
    }

    if(this.permissions.size() == 1) {
      this.next_button = new Button(BUTTON_GRANT_PERMISSIONS, R.string.action_grant_permission, 0);
    } else {
      this.next_button = new Button(BUTTON_GRANT_PERMISSIONS, R.string.action_grant_permissions, 0);
    }

    this.next_button.action = true;

    this.addButton(this.next_button);
  }

  public void onActivated() {
  }

  public boolean canDeactivate() {
    if(this.item.isSkippable()) {
      return true;
    }

    if(this.hasAllPermissions()) {
      return true;
    }

    return false;
  }

  @Override
  @RequiresApi(api = Build.VERSION_CODES.M)
  protected void buttonClicked(Button button) {

    switch(button.getId()) {

      case BUTTON_SKIP:

        if(this.canDeactivate()) {

          this.item.addStatus(Item.STATUS_PERMISSION_DENIED | Item.STATUS_SKIPPED);

          this.item.next();
        }

        break;

      case BUTTON_GRANT_PERMISSIONS:

        if(this.permissions.size() == 0) {
          this.item.addStatus(Item.STATUS_PERMISSION_GRANTED);
          this.item.next();
          return;
        }

        String[] needed_permissions = this.permissions.toArray(new String[this.permissions.size()]);

        this.activity.requestPermissions(needed_permissions, this.request_code);

        break;

    }

  }

  public boolean hasPermission(String permission) {
    return checkSelfPermission(this.activity, permission) == PackageManager.PERMISSION_GRANTED;
  }

  public boolean hasAllPermissions() {
    for(String permission : this.permissions) {
      if(!this.hasPermission(permission)) {
        return false;
      }
    }

    return true;
  }

  public int getRequestCode() {
    return this.request_code;
  }

  public void onPermissionsDenied(List<String> permissions) {
    int res = R.string.info_permissions_denied;

    if(!this.item.isSkippable()) {
      res = R.string.info_permissions_denied_unskippable;
    }

    Toast.makeText(this.activity, res, Toast.LENGTH_SHORT).show();
  }

  public void onPermissionsAllGranted() {
    this.item.addStatus(Item.STATUS_PERMISSION_GRANTED);
    this.item.next();
  }

  public void onRequestPermissionsResult(String[] permissions, int[] results) {

    List<String> denied_permissions = new ArrayList<>();

    for(String permission : this.permissions) {
      if(!this.hasPermission(permission)) {
        denied_permissions.add(permission);
      }
    }

    if(denied_permissions.size() > 0) {
      this.onPermissionsDenied(denied_permissions);
    } else {
      this.onPermissionsAllGranted();
    }

  }

}
