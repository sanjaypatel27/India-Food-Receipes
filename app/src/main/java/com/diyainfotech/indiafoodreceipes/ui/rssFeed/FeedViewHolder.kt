package com.diyainfotech.indiafoodreceipes.ui.rssFeed

import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.api.loadAny
import com.diyainfotech.indiafoodreceipes.Constant
import com.diyainfotech.indiafoodreceipes.R
import com.diyainfotech.indiafoodreceipes.rssFeedParser.Article
import com.diyainfotech.indiafoodreceipes.util.CustomWebView
import com.diyainfotech.indiafoodreceipes.util.TimeDiffUtil

class FeedViewHolder(itemView: View, private val feedCardClickListener: FeedCardClickListener) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private val newsCard = itemView.findViewById(R.id.newsCard) as CardView
    private val title = itemView.findViewById(R.id.title) as TextView
    private val description = itemView.findViewById(R.id.description) as TextView
    private val source = itemView.findViewById(R.id.source) as TextView
    private val publishDate = itemView.findViewById(R.id.publishDate) as TextView
    private val articleImage = itemView.findViewById(R.id.articleImage) as ImageView
    private val sourceFavicon = itemView.findViewById(R.id.sourceFavicon) as ImageView
    private val customWebView =
        itemView.findViewById(R.id.customWebView) as CustomWebView
    private lateinit var article: Article


    fun bindData(article: Article) {
        source.setOnClickListener(this)
        newsCard.setOnClickListener(this)
        this.article = article
        title.text = article.title
        if (article.description != null) {
            description.text = (HtmlCompat.fromHtml(article.description!!, 0))
        }
        Log.d("Sanjay", "Link : \n ${article.link}")
        source.text = article.link
        if(article.pubDate != null) {
            publishDate.text = TimeDiffUtil.getLastUpdateTime(
                article.pubDate!!,
                Constant.newsLastUpdatedDateTimeZoneFormat
            )
        }

        if (!TextUtils.isEmpty(article.link)) {
            sourceFavicon.loadAny(Constant.getFaviconFromSiteURL + article.link)
            if (article.link!!.contains("youtube")) {
                articleImage.visibility = View.GONE
                customWebView.visibility = View.VISIBLE
                WebViewUtil.webViewUrl = article.link!!
                WebViewUtil.setUpWebView(customWebView,null)
                var videoId = article.link!!.split("v=")[1]
                WebViewUtil.loadDataWebView(WebViewUtil.getStringForEmbedWebView(videoId),customWebView)
            } else {
                articleImage.visibility = View.VISIBLE
                customWebView.visibility = View.GONE
                if (!TextUtils.isEmpty(article.image)) {
                    articleImage.load(article.image)
                }
            }
        }

    }


    override fun onClick(v: View?) {
        when {
            v!!.id == R.id.source -> {
                feedCardClickListener.onFeedCardSourceLinkClick(article)
            }
            v.id == R.id.newsCard -> {
                feedCardClickListener.onFeedCardClick(article)
            }
        }
    }

}