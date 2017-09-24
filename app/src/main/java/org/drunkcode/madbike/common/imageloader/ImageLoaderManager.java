package org.drunkcode.madbike.common.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ImageLoaderManager implements ImageLoaderBuilder{

    private Context context;
    private ImageLoaderListener listener;

    public ImageLoaderManager(Context context, ImageLoaderListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void loadImage(String path, ImageView imageView) {
        if (path == null || path.equalsIgnoreCase("") || imageView == null) {
            //TODO:ERROR LOADING IMAGE
        }else{
            Picasso.with(context).load(path).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    listener.onImageLoaded();
                }

                @Override
                public void onError() {
                    listener.onImageError();
                }
            });
        }
    }
}
