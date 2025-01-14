package com.muxiyun.bika.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.*
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.muxiyun.bika.BR
import com.muxiyun.bika.R
import com.muxiyun.bika.adapter.CategoriesAdapter
import com.muxiyun.bika.base.BaseActivity
import com.muxiyun.bika.bean.CategoriesBean
import com.muxiyun.bika.databinding.ActivityMainBinding
import com.muxiyun.bika.ui.account.AccountActivity
import com.muxiyun.bika.ui.apps.AppsActivity
import com.muxiyun.bika.ui.collections.CollectionsActivity
import com.muxiyun.bika.ui.comiclist.ComicListActivity
import com.muxiyun.bika.ui.comment.CommentsActivity
import com.muxiyun.bika.ui.games.GamesActivity
import com.muxiyun.bika.ui.history.HistoryActivity
import com.muxiyun.bika.ui.leaderboard.LeaderboardActivity
import com.muxiyun.bika.ui.mycomments.MyCommentsActivity
import com.muxiyun.bika.ui.notifications.NotificationsActivity
import com.muxiyun.bika.ui.search.SearchActivity
import com.muxiyun.bika.ui.settings.SettingsActivity
import com.muxiyun.bika.utils.*
import com.yalantis.ucrop.UCrop
import me.jingbin.library.skeleton.ByRVItemSkeletonScreen
import me.jingbin.library.skeleton.BySkeleton


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    private var cList = ArrayList<CategoriesBean.Category>()
    private lateinit var adapter_categories: CategoriesAdapter
    private lateinit var skeletonScreen: ByRVItemSkeletonScreen

    private val ERROR_PROFILE = 0
    private val ERROR_CATEGORIES = 1
    private var ERROR = this.ERROR_PROFILE //这里标记哪个网络请求异常

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_main
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        binding.toolbar.title = "哔咔"
        setSupportActionBar(binding.toolbar)

        val cTitle = arrayOf(
            "推荐",
            "排行榜",
            "游戏推荐",
            "哔咔小程序",
            "留言板",
            "最近更新",
            "随机本子"
        )
        val cRes = arrayOf(
            R.drawable.logo_round,
            R.drawable.cat_leaderboard,
            R.drawable.cat_game,
            R.drawable.cat_love_pica,
            R.drawable.cat_forum,
            R.drawable.cat_latest,
            R.drawable.cat_random
        )

        for (index in 0..5) {
            val category = CategoriesBean.Category(
                "",
                false,
                "",
                false,
                "",
                thumb = CategoriesBean.Category.Thumb("", "", ""),
                cTitle[index],
                cRes[index]
            )
            cList.add(index, category)
        }
        adapter_categories = CategoriesAdapter()
        binding.mainRv.layoutManager = GridLayoutManager(
            this@MainActivity,
            if (getWindowWidth() > getWindowHeight()) 6 else 3
        )//初步适配平板（没卵用
        skeletonScreen = BySkeleton
            .bindItem(binding.mainRv)
            .adapter(adapter_categories)// 必须设置adapter，且在此之前不要设置adapter
            .load(R.layout.item_categories_skeleton)// item骨架图
            .duration(2000)// 微光一次显示时间
            .count(18)// item个数
            .show()

        initListener()

        viewModel.getUpdate()

        if (cList.size <= 6) {
            //防止重复添加 TODO 先解决了 后面再修改
            showProgressBar(true, "检查账号信息...")
            viewModel.getProfile() //先获得个人信息
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mainNavView.setCheckedItem(R.id.drawer_menu_home)
    }

    //toolbar菜单
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                startActivity(SearchActivity::class.java)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initListener() {
        //侧滑
        binding.drawerLayout.addDrawerListener(
            ActionBarDrawerToggle(
                this@MainActivity,
                binding.drawerLayout,
                binding.toolbar,
                R.string.drawer_show,
                R.string.drawer_hide
            )
        )
        (binding.mainNavView.getHeaderView(0)
            .findViewById(R.id.main_drawer_modify) as TextView).setOnClickListener {
            Toast.makeText(this, "功能未实现", Toast.LENGTH_SHORT).show()

        }
        (binding.mainNavView.getHeaderView(0)
            .findViewById(R.id.main_drawer_character) as ImageView).setOnClickListener {
            if (viewModel.userId != "") {
                PictureSelector.create(this)
                    .openGallery(SelectMimeType.ofImage())
                    .isCameraForegroundService(true)
                    .setSelectionMode(1)
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .setCropEngine { fragment, srcUri, destinationUri, dataSource, requestCode ->
                        UCrop.of(srcUri, destinationUri, dataSource)
                            .withAspectRatio(1f, 1f)
                            .withMaxResultSize(200, 200) //图片压缩没官方清晰
                            .start(fragment.requireActivity(), fragment, requestCode)
                    }
                    .forResult(object : OnResultCallbackListener<LocalMedia> {
                        override fun onResult(result: ArrayList<LocalMedia>) {
                            GlideApp.with(this@MainActivity)
                                .load(R.drawable.placeholder_avatar_2)
                                .into(
                                    binding.mainNavView.getHeaderView(0)
                                        .findViewById(R.id.main_drawer_imageView) as ImageView
                                )
                            viewModel.putAvatar(Base64Util().getBase64(result[0].cutPath))
                        }
                        override fun onCancel() {}
                    })
            }
        }
        binding.mainNavView.setNavigationItemSelectedListener {
            binding.mainNavView.setCheckedItem(it)
            when (it.itemId) {
                R.id.drawer_menu_history -> {
                    startActivity(HistoryActivity::class.java)
                }
                R.id.drawer_menu_bookmark -> {
                    val intent = Intent(this@MainActivity, ComicListActivity::class.java)
                    intent.putExtra("tag", "favourite")
                    intent.putExtra("title", "我的收藏")
                    intent.putExtra("value", "我的收藏")
                    startActivity(intent)

                }
                R.id.drawer_menu_mail -> {
                    startActivity(NotificationsActivity::class.java)

                }
                R.id.drawer_menu_chat -> {
                    startActivity(MyCommentsActivity::class.java)

                }
                R.id.drawer_menu_settings -> {
                    startActivity(SettingsActivity::class.java)
                }

            }
            true
        }

        binding.mainRv.setOnItemClickListener { v, position ->
            val intent = Intent(this, ComicListActivity::class.java)
            val datas = adapter_categories.getItemData(position)
            when (position) {
                0 -> {
                    startActivity(Intent(this, CollectionsActivity::class.java))
                }
                1 -> {
                    startActivity(Intent(this, LeaderboardActivity::class.java))
                }
                2 -> {
                    startActivity(Intent(this, GamesActivity::class.java))
                }
                3 -> {
                    startActivity(Intent(this, AppsActivity::class.java))
                }
                4 -> {
                    val intentComments = Intent(this, CommentsActivity::class.java)
                    intentComments.putExtra("id", "5822a6e3ad7ede654696e482")
                    intentComments.putExtra("comics_games", "comics")
                    startActivity(intentComments)
                }
                5 -> {
                    intent.putExtra("tag", "latest")
                    intent.putExtra("title", datas.title)
                    intent.putExtra("value", datas.title)
                    startActivity(intent)
                }
                6 -> {
                    intent.putExtra("tag", "random")
                    intent.putExtra("title", datas.title)
                    intent.putExtra("value", datas.title)
                    startActivity(intent)
                }
                else -> {
                    if (datas.isWeb) {
                        val intent = Intent()
                        intent.action = "android.intent.action.VIEW"
                        intent.data = Uri.parse(
                            "${datas.link}/?token=${
                                SPUtil.get(
                                    this,
                                    "token",
                                    ""
                                )
                            }&secret=pb6XkQ94iBBny1WUAxY0dY5fksexw0dt"
                        )
                        startActivity(intent)
                    } else {
                        intent.putExtra("tag", "categories")
                        intent.putExtra("title", datas.title)
                        intent.putExtra("value", datas.title)
                        startActivity(intent)
                    }
                }
            }

        }
        //网络重试点击事件监听
        binding.mainLoadLayout.setOnClickListener {
            skeletonScreen.show()
            if (ERROR == ERROR_PROFILE) {
                showProgressBar(true, "检查账号信息...")
                viewModel.getProfile()
            } else {
                showProgressBar(true, "获取主页信息...")
                viewModel.getCategories()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initViewObservable() {
        //user信息
        viewModel.liveData_profile.observe(this) {
            if (it.code == 200) {
                var fileServer = ""
                var path = ""
                var character = ""
                viewModel.userId=it.data.user._id

                if (it.data.user.avatar != null) {//头像
                    fileServer = it.data.user.avatar.fileServer
                    path = it.data.user.avatar.path
                    GlideApp.with(this@MainActivity)
                        .load(
                            GlideUrlNewKey(
                                fileServer,
                                path
                            )
                        )
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_avatar_2)
                        .into(
                            binding.mainNavView.getHeaderView(0)
                                .findViewById(R.id.main_drawer_imageView) as ImageView
                        )
                }
                if (it.data.user.character != null) { //头像框 新用户没有

                    character = it.data.user.character
                    GlideApp.with(this@MainActivity)
                        .load(character)
                        .into(
                            binding.mainNavView.getHeaderView(0)
                                .findViewById(R.id.main_drawer_character) as ImageView
                        )
                }

                val name = it.data.user.name
                (binding.mainNavView.getHeaderView(0)
                    .findViewById(R.id.main_drawer_name) as TextView).text = name //用户名

                val level = it.data.user.level//等级
                (binding.mainNavView.getHeaderView(0)
                    .findViewById(R.id.main_drawer_user_ver) as TextView).text =
                    "Lv.${it.data.user.level}(${it.data.user.exp}/${exp(it.data.user.level)})"//等级

                (binding.mainNavView.getHeaderView(0)
                    .findViewById(R.id.main_drawer_title) as TextView).text = it.data.user.title//称号

                //性别
                val gender = it.data.user.gender
                (binding.mainNavView.getHeaderView(0)
                    .findViewById(R.id.main_drawer_gender) as TextView).text =
                    when (it.data.user.gender) {
                        "m" -> "(绅士)"
                        "f" -> "(淑女)"
                        else -> "(机器人)"
                    }

                //用户签名
                val text_slogan = (binding.mainNavView.getHeaderView(0)
                    .findViewById(R.id.main_drawer_slogan) as TextView)
                text_slogan.text =
                    if (it.data.user.slogan.isNullOrBlank()) {
                        resources.getString(R.string.slogan)
                    } else {
                        it.data.user.slogan
                    }

                if (!it.data.user.isPunched) {//当前用户未打卡时
                    //是否设置自动打卡
                    if (SPUtil.get(this, "setting_punch", true) as Boolean) {
                        viewModel.punch_In()
                    }
                }

                //存一下当前用户信息 用于显示个人评论
                SPUtil.put(this, "user_fileServer", fileServer)
                SPUtil.put(this, "user_path", path)
                SPUtil.put(this, "user_character", character)
                SPUtil.put(this, "user_name", name)
                SPUtil.put(this, "user_birthday", it.data.user.birthday)
                SPUtil.put(this, "user_gender", gender)
                SPUtil.put(this, "user_level", level)
                SPUtil.put(this, "user_exp", it.data.user.exp)
                SPUtil.put(
                    this,
                    "user_slogan",
                    if (it.data.user.slogan.isNullOrBlank()) "" else it.data.user.slogan
                )
                SPUtil.put(this, "user_title", it.data.user.title)
                SPUtil.put(this, "user_id", it.data.user._id)
                SPUtil.put(this, "user_verified", it.data.user.verified)

                if (cList.size <= 10) {
                    //更换头像会重新加载个人信息 防止重复加载
                    showProgressBar(true, "获取主页信息...")
                    viewModel.getCategories() //获得主页信息
                }

            } else if (it.code == 401) {
                if (it.error == "1005") {
                    //token 过期 进行自动登录
                    showProgressBar(true, "账号信息已过期，进行自动登录...")
                    viewModel.getSignIn()//自动登录 重新获取token
                }
            } else {
                ERROR = this.ERROR_PROFILE
                showProgressBar(
                    false,
                    "网络错误，点击重试\ncode=${it.code} error=${it.error} message=${it.message}"
                )
            }
        }

        //自动登录 只要是网络错误请求失败全跳转到登录页
        viewModel.liveData_signin.observe(this) {
            if (it.code == 200) {
                //登录成功 保存token
//                MmkvUtils.putSet("token", bean.data.token)
                SPUtil.put(this, "token", it.data.token)
                showProgressBar(true, "登录成功，检查账号信息...")
                viewModel.getProfile() //重新加载数据

            } else if (it.code == 400) {
                if (it.error == "1004") {
                    //登录失败 账号或密码错误 跳转到登录页面AccountActivity
                    Toast.makeText(this, "自动登录失败 账号或密码错误", Toast.LENGTH_SHORT).show()
                    startActivity(AccountActivity::class.java)
                    finish()
                }
            } else {
                //网络错误 登录失败 跳转到登录页面AccountActivity
                Toast.makeText(
                    this,
                    "自动登录失败code=${it.code} error=${it.error} message=${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(AccountActivity::class.java)
                finish()
            }
        }


        //加载主页
        viewModel.liveData.observe(this) {
            skeletonScreen.hide()
            if (it.code == 200) {
                binding.mainRv.loadMoreEnd()
                binding.mainLoadLayout.visibility = ViewGroup.GONE
                //TODO 有BUG 列表重复
                cList.addAll(it.data.categories)
                adapter_categories.addData(cList)
            } else {
                ERROR = this.ERROR_CATEGORIES
                showProgressBar(
                    false,
                    "网络错误，点击重试\ncode=${it.code} error=${it.error} message=${it.message}"
                )
            }
        }


        //打卡签到
        viewModel.liveData_punch_in.observe(this) {
            if (it.code == 200) {
                //打卡成功 手动加经验来保存
                var exp = SPUtil.get(this, "user_exp", 0) as Int
                var level = SPUtil.get(this, "user_level", 1) as Int
                exp += 10
                if (exp > exp(level)) {
                    level += 1
                }
                //保存
                SPUtil.put(this, "user_level", level)
                SPUtil.put(this, "user_exp", exp)
                Toast.makeText(this, "自动打卡成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "打卡失败message=${it.message} code=${it.code} error=${it.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        //更新
        viewModel.liveData_update.observe(this) {
            if (it != null && it.code > AppVersion().code()) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("新版本 v${it.name}")
                    .setMessage(it.des)
                    .setPositiveButton("更新") { _, _ ->
                        val intent = Intent()
                        intent.action = "android.intent.action.VIEW"
                        intent.data = Uri.parse(it.url)
                        startActivity(intent)
                    }
                    .setNegativeButton("取消", null)
                    .show()
            }
        }

        //上传头像
        viewModel.liveData_avatar.observe(this) {
            if (it.code == 200) {
                //成功就重新获取个人信息
                viewModel.getProfile()
            } else {
                Toast.makeText(this, "更换头像失败", Toast.LENGTH_SHORT).show()
                //失败切换为原来的头像
                val fileServer = SPUtil.get(this, "user_fileServer", "") as String
                val path = SPUtil.get(this, "user_path", "") as String
                GlideApp.with(this)
                    .load(
                        if (path != "") { GlideUrlNewKey(fileServer, path) } else R.drawable.placeholder_avatar_2
                    )
                    .placeholder(R.drawable.placeholder_avatar_2)
                    .into(binding.mainNavView.getHeaderView(0)
                        .findViewById(R.id.main_drawer_imageView) as ImageView)
            }
        }
    }

    private fun showProgressBar(show: Boolean, string: String) {
        binding.mainLoadProgressBar.visibility = if (show) ViewGroup.VISIBLE else ViewGroup.GONE
        binding.loadCategoriesError.visibility = if (show) ViewGroup.GONE else ViewGroup.VISIBLE
        binding.mainLoadText.text = string
        binding.mainLoadLayout.isEnabled = !show
    }

    private fun exp(i: Int): Int {
        //等级计算是反编译源码找到的
        return 100 * i * i + (100 * i)
    }

    private fun getWindowHeight(): Int {
        return resources.displayMetrics.heightPixels
    }

    private fun getWindowWidth(): Int {
        return resources.displayMetrics.widthPixels
    }

//    private var expand = "[展开]"
//    private var collapse = "[收起]"
//
//    private fun expandText(contentTextView: TextView) {
//        val text: CharSequence = contentTextView.text
//        val width: Int = contentTextView.width
//        val paint: TextPaint = contentTextView.paint
//        val layout: Layout = contentTextView.layout
//        val line: Int = layout.lineCount
//        if (line > 4) {
//            val start: Int = layout.getLineStart(3)
//            val end: Int = layout.getLineVisibleEnd(3)
//            val lastLine = text.subSequence(start, end)
//            val expandWidth = paint.measureText(expand)
//            val remain = width - expandWidth
//            val ellipsize = TextUtils.ellipsize(
//                lastLine,
//                paint,
//                remain,
//                TextUtils.TruncateAt.END
//            )
//            val clickableSpan: ClickableSpan = object : ClickableSpan() {
//                override fun onClick(widget: View) {
//                    collapseText(contentTextView,text)
//                }
//            }
//            val ssb = SpannableStringBuilder()
//            ssb.append(text.subSequence(0, start))
//            ssb.append(ellipsize)
//            ssb.append(expand)
//            ssb.setSpan(
//                ForegroundColorSpan(-0x8c9608),
//                ssb.length - expand.length, ssb.length,
//                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
//            )
//            ssb.setSpan(
//                clickableSpan,
//                ssb.length - expand.length, ssb.length,
//                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
//            )
//            contentTextView.movementMethod = LinkMovementMethod.getInstance()
//            contentTextView.text = ssb
//        }
//    }
//
//    private fun collapseText(contentTextView: TextView,text: CharSequence) {
//
//        // 默认此时文本肯定超过行数了，直接在最后拼接文本
//        val clickableSpan: ClickableSpan = object : ClickableSpan() {
//            override fun onClick(widget: View) {
//                expandText(contentTextView)
//            }
//        }
//        val ssb = SpannableStringBuilder()
//        ssb.append(text.toString().replace(expand, ""))
//        ssb.append(collapse)
//        ssb.setSpan(
//            ForegroundColorSpan(-0x8c9608),
//            ssb.length - collapse.length, ssb.length,
//            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
//        )
//        ssb.setSpan(
//            clickableSpan,
//            ssb.length - collapse.length, ssb.length,
//            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
//        )
//        contentTextView.text = ssb
//    }
}