package us.zoom.app.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.Serializable;
import java.util.List;

import us.zoom.app.R;
import us.zoom.app.inmeetingfunction.customizedmeetingui.view.share.OptionMenu;

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String OPTION_MENUS = "OPTION_MENUS";
    private List<OptionMenu> optionMenus;
    private View inflate;
    private BottomSheetBehavior bottomSheetBehavior;

    public static CustomBottomSheetDialogFragment getInstance(List<OptionMenu> optionMenus) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(OPTION_MENUS, (Serializable) optionMenus);
        CustomBottomSheetDialogFragment customBottomSheetDialogFragment = new CustomBottomSheetDialogFragment();
        customBottomSheetDialogFragment.setArguments(bundle);
        return customBottomSheetDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.layout_option_menu_container, container, false);
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(OPTION_MENUS)) {
            //noinspection unchecked
            optionMenus = (List<OptionMenu>) arguments.getSerializable(OPTION_MENUS);
        }

        if (optionMenus == null) {
            dismiss();
        }

        ViewGroup viewContainer = inflate.findViewById(R.id.bottom_sheet_option_menus_container);
        if (!optionMenus.isEmpty()) {
            optionMenus.get(optionMenus.size() - 1).hideBottomViewDivider();
        }
        for (OptionMenu optionMenu : optionMenus) {
            viewContainer.addView(optionMenu, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return inflate;
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior = BottomSheetBehavior.from((View) inflate.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
                View bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    bottomSheet.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });
        return dialog;
    }
}
