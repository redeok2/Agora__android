package com.cos.Agora.study;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.cos.Agora.CMRespDto;
import com.cos.Agora.R;
import com.cos.Agora.calendar.CalendarActivity;
import com.cos.Agora.global.User;
import com.cos.Agora.main.MainActivity;
import com.cos.Agora.study.adapter.StudyListAdapter;
import com.cos.Agora.study.adapter.DetailAdapter;
import com.cos.Agora.study.model.DetailRespDto;
import com.cos.Agora.study.model.StudyListRespDto;
import com.cos.Agora.retrofitURL;
import com.cos.Agora.study.service.StudyService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private Context mContext;

    private static final String TAG = "DetailActivity";

    //private ViewPagerAdapter viewPagerAdapter;
    //private GridViewAdapter gridViewadapter;
    private ArrayList<String> mImageList;
    private RecyclerView studyDetail;
    private DetailAdapter detailAdapter;
    private int postId;
    private ImageButton mBack;
    private MainActivity activity;
    //private ImageButton mShare, mMore;
    //private ViewPager photoList;
    //private CircleIndicator circleIndicator;
    //private ImageView profile;
    private TextView tvTitle, tvCategori, tvDescrip;
    private TextView tvCurrent, tvLimit, tvCreateDate;
    private ImageButton ivCalendar, ivNotification, ivPlace;
    private RecyclerView rvStudyDetail;
    private long Id;
    private ArrayList<StudyListRespDto> studyRespDtos = new ArrayList<>();

    //private TextView tvNickName,tvAddress,tvTitle,tvTime,tvContent,tvCategori,tvFavorite,tvViewCount,tvAnotherNick,tvPrice;
    private retrofitURL retrofitURL;
    private StudyService studyListService= retrofitURL.retrofit.create(StudyService .class);
//    private StudyService detailService = retrofitURL.retrofit.create(StudyService.class);

    //private DetailService detailService= retrofitURL.retrofit.create(DetailService .class);
    // 프로필 부분 필요 없을 것 같아서 주석 처리함
    //private ProfileService profileService= retrofitURL.retrofit.create(ProfileService .class);
    //private RecyclerView rvProduct;
    //private LinearLayout reportAndProduct,layout_userprofile;

    private Button btn_study_join;
    //private ImageButton btnFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();
        initSetting();

        rvStudyDetail = findViewById(R.id.rv_studylist);
        LinearLayoutManager manager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        rvStudyDetail.setLayoutManager(manager);

    }

    public  void init(){
        // 레이아웃에서 주석 처리한 코드들 DetailActivity에서 변수들 주석처리함 11.12
        // rvProduct =findViewById(R.id.rv_product);
        tvTitle = findViewById(R.id.detail_title); // 스터디 제목
        tvCategori = findViewById(R.id.detail_categories); // 관심분야
        tvCurrent = findViewById(R.id.detail_current);
        tvLimit = findViewById(R.id.detail_limit);
        tvCreateDate = findViewById(R.id.detail_createDate);

        btn_study_join = findViewById(R.id.btn_study_join); // 가입신청

//        ImageButton ib = (ImageButton)findViewById(R.id.ib);

        ivCalendar = findViewById(R.id.iv_calendar); // 일정관리 -> 근데 해당 스터디 안의 일정을 알기가 과정이 쉽지 않을듯
        ivNotification = findViewById(R.id.iv_notification); // 공지
        ivPlace = findViewById(R.id.iv_place); // 장소

        tvDescrip = findViewById(R.id.detail_description); // 상세정보

        rvStudyDetail = findViewById(R.id.rv_study_detail); // 사용자 리사이클러 뷰
        tvCurrent = findViewById(R.id.detail_current);
        tvLimit = findViewById(R.id.detail_limit);

        mBack = findViewById(R.id.product_information_iv_back);

        // 가입 신청 버튼을 눌렀을 때 가입 신청 레이아웃으로 넘어간다.

        btn_study_join.setOnClickListener(v -> {
            Intent intent1 = new Intent(DetailActivity.this, StudyApplicationActivity.class);
            startActivity(intent1);
        });


        // 일정 관리 이미지 버튼을 눌렀을 때 일정 관리 레이아웃으로 넘어간다.

        ivCalendar.setOnClickListener(v -> {
            Intent intent2 = new Intent(DetailActivity.this, CalendarActivity.class);
            startActivity(intent2);
        });



        // 게시물을 클릭했을 때 해당 스터디 아이디를 서버로 보내서 그곳에 속해있는 유저 아이디 및 다른 정보들을
        // 가져오게 해야 하는데 이게 맞나
        getStudyDetail(Id);

        //profile = findViewById(R.id.detail_iv_profile);
        //tvNickName = findViewById(R.id.detail_nickname);
        //tvAddress = findViewById(R.id.detail_tv_address);
        // tvTitle = findViewById(R.id.detail_tv_title);
        //tvContent=findViewById(R.id.detail_tv_content);
        //tvCategori=findViewById(R.id.detail_tv_categories);
        //tvTime = findViewById(R.id.detail_tv_time);
        //tvFavorite = findViewById(R.id.detail_tv_favorite);
        //tvViewCount = findViewById(R.id.detail_tv_viewCount);
        //tvAnotherNick = findViewById(R.id.detail_tv_nickname_another);
        //tvPrice = findViewById(R.id.detail_tv_price);
        //mShare = findViewById(R.id.product_information_iv_share);
        //mMore = findViewById(R.id.product_information_iv_more);

        //reportAndProduct = findViewById(R.id.reportAndProduct);
        //layout_userprofile = findViewById(R.id.layout_userprofile);
        //detail_btn_chat = findViewById(R.id.detail_btn_chat);

        //btnFavorite = findViewById(R.id.btn_favorite);
    }

    public void initSetting(){

        Intent intent = getIntent();
        long Id = intent.getLongExtra("studyId", 1); // id를 받아야 한다.
        String Title = intent.getStringExtra("studyTitle");
        String Category = intent.getStringExtra("studyInterest");
//        Date CreateDate = intent.getStringExtra("studyCreateDate");
        int Current = intent.getIntExtra("studyCurrent", 1);
        int Limit = intent.getIntExtra("studyLimit", 1);

        tvTitle.setText(Title);
        tvCategori.setText(Category);
        tvCreateDate.setText(intent.getStringExtra("studyCreateDate"));
        tvCurrent.setText(String.valueOf(Current));
        tvLimit.setText(String.valueOf(Limit));

        // 이미 StudyListAdapter에서 받아왔기 형을 바꿨기 때문에 또 바꾸면 안된다.
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        tvCreateDate.setText(format.format(CreateDate+""));

//        postId = intent.getIntExtra("postId", 1);
//        getStudyDetail(postId);

        // 뒤로 가기
        mBack.setImageResource(R.drawable.home_as_up);
        mBack.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);

        // 공유, 더보기 부분 뺄 것
        //mShare.setImageResource(R.drawable.ic_share_outline_24);
        //mShare.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        //mMore.setImageResource(R.drawable.icon_ads_more);
        //mMore.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);

    }

    // 뒤로 가기 행동할 때의 함수
    public void productInformationOnClick(View view) {
        switch (view.getId()) {
            case R.id.product_information_iv_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }


    public void getStudyDetail(long Id){
        Call<CMRespDto<List<DetailRespDto>>> call = studyListService.getStudyDetail(Id);

//        Ulatitude = ((User)getApplication()).getLatitude();  // 사용자 위도
//        Ulongitude = ((User)getApplication()).getLongitude();// 사용자 경도

        call.enqueue(new Callback<CMRespDto<List<DetailRespDto>>>() {
            @Override
            public void onResponse(Call<CMRespDto<List<DetailRespDto>>> call, Response<CMRespDto<List<DetailRespDto>>> response) {
                try {
                    CMRespDto<List<DetailRespDto>> cmRespDto = response.body();
                    List<DetailRespDto> details = cmRespDto.getData();
                    Log.d(TAG, "details: " + details);
                    detailAdapter = new DetailAdapter(details, activity, Id);
                    studyDetail.setAdapter(detailAdapter);
                } catch (Exception e) {
                    Log.d(TAG, "null");
                }
            }
            @Override
            public void onFailure(Call<CMRespDto<List<DetailRespDto>>> call, Throwable t) {
                Log.d(TAG, "onFailure: 스터디 그룹원 정보 보기 실패 !!");
            }
        });
    }

// 11.21 주석처리 함 : getStudyDetail -> 필요없을 것 같아서
//    public void getStudyDetail(int id){
//        Call<CMRespDto<StudyListRespDto>> call = detailService.getStudyDetail(id);
//        call.enqueue(new Callback<CMRespDto<StudyListRespDto>>() {
//            @Override
//            public void onResponse(Call<CMRespDto<StudyListRespDto>> call, Response<CMRespDto<StudyListRespDto>> response) {
//                CMRespDto<StudyListRespDto> cmRespDto = response.body();
//                StudyListRespDto posts = cmRespDto.getData();
//                String tmp;

//                if(posts.getPrice().equals("무료나눔")){
//                    tmp ="무료나눔";
//                }else{
//                    tmp = moneyFormatToWon(Integer.parseInt(posts.getPrice()));
//                }

                // 이미지 관련 코드는 주석처리함 11.11 00:08
//                mImageList = new ArrayList();
//
//                for(int i=0; i<posts.getImages().size(); i++) {
//                    mImageList.add(posts.getImages().get(i).getUri());
//                }

                // 게시물 정보를 봤을 때 이미지를 옆으로 넘기면서 볼 수 있도록 하는 코드
//                photoList = findViewById(R.id.detail_viewPager);
//                Log.d(TAG, "mImageList: "+mImageList);
//                viewPagerAdapter = new ViewPagerAdapter(DetailActivity.this,mImageList);
//                photoList.setAdapter(viewPagerAdapter);

                // 이미지 넘기는 화면 밑의 동그라미로 이미지 넘길 때마다 동그라미 색이 바뀌는 코드
//                circleIndicator= findViewById(R.id.detail_indicator);
//                viewPagerAdapter.notifyDataSetChanged();
//                circleIndicator.setViewPager(photoList);


//                if(posts.getUser().getPhoto() !=null){
//                    Glide.with(DetailActivity.this).load(posts.getUser().getPhoto()).into(profile);
//                    profile.setClipToOutline(true);
//                    profile.setBackground(new ShapeDrawable(new OvalShape()));
//                    profile.setScaleType(ImageView.ScaleType.FIT_XY);
//                  /*  Uri uri = Uri.parse(posts.getUser().getPhoto());
//                    profile.setImageURI(uri);
//                    profile.setBackground(new ShapeDrawable(new OvalShape()));
//                    profile.setClipToOutline(true);
//                    profile.setScaleType(ImageView.ScaleType.FIT_XY);*/
//                }

//                tvNickName.setText(posts.getUser().getNickName());
//                //tvAddress.setText(posts.getDong());
//                tvTitle.setText(posts.getTitle());
//                tvTime.setText(posts.getCreateDate().toString());
//                //tvFavorite.setText(posts.getFavorite()+"");
//                tvCategori.setText(posts.getCategory());
//                tvContent.setText(posts.getContent());
//                tvViewCount.setText(posts.getCount()+"");
//                tvAnotherNick.setText(posts.getUser().getNickName());

//                if(tmp.equals("무료나눔")){
//                    tvPrice.setText(tmp);
//                }else{
//                    tvPrice.setText(tmp+"원");
//                }

//                GridLayoutManager gridLayoutManager = new GridLayoutManager(DetailActivity.this,2);
//                rvProduct.setLayoutManager(gridLayoutManager);
//                gridViewadapter = new GridViewAdapter(posts.getUser().getPosts(),DetailActivity.this);
//                rvProduct.setAdapter(gridViewadapter);

//                SharedPreferences pref =getSharedPreferences("pref", Context.MODE_PRIVATE);
//                int userId = pref.getInt("userId",0);

                // 자신이 올린 게시물이면 "게시글 신고하기 및 ~~님의 판매상품"이 보이지 않게 하는 코드
//                if(userId == posts.getUser().getId()){
//                    reportAndProduct.setVisibility(View.INVISIBLE);
//                }else{
//                    reportAndProduct.setVisibility(View.VISIBLE);
//                }

            /*    if(userId ==posts.getUser().getId()){
                    detail_btn_chat.setEnabled(false);
                }else{
                    detail_btn_chat.setEnabled(true);
                }*/


//                mMore.setOnClickListener(v -> {
//                    if(userId ==posts.getUser().getId()){
//                        PopupMenu popup = new PopupMenu(DetailActivity.this ,mMore );
//                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                            @Override
//                            public boolean onMenuItemClick(MenuItem item) {
//                                if (item.getItemId() == R.id.menu1){
//                                    Log.d(TAG, "onMenuItemClick: 수정페이지이동 ");
//                                    Intent intent = new Intent(DetailActivity.this, UpdateActivity.class);
//                                    intent.putExtra("postId", posts.getId());
//                                    intent.putExtra("title", posts.getTitle());
//                                    intent.putExtra("category", posts.getCategory());
//                                    intent.putExtra("content", posts.getContent());
//                                    intent.putExtra("price", posts.getPrice());
//                                    intent.putExtra("mImageList", mImageList);
//                                    startActivity(intent);
//                                    DetailActivity.this.finish();
//                                }else if (item.getItemId() == R.id.menu2){
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
//                                    builder.setTitle("게시물 삭제").setMessage("정말 게시물을 삭제 하시겠습니까?");
//                                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            Log.d(TAG, "onMenuItemClick: 삭제완료");
//                                            removePost(postId);
//                                            Intent intent = new Intent(DetailActivity.this, MainActivity.class);
//                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                            startActivity(intent);     // intent 타입을 넣어야함  !!
//                                            DetailActivity.this.finish();
//                                        }
//                                    });
//                                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int id) {
//
//                                        }
//                                    });
//                                    builder.show();
//
//                                }
//                                return false;
//                            }
//                        });
//                        MenuInflater inf = popup.getMenuInflater();
//                        inf.inflate(R.menu.updatemenu, popup.getMenu());
//                        popup.show();
//                    }else{
//                        detail_btn_chat.setEnabled(true);
//                        PopupMenu popup = new PopupMenu(DetailActivity.this ,mMore );
//                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                            @Override
//                            public boolean onMenuItemClick(MenuItem item) {
//                                if (item.getItemId() == R.id.menu3){
//                                    Toast.makeText(getApplicationContext(),"준비중입니다.",Toast.LENGTH_LONG).show();
//                                }else if (item.getItemId() == R.id.menu4){
//                                    Toast.makeText(getApplicationContext(),"준비중입니다.",Toast.LENGTH_LONG).show();
//                                }
//                                return false;
//                            }
//                        });
//                        MenuInflater inf = popup.getMenuInflater();
//                        inf.inflate(R.menu.hidemenu, popup.getMenu());
//                        popup.show();
//                    }
//                });
//
//
//                layout_userprofile.setOnClickListener(v -> {
//                    Intent intent = new Intent(DetailActivity.this, UserProfileActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.putExtra("userId",posts.getUser().getId());
//                    startActivity(intent);
//                });

                // 이후 채팅 액티비티에서 필요할 것 같은데 일단 주석 처리함
//                detail_btn_chat.setOnClickListener(v -> {
//                    Intent intent = new Intent(DetailActivity.this, ChatActivity.class);
//                    intent.putExtra("userId",userId);
//                    intent.putExtra("postId",posts.getId());
//                    intent.putExtra("nickName",posts.getUser().getNickName());
//                    intent.putExtra("uri",posts.getUser().getPhoto());
//                    startActivity(intent);
//                });
//
//            }
//            @Override
//            public void onFailure(Call<CMRespDto<PostRespDto>> call, Throwable t) {
//
//            }
//        });
//    }



    // menu
    public void removePost(long Id){
        Call<CMRespDto<List<StudyListRespDto>>> call = studyListService.removePost(Id);
        call.enqueue(new Callback<CMRespDto<List<StudyListRespDto>>>() {
            @Override
            public void onResponse(Call<CMRespDto<List<StudyListRespDto>>> call, Response<CMRespDto<List<StudyListRespDto>>> response) {
                Log.d(TAG, "onResponse: 삭제성공");
            }
            @Override
            public void onFailure(Call<CMRespDto<List<StudyListRespDto>>> call, Throwable t) {
                Log.d(TAG, "onFailure: 삭제실패");
            }
        });
    }

//    public static String moneyFormatToWon(int inputMoney) {
//        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
//        return decimalFormat.format(inputMoney);
//    }

}
