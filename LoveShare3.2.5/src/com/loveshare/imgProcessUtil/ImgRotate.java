package com.loveshare.imgProcessUtil;

import com.loveshare.imgProcessUI.ImgRotateActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImgRotate {
    public static final String TAG = "RotateBitmap";
    private Bitmap mBitmap;
    private float mRotation;
    
    public ImgRotate(){
    	mRotation = 0;
    }
    public ImgRotate(Bitmap bitmap) {
        mBitmap = bitmap;
        mRotation = 0;
    }

    public ImgRotate(Bitmap bitmap, int rotation) {
        mBitmap = bitmap;
        mRotation = rotation % 360;
    }

    public void setRotation(int rotation) {
        mRotation = rotation;
    }

    public void setRotation(float rotation) {
        mRotation = rotation;
    }
    
    public double getRotation() {
        return mRotation;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Matrix getRotateMatrix() {
        // By default this is an identity matrix.
        Matrix matrix = new Matrix();
        if (mRotation != 0) {
            // We want to do the rotation at origin, but since the bounding
            // rectangle will be changed after rotation, so the delta values
            // are based on old & new width/height respectively.
            int cx = mBitmap.getWidth() / 2;
            int cy = mBitmap.getHeight() / 2;
            matrix.preTranslate(-cx, -cy);
            matrix.postRotate(mRotation);
            matrix.postTranslate(getWidth() / 2, getHeight() / 2);
        }
        return matrix;
    }

    public boolean isOrientationChanged() {
        return (mRotation / 90) % 2 != 0;
    }

    public int getHeight() {
        if (isOrientationChanged()) {
            return mBitmap.getWidth();
        } else {
            return mBitmap.getHeight();
        }
    }

    public int getWidth() {
        if (isOrientationChanged()) {
            return mBitmap.getHeight();
        } else {
            return mBitmap.getWidth();
        }
    }

    public void recycle() {
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}


