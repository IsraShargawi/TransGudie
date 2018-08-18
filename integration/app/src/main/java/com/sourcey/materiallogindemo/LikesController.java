package com.sourcey.materiallogindemo;
/*
 * Copyright 2017 Rozdoum
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sourcey.materiallogindemo.NewsModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kristina on 12/30/16.
 */

public class LikesController  {

    private static final int ANIMATION_DURATION = 300;

    public enum AnimationType {
        COLOR_ANIM, BOUNCE_ANIM
    }
    private ImageButton likeBtn ,unlikeBtn;
    private Context context;
    private int postId;
    private String postAuthorId;

    private TextView likeCounterTextView;

    private boolean isListView = false;

    private boolean isLiked = false;
    private boolean updatingLikeCounter = true;
    public LikesController(ImageButton likeBtn,ImageButton unlikeBtn,TextView likeCounterTextView,Context context){
        this.likeBtn = likeBtn;
        this.unlikeBtn = unlikeBtn;
        this.likeCounterTextView = likeCounterTextView;
        this.context = context ;
    }
    public void likeClickAction(int prevValue) {
        if (!isLiked) {
            addLike(prevValue);
            likeBtn.setVisibility(View.GONE);
            unlikeBtn.setVisibility(View.VISIBLE);
        } else {
            likeBtn.setVisibility(View.VISIBLE);
            unlikeBtn.setVisibility(View.GONE);
            removeLike(prevValue);
        }

    }

    public void updateInServer(){
        StringRequest request = new StringRequest(Request.Method.POST, Config.URL+"/addingLikes.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> prm = new HashMap<>();

                //prm.put("posttext",txt.getText().toString());
                return prm;
            }
        };
       // Volley.newRequestQueue(LikesController.this).add(request);

    }


    public void likeClickActionLocal(NewsModel post) {
        setUpdatingLikeCounter(false);
        likeClickAction(post.getLikesCount());
       //updateLocalPostLikeCounter(post);
    }

    private void addLike(int prevValue) {
        updatingLikeCounter = true;
        isLiked = true;
        likeCounterTextView.setText(String.valueOf(prevValue + 1));
        //ApplicationHelper.getDatabaseHelper().createOrUpdateLike(postId, postAuthorId);
    }

    private void removeLike(int prevValue) {
        updatingLikeCounter = true;
        isLiked = false;
        likeCounterTextView.setText(String.valueOf(prevValue - 1));
        //ApplicationHelper.getDatabaseHelper().removeLike(postId, postAuthorId);
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isUpdatingLikeCounter() {
        return updatingLikeCounter;
    }

    public void setUpdatingLikeCounter(boolean updatingLikeCounter) {
        this.updatingLikeCounter = updatingLikeCounter;
    }



    private void updateLocalPostLikeCounter(NewsModel post) {
        if (isLiked) {
            post.setLikesCount(post.getLikesCount() + 1);
        } else {
            post.setLikesCount(post.getLikesCount() - 1);
        }
    }
    /*

    public void handleLikeClickAction(final BaseActivity baseActivity, final Post post) {
        PostManager.getInstance(baseActivity.getApplicationContext()).isPostExistSingleValue(post.getId(), new OnObjectExistListener<Post>() {
            @Override
            public void onDataChanged(boolean exist) {
                if (exist) {
                    if (baseActivity.hasInternetConnection()) {
                        doHandleLikeClickAction(baseActivity, post);
                    } else {
                        showWarningMessage(baseActivity, R.string.internet_connection_failed);
                    }
                } else {
                    showWarningMessage(baseActivity, R.string.message_post_was_removed);
                }
            }
        });
    }

    private void showWarningMessage(BaseActivity baseActivity, int messageId) {
        if (baseActivity instanceof MainActivity) {
            ((MainActivity) baseActivity).showFloatButtonRelatedSnackBar(messageId);
        } else {
            baseActivity.showSnackBar(messageId);
        }
    }

    private void doHandleLikeClickAction(BaseActivity baseActivity, Post post) {
        ProfileStatus profileStatus = ProfileManager.getInstance(baseActivity).checkProfile();

        if (profileStatus.equals(ProfileStatus.PROFILE_CREATED)) {
            if (isListView) {
                likeClickActionLocal(post);
            } else {
                likeClickAction(post.getLikesCount());
            }
        } else {
            baseActivity.doAuthorization(profileStatus);
        }
    }*/

}
