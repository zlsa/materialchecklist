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
            .withTitle("Welcome to MaterialChecklist (except it's not compiling properly)!")
            .withContents("It's quite awesome, actually. Unfortunately, you must first click through" +
                " this banal and useless set of screens to demonstrate an understanding of how" +
                " this library functions.")
            .withReversible(false)
            .build()
    );

    this.checklist.add(
        new Item.Builder()
            .withTitle("This is an unimportant step.")
            .withContents("We're so sure of this that we're even letting you skip it if you choose.")
            .withSkippable(true)
            .withReversible(true)
            .build()
    );

    this.checklist.add(
        new Item.Builder()
            .withTitle("Lots of text step.")
            .withContents("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin dapibus porttitor orci, at fringilla arcu mattis id. Maecenas lacinia elementum iaculis. Proin non malesuada nisi, at feugiat mi. Integer rhoncus ligula diam, quis facilisis leo iaculis quis. Nulla facilisi. Maecenas consequat dapibus enim, in dictum ipsum pharetra vitae. Etiam vulputate porttitor viverra. Maecenas enim leo, fermentum a tortor sed, elementum viverra magna. Aliquam pellentesque, purus sed tincidunt volutpat, orci mi eleifend metus, vulputate cursus arcu magna in lorem. Nulla et maximus diam, quis ultrices nisi. In accumsan sem vehicula risus sollicitudin volutpat. Donec in imperdiet urna.")
            .withSkippable(true)
            .withReversible(true)
            .build()
    );

    for(int i=0; i<3; i++) {
      this.checklist.add(
          new Item.Builder()
              .withTitle("Step #" + (i + 4))
              .withContents("Ugh, this is boring.")
              .withSkippable(true)
              .withReversible(true)
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
