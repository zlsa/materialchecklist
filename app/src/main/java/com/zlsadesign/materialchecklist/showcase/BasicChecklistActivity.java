package com.zlsadesign.materialchecklist.showcase;

import android.os.Bundle;

import com.zlsadesign.materialchecklist.checklist.Item;
import com.zlsadesign.materialchecklist.views.ChecklistActivity;

public class BasicChecklistActivity extends ChecklistActivity {

  protected void createChecklist() {

    this.checklist.add(
        new Item.Builder()
            .withTitle("MaterialChecklist is awesome.")
            .withContents("Yes, it is. There's no use disputing this. In fact, it's so awesome" +
                "that you can't even skip this item.")
            .build()
    );

    for(int i=2; i<25; i++) {
      this.checklist.add(
          new Item.Builder()
              .withTitle("MaterialChecklist is awesome, take " + i + ".")
              .withContents("We're so sure of this that we're making you click 'next' once again!.")
              .build()
      );
    }

    this.checklist.add(
        new Item.Builder()
            .withTitle("You're all set!")
            .withContents("Congratulations!")
            .build()
    );

  }


}
