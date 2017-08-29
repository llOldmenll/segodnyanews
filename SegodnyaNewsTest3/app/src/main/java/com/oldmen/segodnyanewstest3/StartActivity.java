package com.oldmen.segodnyanewstest3;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.comix.overwatch.HiveProgressView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.oldmen.segodnyanewstest3.drawer_menu.AdapterNavigation;
import com.squareup.picasso.Picasso;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class StartActivity extends AppCompatActivity implements OnCardClickListener, OnMenuDrawerItemClickListener, DragListener {

    private HiveProgressView progressView;
    private NestedScrollView nestedScrollView;
    private Toolbar startActivityToolbar;

    private ItemInfo itemZero;
    private ItemInfo itemOne;
    private ItemInfo itemTwo;

    static final String HEADLINE_URL = "http://www.segodnya.ua/allnews/p1.html";
    final String POLITIC_URL = "http://www.segodnya.ua/politics/p1.html";
    final String ECONOMIC_URL = "http://www.segodnya.ua/economics/p1.html";
    final String SPORT_URL = "http://www.segodnya.ua/sport/p1.html";
    final String HOT_TOPICS_URL = "http://www.segodnya.ua/hot/p1.html";
    final String UKRAINE_URL = "http://www.segodnya.ua/ukraine/p1.html";
    final String WORLD_URL = "http://www.segodnya.ua/world/p1.html";
    final String KYIV_URL = "http://kiev.segodnya.ua/p1.html";
    final String CULTURE_URL = "http://www.segodnya.ua/culture/p1.html";
    final String LIFE_URL = "http://www.segodnya.ua/life/p1.html";
    final String REGIONES_URL = "http://www.segodnya.ua/regions/p1.html";
    final String CRIME_URL = "http://www.segodnya.ua/criminal/p1.html";
    public static String currentUrl = HEADLINE_URL;
    public static String currentCategory = "Последние новости";
    private int currentPage = 1;
    Elements elements;

    private ArrayList<ItemInfo> itemList = new ArrayList<>();

    private StartRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    List<String> categoryArray = new ArrayList<>();
    RecyclerView recyclerDrawerView;
    private AdapterNavigation adapterDrawer;

    private TextView mainTitle;

    private TextView bigCategoryType;
    private TextView bigTitle;
    private TextView bigDate;
    private ImageView bigImage;

    private TextView middleLeftCategoryType;
    private TextView middleLeftTitle;
    private TextView middleLeftDate;
    private ImageView middleLeftImage;

    private TextView middleRightCategoryType;
    private TextView middleRightTitle;
    private TextView middleRightDate;
    private ImageView middleRightImage;

    Button moreNewsButton;
    ConstraintLayout constrButton;

    Button lessNewsButton;
    ConstraintLayout constrLessButton;

    CardView bigCard;
    CardView middleLeftCard;
    CardView middleRightCard;

    private SlidingRootNav rootNav;

    private ImageButton btnMenuArrow;
    private TextView segodnyaUa;
    private Context context;

    private boolean isItFirstAppStart = true;
    private Realm realm = Realm.getDefaultInstance();
    RealmResults<CategoryIcon> categoryIcons;
    public static ArrayList<CategoryState> currentCategoryStateList = new ArrayList<>();
    private RecyclerCategoriesState categoriesState = new RecyclerCategoriesState();

    public static final String CATEGORIES_STATE = "current Category State List";
    public static final String IS_IT_FIRST_START = "is It First App Start";
    public static final String LAST_CLICKED_POSITION = "lastClickedPosition";
    public static final String LAST_ICON = "lastIcon";
    public static final String CURRENT_URL = "Current URL";
    public static final String CURRENT_CATEGORY = "Current category";
    public static final String CURRENT_PAGE = "Current page";
    public static final String BTN_MENU_ARROW_STATE = "Btn menu arrow state";

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(CATEGORIES_STATE, currentCategoryStateList);
        outState.putBoolean(IS_IT_FIRST_START, isItFirstAppStart);
        outState.putInt(LAST_CLICKED_POSITION, adapterDrawer.getLastClickedPosition());
        outState.putParcelable(LAST_ICON, adapterDrawer.getLastIcon());
        outState.putString(CURRENT_URL, currentUrl);
        outState.putString(CURRENT_CATEGORY, currentCategory);
        outState.putInt(CURRENT_PAGE, currentPage);
        outState.putString(BTN_MENU_ARROW_STATE, btnMenuArrow.getTag().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        overridePendingTransition(0, 0);

        if (savedInstanceState != null) {
            currentCategoryStateList = savedInstanceState.getParcelableArrayList(CATEGORIES_STATE);
            isItFirstAppStart = savedInstanceState.getBoolean(IS_IT_FIRST_START);
            categoriesState.setPosition(savedInstanceState.getInt(LAST_CLICKED_POSITION));
            CategoryIcon categoryIcon = savedInstanceState.getParcelable(LAST_ICON);
            categoriesState.setCategoryIcon(categoryIcon);
            currentCategory = savedInstanceState.getString(CURRENT_CATEGORY);
            currentUrl = savedInstanceState.getString(CURRENT_URL);
            currentPage = savedInstanceState.getInt(CURRENT_PAGE);
        }

        context = this;
        createIconCatalog();

        btnMenuArrow = (ImageButton) findViewById(R.id.btn_menu_or_arrow);
        segodnyaUa = (TextView) findViewById(R.id.segodnya_ua);

        startActivityToolbar = (Toolbar) findViewById(R.id.start_activity_toolbar);
        if (startActivityToolbar != null) {
            setSupportActionBar(startActivityToolbar);
            getSupportActionBar().setTitle(null);
            startActivityToolbar.getBackground().setAlpha(0);
        }

        rootNav = new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.drawer_menu)
                .addDragListener(this)
                .inject();

        if (savedInstanceState != null) {
            if (savedInstanceState.getString(BTN_MENU_ARROW_STATE).equals("Arrow"))
                changeBtnIcon("Menu");
        }


        progressView = (HiveProgressView) findViewById(R.id.progress_view);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        bigCard = (CardView) findViewById(R.id.big_card_view);
        middleLeftCard = (CardView) findViewById(R.id.middle_card_view_left);
        middleRightCard = (CardView) findViewById(R.id.middle_card_view_right);

        moreNewsButton = (Button) findViewById(R.id.more_headline_button);
        constrButton = (ConstraintLayout) findViewById(R.id.constraint_button);

        lessNewsButton = (Button) findViewById(R.id.less_headline_button);
        constrLessButton = (ConstraintLayout) findViewById(R.id.constraint_button_less);

        mainTitle = (TextView) findViewById(R.id.main_title);

        bigCategoryType = (TextView) findViewById(R.id.big_category_type);
        bigTitle = (TextView) findViewById(R.id.big_title);
        bigDate = (TextView) findViewById(R.id.big_date);
        bigImage = (ImageView) findViewById(R.id.big_image);

        middleLeftCategoryType = (TextView) findViewById(R.id.middle_category_type_left);
        middleLeftTitle = (TextView) findViewById(R.id.middle_title_left);
        middleLeftDate = (TextView) findViewById(R.id.middle_left_date);
        middleLeftImage = (ImageView) findViewById(R.id.middle_image_left);

        middleRightCategoryType = (TextView) findViewById(R.id.middle_category_type_right);
        middleRightTitle = (TextView) findViewById(R.id.middle_title_right);
        middleRightDate = (TextView) findViewById(R.id.middle_right_date);
        middleRightImage = (ImageView) findViewById(R.id.middle_image_right);


        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY != nestedScrollView.getTop()) {
                    startActivityToolbar.getBackground().setAlpha(180);
                    segodnyaUa.setVisibility(View.VISIBLE);
                } else {
                    startActivityToolbar.getBackground().setAlpha(0);
                    segodnyaUa.setVisibility(View.GONE);
                }
            }
        });

        btnMenuArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnImgTag = btnMenuArrow.getTag().toString();

                if (btnImgTag.equals("Menu"))
                    rootNav.openMenu();

                if (btnImgTag.equals("Arrow"))
                    rootNav.closeMenu();
            }
        });

        moreNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String prevP = "p" + String.valueOf(currentPage);
                currentPage = currentPage + 1;
                String currentP = "p" + String.valueOf(currentPage);
                currentUrl = currentUrl.replace(prevP, currentP);
                String nextPage = currentUrl;

                progressView.setVisibility(View.VISIBLE);
                nestedScrollView.setVisibility(View.GONE);
                btnMenuArrow.setVisibility(View.GONE);

                if (isConnected(StartActivity.this)) {
                    new TakeNews(nextPage, currentCategory).execute();
                } else
                    createAlertNetworkDialog();

            }
        });

        lessNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentPage > 1) {
                    String prevP = "p" + String.valueOf(currentPage);
                    currentPage = currentPage - 1;
                    String currentP = "p" + String.valueOf(currentPage);
                    currentUrl = currentUrl.replace(prevP, currentP);
                    String nextPage = currentUrl;

                    progressView.setVisibility(View.VISIBLE);
                    nestedScrollView.setVisibility(View.GONE);
                    btnMenuArrow.setVisibility(View.GONE);

                    if (isConnected(StartActivity.this)) {
                        new TakeNews(nextPage, currentCategory).execute();
                    } else
                        createAlertNetworkDialog();
                }

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_start_activity);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        }

        rootNav.setMenuLocked(true);

        if (isConnected(this)) {
            new TakeNews(currentUrl, currentCategory).execute();
        } else
            createAlertNetworkDialog();

        adapter = new StartRecyclerAdapter(itemList, this, this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        Resources res = getResources();
        String[] categoriesArray = res.getStringArray(R.array.news_categories_array);
        categoryArray = Arrays.asList(categoriesArray);
        recyclerDrawerView = (RecyclerView) findViewById(R.id.recycler_navigation);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        recyclerDrawerView.setLayoutManager(linearLayoutManager1);
        adapterDrawer = new AdapterNavigation(categoryArray, this, isItFirstAppStart, categoriesState);
        recyclerDrawerView.setAdapter(adapterDrawer);


        bigCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemZero != null) {
                    Intent intent = new Intent(StartActivity.this, ArticleActivity.class);
                    intent.putExtra("item", itemZero);
                    startActivity(intent);
                }
            }
        });
        middleLeftCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, ArticleActivity.class);
                intent.putExtra("item", itemOne);
                startActivity(intent);
            }
        });
        middleRightCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, ArticleActivity.class);
                intent.putExtra("item", itemTwo);
                startActivity(intent);
            }
        });

    }

    private boolean isConnected(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private void setStartActivityViews(String categoryName) {

        rootNav.setMenuLocked(false);
        mainTitle.setText(categoryName);

        if (itemList != null) {
            ItemInfo itemZero = itemList.get(0);
            ItemInfo itemOne = itemList.get(1);
            ItemInfo itemTwo = itemList.get(2);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                this.itemZero = itemZero;
                setTopViews("zero");

                if (itemOne.getTitle().length() >= itemTwo.getTitle().length()) {
                    this.itemOne = itemOne;
                    this.itemTwo = itemTwo;
                }

                if (itemOne.getTitle().length() < itemTwo.getTitle().length()) {
                    this.itemOne = itemTwo;
                    this.itemTwo = itemOne;
                }
                setTopViews("one");
                setTopViews("two");

            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                int itemZeroLength = itemZero.getTitle().length();
                int itemOneLength = itemOne.getTitle().length();
                int itemTwoLength = itemTwo.getTitle().length();

                if (itemOneLength >= itemZeroLength && itemOneLength >= itemTwoLength) {
                    this.itemZero = itemZero;
                    this.itemOne = itemOne;
                    this.itemTwo = itemTwo;
                } else if (itemZeroLength > itemOneLength && itemZeroLength >= itemTwoLength) {
                    this.itemZero = itemOne;
                    this.itemOne = itemZero;
                    this.itemTwo = itemTwo;
                } else if (itemTwoLength > itemOneLength && itemTwoLength > itemZeroLength) {
                    this.itemZero = itemZero;
                    this.itemOne = itemTwo;
                    this.itemTwo = itemOne;
                }

                setTopViews("zero");
                setTopViews("one");
                setTopViews("two");
            }

        }
        nestedScrollView.scrollTo(0, nestedScrollView.getTop());
        progressView.setVisibility(View.GONE);
        YoYo.with(Techniques.FadeIn)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        nestedScrollView.setVisibility(View.VISIBLE);
                        btnMenuArrow.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .duration(500)
                .playOn(nestedScrollView);
    }

    private void setTopViews(String number) {
        switch (number) {
            case "zero":
                bigCategoryType.setText(category(this.itemZero.getCategory()));
                bigTitle.setText(this.itemZero.getTitle());
                bigDate.setText(this.itemZero.getDate());
                Picasso.with(StartActivity.this)
                        .load(this.itemZero.getImageUrl())
                        .into(bigImage);
                break;
            case "one":
                middleLeftCategoryType.setText(category(this.itemOne.getCategory()));
                middleLeftTitle.setText(this.itemOne.getTitle());
                middleLeftDate.setText(this.itemOne.getDate());
                Picasso.with(StartActivity.this)
                        .load(this.itemOne.getImageUrl())
                        .into(middleLeftImage);
                break;
            case "two":
                middleRightCategoryType.setText(category(this.itemTwo.getCategory()));
                middleRightTitle.setText(this.itemTwo.getTitle());
                middleRightDate.setText(this.itemTwo.getDate());
                Picasso.with(StartActivity.this)
                        .load(this.itemTwo.getImageUrl())
                        .into(middleRightImage);
                break;
        }
    }

    public static int category(String string) {
        switch (string) {
            case "politics":
                return R.string.stringPolitics;
            case "ukraine":
                return R.string.stringUkraine;
            case "economics":
                return R.string.stringEconomics;
            case "world":
                return R.string.stringWorld;
            case "culture":
                return R.string.stringCulture;
            case "life":
                return R.string.stringLife;
            case "sport":
                return R.string.stringSport;
            case "regions":
                return R.string.stringRegions;
            case "Kiev":
                return R.string.stringKiev;
            case "hot":
                return R.string.stringHot;
            case "criminal":
                return R.string.stringCriminal;
            default:
                return R.string.stringOthers;
        }
    }

    @Override
    public void cardClicked(final ItemInfo item) {

        YoYo.with(Techniques.SlideOutUp)
                .duration(400)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Intent intent = new Intent(StartActivity.this, ArticleActivity.class);
                        intent.putExtra("item", item);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .playOn(startActivityToolbar);
    }

    @Override
    public void onItemClicked(String category) {
        rootNav.closeMenu();
        rootNav.setMenuLocked(true);
        progressView.setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.GONE);
        btnMenuArrow.setVisibility(View.GONE);

        currentCategory = category;
        currentUrl = getCategoryUrl(category);

        if (isConnected(this)) {
            new TakeNews(currentUrl, currentCategory).execute();
            currentPage = 1;
        } else
            createAlertNetworkDialog();
    }

    private String getCategoryUrl(String clickedCategoryTitle) {

        switch (clickedCategoryTitle.trim()) {
            case "Последние новости":
                return HEADLINE_URL;
            case "Горячие темы":
                return HOT_TOPICS_URL;
            case "Политика":
                return POLITIC_URL;
            case "Украина":
                return UKRAINE_URL;
            case "Экономика":
                return ECONOMIC_URL;
            case "Мир":
                return WORLD_URL;
            case "Киев":
                return KYIV_URL;
            case "Культура":
                return CULTURE_URL;
            case "Спорт":
                return SPORT_URL;
            case "Жизнь":
                return LIFE_URL;
            case "Регионы":
                return REGIONES_URL;
            case "Криминал":
                return CRIME_URL;
            default:
                return HEADLINE_URL;
        }

    }

    @Override
    public void onDrag(float progress) {
        if (progress < 0.45 && btnMenuArrow.getTag().toString().equals("Arrow"))
            changeBtnIcon("Arrow");
        else if (progress > 0.55 && btnMenuArrow.getTag().toString().equals("Menu"))
            changeBtnIcon("Menu");
    }

    private void changeBtnIcon(String imgTag) {

        YoYo.with(Techniques.FlipOutY)
                .duration(700)
                .playOn(btnMenuArrow);

        if (imgTag.equals("Menu")) {
            btnMenuArrow.setImageResource(R.drawable.ic_arrow_back);
            btnMenuArrow.setTag("Arrow");
            YoYo.with(Techniques.FlipInY)
                    .duration(700)
                    .playOn(btnMenuArrow);
        }
        if (imgTag.equals("Arrow")) {
            btnMenuArrow.setImageResource(R.drawable.ic_menu);
            btnMenuArrow.setTag("Menu");
            YoYo.with(Techniques.FlipInY)
                    .duration(700)
                    .playOn(btnMenuArrow);
        }

    }

    public void createAlertNetworkDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("Internet connection not available!");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
                if (isConnected(context)) {
                    new TakeNews(currentUrl, currentCategory).execute();
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

    private void createIconCatalog() {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                CategoryIcon headlineNews = realm.createObject(CategoryIcon.class);
                headlineNews.setIconBlack(R.drawable.ic_headline_news_black);
                headlineNews.setIconBlue(R.drawable.ic_headline_news_blue);
                headlineNews.setIconGray(R.drawable.ic_headline_news_gray);

                CategoryIcon hotNews = realm.createObject(CategoryIcon.class);
                hotNews.setIconBlack(R.drawable.ic_hot_news_black);
                hotNews.setIconBlue(R.drawable.ic_hot_news_blue);
                hotNews.setIconGray(R.drawable.ic_hot_news_gray);

                CategoryIcon politicsNews = realm.createObject(CategoryIcon.class);
                politicsNews.setIconBlack(R.drawable.ic_politics_news_black);
                politicsNews.setIconBlue(R.drawable.ic_politics_news_blue);
                politicsNews.setIconGray(R.drawable.ic_politics_news_gray);

                CategoryIcon ukraineNews = realm.createObject(CategoryIcon.class);
                ukraineNews.setIconBlack(R.drawable.ic_ukraine_news_black);
                ukraineNews.setIconBlue(R.drawable.ic_ukraine_news_blue);
                ukraineNews.setIconGray(R.drawable.ic_ukraine_news_gray);

                CategoryIcon economicsNews = realm.createObject(CategoryIcon.class);
                economicsNews.setIconBlack(R.drawable.ic_economics_news_black);
                economicsNews.setIconBlue(R.drawable.ic_economics_news_blue);
                economicsNews.setIconGray(R.drawable.ic_economics_news_gray);

                CategoryIcon worldNews = realm.createObject(CategoryIcon.class);
                worldNews.setIconBlack(R.drawable.ic_world_news_black);
                worldNews.setIconBlue(R.drawable.ic_world_news_blue);
                worldNews.setIconGray(R.drawable.ic_world_news_gray);

                CategoryIcon kyivNews = realm.createObject(CategoryIcon.class);
                kyivNews.setIconBlack(R.drawable.ic_kyiv_news_black);
                kyivNews.setIconBlue(R.drawable.ic_kyiv_news_blue);
                kyivNews.setIconGray(R.drawable.ic_kyiv_news_gray);

                CategoryIcon cultureNews = realm.createObject(CategoryIcon.class);
                cultureNews.setIconBlack(R.drawable.ic_culture_news_black);
                cultureNews.setIconBlue(R.drawable.ic_culture_news_blue);
                cultureNews.setIconGray(R.drawable.ic_culture_news_gray);

                CategoryIcon sportNews = realm.createObject(CategoryIcon.class);
                sportNews.setIconBlack(R.drawable.ic_sport_news_black);
                sportNews.setIconBlue(R.drawable.ic_sport_news_blue);
                sportNews.setIconGray(R.drawable.ic_sport_news_gray);

                CategoryIcon lifeNews = realm.createObject(CategoryIcon.class);
                lifeNews.setIconBlack(R.drawable.ic_life_news_black);
                lifeNews.setIconBlue(R.drawable.ic_life_news_blue);
                lifeNews.setIconGray(R.drawable.ic_life_news_gray);

                CategoryIcon regionsNews = realm.createObject(CategoryIcon.class);
                regionsNews.setIconBlack(R.drawable.ic_region_news_black);
                regionsNews.setIconBlue(R.drawable.ic_region_news_blue);
                regionsNews.setIconGray(R.drawable.ic_region_news_gray);

                CategoryIcon crime = realm.createObject(CategoryIcon.class);
                crime.setIconBlack(R.drawable.ic_crime_news_black);
                crime.setIconBlue(R.drawable.ic_crime_news_blue);
                crime.setIconGray(R.drawable.ic_crime_news_gray);
            }
        });

        categoryIcons = realm.where(CategoryIcon.class).findAll();

        if (isItFirstAppStart) {
            for (CategoryIcon category : categoryIcons) {
                CategoryState catState = new CategoryState();
                catState.setImgSrc(category.getIconBlack());
                catState.setTxtColorRes(Color.BLACK);
                currentCategoryStateList.add(catState);
            }
            currentCategoryStateList.get(0).setTxtColorRes(Color.BLUE);
            currentCategoryStateList.get(0).setImgSrc(categoryIcons.get(0).getIconBlue());
        }

    }

    private class TakeNews extends AsyncTask<String, Void, String> {

        private String currentURL;
        private String currentCategory;

        TakeNews(String currentURL, String currentCategory) {
            this.currentURL = currentURL;
            this.currentCategory = currentCategory;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            recyclerView.setAdapter(adapter);
            setStartActivityViews(currentCategory);
            if (currentPage > 1) {
                constrLessButton.setVisibility(View.VISIBLE);
            } else
                constrLessButton.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            itemList.clear();
            Document document;

            try {
                document = Jsoup.connect(currentURL).get();
                elements = document.select(".overflow-wrap.white-frame");

                for (Element elem : elements) {

                    Spanned titleSpan = Html.fromHtml(elem.select("img").attr("alt"));
                    String title = titleSpan.toString();
                    String imageUrl = "http://www.segodnya.ua" + elem.select("img").attr("src");
                    String articleAdress = elem.select("a").attr("href");
                    String date = elem.select(".date").text();
                    String category = articleAdress.substring(1, articleAdress.indexOf('/', articleAdress.indexOf("/") + 1));

                    String articleUrl;
                    if (category.equals("ttp:/")) {
                        category = "Kiev";
                        articleUrl = articleAdress;
                    } else {
                        articleUrl = "http://www.segodnya.ua" + articleAdress;
                    }

                    itemList.add(new ItemInfo(title, imageUrl, articleUrl, date, category));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isItFirstAppStart = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isItFirstAppStart = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        YoYo.with(Techniques.FadeIn)
                .duration(0)
                .playOn(nestedScrollView);
        YoYo.with(Techniques.SlideInDown)
                .duration(600)
                .playOn(startActivityToolbar);

    }
}
