package net.ralphpina.wwd;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.ralphpina.wwd.utils.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar      mToolbar;
    private PeopleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                                                                  DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new PeopleAdapter();
        mRecyclerView.setAdapter(mAdapter);

        new DownloadFilesTask().execute();

    }

    private class DownloadFilesTask extends AsyncTask<Void, Integer, JSONArray> {
        protected JSONArray doInBackground(Void... urls) {
            try {
                return WWDApplication.get()
                                     .getClient()
                                     .fetchEngageResults();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        /*    setProgressPercent(progress[0]);*/
        }

        protected void onPostExecute(JSONArray data) {
            mAdapter.setData(data);
        }
    }

    private class PeopleAdapter extends RecyclerView.Adapter<PeopleViewHolder> {

        private JSONArray mData;

        public void setData(JSONArray data) {
            mData = data;
            notifyDataSetChanged();
        }

        @Override
        public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                                   .inflate(R.layout.list_item_people, parent, false);
            return new PeopleViewHolder(v);
        }

        @Override
        public void onBindViewHolder(PeopleViewHolder holder, int position) {
            JSONObject object = null;
            try {
                object = mData.getJSONObject(position).getJSONObject("$properties");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.configure(object);
        }

        @Override
        public int getItemCount() {
            if (mData == null) {
                return 0;
            } else {
                return mData.length();
            }
        }
    }

    public class PeopleViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.profile_image)
        ImageView mProfileImage;
        @Bind(R.id.person_name)
        TextView  mPersonName;
        @Bind(R.id.city)
        TextView  mCity;
        @Bind(R.id.telephone)
        TextView  mTelephone;
        @Bind(R.id.email)
        TextView  mEmail;

        public PeopleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void configure(JSONObject object) {
            try {
                mPersonName.setText(object.getString("$name"));
                mCity.setText(object.getString("$city"));
                mTelephone.setText(object.getString("phone"));
                mEmail.setText(object.getString("$email"));
                Picasso.with(MainActivity.this).load(object.getString("photo_url")).into(mProfileImage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
