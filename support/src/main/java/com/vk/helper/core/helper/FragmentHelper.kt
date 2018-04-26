package com.vk.helper.core.helper

import android.app.Fragment
import android.app.FragmentManager
import android.support.annotation.IdRes
import android.text.TextUtils
import java.util.ArrayList

/**
 * 描述这个类的作用
 *
 * @author Viking
 * @date 2018/4/25
 * @version V1.0.0 < 创建 >
 */
class FragmentHelper{

    private val DEFAULT_CURRENT_INDEX_NONE = -1

    /**
     * 配合 [.initOrderedShowFragments] 获取到的 list 和 fragmentFactory 使用，切换逻辑
     *
     * @param fragments [.initOrderedShowFragments] 获取到的有序 fragment 列表
     * @param newShowIndex 有序列表想要显示的下标
     * @param fragmentFactory [.initOrderedShowFragments] 中初始化传入的 fragmentFactory 是一样的
     * @return 是否成功 show
     */
    fun showFragment(fragments: List<Fragment>, newShowIndex: Int, fragmentFactory: OrderedShowFragmentsFactory, @IdRes containerId: Int): Boolean {
        return showFragment(fragments, DEFAULT_CURRENT_INDEX_NONE, newShowIndex, fragmentFactory, containerId)
    }

    /**
     * 配合 [.initOrderedShowFragments] 获取到的 list 和 fragmentFactory 使用，切换逻辑
     *
     * @param fragments [.initOrderedShowFragments] 获取到的有序 fragment 列表
     * @param currentIndex 有序列表当前显示的下标，可能不知道，传 [.DEFAULT_CURRENT_INDEX_NONE]
     * @param newShowIndex 有序列表想要显示的下标
     * @param fragmentFactory [.initOrderedShowFragments] 中初始化传入的 fragmentFactory 是一样的
     * @return 是否成功 show
     */
    fun showFragment(fragments: List<Fragment>, currentIndex: Int, newShowIndex: Int, fragmentFactory: OrderedShowFragmentsFactory, @IdRes containerId: Int): Boolean {
        val currentFragmentUnknown = currentIndex == DEFAULT_CURRENT_INDEX_NONE
        val expectedFragmentTag = fragmentFactory.getTag(newShowIndex)

        val manager = fragmentFactory.fragmentManager

        // 在 findFragmentByTag 和 isAdded 之前需要运行完 transaction，对正常切换逻辑影响不大，影响的是频繁切换的逻辑，需要主线程等待
        manager.executePendingTransactions()

        var currentFragment: Fragment? = null
        if (!currentFragmentUnknown) {
            currentFragment = fragments.elementAtOrNull(currentIndex)
        }
        var expectedFragment: Fragment? = manager.findFragmentByTag(expectedFragmentTag)
        val hasExpectedFragment = expectedFragment != null

        // 如果没有添加过，且创建不出新实例的情况下不切换显示
        if (!hasExpectedFragment) {
            expectedFragment = fragments.elementAtOrNull(newShowIndex)

            // 取出 fragment 为空
            if (expectedFragment == null) {
                return false
            }
        }

        // 开始发起切换
        val switchTransaction = manager.beginTransaction()

        if (currentFragment != null) {
            switchTransaction.hide(currentFragment)
        }

        // 保留方式的显示和切换 Fragment，增加 isAdded 判断
        if (hasExpectedFragment || expectedFragment!!.isAdded()) {
            switchTransaction.show(expectedFragment)
        } else {
            switchTransaction.add(containerId, expectedFragment, expectedFragmentTag)
        }

        switchTransaction.commitAllowingStateLoss()
        return true
    }

    /**
     * 用于有序的多个 fragments 被初始化，根据 factory 选择 new 初始化和恢复 fragment，到要显示的 fragment
     *
     * @param hasSavedInstanceState fragment host 是否 "内存重启" 恢复
     * @param fragmentFactory 有序 fragment 列表的创建工厂
     * @param isViewPager 该有序 fragment 列表父级是否是 [ViewPager]
     * @param isRecycledStartNew true 的话表示要移除内存重启自动恢复的 fragment，重新创建，false 表示复用恢复的 fragment
     * @return 有序 fragment 列表
     * @see MJBFragmentPagerAdapter 若是 ViewPager 使用这个方法的话需要配合这个类来达到介入 ViewPager 内部的 fragment tag 创建，或者类似的类也可以
     */
    fun initOrderedShowFragments(hasSavedInstanceState: Boolean, fragmentFactory: OrderedShowFragmentsFactory, isViewPager: Boolean, isRecycledStartNew: Boolean): List<Fragment> {
        val expectedSize = fragmentFactory.size
        val fragments = ArrayList<Fragment>()
        if (expectedSize < 1) {
            return fragments
        }

        if (!hasSavedInstanceState) {
            for (index in 0 until expectedSize) {
                fragments.add(fragmentFactory.createFragment(index))
            }
            return fragments
        }

        val fragmentManager = fragmentFactory.fragmentManager

        // 在 findFragmentByTag 和 isAdded 之前需要运行完 transaction，对正常切换逻辑影响不大，影响的是频繁切换的逻辑，需要主线程等待
        fragmentManager.executePendingTransactions()

        val restoreFragments = ArrayList<Fragment>()
        for (index in 0 until expectedSize) {
            val foundFragment = fragmentManager.findFragmentByTag(fragmentFactory.getTag(index))
            if (foundFragment == null) {
                fragments.add(fragmentFactory.createFragment(index))
            } else if (isRecycledStartNew) {
                // 找到旧的加到 restoreFragments，但返回的是新创建的，因为待会要 remove 掉
                fragments.add(fragmentFactory.createFragment(index))
                restoreFragments.add(foundFragment)
            } else {
                fragments.add(foundFragment)
                restoreFragments.add(foundFragment)
            }
        }

        // 1. 没有需要恢复，直接返回
        // 2. ViewPager 若是需要复用不需要 show、hide，由 ViewPager 管理，返回
        // 3. 若是不需要复用的话，需要自己在 ViewPager 的管理逻辑前 remove 掉 restoreFragments
        val isViewPagerAndReusedFragment = isViewPager && !isRecycledStartNew
        if (isViewPagerAndReusedFragment || restoreFragments.size == 0) {
            return fragments
        }
        val entryFragmentTag = fragmentFactory.getTag(fragmentFactory.entryIndex())

        val transaction = fragmentManager.beginTransaction()
        for (foundFragment in restoreFragments) {
            if (isRecycledStartNew) {
                transaction.remove(foundFragment)
            } else if (TextUtils.equals(entryFragmentTag, foundFragment.getTag())) {
                transaction.show(foundFragment)
            } else {
                transaction.hide(foundFragment)
            }
        }
        transaction.commitAllowingStateLoss()

        return fragments
    }

    /**
     * 用于有序的多个 fragments 被初始化，根据 factory 选择 new 初始化和恢复 fragment，到要显示的 fragment
     *
     * @param hasSavedInstanceState fragment host 是否 "内存重启" 恢复
     * @param fragmentFactory 有序 fragment 列表的创建工厂
     * @param isViewPager 该有序 fragment 列表父级是否是 [ViewPager]
     * @return 有序 fragment 列表
     * @see MJBFragmentPagerAdapter 若是 ViewPager 使用这个方法的话需要配合这个类来达到介入 ViewPager 内部的 fragment tag 创建，或者类似的类也可以
     */
    fun initOrderedShowFragments(hasSavedInstanceState: Boolean, fragmentFactory: OrderedShowFragmentsFactory, isViewPager: Boolean): List<Fragment> {
        return initOrderedShowFragments(hasSavedInstanceState, fragmentFactory, isViewPager, false)
    }

    /**
     * 有序 fragment 列表的创建工厂
     */
    // TODO: 2018/1/15 如果是需要恢复到 "内存回收" 前显示的 fragment index，是否还需要 entryIndex，默认0，目前若需要恢复到 "内存回收" 前的话，需要自己在 Fragment override onSaveInstanceState
    interface OrderedShowFragmentsFactory {

        /**
         * fragment 列表的大小
         *
         * @return 列表大小
         */
        val size: Int

        /**
         * 获取 fragmentManager，用于不同的 fragment manger 获取场景
         *
         * @return FragmentManager 实例对象
         */
        val fragmentManager: FragmentManager

        /**
         * 首先要显示的 fragment 的下标
         *
         * @return fragment 有序数组的下标
         */
        fun entryIndex(): Int

        /**
         * 获取对应下标 fragment 的 tag 名称，用于储存在 Fragment Manager 中，作为 fragment 的保存依据
         *
         * @param index 有序列表的下标
         * @return 对应下标的 tag 名称
         */
        fun getTag(index: Int): String

        /**
         * 获取对应下标新创建的 fragment
         *
         * @param index 有序列表的下标
         * @return 对应下标的新建 fragment 实例
         */
        fun createFragment(index: Int): Fragment
    }
}