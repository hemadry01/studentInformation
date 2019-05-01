package com.example.studentdetails.Features.SubjectCRUD.UpdateSubjectInfo;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.studentdetails.Database.DatabaseQueryClass;
import com.example.studentdetails.Features.SubjectCRUD.CreateSubject.Subject;
import com.example.studentdetails.Features.SubjectCRUD.CreateSubject.SubjectCreateListener;
import com.example.studentdetails.R;
import com.example.studentdetails.Util.Config;
import com.example.studentdetails.Features.SubjectCRUD.UpdateSubjectInfo.SubjectUpdateDialogFragmen;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectUpdateDialogFragmen extends DialogFragment {


    private EditText subjectNameEditText;
    private EditText subjectCodeEditText;
    private EditText subjectCreditEditText;
    private Button updateButton;
    private Button cancelButton;

    private static SubjectCreateListener subjectUpdateListener;
    private static long subjectId;
    private static int position;

    private DatabaseQueryClass databaseQueryClass;

    public SubjectUpdateDialogFragmen() {
        // Required empty public constructor
    }


    public static SubjectUpdateDialogFragmen newInstance(long subId, int pos, SubjectUpdateListene listener){
//        subjectId = subId;
//        position = pos;
//        subjectUpdateListener = listener;
        subjectId = subId;
        position = pos;
        subjectUpdateListener= (SubjectCreateListener) listener;

        SubjectUpdateDialogFragmen subjectUpdateDialogFragment = new SubjectUpdateDialogFragmen();
        Bundle args = new Bundle();
        args.putString("title", "Update subject information");
        subjectUpdateDialogFragment.setArguments(args);

        subjectUpdateDialogFragment.setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return subjectUpdateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subject_update_dialog, container, false);

        subjectNameEditText = view.findViewById(R.id.subjectNameEditText);
        subjectCodeEditText = view.findViewById(R.id.subjectCodeEditText);
        subjectCreditEditText = view.findViewById(R.id.subjectCreditEditText);
        updateButton = view.findViewById(R.id.updateButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        databaseQueryClass = new DatabaseQueryClass(getContext());

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        Subject subject = databaseQueryClass.getSubjectById(subjectId);

        subjectNameEditText.setText(subject.getName());
        subjectCodeEditText.setText(String.valueOf(subject.getCode()));
        subjectCreditEditText.setText(String.valueOf(subject.getCredit()));

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subjectName = subjectNameEditText.getText().toString();
                int subjectCode = Integer.parseInt(subjectCodeEditText.getText().toString());
                double subjectCredit = Double.parseDouble(subjectCreditEditText.getText().toString());

                Subject subject = new Subject(subjectId, subjectName, subjectCode, subjectCredit);

                long rowCount = databaseQueryClass.updateSubjectInfo(subject);

//                if(rowCount>0){
//                    subjectUpdateListener.onSubjectInfoUpdate(subject, position);
////                    subjectUpdateListener.on
//                    getDialog().dismiss();
//                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
        }
    }

}
