package com.wjx.homemaker.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjx.homemaker.R;

public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(Context context) {
        super(context, R.style.CustomProgressDialog);
        init();
    }

	/*
	 * public CustomProgressDialog(Context context, int theme) { super(context,
	 * theme); init(); }
	 *
	 * public CustomProgressDialog(Context context, boolean cancelable,
	 * OnCancelListener cancelListener) { super(context, cancelable,
	 * cancelListener); init(); }
	 */

    private void init() {
        this.setContentView(R.layout.dialog_progress);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        this.setCancelable(false);
    }

    // public static CustomProgressDialog createDialog(Context context) {
    // customProgressDialog = new CustomProgressDialog(context,
    // R.style.CustomProgressDialog);
    // customProgressDialog.setContentView(R.layout.dialog_progress);
    // customProgressDialog.getWindow().getAttributes().gravity =
    // Gravity.CENTER;
    // customProgressDialog.setCancelable(false);
    // return customProgressDialog;
    // }

    public void onWindowFocusChanged(boolean hasFocus) {
        // if (customProgressDialog == null) {
        // return;
        // }

        ImageView imageView = (ImageView) this.findViewById(R.id.diolog_progress_iv);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.setOneShot(false);
        animationDrawable.start();
    }

    /**
     * 设置标题
     *
     * @param
     * @return
     */
    public CustomProgressDialog setTitile(String title) {
        TextView textView = (TextView) this.findViewById(R.id.id_tv_loadingmsg);
        textView.setText(title);
        return this;
    }

    /**
     * 设置内容
     */
    public CustomProgressDialog setMessage(String strMessage) {
        TextView tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);

        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }

        return this;
    }
}
