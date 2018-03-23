package com.oldmen.segodnyanewstest3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.comix.overwatch.HiveProgressView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ArticleActivity extends AppCompatActivity {

    private HiveProgressView progressView;
    private ScrollView articleScrollView;

    TextView articleTitle;
    TextView articleDate;
    private TextView articleSubtitle;
    private WebView articleImage;
    TextView articleSource;
    private WebView articleWebView;
    private LinearLayout sourceContainer;

    private Context context;

    private ItemInfo item;
    private Toolbar articleActivityToolbar;
    private TextView segodnyaUa;

    ImageButton btnArrowBack;
    private ImageButton btnShareArticle;
    String htmlSaveState;
    String subTitleSaveState;

    private String imageFullSizeUrl;
    private String videoTitleRes = null;
    private Typeface helvetica_font;
    private Typeface futura_font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_article);
        overridePendingTransition(0, 0);
        context = this;

        articleActivityToolbar = (Toolbar) findViewById(R.id.article_activity_toolbar);
        btnArrowBack = (ImageButton) findViewById(R.id.btn_back_arrow_article);
        btnShareArticle = (ImageButton) findViewById(R.id.btn_share_article);
        helvetica_font = Typeface.createFromAsset(getAssets(), "fonts/helvetica.ttc");
        futura_font = Typeface.createFromAsset(getAssets(), "fonts/Futura.ttc");

        progressView = (HiveProgressView) findViewById(R.id.progress_view_article);
        articleScrollView = (ScrollView) findViewById(R.id.scrollView_article);

        articleTitle = (TextView) findViewById(R.id.article_title);
        articleDate = (TextView) findViewById(R.id.article_date);
        articleImage = (WebView) findViewById(R.id.article_image);
        articleSubtitle = (TextView) findViewById(R.id.article_subtitle);
        articleSource = (TextView) findViewById(R.id.article_source);
        articleWebView = (WebView) findViewById(R.id.webView_article);
        sourceContainer = (LinearLayout) findViewById(R.id.source_article_view);

        articleTitle.setTypeface(futura_font, Typeface.BOLD);
        articleDate.setTypeface(futura_font);
        articleSubtitle.setTypeface(helvetica_font);
        articleSource.setTypeface(futura_font);

        progressView.setVisibility(View.VISIBLE);
        articleScrollView.setVisibility(View.INVISIBLE);
        articleActivityToolbar.setVisibility(View.INVISIBLE);

        btnArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnShareArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                YoYo.with(Techniques.Landing)
                        .duration(500)
                        .playOn(btnShareArticle);

                String articleUrl = item.getArticleUrl();
                Intent shareUrlIntent = new Intent(Intent.ACTION_SEND);
                shareUrlIntent.setType("text/plain");
                shareUrlIntent.putExtra(Intent.EXTRA_TEXT, articleUrl);
                startActivity(shareUrlIntent);
            }
        });

        if (articleActivityToolbar != null) {
            setSupportActionBar(articleActivityToolbar);
            getSupportActionBar().setTitle(null);
            articleActivityToolbar.getBackground().setAlpha(180);
            articleActivityToolbar.setVisibility(View.GONE);
        }

        articleImage.setBackgroundColor(Color.TRANSPARENT);
        articleImage.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        articleImage.setScrollbarFadingEnabled(false);
        articleImage.getSettings().setJavaScriptEnabled(true);
        articleImage.getSettings().setDomStorageEnabled(true);
        articleImage.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        articleImage.getSettings().setSupportZoom(false);
        articleImage.getSettings().setLoadWithOverviewMode(true);

        articleWebView.setBackgroundColor(Color.TRANSPARENT);
        articleWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        articleWebView.setScrollbarFadingEnabled(false);
        articleWebView.getSettings().setJavaScriptEnabled(true);
        articleWebView.getSettings().setDomStorageEnabled(true);
        articleWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        articleWebView.getSettings().setSupportZoom(false);
        articleWebView.getSettings().setLoadWithOverviewMode(true);

        articleWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                sourceContainer.setVisibility(View.VISIBLE);
            }
        });

        Parcelable parsItem = getIntent().getParcelableExtra("item");
        item = (ItemInfo) parsItem;
        imageFullSizeUrl = item.getImageUrl().replace("main", "main_new");

        articleTitle.setText(item.getTitle());
        articleDate.setText(item.getDate());

        if (isConnected(context)) {
            new ParsingArticle().execute();
        } else {
            createAlertNetworkDialog();

        }

        String htmlTaggedString = "<u>segodnya.ua</u>";
        Spanned textSpan = android.text.Html.fromHtml(htmlTaggedString);
        articleSource.setText(textSpan);

        articleSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String articleUrl = item.getArticleUrl();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl));
                startActivity(browserIntent);
            }
        });

        segodnyaUa = (TextView) findViewById(R.id.segodnya_ua);
        segodnyaUa.setTypeface(futura_font, Typeface.BOLD);
    }

    private void setArticleData(String subtitle, String html) {
        articleSubtitle.setText(subtitle);
        htmlSaveState = html;
        subTitleSaveState = subtitle;
        String minHeight;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            minHeight = String.valueOf(getResources().getInteger(R.integer.min_height_landscape));
        else
            minHeight = String.valueOf(getResources().getInteger(R.integer.min_height_portrait));

        String pish = "<html><head>" +
                "<style type=\"text/css\">" +
                "@font-face {font-family: helvetica; src: url(\\\"file:///android_asset/font/helvetica.ttc\\\"); font-size: medium}" +
                "h1 {font-family: serif; font-size: medium}" +
                "h3 {font-family: serif; font-size: medium}" +
                "body {font-family: helvetica;font-size: medium;text-left: justify;}" +
                "img {min-width: 100%; width: 100%; max-width: 100%; min-height: 200px;}" +
                "table {min-width: 100%; width: 100%; max-width: 100%; min-height: 200px;}" +
                "iframe {display: block; min-width: 100%; width: 100%; max-width: 100%; min-height: " + minHeight + "px; " +
                "margin-top: 10px; margin-left: 0px; margin-right: 0px; padding: 0px; frameborder: 0;}" +
                "</style></head><body>";
        String pas = "</body></html>";

        if (videoTitleRes != null) {
            String video = pish + videoTitleRes + pas;
            articleImage.loadDataWithBaseURL(item.getArticleUrl(), video, "text/html", "UTF-8", "about:blank");

        } else {
            String image = pish + "<img src=\"" + imageFullSizeUrl + "\">" + pas;
            articleImage.loadData(image, "text/html", "UTF-8");
        }

        html = pish + html + pas;
        articleWebView.loadDataWithBaseURL(item.getArticleUrl(), html, "text/html", "UTF-8", "about:blank");

        progressView.setVisibility(View.GONE);
        articleScrollView.setVisibility(View.VISIBLE);
        articleActivityToolbar.setVisibility(View.VISIBLE);
    }

    private class ParsingArticle extends AsyncTask<String, Long, String> {

        private Elements elements;
        private Elements elementsChildren = new Elements();
        private String htmlElementsCode;
        private String subtitle;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setArticleData(subtitle, htmlElementsCode);
        }

        @Override
        protected String doInBackground(String... strings) {
            Document document;

            try {
                document = Jsoup.connect(item.getArticleUrl()).get();
                elements = document.select("div.article-content");
                if (elements == null || elements.size() < 1) {
                    elements = document.select("div.article");
                }
                for (Element e : elements) {
                    for (Element eChild : e.children()) {
                        elementsChildren.add(eChild);
                    }
                }

                if (elementsChildren.hasClass("player-wrapper")) {
                    htmlElementsCode = getSpan(elementsChildren, elements);
                    String str = elements.select(".player-wrapper").first().outerHtml();
                    str = str.replaceAll("width", "");
                    str = str.replaceAll("height", "");
                    videoTitleRes = str;
                } else {
                    htmlElementsCode = getSpan(elementsChildren, elements);
                }

                if (htmlElementsCode.contains("arial"))
                    htmlElementsCode = htmlElementsCode.replace("arial", "serif");

                if (htmlElementsCode.contains("null"))
                    htmlElementsCode = htmlElementsCode.replace("null", "");

                htmlElementsCode = htmlElementsCode.replace("height", "");
                htmlElementsCode = htmlElementsCode.replace("width", "");
                htmlElementsCode = htmlElementsCode.trim();

                Element elementST = document.select(".sub-title").first();
                if (elementST != null) {
                    subtitle = elementST.text();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private boolean isConnected(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public void createAlertNetworkDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("Internet connection not available!");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
                if (isConnected(context)) {
                    new ParsingArticle().execute();
                } else {
                    Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(settingsIntent);
                    createAlertNetworkDialog();
                }
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
    }

    private String getSpan(Elements elemChildren, Elements elements) {
        String htmlElements = null;
        if (elemChildren.hasClass("_ga1_on_")) {

            for (Element el : elements.select("span._ga1_on_")) {
                if (htmlElements == null)
                    htmlElements = el.outerHtml();
                else if (el.outerHtml().length() > htmlElements.length())
                    htmlElements = el.outerHtml();
            }

            return htmlElements;

        } else {
            Elements elements1 = elements.select("span");

            for (Element elem : elements1) {
                if (htmlElements == null)
                    htmlElements = elem.outerHtml();
                else {
                    if (elem.outerHtml().length() > htmlElements.length())
                        htmlElements = elem.outerHtml();
                }
            }
            return htmlElements;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        articleWebView.onPause();
        articleImage.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        articleWebView.onResume();
        articleImage.onResume();
        YoYo.with(Techniques.SlideInDown)
                .playOn(articleActivityToolbar);
    }

}