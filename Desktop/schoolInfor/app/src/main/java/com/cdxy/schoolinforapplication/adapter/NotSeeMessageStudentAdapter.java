package com.cdxy.schoolinforapplication.adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.model.MessageEntity;
import com.cdxy.schoolinforapplication.model.NotSeeMessageStudentEntity;
import com.cdxy.schoolinforapplication.model.SeeMeaaseStudentEntity;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huihui on 2016/12/27.
 */

public class NotSeeMessageStudentAdapter extends BaseAdapter {
    private Context context;
    private List<NotSeeMessageStudentEntity> list;
    private MessageEntity messageEntity;

    public NotSeeMessageStudentAdapter(Context context, List<NotSeeMessageStudentEntity> list, MessageEntity messageEntity) {
        this.context = context;
        this.list = list;
        this.messageEntity=messageEntity;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_not_see_message_students, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final NotSeeMessageStudentEntity entity = (NotSeeMessageStudentEntity) getItem(i);
        String name = entity.getName();
        if (!TextUtils.isEmpty(name))
            viewHolder.txtNotSeeMessageStudentName.setText(name);
        String id = entity.getId();
        if (!TextUtils.isEmpty(id))
            viewHolder.txtNotSeeMessageStudentId.setText(id);
        String department = entity.getDepartment();
        if (!TextUtils.isEmpty(department))
            viewHolder.txtNotSeeMessageStudentDepartment.setText(department);
        String clazz = entity.getClazz();
        if (!TextUtils.isEmpty(clazz))
            viewHolder.txtNotSeeMessageStudentClazz.setText(clazz);
        final String phoneNumber = entity.getPhoneNumber();
        if (!TextUtils.isEmpty(phoneNumber))
            viewHolder.txtNoSeeMessageStudentPhoneNumber.setText(phoneNumber);
        viewHolder.txtNoSeeMessageStudentPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_choose_inform_not_see_message_way, null);
                TextView txtCall = (TextView) dialogView.findViewById(R.id.txt_call);
                TextView txtSendSMS = (TextView) dialogView.findViewById(R.id.txt_send_SMS);
                final AlertDialog dialog = new AlertDialog.Builder(context).setView(dialogView).create();
                dialog.show();
                txtCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.

                            return;
                        }
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                });
                txtSendSMS.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+phoneNumber));
                        String sender=messageEntity.getSender();
                        String content=messageEntity.getContent();
                        if ((!TextUtils.isEmpty(sender))&&(!TextUtils.isEmpty(content)))
                        intent.putExtra("sms_body",content+"    --"+sender);
                       context.startActivity(intent);
                        dialog.dismiss();
                    }
                });
            }
        });
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.txt_not_see_message_student_id)
        TextView txtNotSeeMessageStudentId;
        @BindView(R.id.txt_not_see_message_student_name)
        TextView txtNotSeeMessageStudentName;
        @BindView(R.id.txt_not_see_message_student_department)
        TextView txtNotSeeMessageStudentDepartment;
        @BindView(R.id.txt_not_see_message_student_clazz)
        TextView txtNotSeeMessageStudentClazz;
        @BindView(R.id.txt_no_see_message_student_phoneNumber)
        TextView txtNoSeeMessageStudentPhoneNumber;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
