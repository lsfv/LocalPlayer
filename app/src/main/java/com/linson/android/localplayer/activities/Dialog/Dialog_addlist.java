package com.linson.android.localplayer.activities.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.linson.android.localplayer.R;

public class Dialog_addlist extends Dialog
{
    private Button mBtnSubmit;
    private TextView mTextView;
    private EditText mEtName;
    private Button mBtnCancel;
    private Idialogcallback mIdialogcallback;


    //region  findcontrols and bind click event.
    private void findControls()
    {   //findControls
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
        mTextView = (TextView) findViewById(R.id.textView);
        mEtName = (EditText) findViewById(R.id.et_name);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);

        mBtnSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name=mEtName.getText().toString().trim();
                mIdialogcallback.submit(name);
                Dialog_addlist.this.dismiss();
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Dialog_addlist.this.dismiss();
            }
        });
    }
    //endregion

    //region other member variable
    //endregion

    public Dialog_addlist(Context context,Idialogcallback callback)
    {
        super(context);
        mIdialogcallback=callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_singleinput);
        findControls();
    }


    public  interface Idialogcallback
    {
        void submit(String name);
    }
}
