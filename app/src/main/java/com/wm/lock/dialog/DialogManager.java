package com.wm.lock.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.wm.lock.R;
import com.wm.lock.core.callback.Injector;

import java.util.List;

/**
 * Created by WM on 2015/8/6.
 */
public final class DialogManager {

    public static Dialog showWaittingDialog(Context ctx, int titleResId, String msg, boolean isCancelable) {
        return showWaittingDialog(ctx, titleResId, msg, isCancelable, false);
    }

    public static Dialog showWaittingDialog(Context ctx, int titleResId, String msg, boolean isCancelable, boolean isCancelableOutside) {
        MaterialDialog.Builder builder = getBuilder(ctx, ctx.getString(titleResId), isCancelable);
        builder.canceledOnTouchOutside(isCancelableOutside);
        builder.content(msg).progress(true, 0).progressIndeterminateStyle(false);
        return showAndReturn(builder);
    }

    public static Dialog showNotifyDialog(Context ctx, int titleResId, String msg, boolean isCancelable) {
        return showNotifyDialog(ctx, titleResId, msg, isCancelable, null);
    }

    public static Dialog showNotifyDialog(Context ctx, int titleResId, String msg, boolean isCancelable, final Injector injector) {
        MaterialDialog.Builder builder = getBuilder(ctx, ctx.getString(titleResId), isCancelable);
        builder.content(msg).positiveText(R.string.concern).callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                performInjector(injector);
                super.onPositive(dialog);
            }
        });
        return showAndReturn(builder);
    }

    public static Dialog showConfirmDialog(Context ctx, int titleResId, String msg, boolean isCancelable,
                                  Injector positiveInjector) {
        return showConfirmDialog(ctx, titleResId, msg, isCancelable, R.string.cancel, null, R.string.concern, positiveInjector);
    }

    public static Dialog showConfirmDialog(Context ctx, int titleResId, String msg, boolean isCancelable,
                                   Injector negativeInjector, Injector positiveInjector) {
        return showConfirmDialog(ctx, titleResId, msg, isCancelable, R.string.cancel, negativeInjector, R.string.concern, positiveInjector);
    }


    public static Dialog showConfirmDialog(Context ctx, int titleResId, String msg, boolean isCancelable,
                                  int negativeResId, final Injector negativeInjector,
                                  int positiveResId, final Injector positiveInjector) {
        return showConfirmDialog(ctx, ctx.getString(titleResId), msg, isCancelable,
                negativeResId, negativeInjector, positiveResId, positiveInjector);
    }

    public static Dialog showConfirmDialog(Context ctx, String title, String msg, boolean isCancelable,
                                   int negativeResId, final Injector negativeInjector,
                                   int positiveResId, final Injector positiveInjector) {
        return showConfirmDialog(ctx, title, msg, isCancelable,
                ctx.getString(negativeResId), negativeInjector,
                ctx.getString(positiveResId), positiveInjector);
    }

    public static Dialog showConfirmDialog(Context ctx, String title, String msg, boolean isCancelable,
                                   String negativeTxt, final Injector negativeInjector,
                                   String postiveTxt, final Injector positiveInjector) {
        MaterialDialog.Builder builder = getBuilder(ctx, title, isCancelable);
        builder.content(msg).negativeText(negativeTxt).positiveText(postiveTxt).callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                performInjector(positiveInjector);
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                performInjector(negativeInjector);
            }
        });
        return showAndReturn(builder);
    }

    public static Dialog showSingleChoiceDialog(Context ctx, int titleResId, CharSequence[] items,
                                         final MaterialDialog.ListCallbackSingleChoice injector, boolean isCancelable) {
        return showSingleChoiceDialog(ctx, titleResId, items, -1, injector, isCancelable);
    }

    public static Dialog showSingleChoiceDialog(Context ctx, int titleResId, CharSequence[] items, int selectPos,
                                       final MaterialDialog.ListCallbackSingleChoice injector, boolean isCancelable) {
        return showSingleChoiceDialog(ctx, ctx.getString(titleResId), items, selectPos, injector, null, isCancelable);
    }

    public static Dialog showSingleChoiceDialog(Context ctx, String title, CharSequence[] items,
                                         final MaterialDialog.ListCallbackSingleChoice injector, boolean isCancelable) {
        return showSingleChoiceDialog(ctx, title, items, -1, injector, null, isCancelable);
    }

    public static Dialog showSingleChoiceDialog(Context ctx, String title, CharSequence[] items,
                                        final MaterialDialog.ListCallbackSingleChoice injector, Injector positiveInjector, boolean isCancelable) {
        return showSingleChoiceDialog(ctx, title, items, -1, injector, positiveInjector, isCancelable);
    }

    public static Dialog showSingleChoiceDialog(Context ctx, String title, CharSequence[] items, int selectPos,
                                       final MaterialDialog.ListCallbackSingleChoice injector, final Injector positiveInjector, boolean isCancelable) {
        MaterialDialog.Builder builder = getBuilder(ctx, title, isCancelable);
        builder.items(items).itemsCallbackSingleChoice(selectPos, new MaterialDialog.ListCallbackSingleChoice() {
            @Override
            public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                if (i < 0) {
                    materialDialog.cancel();
                } else {
                    materialDialog.dismiss();
                    if (injector != null) {
                        injector.onSelection(materialDialog, view, i, charSequence);
                    }
                }
                return false;
            }
        }).positiveText(R.string.cancel).callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                performInjector(positiveInjector);
            }
        }).alwaysCallSingleChoiceCallback();
        return showAndReturn(builder);
    }

    public static Dialog showMultiChoiceDialog(Context ctx, int titleResId,  CharSequence[] items, Integer[] selectPos,
                                      MaterialDialog.ListCallbackMultiChoice injector, boolean isCancelable) {
        MaterialDialog.Builder builder = getBuilder(ctx, ctx.getString(titleResId), isCancelable);
        builder.items(items).itemsCallbackMultiChoice(selectPos, injector).positiveText(R.string.concern);
        return showAndReturn(builder);
    }

    public static void dismissDialog(Dialog d) {
        if (d != null && d.isShowing()) {
            d.dismiss();
        }
    }

    public static MaterialDialog.Builder getBuilder(Context ctx, String title, boolean isCancelable) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(ctx);
        if (!TextUtils.isEmpty(title)) {
            builder.title(title);
        }
        builder.theme(Theme.LIGHT).cancelable(isCancelable);
        return builder;
    }

    public static VerticalPopMenu showBottomVerticalPop(Context ctx, View view, List<VerticalPopMenu.VerticalPopMenuItem> itemList) {
        VerticalPopMenu popMenu = new VerticalPopMenu(ctx);
        popMenu.setData(itemList);
        popMenu.showBottom(view);
        return popMenu;
    }

    private static void performInjector(Injector Injector) {
        if (Injector != null) {
            Injector.execute();
        }
    }

    private static Dialog showAndReturn(MaterialDialog.Builder builder) {
        final Dialog dialog = builder.build();
        dialog.show();
        return dialog;
    }

}
