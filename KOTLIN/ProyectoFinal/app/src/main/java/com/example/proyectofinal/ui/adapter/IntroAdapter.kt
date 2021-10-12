package com.example.proyectofinal.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.proyectofinal.R
import kotlinx.android.synthetic.main.activity_slider.view.*

class IntroAdapter(var context: Context): PagerAdapter() {
    private val layoutInfalter: LayoutInflater
    private val SIZE = 3
    init {
        layoutInfalter = LayoutInflater.from(context)

    }

    var slide_background: IntArray = intArrayOf(R.drawable.fondo1, R.drawable.fondo2, R.drawable.fondo3)
    var slide_images: IntArray = intArrayOf(R.drawable.duelo, R.drawable.profile, R.drawable.brain)
    var slide_headings = arrayOf(R.string.intro_bienvenido, R.string.intro_online, R.string.intro_offline)
    var slide_descriptions = arrayOf(R.string.intro_bienvenido_des, R.string.intro_online_des, R.string.intro_offline_des)


    override fun getCount(): Int {
        return SIZE
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as (RelativeLayout)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = layoutInfalter.inflate(R.layout.activity_slider, container, false)
        view.slideLayout.setBackgroundResource(slide_background[position])
        view.foto.setImageResource(slide_images[position])
        view.header.setText(slide_headings[position])
        view.description.setText(slide_descriptions[position])
        if(position == 0){
            view.slideLayoutTvPoint1.setTextColor(context.resources.getColor(R.color.white))
            view.slideLayoutTvPoint2.setTextColor(context.resources.getColor(R.color.black))
            view.slideLayoutTvPoint3.setTextColor(context.resources.getColor(R.color.black))
        }
        if(position == 1){
            view.slideLayoutTvPoint1.setTextColor(context.resources.getColor(R.color.black))
            view.slideLayoutTvPoint2.setTextColor(context.resources.getColor(R.color.white))
            view.slideLayoutTvPoint3.setTextColor(context.resources.getColor(R.color.black))
        }
        if(position == 2){
            view.slideLayoutTvPoint1.setTextColor(context.resources.getColor(R.color.black))
            view.slideLayoutTvPoint2.setTextColor(context.resources.getColor(R.color.black))
            view.slideLayoutTvPoint3.setTextColor(context.resources.getColor(R.color.white))
        }
        container.addView(view)
        return view
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as (RelativeLayout))
    }


}