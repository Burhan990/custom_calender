package nookneeds.business.nooklife.helper


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.text.SpannableString
import android.text.Spanned
import android.text.format.DateFormat
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.text.HtmlCompat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat")
object UIHelper {
    fun changeLanguage(lang: String, context: Context) {
     //   val locale: Locale = if (lang.contains("ar")) Locale(lang, "MA") else Locale(lang)

        var locale = Locale("fr-rDZ")

        //showErrorLog(lang)
        locale = when {
            lang.contains("ar") -> {
                Locale(lang, "MA")
            }
            lang.contains("en") -> {
                Locale(lang, "US")
            }
            else -> {
                Locale(lang)
            }
        }
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics);
    }

    fun setupUI(activity: Activity, view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
    }

    fun createLink(targetTextView: TextView, completeString: String,
                   partToClick1: String, partToClick2: String, clickableAction1: ClickableSpan?, clickableAction2: ClickableSpan?): TextView {
        val spannableString = SpannableString(completeString)

        // make sure the String is exist, if it doesn't exist
        // it will throw IndexOutOfBoundException
        val startPosition1 = completeString.indexOf(partToClick1)
        val endPosition1 = completeString.lastIndexOf(partToClick1) + partToClick1.length
        spannableString.setSpan(clickableAction1, startPosition1, endPosition1,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        val startPosition2 = completeString.indexOf(partToClick2)
        val endPosition2 = completeString.lastIndexOf(partToClick2) + partToClick2.length
        spannableString.setSpan(clickableAction2, startPosition2, endPosition2,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        targetTextView.text = spannableString
        targetTextView.movementMethod = LinkMovementMethod.getInstance()
        return targetTextView
    }

    @JvmStatic
    fun parseDate(time: String?): String? {
        val inputPattern = "yyyy-MM-dd"
        val outputPattern = "dd/MM/yyyy"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        val date: Date
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }

    @JvmStatic
    fun parseRating(rating: String?): Float {
        return rating?.toFloat() ?: 0F
    }

    @JvmStatic
    fun getRatingText(rating: String, count: String): String {
        return rating.substring(0, 3) + " (" + count + ")"
    }

    fun parseRewardID(ID: String): String {
        return "#$ID"
    }

//    @JvmStatic
//    fun parsePoint(points: String): String {
//        return points + " " + AppController.res.getString(R.string.points)
//    }

    fun getRequiredPoint(totalPoints: String?): String {
        return if (totalPoints != null) {
            val currentPoint = totalPoints.toInt()
            var requiredPoint = ""
            if (currentPoint < 1000) requiredPoint = (1000 - currentPoint).toString() else if (currentPoint < 10000) requiredPoint = (10000 - currentPoint).toString() else if (currentPoint < 50000) requiredPoint = (50000 - currentPoint).toString() else if (currentPoint < 100000) requiredPoint = (500000 - currentPoint).toString()
            requiredPoint
        } else "0"
    }

    @JvmStatic
    fun parseTimeStamp(time: String): String {
        return if (time.isEmpty())
            "31/12/2020"
        else {
            val date = time.toInt().toLong()
            val cal = Calendar.getInstance()
            cal.timeInMillis = date * 1000
            DateFormat.format("dd.MM.yyyy", cal).toString()
        }
    }

    @JvmStatic
    fun formatPoints(points: String?): String {
        val nf = NumberFormat.getNumberInstance(Locale.US)
        val formatter = nf as DecimalFormat
        formatter.applyPattern("#,###,###")
        var point = "0"
        if (points != null) {
            try {
                point = formatter.format(points.toInt().toLong())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return point
    }


    @JvmStatic
    fun formatPointsToDouble(points: Float?): String {
        val nf = NumberFormat.getNumberInstance(Locale.US)
        val formatter = nf as DecimalFormat
        formatter.applyPattern("#,###.##")
        var point = "0"
        if (points != null) {
            try {
                point = formatter.format(points.toInt().toLong())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return point
    }


    @JvmStatic
    fun formatHtml(text: String?): Spanned? {
        return text?.trim { it <= ' ' }?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
    }

    @JvmStatic
    fun formatPhone(text: String?): Spanned? {
        var formattedText = ""
        return text?.trim { it <= ' ' }?.let {
            if (it.length > 4) {
                formattedText = "<font color=#B3B3B3>${it.subSequence(0, 4)}\t</font> <font color=#B3B3B3>${it.subSequence(4, it.length)}</font>"
            }
            HtmlCompat.fromHtml(formattedText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

//    fun getMemberSince(creationDate: String?): String {
//        return AppController.res.getString(R.string.member_since) + " " + parseDate(creationDate)
    }
//
//    @JvmStatic
//    @BindingAdapter("loadimage")
//    fun setImageInView(imageView: ImageView?, image_url: String?) {
//        Glide.with(instance)
//                .load(image_url)
//                .apply(RequestOptions()
//                        .placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_placeholder)
//                        .format(DecodeFormat.PREFER_RGB_565))
//                .diskCacheStrategy(DiskCacheStrategy.DATA)
//                .into(imageView!!)
//
//    }


//    fun setImageInViewWithLoading(imageView: ImageView?, image_url: String?, spinKit: SpinKitView, storiesImageViewPreview: ImageView) {
//        spinKit.visibility = View.VISIBLE
//      //  storiesImageViewPreview.visibility=View.VISIBLE
//        Glide.with(instance)
//                .load(image_url)
//                .diskCacheStrategy(DiskCacheStrategy.DATA)
//                .listener(object : RequestListener<Drawable> {
//                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
//                        showErrorLog("onLoadFailed")
//                        return false
//                    }
//
//                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
//                        showErrorLog("OnResourceReady")
//                        //do something when picture already loaded
//                        spinKit.visibility = View.GONE
//                        // storiesImageViewPreview.visibility=View.GONE
//                        return false
//                    }
//                })
//                .into(imageView!!)
//
//    }
//
//    @JvmStatic
//    @BindingAdapter("loadProfileImage")
//    fun setAvatarImageInView(imageView: ImageView?, image_url: String?) {
//        Glide.with(instance)
//                .load(image_url)
//                .apply(RequestOptions().placeholder(R.drawable.ic_profile).error(R.drawable.ic_profile)
//                        .format(DecodeFormat.PREFER_RGB_565))
//                .diskCacheStrategy(DiskCacheStrategy.DATA)
//                .into(imageView!!)
//    }
//
//    fun setWheelImage(imageView: ImageView?, image_url: String?) {
//        Glide.with(instance)
//                .load(image_url)
//                .diskCacheStrategy(DiskCacheStrategy.DATA)
//                .into(imageView!!)
//    }
