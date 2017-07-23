package com.zlsadesign.materialchecklist.showcase;

import com.zlsadesign.materialchecklist.checklist.Item;
import com.zlsadesign.materialchecklist.checklist.controls.ItemControlsButtons;
import com.zlsadesign.materialchecklist.checklist.controls.ItemControlsNext;
import com.zlsadesign.materialchecklist.views.ChecklistActivity;

public class ComplexChecklistActivity extends ChecklistActivity {

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
            .withTitle("Terms of Service")
            .withContents("1. We own all your data.\n2. We own your data even after you've died.\n3. We own your friends' data as well.\n4. Just click Agree like a normal person.")
            .withControls(new ItemControlsButtons().addButton(ItemControlsButtons.BUTTON_DISAGREE).addButton(ItemControlsButtons.BUTTON_AGREE, true))
            .build()
    );

    this.checklist.add(
        new Item.Builder()
            .withTitle("Username")
            .withSubtitle("salsa")
            .withContents("Pretend there's a username field here.")
            .withReversible(true)
            .build()
    );

    this.checklist.add(
        new Item.Builder()
            .withTitle("Email address")
            .withSubtitle("emaily@mcemailface.com")
            .withContents("Likewise, pretend there's an email address field here.")
            .withControls(new ItemControlsNext(ItemControlsButtons.BUTTON_BACK))
            .withReversible(true)
            .build()
    );

    this.checklist.add(
        new Item.Builder()
            .withTitle("Password")
            .withSubtitle("hunter2")
            .withContents("Remember, never show anyone else your password.")
            .withControls(new ItemControlsNext(ItemControlsButtons.BUTTON_BACK))
            .withReversible(true)
            .build()
    );

    this.checklist.add(
        new Item.Builder()
            .withTitle("Does this look right?")
            .withContents("Username: salsa\nEmail address: emaily@mcemailface.com\nPassword: hunter2")
            .withControls(new ItemControlsButtons()
                .addButton(ItemControlsButtons.BUTTON_BACK)
                .addButton(ItemControlsButtons.BUTTON_YES, true))
            .withReversible(false)
            .withSkippable(false)
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
