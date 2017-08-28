package tk.samgrogan.al.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import tk.samgrogan.al.Data.ArticlesModel;
import tk.samgrogan.al.R;

/**
 * Created by ghost on 8/4/2017.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    List<ArticlesModel> articles;
    Context mContext;

    public ArticleAdapter(List<ArticlesModel> articles, Context context){
        this.articles = articles;
        this.mContext = context;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        ArticleViewHolder articleViewHolder = new ArticleViewHolder(view, null);
        return articleViewHolder;
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        holder.title.setText(articles.get(position).getTitle());
        //holder.imageView.setImageResource(R.drawable.test);
        Picasso.with(mContext).load(articles.get(position).getUrlToImage()).resize(520, 520).centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView title;
        ImageView imageView;
        ItemListener listener;

        public ArticleViewHolder(View itemView, @Nullable ItemListener listener) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card);
            title = (TextView)itemView.findViewById(R.id.section_label);
            imageView = (ImageView)itemView.findViewById(R.id.art_img);
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface ItemListener {
        void onItemClick(long id);
    }

    public void updateAnswers(List<ArticlesModel> items) {
        articles = items;
        notifyDataSetChanged();
    }

    private ArticlesModel getItem(int position){
        return articles.get(position);
    }
}
