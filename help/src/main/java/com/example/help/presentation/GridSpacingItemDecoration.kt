package com.example.help.presentation

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int,
    @DimenRes private val spacingDp: Int,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount
        outRect.left = spacingDp - column * spacingDp / spanCount
        outRect.right = (column + 1) * spacingDp / spanCount
        if (position < spanCount) outRect.top = spacingDp
        outRect.bottom = spacingDp
    }
}
