package com.muxiyun.bika.ui.apps

import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.tabs.TabLayoutMediator
import com.muxiyun.bika.BR
import com.muxiyun.bika.R
import com.muxiyun.bika.adapter.ViewPagerAdapter
import com.muxiyun.bika.base.BaseActivity
import com.muxiyun.bika.databinding.ActivityAppsBinding

//小程序
class AppsActivity : BaseActivity<ActivityAppsBinding, AppsViewModel>() {

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_apps
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        binding.toolbar.title = "哔咔小程序"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val chat = AppsFragment()
        val apps = AppsFragment()

        val chatBundle = Bundle()
        chatBundle.putString("key", "chat")
        chat.arguments = chatBundle

        val appsBundle = Bundle()
        appsBundle.putString("key", "apps")
        apps.arguments = appsBundle

        val tabText = listOf("聊天室", "小程序")
        val tabList = listOf(chat, apps)
        val tabAdapter = ViewPagerAdapter(tabList, supportFragmentManager, this.lifecycle)
        binding.appsVp.apply {
            adapter = tabAdapter
            offscreenPageLimit = 1
        }
        TabLayoutMediator(binding.appsTab, binding.appsVp) { tab, position ->
            tab.text = tabText[position]
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

}