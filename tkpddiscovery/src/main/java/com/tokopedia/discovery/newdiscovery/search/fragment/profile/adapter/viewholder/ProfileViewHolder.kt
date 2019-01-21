package com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileViewModel

import com.tokopedia.discovery.R
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener.ProfileListListener
import kotlinx.android.synthetic.main.search_result_profile.view.*

class ProfileViewHolder(itemView: View?, val profileListListener: ProfileListListener) : AbstractViewHolder<ProfileViewModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.search_result_profile
    }
    override fun bind(profileData: ProfileViewModel?) {
        when(profileData!!.imgUrl != ""){
            true -> ImageHandler.loadImageCircle2(itemView.context, itemView.img_profile, profileData!!.imgUrl)
        }
        itemView.tv_name.text = profileData!!.name
        itemView.tv_username.text = profileData!!.username
        itemView.tv_post_count.text = String.format(
                itemView.context.getString(R.string.post_count_value),
                profileData!!.post_count.toString()
        )

        when(profileData!!.followed){
            true -> {
                itemView.btn_follow.text = itemView.context.getString(R.string.btn_following_text)
                itemView.btn_follow.buttonCompatType = ButtonCompat.SECONDARY
                itemView.btn_follow.isClickable = true
            }
            false -> {
                itemView.btn_follow.text = itemView.context.getString(R.string.btn_follow_text)
                itemView.btn_follow.buttonCompatType = ButtonCompat.PRIMARY
                itemView.btn_follow.isClickable = true
            }
        }

        itemView.btn_follow.setOnClickListener {
            profileListListener.onFavoriteButtonClicked(adapterPosition, profileData)
        }
    }
}