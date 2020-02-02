package com.binaryBeasts.consumerapp.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.binaryBeasts.consumerapp.Models.Products;
import com.binaryBeasts.consumerapp.R;

public class EditOrderDialog extends AppCompatDialogFragment {
    EditText quantity,price,deliveryPrice;
    String Quantity,Price,DeliveryPrice;
    CheckBox isAvail;
    DialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_product_fragment_dialog, null);
        init(view);
        setData();
        setListeners();
        builder.setView(view)
                .setTitle("Bargain")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Quantity=quantity.getText().toString().trim();
                Price=price.getText().toString().trim();
                DeliveryPrice=deliveryPrice.getText().toString().trim();
                if(!isAvail.isChecked())
                {
                    DeliveryPrice="N/A";
                }
                listener.applyEdits(Quantity,Price,DeliveryPrice);
            }
        });

        return builder.create();
    }

    private void setData() {
        Products products=listener.getDetails();
        price.setText(products.getPrice());
        quantity.setText(products.getQuality());
        deliveryPrice.setText(products.getDelivery());
        if(products.getDelivery().trim().equalsIgnoreCase("N/A")){
            isAvail.setVisibility(View.GONE);
            deliveryPrice.setVisibility(View.GONE);
            return;
        }
        deliveryPrice.setVisibility(View.VISIBLE);
    }

    private void setListeners() {
        isAvail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    deliveryPrice.setVisibility(View.VISIBLE);
                }else {
                    deliveryPrice.setVisibility(View.GONE);
                }
            }
        });
    }

    private void init(View view) {
        price=view.findViewById(R.id.productRequestRate);
        deliveryPrice=view.findViewById(R.id.productRequestDeliveryCharge);
        quantity=view.findViewById(R.id.productRequestQuantity);
        isAvail=view.findViewById(R.id.availDelivery);
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
        void applyEdits(String quantity, String price, String deliverPrice);
        Products getDetails();
    }
}
