package id.deadlock.smartrepository.helper

import android.content.Context
import android.os.Build
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar
import id.deadlock.smartrepository.R

object SnackbarHelper {
    fun configSnackbar(context: Context, snack: Snackbar) {
        addMargins(snack)
        setRoundBordersBg(context, snack)
        ViewCompat.setElevation(snack.view, 6f)
    }

    private fun addMargins(snack: Snackbar) {
        val params = snack.view.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(32, 16, 32, 32)
        snack.view.layoutParams = params
    }

    private fun setRoundBordersBg(context: Context, snackbar: Snackbar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            snackbar.view.background = context.getDrawable(R.drawable.snackbar_temp)
        }else{
            snackbar.view.background = ContextCompat.getDrawable(context, R.drawable.snackbar_temp)
        }

    }
}