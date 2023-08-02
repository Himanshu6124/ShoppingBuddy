package com.himanshu.shoppingbuddy.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class CustomEditText(context :Context , attrs: AttributeSet) : AppCompatEditText(context,attrs)
{
    init {
        applyfont()
    }

    private fun applyfont() {
        val typeface : Typeface = Typeface.createFromAsset(context.assets,"baloo2-bold.ttf")
        setTypeface(typeface)
    }
}