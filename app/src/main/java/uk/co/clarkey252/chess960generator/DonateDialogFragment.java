package uk.co.clarkey252.chess960generator;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import static android.content.Context.CLIPBOARD_SERVICE;

public class DonateDialogFragment extends DialogFragment{

    String mBitcoinAddress;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.donate_layout)
                .setTitle(R.string.donate)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNeutralButton(R.string.copy_address, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClipboardManager clipBoard = (ClipboardManager) getActivity().getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Bitcoin Address", mBitcoinAddress);
                        clipBoard.setPrimaryClip(clip);
                        Toast.makeText(getActivity().getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                    }
                });
        mBitcoinAddress = getResources().getString(R.string.bitcoin_address);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        d.findViewById(R.id.bitcoin_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW);
                mIntent.setData(Uri.parse("bitcoin:" + mBitcoinAddress));
                try {
                    startActivity(mIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.no_bitcoin_handler), Toast.LENGTH_SHORT).show();
                    mIntent.setData(Uri.parse("http://play.google.com/store/search?q=Bitcoin&c=apps"));
                    startActivity(mIntent);
                }
            }
        });
        d.findViewById(R.id.paypal_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW);
                mIntent.setData(Uri.parse(getResources().getString(R.string.paypal_address)));
                startActivity(mIntent);
            }
        });
        d.findViewById(R.id.github_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW);
                mIntent.setData(Uri.parse(getResources().getString(R.string.github_address)));
                startActivity(mIntent);
            }
        });
    }
}
