package com.mredrock.cyxbs.freshman.ui.activity

import android.animation.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.mvp.contract.MainContract
import com.mredrock.cyxbs.freshman.mvp.presenter.MainPresenter
import com.mredrock.cyxbs.freshman.utils.DensityUtils
import com.mredrock.cyxbs.freshman.utils.kt.BaseActivity
import kotlinx.android.synthetic.main.freshman_activity_main.*

class MainActivity : BaseActivity<MainContract.IMainView, MainContract.IMainPresenter>(), MainContract.IMainView {

    private val mCars by lazy { arrayOf(car1, car2, car3, car4, car5) }
    private val mPassCars by lazy { arrayOf(car1_2, car2_3, car3_4, car4_5) }
    private val scale by lazy { DensityUtils.getScreenWidth(this).toDouble() / iv_bg.drawable.intrinsicWidth }
    private val carScale by lazy {
        arrayOf(0.4762f, 0.5556f, 0.7143f, 0.8741f, 1f)
                .map { it * scale }
    }
    private val buildings by lazy {
        arrayOf(
                freshman_building_request1 to (R.drawable.freshman_building_request to R.drawable.freshman_building_request),
                freshman_building_strategy2 to (R.drawable.freshman_building_strategy_unlock to R.drawable.freshman_building_strategy_locked),
                freshman_building_chat3 to (R.drawable.freshman_building_chat_unlock to R.drawable.freshman_building_chat_locked),
                freshman_building_process4 to (R.drawable.freshman_building_process_unlock to R.drawable.freshman_building_process_locked),
                freshman_building_words5 to (R.drawable.freshman_building_words_unlock to R.drawable.freshman_building_words_locked))
    }

    override fun initCars(pos: Int) {
        with(mCars) {
            forEach {
                it.visibility = android.view.View.INVISIBLE
            }
            val bitmap = android.graphics.BitmapFactory.decodeResource(resources, when (pos) {
                1, 2, 4 -> com.mredrock.cyxbs.freshman.R.drawable.freshman_img_car_left
                else -> com.mredrock.cyxbs.freshman.R.drawable.freshman_img_car_right
            })
            kotlin.with(get(pos - 1)) {
                setImageBitmap(android.graphics.Bitmap.createScaledBitmap(bitmap,
                        (carScale[pos - 1] * bitmap.width).toInt(),
                        (carScale[pos - 1] * bitmap.height).toInt(),
                        true))
                visibility = android.view.View.VISIBLE
            }
            bitmap.recycle()
        }
    }

    override fun initBuilding(pos: Int) =
            buildings.forEachIndexed { i, building ->
                setScaledBitmap(building.first, building.second.first, building.second.second, pos <= i)
            }.also {
                setScaledBitmap(freshman_building_mien, R.drawable.freshman_building_mien)
                setScaledBitmap(freshman_building_military, R.drawable.freshman_building_military)
            }

    fun unlockBuilding(building:Pair<ImageView,Pair<Int,Int>>) =
            setScaledBitmap(building.first, building.second.first)

    private fun setScaledBitmap(view: ImageView, unlockId: Int, lockedId: Int = unlockId, isLocked: Boolean = false) {
        val bitmap = BitmapFactory.decodeResource(resources, if (isLocked) lockedId else unlockId)
        view.setImageBitmap(Bitmap.createScaledBitmap(bitmap, (scale * bitmap.width).toInt(), (scale * bitmap.height).toInt(), true))
        bitmap.recycle()
    }

    override fun driveAnimator(pos: Int) {

        val nowIndex = pos - 2
        val nextIndex = pos - 1

        val now = mCars[nowIndex]
        val pass = mPassCars[nowIndex]
        val target = mCars[nextIndex]

        AnimatorSet().apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            playTogether(
                    ValueAnimator.ofFloat(1f, (carScale[nextIndex] / carScale[nowIndex]).toFloat())
                            .apply {
                                addUpdateListener {
                                    now.scaleX = it.animatedValue as Float
                                    now.scaleY = now.scaleX
                                }
                            },

                    ObjectAnimator.ofFloat(now, "translationX",
                            0f, pass.x - now.x, target.x - now.x),

                    ObjectAnimator.ofFloat(now, "translationY",
                            0f, pass.y - now.y, target.y - now.y).apply {
                        if (pos > 2) {
                            val half = pass.y - now.y
                            addUpdateListener {
                                if (it.animatedValue as Float > half) {
                                    val bitmap = BitmapFactory.decodeResource(resources,
                                            if (pos == 4) R.drawable.freshman_img_car_left
                                            else R.drawable.freshman_img_car_right)
                                    now.setImageBitmap(android.graphics.Bitmap.createScaledBitmap(bitmap,
                                            (carScale[nowIndex] * bitmap.width).toInt(),
                                            (carScale[nowIndex] * bitmap.height).toInt(),
                                            true))
                                    bitmap.recycle()
                                    removeAllUpdateListeners()
                                }
                            }
                        }
                    })
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    unlockBuilding(buildings[nextIndex])
                    persenter.onDriveEnd(pos)
                }
            })
        }.start()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.freshman_activity_main)
        persenter.onContentViewSet()
    }
    override fun onResume() {
        super.onResume()
        persenter.onResume()
    }
    fun onBuildingClick(view: View) = persenter.onBuildingClick(view.id)

    override fun getContext() = this
    override fun getViewAttachPersenter(): MainContract.IMainView = this
    override fun createPersenter(): MainContract.IMainPresenter = MainPresenter()
}
