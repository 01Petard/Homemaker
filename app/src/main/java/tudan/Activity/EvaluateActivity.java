package tudan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjx.homemaker.R;
import com.wjx.homemaker.dialog.CustomProgressDialog;
import com.wjx.homemaker.utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

public class EvaluateActivity extends AppCompatActivity implements View.OnClickListener{
    private RatingBar as,ls,is,ss;
    private EditText review;
    private TextView submit,back;
    private CustomProgressDialog customProgressDialog;
    private HttpUtils httpUtils;
    private String oid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        as= (RatingBar) findViewById(R.id.as);
        is= (RatingBar) findViewById(R.id.is);
        ls= (RatingBar) findViewById(R.id.ls);
        ss= (RatingBar) findViewById(R.id.ss);
        review= (EditText) findViewById(R.id.edid_review_content);
        submit= (TextView) findViewById(R.id.btn_submit_edit);
        back= (TextView) findViewById(R.id.btn_return);
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
        as.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
        Intent intent=getIntent();
        if (intent!=null){
            oid=intent.getStringExtra("oid");

        }
        httpUtils=new HttpUtils();
        customProgressDialog=new CustomProgressDialog(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit_edit:
                customProgressDialog.setMessage("正在提交评价...");
                customProgressDialog.show();
                httpUtils.send(HttpRequest.HttpMethod.POST, Config.BASEHOST+"/mobile/OrderServlet?method=finishOrderByCustomer&oid=" + oid+"&as="+String.valueOf(as.getRating())+"&review="+review.getText().toString()+"&is="+String.valueOf(is.getRating())+"&ls="+String.valueOf(ls.getRating())+"&ss="+String.valueOf(ss.getRating()), new RequestCallBack<Object>() {
                    @Override
                    public void onSuccess(ResponseInfo<Object> reponseInfo) {
                        System.out.println("提交评价:"+reponseInfo.result);
                        try {
                                System.out.println(as.getRating()+is.getRating()+ss.getRating()+ls.getRating());
                            JSONObject ret=new JSONObject(reponseInfo.result.toString());
                            if (ret.getInt("errcode")==0){
                        if (customProgressDialog!=null&&customProgressDialog.isShowing()){
                            customProgressDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(),"评价提交成功",Toast.LENGTH_LONG).show();
                        EvaluateActivity.this.finish();}
                            else {
                                Toast.makeText(getApplicationContext(), "评价提交失败", Toast.LENGTH_LONG).show();
                                customProgressDialog.dismiss();
                            }
                    } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                        System.out.println("提交评价:"+s);

                    }
                });
                break;
            case R.id.btn_return:
                EvaluateActivity.this.finish();
                break;
        }

    }
}
