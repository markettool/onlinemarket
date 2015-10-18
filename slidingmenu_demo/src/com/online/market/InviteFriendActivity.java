package com.online.market;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/***
 * 邀请朋友
 * @author majie
 *
 */
public class InviteFriendActivity extends BaseActivity {
	
	private TextView tvCouponId;
	private Button btInvite;
	
	private UMSocialService mController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invitefriend);
		
		initView();
		setListeners();
		initData();
	}

	@Override
	protected void initView() {
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("优惠");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		
		tvCouponId=(TextView) findViewById(R.id.tv_coupon_id);
		btInvite=(Button) findViewById(R.id.bt_invite);
		
	}

	@Override
	protected void initData() {
		if(user==null){
			startActivity(LoginActivity.class);
			finish();
			return;
		}
		
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 设置分享内容
//		mController.setShareContent("请使用我的推荐码"+user.getInviteCode()+"。\n天天在线，您的掌上超市。");
//		// 设置分享图片, 参数2为图片的url地址
//		mController.setShareMedia(new UMImage(this, R.drawable.ic_launcher));
		
		//设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		//设置分享文字
		weixinContent.setShareContent("请使用我的推荐码"+user.getInviteCode()+"。\n天天在线，您的掌上超市。");
		//设置title
		weixinContent.setTitle("天天在线");
		//设置分享内容跳转URL
		weixinContent.setTargetUrl("http://onlinemarket.sinaapp.com/");
		//设置分享图片
		weixinContent.setShareImage(new UMImage(this, R.drawable.ic_launcher));
		mController.setShareMedia(weixinContent);
		
		//设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent("请使用我的推荐码"+user.getInviteCode()+"。\n天天在线，您的掌上超市。");
		//设置朋友圈title
		circleMedia.setTitle("天天在线,，您的掌上超市。我的邀请码"+user.getInviteCode());
		circleMedia.setShareImage(new UMImage(this, R.drawable.ic_launcher));
		circleMedia.setTargetUrl("http://onlinemarket.sinaapp.com/");
		mController.setShareMedia(circleMedia);

		addWeixin();
		
		tvCouponId.setText(user.getInviteCode());
	}

	@Override
	protected void setListeners() {
		 mImgLeft.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
		 
		 btInvite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				onClickShare();
			}
		});
	}
	
	private void onClickShare() {  
		  
		mController.getConfig().removePlatform( SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);
		mController.openShare(this, false);
  
    } 
	
	private void addWeixin(){
		String appID = "wxb9c4998ba1292cfc";
		String appSecret = "f35727b904728c95ba8f015c8274b08d";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this,appID,appSecret);
//		wxHandler.setTargetUrl("http://onlinemarket.sinaapp.com/");
		wxHandler.addToSocialSDK();
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this,appID,appSecret);
		wxCircleHandler.setToCircle(true);
//		wxCircleHandler.setTargetUrl("http://onlinemarket.sinaapp.com/");
		wxCircleHandler.addToSocialSDK();
		
	}

}
