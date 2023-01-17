package com.example.shopify.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomTextView(context :Context , attrs: AttributeSet) : AppCompatTextView(context,attrs)
{
    init {
        applyfont()
    }

    private fun applyfont() {
        val typeface : Typeface = Typeface.createFromAsset(context.assets,"baloo2-bold.ttf")
        setTypeface(typeface)
    }
}