package com.github.lzyzsd.circleprogress;

import com.github.lzyzsd.progress.R;
import com.github.lzyzsd.viewutils.ViewUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore.Images.Thumbnails;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @ClassName: DonutProgress
 * @Description: Created by bruce on 14-10-30. 双圆进度条
 * @author marktoo www.marktoo.com.cn
 * @date 2015-6-16 上午11:19:44
 * 
 */
public class DonutProgress extends View {
	/**
	 * @Fields finishedPaint
	 * @Description: 完成进度画笔
	 */
	private Paint finishedPaint;
	/**
	 * @Fields unfinishedPaint
	 * @Description: 未完成画笔颜色
	 */
	private Paint unfinishedPaint;
	/**
	 * @Fields innerCirclePaint
	 * @Description: 内圈圆画笔
	 */
	private Paint innerCirclePaint;
	/**
	 * @Fields textPaint
	 * @Description: 进度提示文本画笔
	 */
	protected Paint textPaint;
	/**
	 * @Fields innerBottomTextPaint
	 * @Description: 内圈底部文本画笔
	 */
	protected Paint innerBottomTextPaint;

	/**
	 * @Fields finishedOuterRect
	 * @Description: 完成进度部分外框矩形
	 */
	private RectF finishedOuterRect = new RectF();
	/**
	 * @Fields unfinishedOuterRect
	 * @Description: 未完成进度部分外框矩形
	 */
	private RectF unfinishedOuterRect = new RectF();

	/**
	 * @Fields textSize
	 * @Description: 文字大小
	 */
	private float textSize;
	/**
	 * @Fields textColor
	 * @Description: 文字颜色
	 */
	private int textColor;
	private int innerBottomTextColor;
	private int progress = 0;
	private int max;
	/**
	 * @Fields finishedStrokeColor
	 * @Description: 完成部分颜色
	 */
	private int finishedStrokeColor;
	/**
	 * @Fields unfinishedStrokeColor
	 * @Description: 未完成部分颜色
	 */
	private int unfinishedStrokeColor;
	/**
	 * @Fields finishedStrokeWidth
	 * @Description: 完成进度部分进度条宽度
	 */
	private float finishedStrokeWidth;
	/**
	 * @Fields unfinishedStrokeWidth
	 * @Description: 未完成进度部分进度条宽度
	 */
	private float unfinishedStrokeWidth;
	/**
	 * @Fields innerBackgroundColor
	 * @Description: 进度条内部背景色
	 */
	private int innerBackgroundColor;
	/**
	 * @Fields innerBackgroundBmp
	 * @Description: 进度条内部背景图像
	 */
	private int innerBackgroundBmp;
	/**
	 * @Fields prefixText
	 * @Description: 前缀文本
	 */
	private String prefixText = "";
	/**
	 * @Fields suffixText
	 * @Description: 后缀文本--进度更新的单位
	 */
	private String suffixText = "%";
	private String text = null;
	/**
	 * @Fields innerBottomTextSize
	 * @Description: 内部底部文本大小
	 */
	private float innerBottomTextSize;
	/**
	 * @Fields innerBottomText
	 * @Description: 内部底部文本
	 */
	private String innerBottomText;
	/**
	 * @Fields innerBottomTextHeight
	 * @Description: 内部底部文本高度
	 */
	private float innerBottomTextHeight;

	private final float default_stroke_width;
	private final int default_finished_color = Color.rgb(66, 145, 241);
	private final int default_unfinished_color = Color.rgb(204, 204, 204);
	private final int default_text_color = Color.rgb(66, 145, 241);
	private final int default_inner_bottom_text_color = Color.rgb(66, 145, 241);
	private final int default_inner_background_color = Color.TRANSPARENT;
	private final int default_max = 100;
	private final float default_text_size;
	private final float default_inner_bottom_text_size;
	private final int min_size;

	private static final String INSTANCE_STATE = "saved_instance";
	private static final String INSTANCE_TEXT_COLOR = "text_color";
	private static final String INSTANCE_TEXT_SIZE = "text_size";
	private static final String INSTANCE_TEXT = "text";
	private static final String INSTANCE_INNER_BOTTOM_TEXT_SIZE = "inner_bottom_text_size";
	private static final String INSTANCE_INNER_BOTTOM_TEXT = "inner_bottom_text";
	private static final String INSTANCE_INNER_BOTTOM_TEXT_COLOR = "inner_bottom_text_color";
	private static final String INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color";
	private static final String INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color";
	private static final String INSTANCE_MAX = "max";
	private static final String INSTANCE_PROGRESS = "progress";
	private static final String INSTANCE_SUFFIX = "suffix";
	private static final String INSTANCE_PREFIX = "prefix";
	private static final String INSTANCE_FINISHED_STROKE_WIDTH = "finished_stroke_width";
	private static final String INSTANCE_UNFINISHED_STROKE_WIDTH = "unfinished_stroke_width";
	private static final String INSTANCE_BACKGROUND_COLOR = "inner_background_color";
	private static final String INSTANCE_BACKGROUND_BMP = "inner_background_src";

	public DonutProgress(Context context) {
		this(context, null);
	}

	public DonutProgress(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DonutProgress(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		default_text_size = ViewUtil.sp2px(getResources(), 18);
		min_size = (int) ViewUtil.dp2px(getResources(), 100);
		default_stroke_width = ViewUtil.dp2px(getResources(), 10);
		default_inner_bottom_text_size = ViewUtil.sp2px(getResources(), 18);

		// final TypedArray attributes = context.getTheme()
		// .obtainStyledAttributes(attrs, R.styleable.DonutProgress,
		// defStyleAttr, 0);
		TypedArray attributes = null;
		try {
			attributes = context.obtainStyledAttributes(attrs,
					R.styleable.DonutProgress, defStyleAttr, 0);
			initByAttributes(attributes);
		} finally {
			if (attributes != null)
				attributes.recycle();
		}
		initPainters();
	}

	/**
	 * @Title: initPainters
	 * @Description: 初始化画笔
	 * @param
	 * @return void
	 * @throws
	 */
	protected void initPainters() {
		textPaint = new TextPaint();
		textPaint.setColor(textColor);
		textPaint.setTextSize(textSize);
		textPaint.setAntiAlias(true);

		innerBottomTextPaint = new TextPaint();
		innerBottomTextPaint.setColor(innerBottomTextColor);
		innerBottomTextPaint.setTextSize(innerBottomTextSize);
		innerBottomTextPaint.setAntiAlias(true);

		finishedPaint = new Paint();
		// 设置画笔颜色
		finishedPaint.setColor(finishedStrokeColor);
		// 设置画笔实心填充
		finishedPaint.setStyle(Paint.Style.STROKE);
		// 设置画笔抗锯齿，边缘圆滑
		finishedPaint.setAntiAlias(true);
		// 设置画笔笔触宽度
		finishedPaint.setStrokeWidth(finishedStrokeWidth);

		unfinishedPaint = new Paint();
		unfinishedPaint.setColor(unfinishedStrokeColor);
		unfinishedPaint.setStyle(Paint.Style.STROKE);
		unfinishedPaint.setAntiAlias(true);
		unfinishedPaint.setStrokeWidth(unfinishedStrokeWidth);

		innerCirclePaint = new Paint();
		innerCirclePaint.setColor(innerBackgroundColor);
		innerCirclePaint.setAntiAlias(true);
	}

	/**
	 * @Title: initByAttributes
	 * @Description: 根据传入参数初始化view
	 * @param attributes
	 * @return void
	 */
	protected void initByAttributes(TypedArray attributes) {
		finishedStrokeColor = attributes.getColor(
				R.styleable.DonutProgress_donut_finished_color,
				default_finished_color);
		unfinishedStrokeColor = attributes.getColor(
				R.styleable.DonutProgress_donut_unfinished_color,
				default_unfinished_color);
		textColor = attributes.getColor(
				R.styleable.DonutProgress_donut_text_color, default_text_color);
		textSize = attributes.getDimension(
				R.styleable.DonutProgress_donut_text_size, default_text_size);

		setMax(attributes.getInt(R.styleable.DonutProgress_donut_max,
				default_max));
		setProgress(attributes.getInt(R.styleable.DonutProgress_donut_progress,
				0));
		finishedStrokeWidth = attributes.getDimension(
				R.styleable.DonutProgress_donut_finished_stroke_width,
				default_stroke_width);
		unfinishedStrokeWidth = attributes.getDimension(
				R.styleable.DonutProgress_donut_unfinished_stroke_width,
				default_stroke_width);
		if (attributes.getString(R.styleable.DonutProgress_donut_prefix_text) != null) {
			prefixText = attributes
					.getString(R.styleable.DonutProgress_donut_prefix_text);
		}
		if (attributes.getString(R.styleable.DonutProgress_donut_suffix_text) != null) {
			suffixText = attributes
					.getString(R.styleable.DonutProgress_donut_suffix_text);
		}
		if (attributes.getString(R.styleable.DonutProgress_donut_text) != null) {
			text = attributes.getString(R.styleable.DonutProgress_donut_text);
		}
		innerBackgroundColor = attributes.getColor(
				R.styleable.DonutProgress_donut_background_color,
				default_inner_background_color);
		// innerBackgroundBmp = BitmapFactory.decodeResource(getResources(),
		// attributes.getResourceId(
		// R.styleable.DonutProgress_donut_background_src, 0));
		innerBackgroundBmp = attributes.getResourceId(
				R.styleable.DonutProgress_donut_background_src, 0);

		innerBottomTextSize = attributes.getDimension(
				R.styleable.DonutProgress_donut_inner_bottom_text_size,
				default_inner_bottom_text_size);
		innerBottomTextColor = attributes.getColor(
				R.styleable.DonutProgress_donut_inner_bottom_text_color,
				default_inner_bottom_text_color);
		innerBottomText = attributes
				.getString(R.styleable.DonutProgress_donut_inner_bottom_text);
	}

	@Override
	public void invalidate() {
		initPainters();
		super.invalidate();
	}

	public float getFinishedStrokeWidth() {
		return finishedStrokeWidth;
	}

	public void setFinishedStrokeWidth(float finishedStrokeWidth) {
		this.finishedStrokeWidth = finishedStrokeWidth;
		this.invalidate();
	}

	public float getUnfinishedStrokeWidth() {
		return unfinishedStrokeWidth;
	}

	public void setUnfinishedStrokeWidth(float unfinishedStrokeWidth) {
		this.unfinishedStrokeWidth = unfinishedStrokeWidth;
		this.invalidate();
	}

	private float getProgressAngle() {
		return getProgress() / (float) max * 360f;
	}

	public int getProgress() {
		return progress;
	}

	/**
	 * @Title: setProgress
	 * @Description: 设置当前的进度值
	 * @param progress
	 * @return void
	 */
	public void setProgress(int progress) {
		this.progress = progress;
		if (this.progress > getMax()) {
			this.progress %= getMax();
		}
		invalidate();
	}

	public int getMax() {
		return max;
	}

	/**
	 * @Title: setMax
	 * @Description: 设置进度的最大值
	 * @param max
	 * @return void
	 */
	public void setMax(int max) {
		if (max > 0) {
			this.max = max;
			invalidate();
		}
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
		this.invalidate();
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
		this.invalidate();
	}

	public int getFinishedStrokeColor() {
		return finishedStrokeColor;
	}

	public void setFinishedStrokeColor(int finishedStrokeColor) {
		this.finishedStrokeColor = finishedStrokeColor;
		this.invalidate();
	}

	public int getUnfinishedStrokeColor() {
		return unfinishedStrokeColor;
	}

	public void setUnfinishedStrokeColor(int unfinishedStrokeColor) {
		this.unfinishedStrokeColor = unfinishedStrokeColor;
		this.invalidate();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		this.invalidate();
	}

	public String getSuffixText() {
		return suffixText;
	}

	public void setSuffixText(String suffixText) {
		this.suffixText = suffixText;
		this.invalidate();
	}

	public String getPrefixText() {
		return prefixText;
	}

	public void setPrefixText(String prefixText) {
		this.prefixText = prefixText;
		this.invalidate();
	}

	public int getInnerBackgroundBmp() {
		return innerBackgroundBmp;
	}

	public void setInnerBackgroundBmp(int innerBackgroundBmp) {
		this.innerBackgroundBmp = innerBackgroundBmp;
		this.invalidate();
	}

	public int getInnerBackgroundColor() {
		return innerBackgroundColor;
	}

	public void setInnerBackgroundColor(int innerBackgroundColor) {
		this.innerBackgroundColor = innerBackgroundColor;
		this.invalidate();
	}

	public String getInnerBottomText() {
		return innerBottomText;
	}

	public void setInnerBottomText(String innerBottomText) {
		this.innerBottomText = innerBottomText;
		this.invalidate();
	}

	public float getInnerBottomTextSize() {
		return innerBottomTextSize;
	}

	public void setInnerBottomTextSize(float innerBottomTextSize) {
		this.innerBottomTextSize = innerBottomTextSize;
		this.invalidate();
	}

	public int getInnerBottomTextColor() {
		return innerBottomTextColor;
	}

	public void setInnerBottomTextColor(int innerBottomTextColor) {
		this.innerBottomTextColor = innerBottomTextColor;
		this.invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measure(widthMeasureSpec),
				measure(heightMeasureSpec));

		// TODO calculate inner circle height and then position bottom text at
		// the bottom (3/4)
		innerBottomTextHeight = getHeight() - (getHeight() * 3) / 4;
	}

	private int measure(int measureSpec) {
		int result;
		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);
		if (mode == MeasureSpec.EXACTLY) {
			result = size;
		} else {
			result = min_size;
			if (mode == MeasureSpec.AT_MOST) {
				result = Math.min(result, size);
			}
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		float delta = Math.max(finishedStrokeWidth, unfinishedStrokeWidth);
		float deltaOffset = delta * 3 / 2;
		finishedOuterRect.set(deltaOffset, deltaOffset, (float) getWidth() - deltaOffset,
				(float) getHeight() - deltaOffset);
		unfinishedOuterRect.set(deltaOffset, deltaOffset, (float) getWidth() - deltaOffset,
				(float) getHeight() - deltaOffset);
		// 内部圆圈半径
		float innerCircleRadius = (getWidth() - delta * 4) / 2f;

		if (innerBackgroundBmp != 0) {
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					getInnerBackgroundBmp());

			int diameter = (int) (innerCircleRadius * 2 /*+ delta*/);
			/**
			 * 长度如果不一致，按小的值进行压缩
			 */
			Bitmap innerBmp = Bitmap.createScaledBitmap(bmp, diameter,
					diameter, false);
			canvas.drawBitmap(createCircleImage(innerBmp, diameter), getWidth()
					/ 2.0f - diameter / 2.0f, getHeight() / 2.0f - diameter
					/ 2.0f, null);
		} else {
			canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f,
					innerCircleRadius, innerCirclePaint);
		}
		// 在指定范围（矩形）中绘制弧形线段，参数：矩形，起始角度，结束角度，是否绘制半径连接线，画笔
		canvas.drawArc(finishedOuterRect, 0, getProgressAngle(), false,
				finishedPaint);
		canvas.drawArc(unfinishedOuterRect, getProgressAngle(),
				360 - getProgressAngle(), false, unfinishedPaint);

		String text = this.text != null ? this.text : prefixText + progress
				+ suffixText;
		if (!TextUtils.isEmpty(text)) {
			float textHeight = textPaint.descent() + textPaint.ascent();
			canvas.drawText(text,
					(getWidth() - textPaint.measureText(text)) / 2.0f,
					(getWidth() - textHeight) / 2.0f, textPaint);
		}

		if (!TextUtils.isEmpty(getInnerBottomText())) {
			innerBottomTextPaint.setTextSize(innerBottomTextSize);
			float bottomTextBaseline = getHeight() - innerBottomTextHeight
					- (textPaint.descent() + textPaint.ascent()) / 2;
			canvas.drawText(getInnerBottomText(),
					(getWidth() - innerBottomTextPaint
							.measureText(getInnerBottomText())) / 2.0f,
					bottomTextBaseline, innerBottomTextPaint);
		}
	}

	/**
	 * 根据原图和变长绘制圆形图片
	 * 
	 * @param source
	 * @param min
	 * @return
	 */
	private Bitmap createCircleImage(Bitmap source, int min) {

		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
		/**
		 * 产生一个同样大小的画布
		 */
		Canvas canvas = new Canvas(target);
		/**
		 * 首先绘制圆形
		 */
		canvas.drawCircle(min / 2, min / 2, min / 2, paint);
		/**
		 * 使用SRC_IN，参考上面的说明
		 */
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		/**
		 * 绘制图片
		 */
		canvas.drawBitmap(source, 0, 0, paint);

		return target;
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final Bundle bundle = new Bundle();
		bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
		bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor());
		bundle.putFloat(INSTANCE_TEXT_SIZE, getTextSize());
		bundle.putFloat(INSTANCE_INNER_BOTTOM_TEXT_SIZE,
				getInnerBottomTextSize());
		bundle.putFloat(INSTANCE_INNER_BOTTOM_TEXT_COLOR,
				getInnerBottomTextColor());
		bundle.putString(INSTANCE_INNER_BOTTOM_TEXT, getInnerBottomText());
		bundle.putInt(INSTANCE_INNER_BOTTOM_TEXT_COLOR,
				getInnerBottomTextColor());
		bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedStrokeColor());
		bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR,
				getUnfinishedStrokeColor());
		bundle.putInt(INSTANCE_MAX, getMax());
		bundle.putInt(INSTANCE_PROGRESS, getProgress());
		bundle.putString(INSTANCE_SUFFIX, getSuffixText());
		bundle.putString(INSTANCE_PREFIX, getPrefixText());
		bundle.putString(INSTANCE_TEXT, getText());
		bundle.putFloat(INSTANCE_FINISHED_STROKE_WIDTH,
				getFinishedStrokeWidth());
		bundle.putFloat(INSTANCE_UNFINISHED_STROKE_WIDTH,
				getUnfinishedStrokeWidth());
		bundle.putInt(INSTANCE_BACKGROUND_COLOR, getInnerBackgroundColor());
		bundle.putInt(INSTANCE_BACKGROUND_BMP, getInnerBackgroundBmp());
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			final Bundle bundle = (Bundle) state;
			textColor = bundle.getInt(INSTANCE_TEXT_COLOR);
			textSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
			innerBottomTextSize = bundle
					.getFloat(INSTANCE_INNER_BOTTOM_TEXT_SIZE);
			innerBottomText = bundle.getString(INSTANCE_INNER_BOTTOM_TEXT);
			innerBottomTextColor = bundle
					.getInt(INSTANCE_INNER_BOTTOM_TEXT_COLOR);
			finishedStrokeColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR);
			unfinishedStrokeColor = bundle
					.getInt(INSTANCE_UNFINISHED_STROKE_COLOR);
			finishedStrokeWidth = bundle
					.getFloat(INSTANCE_FINISHED_STROKE_WIDTH);
			unfinishedStrokeWidth = bundle
					.getFloat(INSTANCE_UNFINISHED_STROKE_WIDTH);
			innerBackgroundColor = bundle.getInt(INSTANCE_BACKGROUND_COLOR);
			innerBackgroundBmp = bundle.getInt(INSTANCE_BACKGROUND_BMP);
			initPainters();
			setMax(bundle.getInt(INSTANCE_MAX));
			setProgress(bundle.getInt(INSTANCE_PROGRESS));
			prefixText = bundle.getString(INSTANCE_PREFIX);
			suffixText = bundle.getString(INSTANCE_SUFFIX);
			text = bundle.getString(INSTANCE_TEXT);
			super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
			return;
		}
		super.onRestoreInstanceState(state);
	}
}
