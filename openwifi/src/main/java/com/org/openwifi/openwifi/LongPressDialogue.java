package com.org.openwifi.openwifi;

import android.app.AlertDialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack gurulian.
 */
public class LongPressDialogue extends Dialogue {

    private final int FORGET_NETWORK_POSITION = 0;
    private final int MODIFY_NETWORK_POSITION = 1;
    private Context context;
    private String wifiName;
    private Connector connector;
    private List<ScanResult> results;
    private int pos;
    private AlertDialog alertConnect;

    /**
     * Constructor. Brings in all the necessary information in order to present the correct info on
     * the long press WiFi item dialogue.
     *
     * @param context   the application Context
     * @param wifiName  the name of the target WiFi
     * @param connector the connector for the WiFi
     * @param results   the list of scanned WiFis
     * @param pos       the position of the selected WiFi on the list
     */
    protected LongPressDialogue(Context context, String wifiName, Connector connector,
                                List<ScanResult> results, int pos) {
        super(context);

        this.context = context;
        this.wifiName = wifiName;
        this.connector = connector;
        this.results = results;
        this.pos = pos;
    }

    /**
     * Builds the alert dialogue, without presenting it
     */
    public void build() {

        ArrayAdapter<String> optionAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, buildOptions());

        ListView optionList = new ListView(context);
        optionList.setAdapter(optionAdapter);
        optionList.setBackgroundColor(0xFF000000);

        this.createDialogue(wifiName, null, optionList);

        alertConnect = this.createAlert();

        keyListener(optionList);
    }

    /**
     * Presents the alert dialogue
     */
    public void showAlert() {
        alertConnect.show();
    }

    /**
     * Hides the alert dialogue, without dismissing it
     */
    public void hideAlert() {
        alertConnect.hide();
    }

    /**
     * Dismisses the alert dialogue
     */
    public void dismissAlert() {
        alertConnect.dismiss();
    }

    private ArrayList<String> buildOptions() {
        ArrayList<String> options = new ArrayList<String>();
        options.add(context.getString(R.string.forget));
        options.add(context.getString(R.string.modify));

        return options;
    }

    private void keyListener(ListView optionList) {
        optionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                switch (position) {
                    case MODIFY_NETWORK_POSITION:
                        // ConnectionDialogue conDial = new ConnectionDialogue();
                        dismissAlert();
                        break;
                    case FORGET_NETWORK_POSITION:
                        if (connector.forgetNetwork(wifiName))
                            dismissAlert();
                        else
                            Toast.makeText(context, context.getString(R.string.forget_fail), Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
