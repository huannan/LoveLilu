/*******************************************************************************
 * Copyright 2015 CCwant
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.lovelilu.ui.adapter;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lovelilu.R;

import java.util.List;

public class ImageGridAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater inflater;
    List<String> mImageList;
    private int mMaxPosition;

    public ImageGridAdapter(Context context, List<String> list) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mImageList = list;
    }

    public int getCount() {

        //加一个是添加图片用的
        mMaxPosition = mImageList.size() + 1;
        return mMaxPosition;
    }

    public int getMaxPosition() {
        return mMaxPosition;
    }

    public Object getItem(int position) {

        return mImageList.get(position);
    }

    public long getItemId(int position) {

        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_grid_image, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.item_image_publish_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Log.d("", "position:" + position + "  mMaxPosition:" + mMaxPosition);

        if (position == mMaxPosition - 1) {
            holder.image.setTag("default");
//            holder.image.setImageResource(R.drawable.ccwant_addpic_unfocused);
        } else {
            final String path = mImageList.get(position);

            holder.image.setImageBitmap(BitmapFactory.decodeFile(path));

//            Picasso.with(mContext).load(new File(path)).into(holder.image);
        }

        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
    }


}
