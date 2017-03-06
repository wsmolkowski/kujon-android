package mobi.kujon.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import mobi.kujon.R;
import mobi.kujon.google_drive.ui.util.AbstractAnimatorListener;

/**
 *
 */

public class ExpandableTextView extends android.support.v7.widget.AppCompatTextView {
    private static final int DEFAULT_TRIM_LENGTH = 200;
    private static final String ELLIPSIS = "  ...  ";

    private CharSequence originalText;
    private CharSequence trimmedText;
    private BufferType bufferType;
    private boolean trim = true;
    private boolean firstExpand = true;
    private int firstHeight = 0;
    private int trimLength;
    private int maxHeight = 0;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        this.trimLength = typedArray.getInt(R.styleable.ExpandableTextView_trimLength, DEFAULT_TRIM_LENGTH);
        typedArray.recycle();

        setOnClickListener(v -> {
            if(firstExpand){
                firstHeight = this.getHeight();
                firstExpand = false;
            }
            int height = this.getHeight();
            int maxHeight = trim?this.getHeight(Typeface.DEFAULT):firstHeight;
            trim = !trim;
            ValueAnimator valueAnimator = ValueAnimator.ofInt(height, maxHeight);
            valueAnimator.addUpdateListener(animation -> {
                this.getLayoutParams().height = (int)animation.getAnimatedValue();
                this.requestLayout();
            });
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.setDuration(300);
            valueAnimator.addListener(new AbstractAnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    if(!trim)setText();
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if(trim)setText();
                }
            });

            valueAnimator.start();
        });
    }

    private void setText() {
        super.setText(getDisplayableText(), bufferType);
    }

    private CharSequence getDisplayableText() {
        return trim ? trimmedText : originalText;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        originalText = text;
        trimmedText = getTrimmedText(text);
        bufferType = type;
        setText();
    }

    private CharSequence getTrimmedText(CharSequence text) {
        if (originalText != null && originalText.length() > trimLength) {
            SpannableString str2= new SpannableString(getResources().getString(R.string.more));
            str2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dark_60)), 0, str2.length(), 0);
            return new SpannableStringBuilder(originalText, 0, trimLength + 1).append(ELLIPSIS).append(str2);
        } else {
            return originalText;
        }
    }

    public CharSequence getOriginalText() {
        return originalText;
    }

    public void setTrimLength(int trimLength) {
        this.trimLength = trimLength;
        trimmedText = getTrimmedText(originalText);
        setText();
    }

    public int getTrimLength() {
        return trimLength;
    }


    private int getHeight(Typeface typeface) {
        if(maxHeight == 0){
            int deviceWidth;
            WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();

            Point size = new Point();
            display.getSize(size);
            deviceWidth = size.x;
            int padding = getContext().getResources().getDimensionPixelSize(R.dimen.kujon_margin_typ);
            TextView textView = new TextView(this.getContext());
            textView.setPadding(padding, 0, padding, 0);
            textView.setTypeface(typeface);
            textView.setText(getOriginalText(), TextView.BufferType.SPANNABLE);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize());
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            textView.measure(widthMeasureSpec, heightMeasureSpec);
            this.maxHeight  = textView.getMeasuredHeight()+10;
        }

        return maxHeight;
    }

}