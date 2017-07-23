package com.zlsadesign.materialchecklist.views;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zlsadesign.materialchecklist.R;
import com.zlsadesign.materialchecklist.checklist.Checklist;
import com.zlsadesign.materialchecklist.checklist.Item;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChecklistFragment extends Fragment {

  private Checklist checklist = new Checklist();

  private LinearLayout root = null;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle state) {
    View view = inflater.inflate(R.layout.fragment_checklist, root, false);

    this.root = ButterKnife.findById(view, R.id.checklist_root);

    this.rebuild();

    return view;
  }

  public void setChecklist(Checklist checklist) {
    this.checklist = checklist;

    if(this.root != null) {
      this.rebuild();
    }

  }

  public void rebuild() {
    this.root.removeAllViews();

    for(Item item : this.checklist.getItems()) {
      Log.d("ChecklistFragment", "item " + item.getTitle());

      Item.View item_view = new Item.View(item);

      this.root.addView(item_view.getView(this.getActivity(), this.root));
    }

  }

}
