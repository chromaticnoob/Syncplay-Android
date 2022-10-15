package app.popups

import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import app.R
import app.sharedplaylist.SharedPlaylistPopup
import app.utils.UIUtils.toasty
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import razerdp.basepopup.BasePopupWindow

class SHPAddURLsPopup(val fragment: SharedPlaylistPopup) : BasePopupWindow(fragment) {
    init {
        setContentView(R.layout.popup_shp_add_urls)

        val edittext = findViewById<TextInputEditText>(R.id.urls_edittext)

        findViewById<MaterialButton>(R.id.urls_confirm).setOnClickListener {
            it.visibility = View.GONE
            val url = edittext.text.toString().trim()
            if (url.isNotBlank()) {
                //TODO: Add URLs to Playlist
                dismiss()
            }
        }
        findViewById<ImageButton>(R.id.urls_paste).setOnClickListener {
            edittext.setText((context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).text.toString())
            context.toasty("Pasted clipboard content")
        }

        findViewById<FrameLayout>(R.id.addurls_popup_dismisser).setOnClickListener {
            this.dismiss()
        }
    }
}