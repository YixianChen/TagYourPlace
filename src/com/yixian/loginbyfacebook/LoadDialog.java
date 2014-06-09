package com.yixian.loginbyfacebook;

 
import android.app.ProgressDialog;  
import android.content.Context;  
 
  
/** 
 * 封装ProecssDialog对话框 
 *  
 * @author Administrator 
 *  
 */  
public class LoadDialog extends ProgressDialog {  
    private String title = "Process Bar";  
    private String message = "data uploading...。";  
  
    public LoadDialog(Context context, int theme) {  
        super(context, theme);  
    }  
  
    /** 
     * 用默认的标题和内容来创建对话框 
     * 
     *  
     * @param context 
     */  
    public LoadDialog(Context context) {  
        super(context);  
        initDialog();  
    }  
  
    /** 
     * 用指定的标题和内容来创建对话框 
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
     * 初始化对话框参数，默认对话框不可以取消 
     */  
    public void initDialog() {  
        setTitle(title);  
        setMessage(message);  
        setProgressStyle(ProgressDialog.STYLE_SPINNER);  
        setCancelable(false);  
    }  
  
}