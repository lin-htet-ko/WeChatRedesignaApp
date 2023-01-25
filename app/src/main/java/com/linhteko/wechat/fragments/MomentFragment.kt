package com.linhteko.wechat.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.linhteko.shared.data.vos.PostVO
import com.linhteko.shared.mvp.presenters.MomentPresenter
import com.linhteko.shared.mvp.views.MomentView
import com.linhteko.shared.utils.hide
import com.linhteko.shared.utils.show
import com.linhteko.wechat.R
import com.linhteko.wechat.adapters.PostAdapter
import com.linhteko.wechat.presenters.MomentPresenterImpl
import kotlinx.android.synthetic.main.fragment_moment.*

class MomentFragment : Fragment(), MomentView {

    companion object {
        const val ARG_USER_ID = "user-id"
        fun newInstance(userId: String) = MomentFragment().apply {
            arguments = Bundle().apply { putString(ARG_USER_ID, userId) }
        }
    }

    private lateinit var viewModel: MomentPresenter
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_moment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvMomentEmpty.show()
        setUpPresenter()
        getArgs()
        setRecyclerViews()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUiReady(requireActivity())
    }

    private fun getArgs() {
        viewModel.userId = arguments?.getString(ARG_USER_ID)
    }

    private fun setRecyclerViews() {
        postAdapter = PostAdapter(postDelegate = viewModel, userId = viewModel.userId)
        rvMomentPosts.adapter = postAdapter
    }

    private fun setUpPresenter() {
        viewModel = ViewModelProvider(requireActivity())[MomentPresenterImpl::class.java]
        viewModel.initView(this)
    }

    override fun showMoments(moments: List<PostVO>) {
        if (moments.isEmpty())
            tvMomentEmpty.show()
        else
            tvMomentEmpty.hide()
        postAdapter.setNewData(moments)
    }

    override fun showError(message: String) {
        
    }

}