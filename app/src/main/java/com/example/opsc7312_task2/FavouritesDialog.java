package com.example.opsc7312_task2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class FavouritesDialog extends AppCompatDialogFragment {
    private EditText editFav, editFav_Desc;
    private FavouriteDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.save_landmark, null);
        builder.setView(view)
                .setTitle("New Favourite")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String fav = editFav.getText().toString();
                        String fav_desc = editFav_Desc.getText().toString();
                        listener.applyTexts(fav,fav_desc);
                    }
                });
        editFav = view.findViewById(R.id.save_name_input);
        editFav_Desc = view.findViewById(R.id.save_desc_input);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (FavouriteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface FavouriteDialogListener {
        void applyTexts(String favourite,String desc);

    }
}
