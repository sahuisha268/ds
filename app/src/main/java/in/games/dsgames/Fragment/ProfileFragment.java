package in.games.dsgames.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.dsgames.Activity.BankDetailActivity;
import in.games.dsgames.Activity.ChangePasswordActivity;
import in.games.dsgames.Activity.GameRatesActivity;
import in.games.dsgames.Activity.HowToPlayActivity;
import in.games.dsgames.Activity.LoginActivity;
import in.games.dsgames.Activity.NoticeBoardActivity;
import in.games.dsgames.Adapter.MenuAdapter;
import in.games.dsgames.Model.MenuModel;
import in.games.dsgames.R;
import in.games.dsgames.utils.RecyclerTouchListener;
import in.games.dsgames.utils.Session_management;

import static in.games.dsgames.Activity.SplashActivity.share_link;
import static in.games.dsgames.Config.Constants.KEY_MOBILE;
import static in.games.dsgames.Config.Constants.KEY_NAME;


public class ProfileFragment extends Fragment {
    TextView tv_name , tv_mobile ,tv_version;
    ImageView iv_profile;
RecyclerView rv_menu;
ArrayList<MenuModel>menuList;
Session_management session_management ;
int version_code ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(root);
        return root;
    }
    void initViews(View v)
    {
        session_management = new Session_management(getActivity());
        tv_name = v.findViewById(R.id.tv_name);
        tv_mobile = v.findViewById(R.id.tv_mobile);
        tv_version = v.findViewById(R.id.tv_version);
        rv_menu = v.findViewById(R.id.rv_profile_menu);
        menuList = new ArrayList<>();
        tv_name.setText(session_management.getUserDetails().get(KEY_NAME));
        tv_mobile.setText(session_management.getUserDetails().get(KEY_MOBILE));
        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version_code = pInfo.versionCode;
        tv_version.setText("App Version "+version_code);

        createMenu();
        rv_menu.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_menu, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Fragment fm=null;
                Bundle args = new Bundle();

                switch (menuList.get(position).getName()){
                    case "Add/Edit Bank Details":
                        startActivity(new Intent(getActivity(), BankDetailActivity.class));
                        break;
                    case "Change Password":
                      startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                    break;
                    case "Notice Board":
                        startActivity(new Intent(getActivity(), NoticeBoardActivity.class));
                        break;
                        case "Rate":
                            startActivity(new Intent(getActivity(), GameRatesActivity.class));
                        break;
                        case "How to Play":
                            startActivity(new Intent(getActivity(), HowToPlayActivity.class));
                        break;

//                        case "Check Results":
//                            startActivity(new Intent(getActivity(), CheckResultActivity.class));
//                        break;

//                    case "Monthly Report":
//                       startActivity(new Intent(getActivity(), MonthlyReportActivity.class));
//                       break;
//                       case "Account Statement":
//                       startActivity(new Intent(getActivity(), PaymentReportsActivity.class));
//                       break;

//                    case "Contact Us":
//                      startActivity(new Intent(getActivity(), ContactUsActivity.class));
//
//                        break;

                    case "Logout":
                        logout();
                        break;
                    case "Share":
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,share_link);
//                                "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        break;

                }


            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


    }

    private void createMenu() {
        menuList=new ArrayList<>();
        menuList.add(new MenuModel(R.drawable.icons8_museum_32px,"Add/Edit Bank Details",getResources().getColor(R.color.white)));
        //menuList.add(new MenuModel(R.drawable.icons8_agreement_50px,"Monthly Report",getResources().getColor(R.color.white)));
      //  menuList.add(new MenuModel(R.drawable.icons8_pie_chart_report_64px, "Account Statement" , getResources().getColor(R.color.white)));
      //  menuList.add(new MenuModel(R.drawable.icons8_agreement_50px,"Check Results",getResources().getColor(R.color.white)));
        menuList.add(new MenuModel(R.drawable.password_26px,"Change Password",getResources().getColor(R.color.white)));
        menuList.add(new MenuModel(R.drawable.icons8_agreement_50px,"Notice Board",getResources().getColor(R.color.white)));
        menuList.add(new MenuModel(R.drawable.icons8_stop_pie_chart_report_50px,"Rate",getResources().getColor(R.color.white)));
        menuList.add(new MenuModel(R.drawable.icons8_play_48px,"How to Play",getResources().getColor(R.color.white)));
        menuList.add(new MenuModel(R.drawable.icons8_share_30px,"Share",getResources().getColor(R.color.white)));
       // menuList.add(new MenuModel(R.drawable.icons8_office_phone_32px,"Contact Us",getResources().getColor(R.color.white)));
        menuList.add(new MenuModel(R.drawable.icons8_enter_60px,"Logout",getResources().getColor(R.color.white)));
        MenuAdapter menuAdapter=new MenuAdapter(menuList,getActivity());
        rv_menu.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_menu.setAdapter(menuAdapter);
        menuAdapter.notifyDataSetChanged();
    }
    private void logout() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        builder.setMessage("LOGOUT?")
                .setCancelable(false)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        session_management.logoutSession();
                        Intent intent=new Intent(getActivity(),LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }
}