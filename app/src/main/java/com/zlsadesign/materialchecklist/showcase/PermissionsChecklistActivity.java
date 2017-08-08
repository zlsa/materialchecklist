package com.zlsadesign.materialchecklist.showcase;

import android.Manifest;
import android.os.Build;

import com.zlsadesign.materialchecklist.checklist.Item;
import com.zlsadesign.materialchecklist.checklist.controls.ItemControlsButtons;
import com.zlsadesign.materialchecklist.checklist.controls.ItemControlsNext;
import com.zlsadesign.materialchecklist.checklist.controls.ItemControlsPermissions;
import com.zlsadesign.materialchecklist.views.ChecklistActivity;

public class PermissionsChecklistActivity extends ChecklistActivity {

  protected void createChecklist() {

    this.checklist.add(
        new Item.Builder()
            .withTitle("Welcome to MaterialChecklist!")
            .withContents("It's quite awesome, actually. Unfortunately, you must first click through" +
                " this banal and useless set of screens to demonstrate an understanding of how" +
                " this library functions.")
            .withReversible(false)
            .build()
    );

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      this.checklist.add(
          new Item.Builder()
              .withTitle("Storage Access")
              .withContents("Don't worry; this showcase app doesn't do anything at all with this permission.")
              .withControls(new ItemControlsPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 0))
              .build()
      );

      this.checklist.add(
          new Item.Builder()
              .withTitle("Contacts and camera access")
              .withContents("Don't worry; this showcase app doesn't do anything at all with this permission. You can skip this one if you'd like.")
              .withControls(new ItemControlsPermissions(this, new String[] { Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA }, 1))
              .withSkippable(true)
              .build()
      );
    } else {
      this.checklist.add(
          new Item.Builder()
              .withTitle("No runtime permissions")
              .withContents("You're running Lollipop or older, so you don't need to allow permissions" +
                  " at runtime. Note that MaterialChecklist does not automatically hide a permissions" +
                  " check item if you're using Lollipop or older; you'll need to do that manually.")
              .build()
      );
    }

    this.checklist.addFinal(
        new Item.Builder()
            .withTitle("You're all set!")
            .withContents("It's been fun. Don't come back.")
            .build()
    );

  }


}
