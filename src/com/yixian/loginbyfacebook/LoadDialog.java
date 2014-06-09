package com.yixian.loginbyfacebook;

 
import android.app.ProgressDialog;  
import android.content.Context;  
 
  
/** 
 * ��װProecssDialog�Ի��� 
 *  
 * @author Administrator 
 *  
 */  
public class LoadDialog extends ProgressDialog {  
    private String title = "Process Bar";  
    private String message = "data uploading...��";  
  
    public LoadDialog(Context context, int theme) {  
        super(context, theme);  
    }  
  
    /** 
     * ��Ĭ�ϵı���������������Ի��� 
     * 
     *  
     * @param context 
     */  
    public LoadDialog(Context context) {  
        super(context);  
        initDialog();  
    }  
  
    /** 
     * ��ָ���ı���������������Ի��� 
     *  
     * @param context 
     * @param title 
     * @param message 
     */  
    public LoadDialog(Context context, String title, String message) {  
        super(context);  
        if (title != null) {  
            this.title = title;  
        }  
        if (message != null) {  
            this.message = message;  
        }  
        initDialog();  
    }  
  
    /** 
     * ��ʼ���Ի��������Ĭ�϶Ի��򲻿���ȡ�� 
     */  
    public void initDialog() {  
        setTitle(title);  
        setMessage(message);  
        setProgressStyle(ProgressDialog.STYLE_SPINNER);  
        setCancelable(false);  
    }  
  
}