package jp.tomorrowkey.android.ribbontransformation.sample.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.tomorrowkey.android.ribbontransformation.sample.R;
import jp.tomorrowkey.android.ribbontransformation.sample.databinding.ActivityMainBinding;
import jp.tomorrowkey.android.ribbontransformation.sample.databinding.ListItemBinding;
import jp.tomorrowkey.android.ribbontransformation.sample.model.ThumbnailImage;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    MainActivity self = this;

    ActivityMainBinding binding;

    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        adapter = new MyAdapter(this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Observable
                .create(new Observable.OnSubscribe<ThumbnailImage>() {
                    @Override
                    public void call(Subscriber<? super ThumbnailImage> subscriber) {
                        for (ThumbnailImage image : ThumbnailImage.load(self)) {
                            image.prepareBitmap(self);
                            subscriber.onNext(image);
                        }
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ThumbnailImage>() {
                    @Override
                    public void onNext(ThumbnailImage thumbnailImage) {
                        adapter.add(thumbnailImage);
                    }

                    @Override
                    public void onCompleted() {
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    private static class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

        Context context;

        LayoutInflater layoutInflater;

        List<ThumbnailImage> list = new ArrayList<>();

        public MyAdapter(Context context) {
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

        public void add(ThumbnailImage image) {
            list.add(image);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ListItemBinding binding = DataBindingUtil.bind(holder.itemView);
            ThumbnailImage image = list.get(position);
            Picasso.with(context)
                    .load(image.getBitmapFile())
                    .transform(image.getTransformation())
                    .into(binding.imageView);
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
