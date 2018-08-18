package com.sourcey.materiallogindemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private static final String TAG = "NewsAdapter";

    private List<NewsModel> list;
    Context context;
    //private final OnItemClickListener listener;
    // public final View.OnClickListener onClickListener;

    public NewsAdapter(List<NewsModel> newsList, PostActivity context) {
        this.list = newsList;
        this.context = context;
    }
   /* public NewsAdapter(List<NewsModel> newsList,View.OnClickListener onClickListener) {
        this.list = newsList;
        this.onClickListener = onClickListener;
    }*/

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //(MyViewHolder)holder.likebtn.setOnClickListener;
        // holder.bind(list.get(position), onClickListener);
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc, date, likes;
        public ImageButton likebtn, unlikebtn;
        public CircleImageView proImg;
        public Bitmap myBitMap ;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            title = (TextView) view.findViewById(R.id.txtName);
            date = (TextView) view.findViewById(R.id.txtDate);
            desc = (TextView) view.findViewById(R.id.txtDesc);
            likes = (TextView) view.findViewById(R.id.likenum);
            likebtn = (ImageButton) view.findViewById(R.id.like);
            unlikebtn = (ImageButton) view.findViewById(R.id.unlike);
            proImg = (CircleImageView) view.findViewById(R.id.profile_image);
        }

        public void bind(final NewsModel item) {
            title.setText(item.getAuthor());
            date.setText(item.getDate());
            desc.setText(item.getTitle());
            likes.setText(Integer.toString(item.getLikesCount()));
            unlikebtn.setVisibility(View.GONE);
            try {
                myBitMap = new myTask().execute(item.getImgsrc()).get();
                proImg.setImageBitmap(myBitMap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            final LikesController likecontroller = new LikesController(likebtn,unlikebtn,likes,context);
            likebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likecontroller.likeClickAction(Integer.parseInt(likes.getText().toString()));
                }
            });
            unlikebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likecontroller.likeClickAction(Integer.parseInt(likes.getText().toString()));
                }
            });

        }


    }
    private class myTask extends AsyncTask<String,String,Bitmap> {
        protected Bitmap doInBackground(String... params) {
            String s = params[0];
            try {
                URL newurl = new URL(Config.URL+"/uploads/"+s);
                HttpURLConnection connection = (HttpURLConnection)newurl.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap mybitmap = BitmapFactory.decodeStream(input);
                return mybitmap;
            } catch (Exception e) {
                e.getStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            //do stuff

        }
    }
}