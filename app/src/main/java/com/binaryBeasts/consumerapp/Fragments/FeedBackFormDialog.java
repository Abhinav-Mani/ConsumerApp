package com.binaryBeasts.consumerapp.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.binaryBeasts.consumerapp.Models.Products;
import com.binaryBeasts.consumerapp.R;

public class FeedBackFormDialog extends AppCompatDialogFragment {
    EditText FarmerReview,DeliverBoyReview;
    DialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.feedbacklayout, null);
        init(view);
        builder.setView(view)
                .setTitle("FeedBack")
                .setNegativeButton("Pass!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.pass();
                    }
                }).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String farmerReview=FarmerReview.getText().toString().trim();
                String deliveryReview=DeliverBoyReview.getText().toString().trim();
                if(TextUtils.isEmpty(farmerReview)||TextUtils.isEmpty(deliveryReview))
                {
                    Toast.makeText(getContext(),"Cannot Be empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.submit(farmerReview,deliveryReview);
            }
        });

        return builder.create();
    }




    private void init(View view) {
        FarmerReview=view.findViewById(R.id.reviewFarmer);
        DeliverBoyReview=view.findViewById(R.id.reviewDeliveyPerson);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("ak47", "onAttach: ");
        try {
            listener=(DialogListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }

    }

    public interface DialogListener {
        void submit(String Farmer,String Delivery);
        void pass();
    }
}
