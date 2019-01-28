package com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.viewholder

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileViewModel

import com.tokopedia.discovery.R
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener.ProfileListListener
import kotlinx.android.synthetic.main.search_result_profile.view.*

class ProfileViewHolder(itemView: View, val profileListListener: ProfileListListener) : AbstractViewHolder<ProfileViewModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.search_result_profile
        val TEXT_WITH_ELLIPSIZE = "%s..."
        val MAX_NAME_LENGHT = 20
    }
    override fun bind(profileData: ProfileViewModel) {
        when(!TextUtils.isEmpty(profileData?.imgUrl?:"")){
            true -> ImageHandler.loadImageCircle2(itemView.context, itemView.img_profile, profileData!!.imgUrl)
        }
        itemView.tv_username.text = profileData.username

        when(profileData.name.length > MAX_NAME_LENGHT) {
            true -> {
                itemView.tv_name.text = String.format(TEXT_WITH_ELLIPSIZE, profileData.name.subSequence(0, MAX_NAME_LENGHT))
            }
            false -> {
                itemView.tv_name.text = profileData.name
            }
        }

        when(profileData.isKol) {
            true -> {
                val kolDrawable : Drawable = itemView.context.resources.getDrawable(R.drawable.search_kol_badge)
                itemView.tv_name.setCompoundDrawablesWithIntrinsicBounds(
                        kolDrawable,
                        null,
                        null,
                        null
                )
            }
        }

        when(TextUtils.isEmpty(profileData.username)) {
            true -> itemView.tv_username.visibility = View.GONE
            false -> itemView.tv_username.visibility = View.VISIBLE
        }

        when(profileData.post_count != 0) {
            true -> {
                itemView.tv_post_count.text = String.format(
                        itemView.context.getString(R.string.post_count_value),
                        profileData.post_count.toString()
                )
                itemView.tv_post_count.visibility = View.VISIBLE
            }
            false -> {
                itemView.tv_post_count.visibility = View.GONE
            }
        }

//        when(profileData!!.isKol || profileData.isAffiliate) {
//            true -> {
//                itemView.btn_follow.visibility = View.VISIBLE
//            }
//            false -> {
//                itemView.btn_follow.visibility = View.GONE
//            }
//        }

        when(profileData.followed){
            true -> {
                itemView.btn_follow.text = itemView.context.getString(R.string.btn_following_text)
                itemView.btn_follow.buttonCompatType = ButtonCompat.SECONDARY
                itemView.btn_follow.isClickable = true

                itemView.label_following.visibility = View.VISIBLE
            }
            false -> {
                itemView.btn_follow.text = itemView.context.getString(R.string.btn_follow_text)
                itemView.btn_follow.buttonCompatType = ButtonCompat.PRIMARY
                itemView.btn_follow.isClickable = true

                itemView.label_following.visibility = View.GONE
            }
        }

        itemView.setOnClickListener {
            profileListListener.onHandleProfileClick(profileData)
        }

        itemView.btn_follow.setOnClickListener {
            profileListListener.onFollowButtonClicked(adapterPosition, profileData)
        }
    }
}