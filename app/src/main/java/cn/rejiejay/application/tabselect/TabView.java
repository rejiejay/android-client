package cn.rejiejay.application.tabselect;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class TabView extends ListView {

    public TabView(Context paramContext) {
        super(paramContext);
    }

    public TabView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public TabView(Context paramContext, AttributeSet paramAttributeSet,
                      int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
