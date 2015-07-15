package com.loveshare.UI;

import com.loveshare.activity.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WelcomeActivity extends FragmentActivity {

	private ViewPager pager_welcome;
	private static final int NUM = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		pager_welcome = (ViewPager) this.findViewById(R.id.pager_welcome);
		pager_welcome
				.setAdapter(new MyAdapter(this.getSupportFragmentManager()));

	}

	public class MyAdapter extends FragmentStatePagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return WeclomeFragment.newInstance(WelcomeActivity.this, position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return NUM;
		}

	}

	public static class WeclomeFragment extends Fragment implements
			OnClickListener {

		private int position;
		private static Context context;

		public static WeclomeFragment newInstance(Context context1, int position) {

			context = context1;

			WeclomeFragment fragment = new WeclomeFragment();

			Bundle bundle = new Bundle();
			bundle.putString("position", position + "");
			fragment.setArguments(bundle);

			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			String position1 = this.getArguments().getString("position");
			position = (position1 == null ? 0 : Integer.parseInt(position1));
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = null;

			if (position == NUM - 1) {
				view = View.inflate(this.getActivity(),
						R.layout.view_welcomfragment2, null);

				Button btn_ToMain = (Button) view.findViewById(R.id.btn_ToMain);
				btn_ToMain.setOnClickListener(this);
			} else
				view = View.inflate(this.getActivity(),
						R.layout.view_welcomfragment, null);

			ImageView imageView = (ImageView) view
					.findViewById(R.id.img_welcome);

			switch (position) {
			case 0:
				imageView.setImageResource(R.drawable.welcome1);
				break;
			case 1:
				imageView.setImageResource(R.drawable.welcome2);
				break;
			case 2:
				imageView.setImageResource(R.drawable.welcome3);
				break;
			}

			LinearLayout linear_hintImg = (LinearLayout) view
					.findViewById(R.id.linear_hintImg);

			for (int i = 0; i < NUM; i++) {
				ImageView img = new ImageView(this.getActivity());
				if (i == position)
					img.setImageResource(R.drawable.guide_dot_blue);
				else
					img.setImageResource(R.drawable.guide_dot_white);

				linear_hintImg.addView(img);
			}
			return view;
		}

		@Override
		public void onClick(View v) {
			if (this.getActivity().getIntent()
					.getBooleanExtra("isNavigation", false)) {
				this.getActivity().finish();
			} else {
				this.startActivity(new Intent(this.getActivity(),
						MainActivity.class));
				this.getActivity().finish();
			}
		}
	}

}
