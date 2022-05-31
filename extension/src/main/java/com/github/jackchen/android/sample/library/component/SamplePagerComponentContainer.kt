package com.github.jackchen.android.sample.library.component

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.github.jackchen.android.core.appcompat.SampleWrapperViewFragment
import com.github.jackchen.android.core.component.ComponentContainer
import com.github.jackchen.android.sample.api.Extension
import com.github.jackchen.android.sample.library.R
import com.github.jackchen.android.sample.library.adapter.SimpleFragmentPagerAdapter
import com.github.jackchen.android.sample.library.component.code.SampleSourceCode
import com.github.jackchen.android.sample.library.component.code.SampleSourceFileFragmentListFragment
import com.github.jackchen.android.sample.library.component.document.SampleDocument
import com.github.jackchen.android.sample.library.component.document.SampleDocumentFragment
import com.google.android.material.tabs.TabLayout

@Extension
open class SamplePagerComponentContainer : ComponentContainer {

    override fun isComponentAvailable(component: Any): Boolean {
        val sampleSourceCode = component.javaClass.getAnnotation(SampleSourceCode::class.java)
        val sampleDocument = component.javaClass.getAnnotation(SampleDocument::class.java)
        return sampleSourceCode?.value != null || sampleDocument?.value != null
    }

    override fun getComponentView(
        context: AppCompatActivity,
        component: Any,
        parentView: ViewGroup,
        view: View
    ): View {
        val layoutInflater = LayoutInflater.from(context)
        val contentLayout = layoutInflater.inflate(R.layout.sample_fragment_tab, parentView, false)
        val sampleTabLayout = contentLayout.findViewById<TabLayout>(R.id.sampleTabLayout)
        val sampleViewPager = contentLayout.findViewById<ViewPager>(R.id.sampleViewPager)
        val titleList: MutableList<CharSequence> = ArrayList()
        titleList.add(context.getString(R.string.sample))
        val fragmentList: MutableList<Fragment> = java.util.ArrayList()
        fragmentList.add(SampleWrapperViewFragment.newFragment(view))

        val sampleSourceCode = component.javaClass.getAnnotation(SampleSourceCode::class.java)
        if (null != sampleSourceCode) {
            val filter: String = sampleSourceCode.value
            //Plus our component
            titleList.add(context.getString(R.string.sample_source_code))
            val packageName = component.javaClass.getPackage().name
            fragmentList.add(SampleSourceFileFragmentListFragment.newInstance(packageName, filter))
        }

        val sampleDocument = component.javaClass.getAnnotation(SampleDocument::class.java)
        if (null != sampleDocument) {
            val url: String = sampleDocument.value
            val packageName = component.javaClass.getPackage().name
            titleList.add(context.getString(R.string.sample_document))
            fragmentList.add(SampleDocumentFragment.newInstance(packageName, url))
        }
        sampleTabLayout.setupWithViewPager(sampleViewPager)
        sampleViewPager.offscreenPageLimit = 3
        sampleViewPager.adapter =
            SimpleFragmentPagerAdapter.create(context.supportFragmentManager, fragmentList, titleList)
        return contentLayout
    }

    override fun onCreatedView(context: AppCompatActivity, `object`: Any, view: View) = Unit

    override fun getComponentPriority(): Int = 999

}