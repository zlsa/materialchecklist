package com.zlsadesign.materialchecklist.showcase;

import android.os.Bundle;

import com.zlsadesign.materialchecklist.checklist.Item;
import com.zlsadesign.materialchecklist.checklist.controls.ItemControlsFinish;
import com.zlsadesign.materialchecklist.checklist.controls.ItemControlsNext;
import com.zlsadesign.materialchecklist.views.ChecklistActivity;

public class BasicChecklistActivity extends ChecklistActivity {

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

    this.checklist.add(
        new Item.Builder()
            .withTitle("This is an unimportant step.")
            .withContents("We're so sure of this that we're making you click 'next' once again! " +
                "We're even letting you skip it if you choose.")
            .withSkippable(true)
            .withReversible(true)
            .build()
    );

    this.checklist.addFinal(
        new Item.Builder()
            .withTitle("You're all set!")
            .withContents("It's been fun. Don't come back.")
            .build()
    );

  }


}
