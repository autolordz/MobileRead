package cn.univs.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

public class SlideMenu extends FrameLayout{

	private View menuView,mainView;
	private int menuWidth = 0;

	private ScrollAnimation openMenuAnimation,closeMenuAnimation;

	public boolean isOpend = false;

	public SlideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SlideMenu(Context context) {
		super(context);
		init();
	}

	private void init(){
	}

	/**
	 * 当1级的子view全部加载完调用，可以用来初始化子view的引用
	 * 注意，这里无法获取子view的宽高
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		menuView = getChildAt(0);
		mainView = getChildAt(1);
		menuWidth = menuView.getLayoutParams().width;
	}

	/**
	 * widthMeasureSpec和heightMeasureSpec是系统测量SlideMenu时传入的参数，
	 * 这2个参数测量出的宽高能让SlideMenu充满窗体，其实是正好等于屏幕宽高
	 */
	//	@Override
	//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	//		
	//		int measureSpec = MeasureSpec.makeMeasureSpec(menuWidth, MeasureSpec.EXACTLY);
	//		
	//		//测量所有子view的宽高
	//		//通过getLayoutParams方法可以获取到布局文件中指定宽高
	//		menuView.measure(measureSpec, heightMeasureSpec);
	//		//直接使用SlideMenu的测量参数，因为它的宽高都是充满父窗体
	//		mainView.measure(widthMeasureSpec, heightMeasureSpec);
	//		
	//	}

	/**
	 * 	拦截函数，当返回true时拦截事件，放在ViewGroup里的子控件将接受不到事件
	 * 	所以，当拦截时，说明ViewGroup要自己处理该事件，与子控件无关
	 * 
	 */
	private int downY;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) ( ev.getX()- downX);
			int deltaY = (int) ( ev.getY()- downY);
			if(Math.abs(deltaY)<10){
				//取绝对值
				if(Math.abs(deltaX)>50){
					return true;
				}
			}
			break;
		}
		return super.onInterceptTouchEvent(ev);//默认返回值，false，表示不拦截
	}

	/**
	 * l: 当前子view的左边在父view的坐标系中的x坐标
	 * t: 当前子view的顶边在父view的坐标系中的y坐标
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//		Log.e("MAIN", "L: "+l+"   t: "+t  +"  r: "+r  + "   b: "+b);
		menuView.layout(-menuWidth, 0, 0, menuView.getMeasuredHeight());
		mainView.layout(0, 0, r, b);
	}

	private int downX;
	private int newScrollX;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) event.getX();
			int deltaX = (int) ( moveX- downX);

			newScrollX = getScrollX() - deltaX;

			if(newScrollX<-menuWidth)newScrollX = -menuWidth;
			if(newScrollX>0)newScrollX = 0;

			scrollTo(newScrollX, 0);
			downX = moveX;
			break;
		case MotionEvent.ACTION_UP:
			if(newScrollX>-menuWidth/2){
				//				//关闭菜单
				closeMenu();
			}else {
				//打开菜单
				openMenu();
			}
			break;
		}
		return true;
	}

	public void closeMenu(){
		closeMenuAnimation = new ScrollAnimation(this, 0);
		startAnimation(closeMenuAnimation);
		isOpend = false;
	}

	public void openMenu(){
		openMenuAnimation = new  ScrollAnimation(this, -menuWidth);
		startAnimation(openMenuAnimation);
		isOpend = true;
	}

	/**
	 * 让指定view在一段时间内scrollTo到指定位置
	 * @author Administrator
	 *
	 */
	private class ScrollAnimation extends Animation{

		private View view;
		private int targetScrollX;
		private int startScrollX;
		private int totalValue;

		public ScrollAnimation(View view, int targetScrollX) {
			super();
			this.view = view;
			this.targetScrollX = targetScrollX;

			startScrollX = view.getScrollX();
			totalValue = this.targetScrollX - startScrollX;

			int time = Math.abs(totalValue);
			setDuration(time);
		}
		/**
		 * 在指定的时间内一直执行该方法，直到动画结束
		 * interpolatedTime：0-1  标识动画执行的进度或者百分比
		 * time :  0   - 0.5  - 0.7  -   1
		 * value:  10  -  60  -  80  -  110
		 * 当前的值 = 起始值 + 总的差值*interpolatedTime
		 */
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			int currentScrollX = (int) (startScrollX + totalValue*interpolatedTime);
			view.scrollTo(currentScrollX, 0);
		}
	}

}
