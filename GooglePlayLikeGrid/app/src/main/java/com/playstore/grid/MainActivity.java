package com.playstore.grid;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.playstore.grid.adapter.BucketListAdapter;
import com.playstore.grid.database.DBhelper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {


    DBhelper dBhelper;
    ArrayList<String> gridList = new ArrayList<String>();
    Button cancelButton, createButton;
    EditText gridName;

    //Custom grid
    private ArrayList<String> listitems;
    private CustomAdapter adapter;
    private ListView cardList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_listview);

        dBhelper = new DBhelper(this);
        gridList = dBhelper.getGrid();

        //My function
        cardList = (ListView) findViewById(R.id.card_list);
        listitems = new ArrayList<String>();
        if (gridList.size() == 0) {
        } else {

            for (String s : gridList) {
                listitems.add(s);
            }
            adapter = new CustomAdapter(MainActivity.this, listitems);
            adapter.enableAutoMeasure(150);
            cardList.setAdapter(adapter);
        }
    }


    private void showMessage(String message) {
        Toast.makeText(MainActivity.this, message,
                Toast.LENGTH_SHORT).show();
    }

    public boolean AlbumDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.create_grid_dialog);
        dialog.setTitle("Create Grid");


        cancelButton = (Button) dialog.findViewById(R.id.dialogCancelOK);
        createButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        gridName = (EditText) dialog.findViewById(R.id.folderNameId);

        final Editable name = gridName.getText();
        //
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dBhelper.insertGrid(name.toString());
                gridList = dBhelper.getGrid();

                listitems.add(name.toString());
                adapter = new CustomAdapter(MainActivity.this, listitems);
                adapter.enableAutoMeasure(150);
                if (adapter == null) {
                    Log.d("Adapter", "--" + adapter);
                } else {
                    cardList.setAdapter(adapter);
                }

                dialog.dismiss();

            }
        });

        dialog.show();
        return true;
    }

    //Getting custom grid
    public class CustomAdapter extends BucketListAdapter<String> {

        private Activity mActivity;
        private List<String> items;

        public CustomAdapter(Activity ctx, List<String> elements) {
            super(ctx, elements);
            this.mActivity = ctx;
            this.items = elements;
        }

        @Override
        protected View getBucketElement(final int position, final String currentElement) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            View bucketElement = null;

            LayoutInflater inflater = mActivity.getLayoutInflater();
            bucketElement = inflater.inflate(R.layout.custom_grid, null);
            holder = new ViewHolder(bucketElement);
            bucketElement.setTag(holder);
            holder.name.setText(currentElement);

            holder.options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessage("Edit options for album : " + currentElement);
                }
            });

            bucketElement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessage("You choosed album : " + currentElement);


                }
            });


            return bucketElement;
        }

    }

    class ViewHolder {
        public TextView name = null;
        public CheckBox select = null;
        public ImageView image = null, app_icon = null;
        public LinearLayout rowlayout;
        public ImageButton options = null;

        ViewHolder(View row) {
            name = (TextView) row.findViewById(R.id.projectName);
            image = (ImageView) row.findViewById(R.id.listicon);
            options = (ImageButton) row.findViewById(R.id.overflowButton);
        }

        void populateFrom(String s) {
            name.setText(s);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add_grid) {
            AlbumDialog();
        }

        return super.onOptionsItemSelected(item);
    }


}