package com.zlsadesign.materialchecklist.views;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.zlsadesign.materialchecklist.R;
import com.zlsadesign.materialchecklist.checklist.Checklist;
import com.zlsadesign.materialchecklist.checklist.Item;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChecklistFragment extends Fragment implements Checklist.Listener {

  private Checklist checklist = null;

  private LinearLayout root = null;
  private ScrollView scroll;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle state) {
    View view = inflater.inflate(R.layout.fragment_checklist, root, false);

    this.scroll = ButterKnife.findById(view, R.id.checklist_scroll);
    this.root = ButterKnife.findById(view, R.id.checklist_root);

    this.setRetainInstance(true);

    this.rebuild();

    return view;
  }

  public void setChecklist(Checklist checklist) {
    this.checklist = checklist;

    this.checklist.setListener(this);

    if(this.root != null) {
      this.rebuild();
    }

  }

  public void rebuild() {
    this.root.removeAllViews();

    for(Item item : this.checklist.getItems()) {
      Item.View item_view = new Item.View(item);

      this.root.addView(item_view.createView(this.getActivity(), this.root));
    }

  }

  @Override
  public void onClose() {
    ((ChecklistActivity) this.getActivity()).onClose();
  }

  @Override
  public void onActiveItemChange() {

  }

  @Override
  public void onUpdateScroll() {

    this.scroll.post(new Runnable() {

      @Override
      public void run() {
        int collapsed_height = (int) getActivity().getResources().getDimension(R.dimen.item_height);

        if(checklist.getActiveItemIndex() == 0) {
          scroll.scrollTo(0, 0);
          return;
        }

        View item_view = checklist.getItem(checklist.getActiveItemIndex() - 1).getView().getRoot();

        scroll.smoothScrollTo(0, (int) (item_view.getTop() + collapsed_height * 0.5));
      }

    });

  }

}
