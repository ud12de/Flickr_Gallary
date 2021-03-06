package com.flickrgallery.activity;

import java.io.File;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flickrgallery.observer.Observer;
import com.flickrgallery.request.GetOrigianlPhoto;
import com.flickrgallery.util.Util;

/**
 * Download the larger image and display in Imageview.
 * @author Umang
 */
public class DetailFragment extends Fragment implements Observer {

	private String id;
	private String farm, server, secret, title;
	private ImageView imageView1;
	private TextView imageTitle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.full_image, container, false);
		imageView1 = (ImageView) view.findViewById(R.id.imageView1);
		imageTitle = (TextView) view.findViewById(R.id.imageTitle);

		if (this.getArguments() != null) {
			farm = this.getArguments().getString("farm");
			id = this.getArguments().getString("id");
			server = this.getArguments().getString("server");
			secret = this.getArguments().getString("secret");
			title = this.getArguments().getString("title");
			File file = new File(Util.DIR_PATH_FULL_IMAGE + id);
			if (file.exists()) {
				displayImage(file);
			} else {
				GetOrigianlPhoto getOrigianlPhoto = new GetOrigianlPhoto();
				getOrigianlPhoto.registerObserver(this);
				getOrigianlPhoto.execute(farm + ":" + id + ":" + server + ":"
						+ secret);
			}
		}
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (outState != null) {
			outState.putString("farm", farm);
			outState.putString("id", id);
			outState.putString("server", server);
			outState.putString("secret", secret);
			outState.putString("title", title);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null) {
			farm = savedInstanceState.getString("farm");
			id = savedInstanceState.getString("id");
			server = savedInstanceState.getString("server");
			secret = savedInstanceState.getString("secret");
			title = savedInstanceState.getString("title");
		}
	}

	@Override
	public void update(String string) {
		if (string.equalsIgnoreCase("DOWNLOAD_COMPLETE")) {
			File file = new File(Util.DIR_PATH_FULL_IMAGE + id);
			displayImage(file);
		}
	}

	private void displayImage(File file) {
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		imageView1.setImageBitmap(bitmap);
		imageTitle.setText(title);
	}

	public static DetailFragment newInstance(int index) {
		DetailFragment f = new DetailFragment();

		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		f.setArguments(args);
		return f;
	}

	public int getShownIndex() {
		if (getArguments() == null)
			return -1;
		return getArguments().getInt("index", 0);
	}
}
