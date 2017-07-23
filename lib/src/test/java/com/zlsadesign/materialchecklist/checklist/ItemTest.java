package com.zlsadesign.materialchecklist.checklist;

import org.junit.Test;

import static org.junit.Assert.*;

public class ItemTest {

  @Test
  public void cantSkipUnskippable() throws Exception {
    Item item = new Item();
    item.setSkippable(false);

    assertFalse(item.canGoForwards());
  }

  @Test
  public void canActivate() throws Exception {
    Item item = new Item();

    assertTrue(item.canActivate());

    Checklist checklist = new Checklist();
    checklist.add(item);

    item.setReversible(false);

    // Activate
    assertTrue("First activation", item.activate());
    assertTrue("Should be active", item.isActive());
    assertFalse("Shouldn't be able to activate while active", item.canActivate());
    assertTrue("Should be able to deactivate", item.canDeactivate());
    assertTrue("Deactivate", item.deactivate());

    assertFalse("Shouldn't be able to reactivate a second time", item.activate());

    System.out.println("Setting item to be reversible");
    item.setReversible(false);

    assertTrue("Should be able to reactivate a second time", item.activate());
  }

}