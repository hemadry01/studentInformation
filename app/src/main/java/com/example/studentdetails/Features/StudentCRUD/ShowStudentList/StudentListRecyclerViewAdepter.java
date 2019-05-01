package com.example.studentdetails.Features.StudentCRUD.ShowStudentList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.studentdetails.Database.DatabaseQueryClass;
import com.example.studentdetails.Features.StudentCRUD.CreateStudent.Student;
import com.example.studentdetails.Features.StudentCRUD.UpdateStudentInfo.StudentUpdateDialogFragment;
import com.example.studentdetails.Features.StudentCRUD.UpdateStudentInfo.StudentUpdateListene;
import com.example.studentdetails.Features.SubjectCRUD.CreateSubject.Subject;
import com.example.studentdetails.Features.SubjectCRUD.ShowSubjectList.SubjectListActivity;
import com.example.studentdetails.R;
import com.example.studentdetails.Util.Config;

import java.util.List;

public class StudentListRecyclerViewAdepter extends RecyclerView.Adapter<CustomViewHolder> {

    private Context context;
    private List<Student> studentList;
    private DatabaseQueryClass databaseQueryClass;

    public StudentListRecyclerViewAdepter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
        databaseQueryClass = new DatabaseQueryClass(context);
        //Logger.addLogAdapter(new AndroidLogAdapter());
    }


//    public StudentListRecyclerViewAdepter(Context context, List<Subject> subjectList) {
//        this.context = context;
//        this.subjectList = subjectList;
//        databaseQueryClass = new DatabaseQueryClass(context);
//    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final int itemPosition = position;
        final Student student = studentList.get(position);

        holder.nameTextView.setText(student.getName());
        holder.registrationNumTextView.setText(String.valueOf(student.getRegistrationNumber()));
        holder.emailTextView.setText(student.getEmail());
        holder.phoneTextView.setText(student.getPhoneNumber());


        holder.crossButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure, You wanted to delete this student?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteStudent(itemPosition);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        holder.editButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudentUpdateDialogFragment studentUpdateDialogFragment = (StudentUpdateDialogFragment) StudentUpdateDialogFragment.newInstance(student.getRegistrationNumber(), itemPosition, new StudentUpdateListene()
                {
                    @Override
                    public void onStudentInfoUpdated(Student student, int position) {
                        studentList.set(position, student);
                        notifyDataSetChanged();
                    }
                });
                studentUpdateDialogFragment.show(((StudentListActivity) context).getSupportFragmentManager(), Config.UPDATE_STUDENT);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SubjectListActivity.class);
                intent.putExtra(Config.STUDENT_REGISTRATION, student.getRegistrationNumber());
                context.startActivity(intent);
            }
        });
    }

    private void deleteStudent(int position) {
        Student student = studentList.get(position);
        long count = databaseQueryClass.deleteStudentByRegNum(student.getRegistrationNumber());

        if(count>0){
            studentList.remove(position);
            notifyDataSetChanged();
            ((StudentListActivity) context).viewVisibility();
            Toast.makeText(context, "Student deleted successfully", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(context, "Student not deleted. Something wrong!", Toast.LENGTH_LONG).show();

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}
